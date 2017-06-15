#!/usr/bin/env bash

lein clean; lein uberjar
docker build -t mastodonc/kixi.hecuba.dcc.measurements -f deployment/Dockerfile .
docker tag mastodonc/kixi.hecuba.dcc.measurements mastodonc/kixi.hecuba.dcc.measurements:git-$(git rev-parse HEAD | cut -c1-7)
docker push mastodonc/kixi.hecuba.dcc.measurements:git-$(git rev-parse HEAD | cut -c1-7)
echo "tag pushed:" $(git rev-parse HEAD | cut -c1-7)
