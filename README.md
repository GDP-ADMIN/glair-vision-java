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
    <a href="https://github.com/glair-ai/glair-vision-java/releases"><img src="https://img.shields.io/jitpack/v/@glair/vision" alt="Latest Release"></a>
    <a href="https://github.com/glair-ai/glair-vision-java/blob/main/LICENSE"><img src="https://img.shields.io/npm/l/@glair/vision" alt="License"></a>
</p>

## Requirement

1. JDK 17 or later
2. Gradle 8.1.1 or later


## Installation

### Gradle

Add this dependency to your project's build file:

```groovy
implementation 'com.glair:glair-vision-java:0.0.1'
```

### Others
                            
You'll need to manually install the following JARs:

1. [GLAIR Vision JAR](https://drive.google.com/file/d/1D6PUyP_C5AdekwcFjKFu5rpaMp9yg-O8/view?usp=sharing)
   1. Put the jar in the same level as `build.gradle`
   1. Add this dependency to your project's build file:
      
```groovy
implementation files('glairVision-0.0.1.jar')
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

The second parameter is a new `Settings`.
It will be merged with the original `Settings` you set when instantiating the Vision instance.

## Usage with Frameworks

_in development._

## FAQ

1. IntelliJ IDE can't find the module?
   - Only occured when library installation using JAR
   - Remember to add the dependency on `build.gradle(.kts)`
   - Import the library to IDE by File > Project Structure > Modules > 
     Dependencies > Add the JAR > OK

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

### NPWP

_in development_.

### KK

_in development_.

### STNK

_in development_.

### BPKB

_in development_.

### Passport

_in development_.

### License Plate

_in development_.

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

### Passive Liveness Sessions

Create session

```java
String response = "";

try {
   PassiveLivenessSessions.CreateParam param =
      new PassiveLivenessSessions.CreateParam.Builder(
         "https://docs.glair.ai?success=true")
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
   PassiveLivenessSessions.RetrieveParam param =
      new PassiveLivenessSessions.RetrieveParam(
         "session-id");
   response = vision
      .faceBio()
      .passiveLivenessSessions()
      .retrieve(param);
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
      new ActiveLivenessSessions.CreateParam.Builder(
         "https://docs.glair.ai?success=true")
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
   ActiveLivenessSessions.RetrieveParam param =
      new ActiveLivenessSessions.RetrieveParam(
         "session-id");
   response = vision
      .faceBio()
      .activeLivenessSessions()
      .retrieve(param);
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
