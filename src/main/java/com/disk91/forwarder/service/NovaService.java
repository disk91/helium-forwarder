package com.disk91.forwarder.service;

import com.disk91.forwarder.ForwarderConfig;
import com.google.protobuf.ByteString;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import fr.ingeniousthings.tools.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.helium.grpc.*;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class NovaService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    protected PrometeusService prometeusService;

    // ===========================================================
    // GRPC Interface
    // ===========================================================
    @Autowired
    protected ForwarderConfig forwarderConfig;

    private byte[] privateKey;
    private ByteString owner;
    private Ed25519Signer signer;
    protected boolean grpcInitOk = false;

    protected H3Core h3;

    @PostConstruct
    private void loadKeys() {
        log.info("Init Nova GRPC setup");

        // For some demo environments it can be good to not pollute with route update
        // like for testing the migrations of devices.
        if ( ! forwarderConfig.isHeliumGrpcEnable() ) {
            log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.warn("Nova GRPC service is disabled, no location will be get that way");
            log.warn("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            this.grpcInitOk=false;
            return;
        }

        // Load Private key
        this.privateKey = new byte[64];
        try {
            InputStream inputStream = new FileInputStream(forwarderConfig.getHeliumGrpcPrivateKeyfilePath());

            int b;
            int k = 0;
            while ((b = inputStream.read()) != -1) {
                // verifiy key header should be 1 for type of key
                if (k == 0 && b != 1) break;
                if (k > 65) break;
                if (k > 0 && k < 65) {
                    privateKey[k - 1] = (byte) b;
                }
                k++;
            }
            if (k != 65) {
                // error
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.error("Invalid private keyfile");
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return;
            }

        } catch (IOException x) {
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.error("Impossible to access private key file " + x.getMessage());
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        // Prepare owner information
        try {
            byte[] owner_b = Base58.decode(forwarderConfig.getHeliumGprcPublicKey());
            if (owner_b.length == 38) {
                byte owner_b2[] = new byte[33];
                for (int i = 0; i < 33; i++) {
                    owner_b2[i] = owner_b[i + 1];
                }
                this.owner = ByteString.copyFrom(owner_b2);
            } else if (owner_b.length == 37) {
                // no leading  0
                byte owner_b2[] = new byte[33];
                for (int i = 0; i < 33; i++) {
                    owner_b2[i] = owner_b[i];
                }
                this.owner = ByteString.copyFrom(owner_b2);
            } else {
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                log.error("The public key size is not valid");
                log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                return;
            }
        } catch (ITParseException x) {
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.error("Impossible to parse Public Key with Base58");
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            return;
        }

        // Prepare the encryption elements
        Ed25519PrivateKeyParameters secretKeyParameters = new Ed25519PrivateKeyParameters(this.privateKey, 0);
        signer = new Ed25519Signer();
        signer.init(true, secretKeyParameters);

        // H3 position
        try {
            h3 = H3Core.newInstance();
        } catch (IOException x) {
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.error("Impossible de initialize h3 library");
            log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            h3 = null;
            return;
        }

        this.grpcInitOk = true;
    }



    public LatLng grpcGetGatewayLocation(String gatewayId) {
        if ( ! this.grpcInitOk || h3 == null ) return null;

        long start = Now.NowUtcMs();
        log.debug("GRPC Get Gateway location ");
        ManagedChannel channel = null;
        try {
            channel = ManagedChannelBuilder.forAddress(
                forwarderConfig.getHeliumGrpcServer(),
                forwarderConfig.getHeliumGrpcPort()
            ).usePlaintext().build();
            gatewayGrpc.gatewayBlockingStub stub = gatewayGrpc.newBlockingStub(channel);

            long now = Now.NowUtcMs();
            gateway_location_req_v1 requestToSign = gateway_location_req_v1.newBuilder()
                .setGateway(ByteString.copyFrom(HeliumHelper.nameToPubAddress(gatewayId)))
                .setSigner(this.owner)
                .clearSignature()
                .build();
            byte[] requestToSignContent = requestToSign.toByteArray();
            this.signer.update(requestToSignContent, 0, requestToSignContent.length);
            byte[] signature = signer.generateSignature();

            gateway_location_res_v1 response = stub.withDeadlineAfter(3, TimeUnit.SECONDS).location(gateway_location_req_v1.newBuilder()
                .setGateway(ByteString.copyFrom(HeliumHelper.nameToPubAddress(gatewayId)))
                .setSigner(this.owner)
                .setSignature(ByteString.copyFrom(signature))
                .build());
            log.debug("GPRC get location duration " + (Now.NowUtcMs() - start) + "ms");

            LatLng pos = h3.cellToLatLng(response.getLocation());
            if (pos != null && Gps.isAValidCoordinate(pos.lat, pos.lng) ) {
                log.debug("GRPC location is (" + pos.lat + ", "+pos.lng+")");
                return pos;
            }
            return null;

        } catch ( StatusRuntimeException x ) {
            prometeusService.addHeliumTotalError();
            log.warn("Nova Backend not reachable or too slow");
            log.warn(x.getMessage());
            return null;
        } finally {
            if ( channel != null ) channel.shutdown();
            prometeusService.addHeliumApiTotalTimeMs(start);
        }
    }



}

