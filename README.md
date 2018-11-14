# CMXDSLink
CMX Notification link for IoT-DSA platform.

This link is listenning on 0.0.0.0:9090 as HTTP Server and handles CMX Notification POST request.

## Requirements
  - Java (>8.0)
  - Maven

## Installation
1. Run `./gradlew build distZip`
2. Navigate into `build/distributions`
3. Extract the distribution tarball/zip
4. Copy extracted folder in `dslinks` folder of your EFM server (default: `/opt/cisco/kinetic/efm_server/dslinks/`)
5. Restart EFM server or run manually `./bin/CMXDSLink -b http://localhost:8080/conn`

Note: `http://localhost:8080` is the url to the DSA broker that needs to have been installed prior.
