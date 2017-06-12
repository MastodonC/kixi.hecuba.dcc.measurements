#!/usr/bin/env bash
curl "https://s3.amazonaws.com/aws-cli/awscli-bundle.zip" -o "/root/awscli-bundle.zip"
unzip /root/awscli-bundle.zip -d /root/
/root/awscli-bundle/install -i /usr/local/aws -b /usr/local/bin/aws

# do we have access to the bucket?
function check_success {
    if (($? > 0)); then
        echo "ERROR - UNABLE TO DOWNLOAD THE SECRETS"
        exit 1
    fi
}

echo "aws s3 cp s3://$SECRETS_BUCKET/hecuba.login.edn /root/.hecuba.login.secrets.edn --region $AWS_REGION"
aws s3 cp s3://$SECRETS_BUCKET/hecuba.login.edn /root/.hecuba.login.edn --region $AWS_REGION
check_success
