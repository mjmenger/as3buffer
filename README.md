# as3buffer

## Overview
This container is intended to be used for buffer substantial volumes of AS3 traffic to a BIG-IP management endpoint.

```bash
docker run -d --env JENKINS_ADMIN_ID=<jenkins admin username> --env JENKINS_ADMIN_PASSWORD=<jenkins admin password> --env BIGIP_ADMIN_ID=<BIG-IP admin user> --env BIGIP_ADMIN_PASSWORD=<BIG-IP admin password> -p 8080:8080 mmenger/as3buffer:latest 
```