# Base62 [![Build Status](https://travis-ci.org/seruco/base62.svg)](https://travis-ci.org/seruco/base62) [![license](https://img.shields.io/github/license/mashape/apistatus.svg)]()

**A Base62 Encoder/Decoder for Java**

## Getting Started

For Maven-based projects, add the following to your `pom.xml` file. This dependency is available from the Maven Central repository.

```xml
<dependency>
    <groupId>io.seruco.encoding</groupId>
    <artifactId>base62</artifactId>
    <version>0.1.0</version>
</dependency>
```

## Usage

First, we create a `Base62` instance:

```java
Base62 base62 = Base62.createInstance();
```

### Encoding

```java
final byte[] encoded = base62.encode("Hello World".getBytes());

new String(encoded); // is "73XpUgyMwkGr29M"
```

### Decoding

```java
final byte[] decoded = base62.decode("73XpUgyMwkGr29M".getBytes());

new String(decoded); // is "Hello World"
```

## Character Sets

This library supports two character sets: GMP-style or inverted. The difference between these two is whether the upper case letters come first, `0-9A-Za-z` (GMP), or last, `0-9a-zA-Z` (inverted).

By default, we prefer the GMP-style character set. If you want to use the inverted character set, simply do this:

```java
Base62 base62 = Base62.createInstanceWithInvertedAlphabet();
```

## Licensing

This project is licensed under the [MIT License](LICENSE).