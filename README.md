## Requirements
* JDK 21 Corretto.
  If you use https://sdkman.io 
  ```bash
  sdk install java 21.0.5-amzn
  ```
  [How to change JDK in IDE](https://www.jetbrains.com/help/idea/sdk.html#jdk)

## How to run tests
```bash
./gradlew test --continue
```

## Local run 
```bash
./gradlew quarkusRun
```

### Example: Using Own Eggs / Did Not Previously Attempt IVF / Known Infertility Reason
```bash
curl --location 'http://localhost:8080/computeFertilityMetrics' \
--header 'Content-Type: application/json' \
--data '{
    "usingOwnEggs": true,
    "attemptedIvfPreviously": false,
    "isReasonForInfertilityKnown": true,
    
    "age": 32,
    "weight": 150,
    "heightFeet": 5,
    "heightInch": 8,

    "tubalFactor": false,
    "maleFactorInfertility": false,
    "endometriosis": true,
    "ovulatoryDisorder": true,
    "diminishedOvarianReserve": false,
    "uterineFactor": false,
    "otherReason": false,

    "isReasonForInfertilityUnexplained": false,

    "numberOfPriorPregnancies": 1,
    "numberOfLiveBirths": 1
}'
```

### Example: Using Own Eggs / Did Not Previously Attempt IVF / Unknown Infertility Reason
```bash
curl --location 'http://localhost:8080/computeFertilityMetrics' \
--header 'Content-Type: application/json' \
--data '{
    "usingOwnEggs": true,
    "attemptedIvfPreviously": false,
    "isReasonForInfertilityKnown": false,
    
    "age": 32,
    "weight": 150,
    "heightFeet": 5,
    "heightInch": 8,

    "tubalFactor": false,
    "maleFactorInfertility": false,
    "endometriosis": false,
    "ovulatoryDisorder": false,
    "diminishedOvarianReserve": false,
    "uterineFactor": false,
    "otherReason": false,

    "isReasonForInfertilityUnexplained": false,

    "numberOfPriorPregnancies": 1,
    "numberOfLiveBirths": 1
}'
```

### Example: Using Own Eggs / Previously Attempted IVF / Known Infertility Reason
```bash
curl --location 'http://localhost:8080/computeFertilityMetrics' \
--header 'Content-Type: application/json' \
--data '{
    "usingOwnEggs": true,
    "attemptedIvfPreviously": true,
    "isReasonForInfertilityKnown": true,
    
    "age": 32,
    "weight": 150,
    "heightFeet": 5,
    "heightInch": 8,

    "tubalFactor": true,
    "maleFactorInfertility": false,
    "endometriosis": false,
    "ovulatoryDisorder": false,
    "diminishedOvarianReserve": true,
    "uterineFactor": false,
    "otherReason": false,

    "isReasonForInfertilityUnexplained": false,

    "numberOfPriorPregnancies": 1,
    "numberOfLiveBirths": 1
}'
```

### How to build
```bash
./gradlew build -Dquarkus.package.jar.enabled=true -Dquarkus.package.jar.type=uber-jar
```

### How to run jar
```bash
java -jar build/ivf-success-calculator-unspecified-runner.jar
```