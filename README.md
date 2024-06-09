# utils
This is a minimal collection of utils that can be used in a Java project.

## Introduction
The `utils` library is a set of utility functions designed to simplify common tasks in Java projects. It includes a variety of methods for string manipulation, date and time operations, number formatting, and more. The goal of this library is to provide a lightweight and easy-to-use solution for everyday programming needs.

## Getting Started
To start using the `utils` library in your project, follow the installation and usage instructions below.

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
    <version>1.0.0</version>
</dependency>

## Usage
### Ex String Utilities
import io.github.hison.utils.Utils;

// Check if a string contains only alphabetic characters
boolean isAlpha = Utils.isAlpha("HelloWorld");

// Check if a string contains only alphanumeric characters
boolean isAlphaNumber = Utils.isAlphaNumber("Hello123");

// Left pad a string with a specified character
String paddedString = Utils.getLpad("123", "0", 5);

### Ex Date Utilities
import io.github.hison.utils.Utils;

// Get the current system date and time
String sysDatetime = Utils.getSysDatetime();

// Add days to a given date
String newDate = Utils.addDate("2023-06-09", 5);

// Get the last day of a given month
int lastDay = Utils.getLastDay("2023-06");

### Ex Number Utilities
import io.github.hison.utils.Utils;

// Round a number to the nearest integer
double roundedNumber = Utils.getRound(123.456);

// Get the byte length of a string
int byteLength = Utils.getByteLength("Hello World");

## Contributing
Contributions are welcome! If you have any ideas, suggestions, or bug reports, please open an issue or submit a pull request on GitHub. Make sure to follow the project's code style and add tests for any new features or changes.

## License
MIT License

## Authors
Hani Son
hison0319@gmail.com
