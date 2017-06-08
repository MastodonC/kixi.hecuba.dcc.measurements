#!/usr/bin/env bash
pip install --upgrade --user awscli

# do we have access to the bucket?
function check_success {
    if (($? > 0)); then
        echo "ERROR - UNABLE TO DOWNLOAD THE SECRETS"
        exit 1
    fi
}

export PATH=$PATH:/root/.local/bin/

echo "aws s3 cp s3://$SECRETS_BUCKET/hecuba.login.edn /root/.hecuba.login.secrets.edn --region $AWS_REGION"
aws s3 cp s3://$SECRETS_BUCKET/hecuba.login.edn /root/.hecuba.login.edn --region $AWS_REGION
check_success
