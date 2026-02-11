# Bike Search Engine

## Overview

The Bike Search Engine is a JavaFX application that allows users to search for motorcycle listings from a specified
website. The application uses Playwright for web scraping and JSON for data storage. The user interface is built using
JavaFX, and the application is packaged and managed using Maven.

## Features

- Load and display motorcycle listings from a specified website.
- Clear the stored data.
- User-friendly interface with JavaFX.
- Automatic data file initialization via Python script.
- Robust path resolution for flexible working directories.

## Dependencies

### Java

- Java Development Kit (JDK) 23 or higher

### Maven

- Apache Maven 3.10 or higher

### Python

- Python 3.13 or higher
- Playwright

### Libraries

- JavaFX 23.0.1
- Gson 2.8.8
- JUnit 5 (Jupiter) 5.9.3
- org.json 20210307

## Installation

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 23 or higher installed. You can download it
   from [Oracle's website](https://www.oracle.com/java/technologies/javase-jdk23-downloads.html).

2. **Apache Maven**: Ensure you have Maven installed. You can download it
   from [Maven's website](https://maven.apache.org/download.cgi).

3. **Python**: Ensure you have Python 3.13 or higher installed. You can download it
   from [Python's website](https://www.python.org/downloads/).

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

### Initialize Data Files

The application automatically initializes required JSON data files on startup. You can also manually initialize them:

```sh
python scripts/init_data.py
```

This creates:

- `scripts/motorcycles.json` (empty list)
- `scripts/seen_bikes.json` (empty list)
- `scripts/favorite_bikes.json` (empty list)

### Run the Application

#### Using Maven (Recommended)

```sh
mvn javafx:run
```

#### Using IDE

- Open the project in VS Code or your IDE
- Click **Run** or **Debug** on the `Launcher` class

#### Using Java Directly

```sh
cd bike-search-engine
java -cp target/classes com.pawel.Launcher
```

## Running Tests

Run unit tests using Maven:

```sh
mvn test
```

Tests are written using JUnit 5 and located in `src/test/java/`.

## Project Structure

```
bike-search-engine/
├── .vscode/
│   ├── launch.json              # VS Code run/debug configurations
│   └── settings.json            # Java and editor settings
├── pom.xml                      # Maven configuration with centralized versions
├── README.md
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/pawel/
│   │   │       ├── BikeSearchApp.java
│   │   │       ├── BikeSearchAppController.java
│   │   │       ├── DataUtils.java
│   │   │       ├── Launcher.java
│   │   │       └── UIComponents.java
│   │   └── resources/
│   │       └── com/pawel/
│   │           └── BikeSearchApp.fxml
│   └── test/
│       └── java/
│           └── com/pawel/
│               └── AppTest.java           # JUnit 5 tests
├── scripts/
│   ├── init_data.py             # Initialize JSON data files
│   ├── json_operations.py       # JSON file operations (reads/writes data)
│   ├── main.py                  # Web scraping entry point
│   └── playwright_operations.py # Playwright web automation
```

## Configuration

### pom.xml Improvements

The `pom.xml` has been optimized with:

- **Centralized version properties** for easy version management
- **Maven Compiler Plugin 3.10.1** using `<release>${java.version}</release>`
- **Maven Enforcer Plugin** to ensure correct Java version is used
- **Surefire Plugin 3.0.0** for JUnit 5 test execution
- **JavaFX Gradle Plugin** with JVM args to suppress native access warnings

### JavaFX Warnings

The application may show warnings about deprecated native methods:

```
WARNING: A restricted method in java.lang.System has been called
WARNING: sun.misc.Unsafe::allocateMemory has been called
```

These are non-fatal warnings from JavaFX/JDK internals and do not affect functionality. They are suppressed when running
via Maven (`mvn javafx:run`).

### Path Resolution

The application intelligently resolves file paths to work regardless of the working directory:

- Checks both `scripts/` and `bike-search-engine/scripts/` for data files
- Automatically uses the correct path found
- Falls back to defaults if files don't exist yet (they'll be created by `init_data.py`)

## Usage

1. **Initialize Data** (first run):
   ```sh
   python scripts/init_data.py
   ```

2. **Load Motorcycles**: Click the "Load Motorcycles" button to fetch and display motorcycle listings.

3. **Clear Memory**: Click the "Clear Memory" button to clear stored data.

4. **Manage Favorites**: Add/remove bikes from your favorites list.

## Troubleshooting

### Maven not found

Install Maven or add it to your system PATH.

### Python script not found

Ensure you're running from the project root directory, or use the full path:

```sh
python bike-search-engine/scripts/main.py
```

### FileNotFoundException for JSON files

Run the initialization script first:

```sh
python scripts/init_data.py
```

## Recent Improvements (2026)

✅ Migrated from JUnit 4 to JUnit 5  
✅ Updated JavaFX to 23.0.1  
✅ Centralized Maven configuration with version properties  
✅ Added Maven Enforcer Plugin for Java version validation  
✅ Created data file initialization script (`init_data.py`)  
✅ Implemented robust path resolution for cross-platform compatibility  
✅ Added VS Code launch and settings configurations  
✅ Updated test framework to use JUnit 5 Jupiter API

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.
