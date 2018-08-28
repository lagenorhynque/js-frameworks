## Build
# $ docker build -t lagenorhynque/js-frameworks-ci:<tag> .
#
## Check version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> node --version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> npm --version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> google-chrome --version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> mysql --version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> java -version
# $ docker run --rm lagenorhynque/js-frameworks-ci:<tag> lein --version
#
## Push to Docker Hub
# $ docker login
# $ docker push lagenorhynque/js-frameworks-ci:<tag>

FROM centos:centos7

# Git, etc.
RUN yum install -y git wget bzip2

# Node.js
RUN rpm -ivh https://rpm.nodesource.com/pub_6.x/el/7/x86_64/nodesource-release-el7-1.noarch.rpm && \
    yum install -y nodejs && \
    yum remove -y nodejs nodesource-release.noarch && \
    curl -L https://rpm.nodesource.com/setup_8.x | bash && \
    yum makecache && \
    yum install -y nodejs

# Google Chrome
RUN yum -y install https://dl.google.com/linux/direct/google-chrome-stable_current_x86_64.rpm

# MySQL client
RUN rpm -Uvh http://dev.mysql.com/get/mysql-community-release-el7-5.noarch.rpm && \
    yum install -y mysql-community-client

# Java
RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel.x86_64

# Leiningen
RUN wget https://raw.github.com/technomancy/leiningen/stable/bin/lein && \
    chmod +x lein && \
    mv lein /usr/local/bin/lein && \
    lein

# locale & timezone
RUN localedef -v -c -i ja_JP -f UTF-8 ja_JP.UTF-8; echo '' && \
    rm -f /etc/localtime && \
    ln -fs /usr/share/zoneinfo/Asia/Tokyo /etc/localtime
ENV LANG=ja_JP.UTF-8
