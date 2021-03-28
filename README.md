[![Java CI with Maven](https://github.com/Brest-Java-Course-2021/aisachenka/actions/workflows/maven.yml/badge.svg)](https://github.com/Brest-Java-Course-2021/aisachenka/actions/workflows/maven.yml)
# aisachenka Blogs and Posts project

# Brest Java Course 2021 (winter)

This is sample 'Blogs and Posts' web application.

## Requirements

- JDK 11
- Apache Maven

## Build application:
```shell
cd Blog/
mvn clean install
```

## Local tests with Spring Boot

The Spring Boot is useful for development and local testing.

After building of application go to the web-app's or rest-app's target directory and execute 
```shell
java -jar name_of_spring_boot_jar_file.jar
```

This starts Spring boot and serves up your project on [http://localhost:8080](http://localhost:8080) and [http://localhost:8090](http://localhost:8090).

