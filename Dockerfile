# Set the base image
FROM maven:3-openjdk-11 AS builder

# Install linux dependencies
RUN apt-get update && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y curl git wget \
        sudo && \
    rm -rf /var/lib/apt/lists/*

RUN useradd -m -s /bin/bash ghost

# Switch to ghost user
USER ghost:ghost
WORKDIR /home/ghost

RUN git clone https://github.com/nationminu/jpetstore-msa-products.git jpetstore && \
    cd jpetstore && \
    mvn clean package -DskipTests


# Build final image using artifacts from builer
FROM openjdk:11 AS release

LABEL maintainer="ssong. <mwsong@rockplace.co.kr>"

# Add ghost user
RUN useradd -m -s /bin/bash ghost && \
    mkdir -p /usr/app/jpetstore && \
    chown -R ghost:ghost /usr/app/jpetstore

COPY --from=builder --chown=ghost:ghost /home/ghost/jpetstore/target/*.jar /usr/app/jpetstore

# Switch to ghost user
USER ghost:ghost
WORKDIR /usr/app/jpetstore

RUN echo "java -jar *.jar" > /usr/app/jpetstore/entrypoint.sh && \
    chmod 700  /usr/app/jpetstore/entrypoint.sh 

ENTRYPOINT ["bash","-c","./entrypoint.sh"]



