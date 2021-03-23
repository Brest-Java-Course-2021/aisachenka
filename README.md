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

## Local tests with Jetty Maven Plugin

The [Jetty Maven plugin](https://www.eclipse.org/jetty/documentation/jetty-10/programming-guide/index.html#jetty-maven-plugin) is useful for development and nd local testing.

From the same directory as your root pom.xml, type:
```shell
mvn jetty:run
```

This starts Jetty and serves up your project on [http://localhost:8080](http://localhost:8080).

