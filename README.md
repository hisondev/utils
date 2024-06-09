# utils [![Maven Central](https://img.shields.io/maven-central/v/io.github.hisondev/utils.svg?label=Maven%20Central)](https://mvnrepository.com/artifact/io.github.hisondev/utils)
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
```

## Usage
### Property Configuration
The `utils` library allows you to configure certain properties using a properties file. Here is an example of how to set up the properties:

1. **Create the properties file**:
   Create a file named `hison-utils-config.properties` in your project's resources directory.

2. **Add properties to the file**:
   Define the properties you want to customize in the `hison-utils-config.properties` file. Here are some example properties you can set:

   ```properties
   date.formatter=dd/MM/yyyy
   datetime.formatter=dd/MM/yyyy HH:mm:ss
   add.type=d
   diff.type=d
   dayofweek.type=day
   lessoreq.0x7ff.byte=2
   lessoreq.0xffff.byte=3
   greater.0xffff.byte=4
   number.formatter=#,##0.##### 
   propertie.file.path=./config/
   ```

***This is because the logic for retrieving information about Utils properties is as follows.***

```java
private static String DATE_FORMATTER = "yyyy-MM-dd";
private static String DATETIME_FORMATTER = "yyyy-MM-dd HH:mm:ss";
private static String ADD_TYPE = "d";
private static String DIFF_TYPE = "d";
private static String DAY_OF_WEEK_TYPE = "d";
private static int LESSOREQ_0X7FF_BYTE = 2;
private static int LESSOREQ_0XFFFF_BYTE = 3;
private static int GREATER_0XFFFF_BYTE = 4;
private static String NUMBER_FORMATTER = "#,##0.#####";
private static String PROPERTIE_FILE_PATH = "";

static {
    Properties prop = new Properties();
    try (InputStream input = Utils.class.getClassLoader().getResourceAsStream("hison-utils-config.properties")) {
        if (input != null) {
            prop.load(input);
        }
    } catch (IOException ex) {
    }
    if(!prop.isEmpty()){
        DATE_FORMATTER = prop.getProperty("date.formatter") != null ? prop.getProperty("date.formatter") : DATE_FORMATTER;
        DATETIME_FORMATTER = prop.getProperty("datetime.formatter") != null ? prop.getProperty("datetime.formatter") : DATETIME_FORMATTER;
        ADD_TYPE = prop.getProperty("add.type") != null ? prop.getProperty("add.type") : ADD_TYPE;
        DIFF_TYPE = prop.getProperty("diff.type") != null ? prop.getProperty("diff.type") : DIFF_TYPE;
        DAY_OF_WEEK_TYPE = prop.getProperty("dayofweek.type") != null ? prop.getProperty("dayofweek.type") : DAY_OF_WEEK_TYPE;
        LESSOREQ_0X7FF_BYTE = prop.getProperty("lessoreq.0x7ff.byte") != null && prop.getProperty("lessoreq.0x7ff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("lessoreq.0x7ff.byte")) : LESSOREQ_0X7FF_BYTE;
        LESSOREQ_0XFFFF_BYTE = prop.getProperty("lessoreq.0xffff.byte") != null && prop.getProperty("lessoreq.0xffff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("lessoreq.0xffff.byte")) : LESSOREQ_0XFFFF_BYTE;
        GREATER_0XFFFF_BYTE = prop.getProperty("greater.0xffff.byte") != null && prop.getProperty("greater.0xffff.byte").matches("\\d+") ? Integer.parseInt(prop.getProperty("greater.0xffff.byte")) : GREATER_0XFFFF_BYTE;
        NUMBER_FORMATTER = prop.getProperty("number.formatter") != null ? prop.getProperty("number.formatter") : NUMBER_FORMATTER;
        PROPERTIE_FILE_PATH = prop.getProperty("propertie.file.path") != null ? prop.getProperty("propertie.file.path") : PROPERTIE_FILE_PATH;
    }
}
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
