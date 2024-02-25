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
   - Mqtt integration

 * coming later 
   - streamr integration
   - data store

## Architecture
The component can be deployed as a worker or as a load-balancer, purpose is to be able to push a worker upgrade w/o
having to stop the integration. Configuration file decides what mode to be used.

Forwarder is an additional component of [helium-chirpstack-community](https://github.com/disk91/helium-chirpstack-community) project, you can refer to the documentation of the master project
to see the configuration in action.

## Dependencies

The Hostpot position can be obtained from Helium Packet Router (HPR) of Helium ETL. Default and recommended is HPR. 

## Build the application

See Makefile.

## Stop the application
- send SIGTERM message to the process (sent when docker stop), this will be clean the application properly.

## License
This program is distributed according to [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) 

## Misc
- Nova service GRPC proto can be found on - https://github.com/helium/proto/blob/master/src/service/iot_config.proto
- Protoc - https://github.com/protocolbuffers/protobuf
- Stub plugin - https://github.com/grpc/grpc-java/releases
- Download Stub plugin - https://repo.maven.apache.org/maven2/io/grpc/protoc-gen-grpc-java/1.60.1/
- Command lines
```agsl
./protoc/bin/protoc --plugin=protoc-gen-grpc-java=.../protoc-gen-grpc-java-1.60.1-osx-x86_64.exe --java_out=... \
                    --grpc-java_out=...same... --proto_path=.../proto/src/ service/iot_config.proto
./protoc/bin/protoc --java_out=... --proto_path=.../proto/src/ region.proto
./protoc/bin/protoc --java_out=... --proto_path=.../proto/src/ blockchain_region_param_v1.proto
```

```
./protoc/bin/protoc --plugin=protoc-gen-grpc-java=./codeGenPlugin/protoc-gen-grpc-java-1.60.1-osx-x86_64.exe --java_out=./out  --grpc-java_out=./out --proto_path=./myHeliumProto/proto/src/ service/iot_config.proto
./protoc/bin/protoc --java_out=./out --proto_path=./myHeliumProto/proto/src/ region.proto
./protoc/bin/protoc --java_out=./out --proto_path=./myHeliumProto/proto/src/ blockchain_region_param_v1.proto
```

- Upgrade Gradle
```
./gradlew wrapper --gradle-version 8.5
```