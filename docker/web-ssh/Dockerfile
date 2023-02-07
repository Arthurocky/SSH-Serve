FROM openjdk:8-jdk

MAINTAINER username: lijunpeng, email: 1635975742@qq.com

RUN mkdir -p /usr/local/web-ssh

COPY ./lib/web-ssh-1.0.5.jar /usr/local/web-ssh

WORKDIR /usr/local/web-ssh

EXPOSE 8080

CMD ["java", "-jar", "web-ssh-1.0.5.jar"]

