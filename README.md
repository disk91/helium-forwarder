# Chirpstack to Helium integration formatter

This project aimed to transform a Chirpstack Integration Payload into a Helium integration payload
the  purpose is to maintain a retro-compatibility with existing integrations for uplink.

### Features (development in progress)

 * supported
   - Http integration with Chirpstack to Helium payload transformation
   - Downlink forward to Chirpstack (1 min validity for the given endpoint)
   - Payload enrichment with gateway location
   - Payload transformation api endpoint Chirsptack to Helium
   - Payload enrichment api endpoint Chipstack to Chirpstack

 * coming next 
   - mqtt integration
   - streamr integration
   - data store

## Architecture
The component can be deployed as a worker or as a load-balancer, purpose is to be able to push a worker upgrade w/o
having to stop the integration. Configuration file decides what mode to be used.

Forwarder is an additional component of [helium-chirpstack](https://github.com/disk91/helium-chirpstack) project, you can refer to the documentation of the master project
to see the configuration in action.

## Dependencies

This solution rely on [helium-etl](https://github.com/disk91/helium-etl) API to determine the hotspot positions. If you need
this information, you need to run your own etl or contract for accessing my running API. Contact me for this.

## Build the application

See Makefile.

## Stop the application
- send SIGTERM message to the process (sent when docker stop), this will be clean the application properly.

## License
This program is distributed according to [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) licence for any private 
non-commercial IoT device fleet purpose, without restriction. This license is also applicable for commercial fleets under 
500 devices.

When used in a public / commercial service a license must be contracted for using this software ($500) price can be adjusted.

## Misc
- Nova service GRPC proto can be found on - https://github.com/helium/proto/blob/master/src/service/iot_config.proto
- Protoc - https://github.com/protocolbuffers/protobuf
- Stub pluggin - https://github.com/grpc/grpc-java/releases
- Download Stub pluggin - https://repo.maven.apache.org/maven2/io/grpc/protoc-gen-grpc-java/1.53.0/
- Command lines
```agsl
./protoc/bin/protoc --plugin=protoc-gen-grpc-java=.../protoc-gen-grpc-java-1.53.0-osx-x86_64.exe --java_out=... \
                    --grpc-java_out=...same... --proto_path=.../proto/src/ service/iot_config.proto
./protoc/bin/protoc --java_out=... --proto_path=.../proto/src/ region.proto
./protoc/bin/protoc --java_out=... --proto_path=.../proto/src/ blockchain_region_param_v1.proto
```
