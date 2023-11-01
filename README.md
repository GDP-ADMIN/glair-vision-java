<p align="center">
  <a href="https://docs.glair.ai/vision" target="_blank">
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
    <a href="https://central.sonatype.com/artifact/io.github.vincentchuardi/glair-vision"><img src="https://img.shields.io/maven-central/v/io.github.vincentchuardi/glair-vision.svg" alt="Latest Release"></a>
    <a href="https://github.com/glair-ai/glair-vision-java/blob/main/LICENSE"><img src="https://img.shields.io/npm/l/@glair/vision" alt="License"></a>
</p>

## Requirement

You need <span style="color: green">**Java 8 or higher**</span>. For local development, we recommend to use [IntelliJ IDEA](https://www.jetbrains.com/idea/).

## Installation

You can refer to [this page](https://central.sonatype.com/artifact/io.github.vincentchuardi/glair-vision)

## Usage

The package needs to be configured with your credentials, see [here](https://docs.glair.ai/vision/authentication) for more details.

```java
import glair.vision.Vision;
import glair.vision.model.VisionSettings;

public class App {
   public static void main(String[] args) {
      VisionSettings visionSettings = new VisionSettings.Builder()
         .username("username")
         .password("password")
         .apiKey("api-key")
         .build();

      Vision vision = new Vision(visionSettings);
   }
}
```

The SDK's VisionSettings can be initialized with several options:

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
import glair.vision.Vision;
import glair.vision.model.VisionSettings;

public class App {
   public static void main(String[] args) {
      VisionSettings visionSettings = new VisionSettings.Builder()
              .username("username")
              .password("password")
              .apiKey("api-key")
              .build();

      Vision vision = new Vision(visionSettings);

      String imagePath = "/path/to/image.jpg";
      String response = "";

      try {
         VisionSettings newVisionSettings = new VisionSettings.Builder()
                 .apiKey("new-api-key")
                 .build();

         response = vision
                 .ocr()
                 .npwp(imagePath, newVisionSettings);
         // The Configurations' values that will be used on NPWP are
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

The second parameter is a new `VisionSettings` object.
It will be merged with the original `VisionSettings` that you set when instantiating the Vision instance.

## Logging

The GLAIR Vision SDK provides comprehensive logging capabilities to help you monitor and debug your application's interactions with GLAIR's services. By default, the SDK includes a logging output that you can customize to suit your needs.

### Checking Logger Configuration

To inspect the current logger configuration, you can use the `printLoggerConfig()` method within the `Vision` object. This allows you to see the current logging settings.

```java
VisionSettings visionSettings = new VisionSettings.Builder()
   .username("username")
   .password("password")
   .apiKey("apiKey")
   .build();

Vision vision = new Vision(visionSettings);

// Check the current logger configuration
vision.printLoggerConfig();
```

### Customizing Logger Configuration

You can customize the logger configuration when initializing the `Vision` object. This enables you to control the log level and log pattern according to your preferences.

Here's how you can set up the logger configuration:

```java
VisionSettings visionSettings = new VisionSettings.Builder()
   .username("username")
   .password("password")
   .apiKey("apiKey")
   .build();

// Define your custom logger configuration
LoggerConfig loggerConfig = new LoggerConfig(LoggerConfig.INFO);

Vision vision = new Vision(visionSettings, loggerConfig);
```

#### LoggerConfig Options

The `LoggerConfig` class provides several options to tailor the logging behavior:

| Option       | Default                                               | Description                                                                                                                                                                                                                                            |
| ------------ | ----------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `logLevel`   | `LoggerConfig.INFO`                                   | Specifies the level of logging, ranging from `ERROR` (highest severity) to `WARN`, `INFO`, and `DEBUG` (lowest severity). Adjust this setting to control the verbosity of your logs.                                                                   |
| `logPattern` | `[{timestamp}] [{level}] GLAIR Vision SDK: {message}` | Defines the format of log entries. You can customize the placement and content of log components using placeholders like `{timestamp}`, `{level}`, and `{message}`. Modify the log pattern to include additional context or change the display format. |

By adjusting these options, you can fine-tune the logging behavior of the GLAIR Vision SDK to match your application's requirements.

## FAQ

1. Can this SDK be used in an Android Studio project?
   - Yes, you can include this SDK as a dependency in your `build.gradle` (or `build.gradle.kts`) file.
   - Any project that can use dependencies from the Maven Central Repository and is running on Java 8 or higher can utilize this SDK.

---

## OCR

### KTP

#### Without Qualities

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   KtpParam param = new KtpParam("path/to/image.jpg");
   response = vision
      .ocr()
      .ktp(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### With Qualities

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   KtpParam param = new KtpParam("path/to/image.jpg", true);
   response = vision
      .ocr()
      .ktp(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

The `KtpParam` class is used for the necessary KTP API. It consists of the `imagePath` and `qualitiesDetector` attributes. The `qualitiesDetector` attribute is used only when you need to enable qualities detection for KTP document.

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

#### Without Specific Page

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   BpkbParam param = new BpkbParam(imagePath);
   response = vision
      .ocr()
      .bpkb(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### With Specific Page

```java
String imagePath = "/path/to/image.jpg";
String response = "";

try {
   BpkbParam param = new BpkbParam(imagePath, 1);
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
      .licensePlate(imagePath);
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
   FaceMatchParam matchParam = new FaceMatchParam(capturedPath,
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
   response = vision
      .faceBio()
      .passiveLiveness(imagePath);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Active Liveness

```java
String imagePath = "/path/to/image.jpg";
String response = "";
GestureCode gestureCode = GestureCode.HAND_00000

try {
   ActiveLivenessParam param =
      new ActiveLivenessParam(
         imagePath,
         gestureCode);
   response = vision
      .faceBio()
      .activeLiveness(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

## Session

When creating a session using the GLAIR Vision SDK, you'll need to use the `BaseSessionsParam` class as a parameter. `BaseSessionsParam` requires the `successUrl` to be provided in the constructor, and you can optionally set the `cancelUrl` using the `setCancelUrl` method.

```java
BaseSessionsParam param = new BaseSessionsParam("https://docs.glair.ai/vision?success=true");
param.setCancelUrl("https://docs.glair.ai/vision?success=false");

response = vision
   .ocr()
   .npwpSessions()
   .create(param);
```

### KTP Sessions

For KTP sessions, you'll use the `KtpSessionsParam` class, which is a class that inherits from `BaseSessionsParam`.

#### Create Session without Qualities Detector

You can create a KTP session without specifying the qualities detector as follows:

```java
String response = "";

try {
   KtpSessionsParam param = new KtpSessionsParam("https://docs.glair.ai/vision?success=true");

   response = vision
      .ocr()
      .ktpSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Create Session with Qualities Detector

To create a KTP session with the qualities detector enabled, use the following code:

```java
String response = "";

try {
   KtpSessionsParam param = new KtpSessionsParam("https://docs.glair.ai/vision?success=true");
   param.setQualitiesDetector(true);

   response = vision
      .ocr()
      .ktpSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Retrieve Session

To retrieve a KTP session, you can use the following code:

```java
String response = "";

try {
   response = vision
      .ocr()
      .ktpSessions()
      .retrieve("session-id");
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### NPWP Sessions

For NPWP sessions, you'll use the `BaseSessionsParam` class.

#### Create Session

You can create a NPWP session as follows:

```java
String response = "";

try {
   BaseSessionsParam param = new BaseSessionsParam("https://docs.glair.ai/vision?success=true");

   response = vision
      .ocr()
      .npwpSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Retrieve Session

To retrieve a NPWP session, you can use the following code:

```java
String response = "";

try {
   response = vision
      .ocr()
      .npwpSessions()
      .retrieve("session-id");
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

### Passive Liveness Sessions

For Passive Liveness sessions, you'll use the `BaseSessionsParam` class.

#### Create Session

You can create a Passive Liveness session as follows:

```java
String response = "";

try {
   BaseSessionsParam param = new BaseSessionsParam("https://docs.glair.ai/vision?success=true");

   response = vision
      .faceBio()
      .passiveLivenessSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Retrieve Session

To retrieve a NPWP session, you can use the following code:

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

For Active Liveness sessions, you'll use the `ActiveLivenessSessionsParam` class, which is a class that inherits from `BaseSessionsParam`.

#### Create Session with Default Number of Gestures

You can create a Active Liveness session with default number of gestures as follows:

```java
String response = "";

try {
   ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://docs.glair.ai/vision?success=true");

   response = vision
      .faceBio()
      .activeLivenessSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Create Session with Custom Number of Gestures

You can create a Active Liveness session with custom number of gestures as follows:

```java
String response = "";

try {
   ActiveLivenessSessionsParam param = new ActiveLivenessSessionsParam("https://docs.glair.ai/vision?success=true");
   param.setNumberOfGestures(2);

   response = vision
      .faceBio()
      .activeLivenessSessions()
      .create(param);
} catch (Exception e) {
   response = e.getMessage();
}

System.out.println("Response: " + response);
```

#### Retrieve Session

To retrieve a Active Liveness session, you can use the following code:

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
   IdentityVerificationParam param =
      new IdentityVerificationParam.Builder()
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
   IdentityFaceVerificationParam param =
      new IdentityFaceVerificationParam.Builder()
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
