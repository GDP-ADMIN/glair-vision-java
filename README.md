<p align="center">
  <a href="https://docs.glair.ai" target="_blank">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://glair-chart.s3.ap-southeast-1.amazonaws.com/images/glair-horizontal-logo-blue.png">
      <source media="(prefers-color-scheme: light)" srcset="https://glair-chart.s3.ap-southeast-1.amazonaws.com/images/glair-horizontal-logo-color.png">
      <img alt="GLAIR" src="https://glair-chart.s3.ap-southeast-1.amazonaws.com/images/glair-horizontal-logo-color.png" width="180" height="60" style="max-width: 100%;">
    </picture>
  </a>
</p>

<p align="center">
  GLAIR Vision Java SDK
<p>

<p align="center">
    <a href="https://central.sonatype.com/artifact/io.github.vincentchuardi/test-publish"><img src="https://img.shields.io/maven-central/v/io.github.vincentchuardi/test-publish.svg" alt="Latest Release"></a>
    <a href="https://github.com/glair-ai-shadow/glair-vision-java/blob/main/LICENSE"><img src="https://img.shields.io/npm/l/@glair/vision" alt="License"></a>
</p>

## Requirement to Develop

1. JDK 17 or later
1. Gradle 8.1.1 or later
1. IntelliJ IDEA 2023 or later

## Installation

