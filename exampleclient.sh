# Replace with your Jenkins URL and admin credentials
SERVER="http://containerserver:8080"
CREDS="admin:passord"

# File where web session cookie is saved
COOKIEJAR="$(mktemp)"
CRUMB=$(curl -u $CREDS --cookie-jar "$COOKIEJAR" "$SERVER/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,%22:%22,//crumb)")
echo "crumb is $CRUMB"
for i in {0..5}
do
    echo "run $i"
    curl -v -X POST  -u $CREDS --cookie "$COOKIEJAR" -H "$CRUMB" "$SERVER"/job/testgroovypipeline/buildWithParameters  --data "MGMT_URI=https://<BIG-IP address>/mgmt/shared/appsvcs/declare" --data-urlencode "AS3_JSON@sampleas3.json"
done