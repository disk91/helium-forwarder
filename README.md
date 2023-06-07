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
