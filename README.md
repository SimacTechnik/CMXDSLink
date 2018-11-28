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

## Features
This link has some hidden features, which need some notes

### Adding more CMX
After right-clicking on root node of link CMXDSLink, there is option to add new CMX, which will show you small form. There you set type of supported message type sent by specified CMX, some nickname for this CMX and url.
The URL is tricky part. This version of CMXDSLink is able to listen only on all interfaces and one port (which is `0.0.0.0:9090`), so you can separate these CMX's by context. So format of URL input have to start with `/` and then the rest of path, where are CMXNotification going to be sent. (e.g. if CMX is sending notifications on `http://10.0.0.1:9090/mycmx/1` the url must be `/mycmx/1`)

### Group By
Every CMX data can be grouped by first level simple field (there is option to filter by complex object types, but system calls function toString() on them and group data by result of this function).
This feature is hidden in action of CMX sub-node (which shows after right-click on the node).

## Filtering content
Because of lots of not-needed content in every message, you can 'filter out' some content of shown data. Just right click on attribute of some notification object and click on option 'Filter out'. This attribute will be removed from all notifications in current CMX's tree. If you want to show all attributes again, right-click on root node of current CMX's tree and click on 'Reset filter' option.

Note: Sometimes there are some cached data of nodes, so filtering doesn't affect all nodes. To fix it you need to collapse and expand again whole CMX's tree.
