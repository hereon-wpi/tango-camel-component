# Tango Camel Component Project

[![Download](https://img.shields.io/github/release/hzg-wpi/tango-camel-component.svg?style=flat)](https://github.com/hzg-wpi/tango-camel-component/releases/latest)


This project is a template of a Camel component.

For more help see the Apache Camel documentation:

    http://camel.apache.org/writing-components.html

# How to use

1. Add  GitHub Maven packages repo to pom.xml/settings.xml

```xml
<repositories>
    <repository>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
        <id>github-hzg</id>
        <url>https://maven.pkg.github.com/hzg-wpi/*</url>
    </repository>
</repositories>
```

2. Add corresponding server to settings.xml

```xml
 <server>
    <id>github-hzg</id>
    <username>GITHUB_USER</username>
    <password>GITHUB_TOKEN</password>
</server>
```

3. Add corresponding dependcy to your pom.xml e.g. server:

```xml
<dependency>
    <groupId>de.hereon.tango</groupId>
    <artifactId>camel-component</artifactId>
    <version>2.2</version>
</dependency>
```

See GitHub docs: [here](https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages)



    
