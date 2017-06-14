FROM java:8-jre-alpine
MAINTAINER Jason Bell <jason.bell@mastodonc.com>

ADD https://github.com/just-containers/s6-overlay/releases/download/v1.11.0.1/s6-overlay-amd64.tar.gz /tmp/

RUN tar xzf /tmp/s6-overlay-amd64.tar.gz -C /
RUN apk add --update libgcc libstdc++ bash bash-doc bash-completion

# installing aws-cli: python and pip
RUN apk add --no-cache python3 && \
    python3 -m ensurepip && \
    rm -r /usr/lib/python*/ensurepip && \
    pip3 install --upgrade pip setuptools && \
    if [ ! -e /usr/bin/pip ]; then ln -s pip3 /usr/bin/pip ; fi && \
    rm -r /root/.cache

ADD deployment/run-stream.sh /opt/run-stream.sh
ADD target/kixi-hecuba-measurements.jar /opt/kixi-hecuba-measurements.jar
ADD deployment/download-secrets.sh /root/download-secrets.sh

ENTRYPOINT ["/init"]

CMD ["opt/run-stream.sh"]
