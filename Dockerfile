# 1. Use lightweight base image with Java 17
FROM openjdk:17-alpine AS builder

# 2. Install required tools: git, bash, curl, unzip
RUN apk update && apk add --no-cache \
    git \
    bash \
    curl \
    unzip

# 3. Install Helm CLI (v3.14.0 as example)
RUN curl -LO https://get.helm.sh/helm-v3.14.0-linux-amd64.tar.gz \
    && tar -zxvf helm-v3.14.0-linux-amd64.tar.gz \
    && mv linux-amd64/helm /usr/local/bin/helm \
    && chmod +x /usr/local/bin/helm \
    && rm -rf linux-amd64 helm-v3.14.0-linux-amd64.tar.gz

# 4. Set work directory and copy jar
WORKDIR /app
COPY build/libs/*.jar app.jar

# 5. Expose port and run
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app.jar"]