You can refer to [this page](https://central.sonatype.com/artifact/io.github.vincentchuardi/test-publish)

After installing the GLAIR Vision SDK, you also need to install the [logging library](https://central.sonatype.com/artifact/org.apache.logging.log4j/log4j/2.20.0/overview).

Add a file named `log4j2.xml` inside `src/main/resources` to output logs.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration>
    <Appenders>
        <Console name="ConsoleAppender" target="SYSTEM_OUT">
            <PatternLayout
                    pattern="[%d{ISO8601}] %-5level GLAIR Vision SDK: %msg%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="ConsoleAppender"/>
        </Root>
    </Loggers>
</Configuration>
```

## Usage

The package needs to be configured with your credentials, see [here](https://docs.glair.ai/authentication) for more details.

```java
import glair.vision.Settings;
import glair.vision.Vision;

public class App {
   public static void main(String[] args) {
      Settings settings = new Settings.Builder()
         .username("username")
         .password("password")
         .apiKey("api-key")
         .build();

      Vision vision = new Vision(settings);
   }
}
```

The SDK's settings can be initialized with several options:

| Option       | Default                       | Description                         |
| ------------ | ----------------------------- | ----------------------------------- |
| `baseUrl`    | `https://api.vision.glair.ai` | Base URL for the API                |
| `apiVersion` | `v1`                          | GLAIR Vision API version to be used |
| `apiKey`     | `default-api-key`             | Your API Key                        |
| `username`   | `default-username`            | Your username                       |
| `password`   | `default-password`            | Your password                       |

Afterwards, you can use the provided functions to access GLAIR Vision API:

1. [OCR](#ocr)
2. [Face Biometric](#face-biometric)
3. [Session](#session)
4. [Identity](#identity)

## Override Configuration

You can override the configuration values for one-time only:

```java
import glair.vision.Settings;
import glair.vision.Vision;

public class App {
   public static void main(String[] args) {
      Settings settings = new Settings.Builder()
              .username("username")
              .password("password")
              .apiKey("api-key")
              .build();

      Vision vision = new Vision(settings);

      String imagePath = "/path/to/image.jpg";
      String response = "";

      try {
         Settings newSettings = new Settings.Builder()
                 .apiKey("new-api-key")
                 .build();
         Ocr.KtpParam param = new Ocr.KtpParam(imagePath);
         response = vision
                 .ocr()
                 .ktp(param, newSettings);
         // The Configurations' values that will be used on KTP are
         // username: username
         // password: password
         // apiKey: new-api-key
      } catch (Exception e) {
         response = e.getMessage();
      }

      System.out.println("Response: " + response);
   }
}
```

The second parameter is a new `Settings` object.
It will be merged with the original `Settings` that you set when instantiating the Vision instance.

## FAQ

1. IntelliJ IDE can't find the module?
   - This issue may occur when the library is installed using a JAR file.
   - Please ensure that you have added the dependency to your `build.gradle` file.
   - To import the library into the IDE, follow these steps: File > Project Structure > Modules > Dependencies > Add the JAR > OK.

---

## OCR

### KTP

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   Ocr.KtpParam param = new Ocr.KtpParam(imagePath);
   response = vision
      .ocr()
      .ktp(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

The `KtpParam` class is used for the necessary KTP API. It consists of the `imagePath` and the future `condition` attributes.

### NPWP

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .npwp(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### KK

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .kk(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### STNK

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .stnk(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### BPKB

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   Ocr.BpkbParam param = new Ocr.BpkbParam(imagePath, 1);
   response = vision
      .ocr()
      .bpkb(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

The `BpkbParam` class is used for the necessary BPKB API. It consists of the `imagePath` and `page` attributes. The `page` attribute is used only when you need specific page from the BPKB document.

### Passport

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .passport(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### License Plate

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .plate(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### General Document

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .generalDocument(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Invoice

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .invoice(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Receipt

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   response = vision
      .ocr()
      .receipt(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

## Face Biometric

### Face Matching

```java
String capturedPath = "/path/to/captured.jpg";
String storedPath = "/path/to/stored.jpg";
String response = "";

try {
   FaceBio.MatchParam matchParam = new FaceBio.MatchParam(capturedPath,
        storedPath);
   response = vision
      .faceBio()
      .match(matchParam);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Passive Liveness

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   FaceBio.PassiveLivenessParam param =
      new FaceBio.PassiveLivenessParam(imagePath);
   response = vision
      .faceBio()
      .passiveLiveness(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Active Liveness

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   FaceBio.ActiveLivenessParam param =
      new FaceBio.ActiveLivenessParam(
         imagePath,
         "HAND_00000");
   response = vision
      .faceBio()
      .activeLiveness(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

## Session

### KTP Sessions

Create session

```java
String response = "";

try {
   KtpSessions.CreateParam param =
      new KtpSessions.CreateParam.Builder()
         .successUrl("https://docs.glair.ai?success=true")
         .cancelUrl("https://docs.glair.ai?success=false")
         .build();
   response = vision
      .ocr()
      .ktpSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

Retrieve Session

```java
String response = "";

try {
   response = vision
      .faceBio()
      .ktpSessions()
      .retrieve("session-id");
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Passive Liveness Sessions

Create session

```java
String response = "";

try {
   PassiveLivenessSessions.CreateParam param =
      new PassiveLivenessSessions.CreateParam.Builder()
         .successUrl("https://docs.glair.ai?success=true")
         .cancelUrl("https://docs.glair.ai?success=false")
         .build();
   response = vision
      .faceBio()
      .passiveLivenessSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

Retrieve Session

```java
String response = "";

try {
   response = vision
      .faceBio()
      .passiveLivenessSessions()
      .retrieve("session-id");
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Active Liveness Sessions

Create session

```java
String response = "";

try {
   ActiveLivenessSessions.CreateParam param =
      new ActiveLivenessSessions.CreateParam.Builder()
         .successUrl("https://docs.glair.ai?success=true")
         .cancelUrl("https://docs.glair.ai?success=false")
         .numberOfGestures(2)
         .build();
   response = vision
      .faceBio()
      .activeLivenessSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

Retrieve Session

```java
String response = "";

try {
   response = vision
      .faceBio()
      .activeLivenessSessions()
      .retrieve("session-id");
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

## Identity

### Basic Verification

```java
String response = "";

try {
   Identity.VerificationParam param =
      new Identity.VerificationParam.Builder()
         .nik("1234567890123456")
         .name("John Doe")
         .dateOfBirth("01-01-2000")
         .build();
   response = vision
       .identity()
       .verification(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Face Verification

```java
String response = "";

try {
   Identity.FaceVerificationParam param =
      new Identity.FaceVerificationParam.Builder()
         .nik("1234567890123456")
         .name("John Doe")
         .dateOfBirth("01-01-2000")
         .faceImage("path/to/image.jpg")
         .build();
   response = vision
       .identity()
       .faceVerification(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```
