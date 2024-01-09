# 使用官方的OpenJDK镜像作为基础镜像
FROM openjdk:11-jre-slim

# 设置工作目录
WORKDIR /app

# 复制编译好的JAR文件到镜像中
COPY target/core-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8081
CMD ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]

