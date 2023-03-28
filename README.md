# Chirpstack to Helium integration formater

This project aimed to transform a Chirpstack Integration Payload into a Helium integration payload
the  purpose is to maintain a retro-compatibility with existing integrations for uplink.

### Features (development in progress)


## Build the application


## Stop the application
- clean exist with cache purge and queueing processes
 `curl -fX GET http://127.0.0.1:8090/internal/3.0/exit`
- send SIGTERM message to the process (sent when docker stop)


## License
This program is distributed according to [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) licence for any private 
non-commercial IoT device fleet purpose, without restriction.

When used in a public / commercial service a license must be contracted for using this software ($500) price can be adjusted.
