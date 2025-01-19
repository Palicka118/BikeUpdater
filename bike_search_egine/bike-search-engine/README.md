# Bike Search Engine

## Overview

The Bike Search Engine is a JavaFX application that allows users to search for motorcycle listings from a specified website. The application uses Playwright for web scraping and JSON for data storage. The user interface is built using JavaFX, and the application is packaged and managed using Maven.

## Features

- Load and display motorcycle listings from a specified website.
- Clear the stored data.
- User-friendly interface with JavaFX.

## Dependencies

### Java

- Java Development Kit (JDK) 23 or higher

### Maven

- Apache Maven 3.13 or higher

### Python

- Python 3.13 or higher
- Playwright

### Libraries

- JavaFX 17
- Gson 2.8.8
- JUnit 4.11
- org.json 20210307

## Installation

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 23 or higher installed on your system. You can download it from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk23-downloads.html).

2. **Apache Maven**: Ensure you have Maven installed on your system. You can download it from [Maven's website](https://maven.apache.org/download.cgi).

3. **Python**: Ensure you have Python 3.13 or higher installed on your system. You can download it from [Python's website](https://www.python.org/downloads/).

4. **Playwright**: Install Playwright using pip:
   ```sh
   pip install playwright
   playwright install
   ```

### Clone the Repository

Clone the repository to your local machine:
```sh
git clone https://github.com/palicka118/bike-search-engine.git
cd bike-search-engine
```

### Build the Project

Navigate to the project directory and build the project using Maven:
```sh
mvn clean install
```

### Run the Application

Run the application using Maven:
```sh
mvn javafx:run
```

## Project Structure

```
bike-search-engine/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── pawel/
│   │   │           ├── BikeSearchApp.java
│   │   │           ├── BikeSearchAppController.java
│   │   │           ├── DataUtils.java
│   │   │           ├── Launcher.java
│   │   │           ├── MotorcycleLoader.java
│   │   │           └── UIComponents.java
│   │   ├── resources/
│   │   │   └── com/
│   │   │       └── pawel/
│   │   │           └── BikeSearchApp.fxml
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── pawel/
│   │   │           └── BikeSearchAppTest.java
├── scripts/
│   ├── json_operations.py
│   ├── main.py
│   └── playwright_operations.py
```

## Usage

1. **Load Motorcycles**: Click the "Load Motorcycles" button to fetch and display motorcycle listings from the specified website.
2. **Clear Memory**: Click the "Clear Memory" button to clear the stored data.

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.