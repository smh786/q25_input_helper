FROM mcr.microsoft.com/devcontainers/java:1-17-bookworm

ARG ANDROID_SDK_ROOT=/opt/android-sdk
ARG ANDROID_CMDLINE_TOOLS_VERSION=11076708

ENV ANDROID_HOME=${ANDROID_SDK_ROOT}
ENV ANDROID_SDK_ROOT=${ANDROID_SDK_ROOT}
ENV PATH=${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools:${PATH}

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
        ca-certificates \
        curl \
        git \
        jq \
        less \
        libglu1-mesa \
        openssh-client \
        procps \
        unzip \
        usbutils \
    && rm -rf /var/lib/apt/lists/*

RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools \
    && curl -fsSL "https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_CMDLINE_TOOLS_VERSION}_latest.zip" -o /tmp/android-commandline-tools.zip \
    && unzip -q /tmp/android-commandline-tools.zip -d /tmp/android-commandline-tools \
    && mv /tmp/android-commandline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest \
    && rm -rf /tmp/android-commandline-tools /tmp/android-commandline-tools.zip \
    && yes | sdkmanager --licenses >/dev/null \
    && sdkmanager \
        "platform-tools" \
        "platforms;android-35" \
        "build-tools;35.0.0" \
    && groupadd -f plugdev \
    && chown -R vscode:vscode ${ANDROID_SDK_ROOT} \
    && usermod -aG plugdev vscode
