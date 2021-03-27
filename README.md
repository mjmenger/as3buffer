# as3buffer

## Overview
This container is intended to be used for buffer substantial volumes of AS3 traffic to a BIG-IP management endpoint. You can find the source [here](https://github.com/mjmenger/as3buffer). While you can use this image, it's highly recommended that you use the source and build your own image so you have a clear grasp of what is in the container and what it does on your behalf.

## Setup
If you don't have Docker installed, [install Docker](https://docs.docker.com/get-docker/). I acknowledge that it's silly to include this link in a README that will show up in Docker Hub.

Set the following environment variables
```bash
export JENKINS_ADMIN_ID=admin
export JENKINS_ADMIN_PASSWORD=supersecretpassword
export BIGIP_MGMT_URI=https://bigipaddress/mgmt/shared/appsvcs/declare
export BIGIP_ADMIN_ID=admin
export BIGIP_ADMIN_PASSWORD=anothersupersecretpassword
```
The Jenkins admin user is created with the password as the Docker container starts up. Consequently, they can be whatever you'd like.  

The BIG-IP admin user and password is associated with a previously built BIG-IP. The BIG-IP must be available from the location where the as3buffer container will run. The BIG-IP must also have the [AS3 extension](https://clouddocs.f5.com/products/extensions/f5-appsvcs-extension/latest/) [installed](https://clouddocs.f5.com/products/extensions/f5-appsvcs-extension/latest/userguide/installation.html).

## Run
```bash
docker run -d --env JENKINS_ADMIN_ID=$JENKINS_ADMIN_ID --env JENKINS_ADMIN_PASSWORD=$JENKINS_ADMIN_PASSWORD --env BIGIP_HOST=$BIGIP_HOST --env BIGIP_MGMT_URI=$BIGIP_MGMT_URI --env BIGIP_ADMIN_ID=$BIGIP_ADMIN_ID --env BIGIP_ADMIN_PASSWORD=$BIGIP_ADMIN_PASSWORD -p 8080:8080 mmenger/as3buffer:latest 
```

## Deal with an issue
For an as yet unfathomed reason, the parameters described in the declarations don't show up in the Jenkins job until after the first build. The following should trigger that build, which should error out.
```bash
SERVER="http://localhost:8080"
CREDS="$JENKINS_ADMIN_ID:$JENKINS_ADMIN_PASSWORD"
COOKIEJAR="$(mktemp)"
CRUMB=$(curl -u $CREDS --cookie-jar "$COOKIEJAR" "$SERVER/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,%22:%22,//crumb)")
curl -v -X POST  -u $CREDS --cookie "$COOKIEJAR" -H "$CRUMB" "$SERVER"/job/as3buffer/build 
```

## Send AS3 declarations 
After the initial null request, you can pass AS3 declarations to the container. The declarations are passed to the BIG-IP in a serial fashion. The Jenkins job within the container is set with [concurrent execution disabled and three retries on failure](https://www.jenkins.io/doc/book/pipeline/syntax/#options).

```bash
# Replace with your Jenkins URL and admin credentials
SERVER="http://localhost:8080"
CREDS="$JENKINS_ADMIN_ID:$JENKINS_ADMIN_PASSWORD"

# File where web session cookie is saved
COOKIEJAR="$(mktemp)"
CRUMB=$(curl -u $CREDS --cookie-jar "$COOKIEJAR" "$SERVER/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,%22:%22,//crumb)")
curl -v -X POST  -u $CREDS --cookie "$COOKIEJAR" -H "$CRUMB" "$SERVER"/job/as3buffer/buildWithParameters --data-urlencode "AS3_JSON@sampleas3.json"
```
