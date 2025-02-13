# utils [![Maven Central](https://img.shields.io/maven-central/v/io.github.hisondev/utils.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/io.github.hisondev/utils)
This is a minimal collection of utils that can be used in a Java project.

## Introduction
The `utils` library is a set of utility functions designed to simplify common tasks in Java projects. It includes a variety of methods for string manipulation, date and time operations, number formatting, and more. The goal of this library is to provide a lightweight and easy-to-use solution for everyday programming needs.

## Getting Started
To start using the `utils` library in your project, follow the installation and usage instructions below.

This library, composed of JavaScript, can use `hison.min.js` from [hison-js](https://github.com/hisondev/hison-js).

### Prerequisites
Before you can use the `utils` library, you need to have the following software installed on your system:
- Java Development Kit (JDK) 8 or higher
- Apache Maven (for building the project)

### Installation
You can add the `utils` library to your project by including the following dependency in your Maven `pom.xml` file:

```xml
<dependency>
    <groupId>io.github.hison</groupId>
    <artifactId>utils</artifactId>
    <version>1.0.1</version>
</dependency>
```

## Usage
### Property Configuration
The `utils` library allows you to configure certain properties using a properties file. Here is an example of how to set up the properties:

1. **Create the properties file**:
Create a file named `hison-utils-config.properties` in your project's resources directory.

2. **Add properties to the file**:
Define the properties you want to customize in the `hison-utils-config.properties` file. Here are some example properties you can set:

```properties
# application.properties
hison.utils.format.date=dd/MM/yyyy
hison.utils.format.datetime=dd/MM/yyyy HH:mm:ss
hison.utils.type.date-add=d
hison.utils.type.date-diff=d
hison.utils.type.dayofweek=day
hison.utils.charbyte.less2047=2
hison.utils.charbyte.less65535=3
hison.utils.charbyte.greater65535=4
hison.utils.format.number=#,##0.##### 
hison.utils.propertie.file.path=./config/
```

### Ex) String Utilities
```java
import io.github.hison.utils.Utils;

// Check if a string contains only alphabetic characters
boolean isAlpha = Utils.isAlpha("HelloWorld");

// Check if a string contains only alphanumeric characters
boolean isAlphaNumber = Utils.isAlphaNumber("Hello123");

// Left pad a string with a specified character
String paddedString = Utils.getLpad("123", "0", 5);
```

### Ex) Date Utilities
```java
import io.github.hison.utils.Utils;

// Get the current system date and time
String sysDatetime = Utils.getSysDatetime();

// Add days to a given date
String newDate = Utils.addDate("2023-06-09", 5);

// Get the last day of a given month
int lastDay = Utils.getLastDay("2023-06");
```

### Ex) Number Utilities
```java
import io.github.hison.utils.Utils;

// Round a number to the nearest integer
double roundedNumber = Utils.getRound(123.456);

// Get the byte length of a string
int byteLength = Utils.getByteLength("Hello World");
```

## Contributing
Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request on GitHub. Make sure to follow the project's code style and add tests for any new features or changes.

## License
MIT License

## Authors
Hani Son
hison0319@gmail.com
