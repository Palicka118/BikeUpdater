from playwright.sync_api import sync_playwright
"""This script performs web automation tasks using the Playwright library and handles JSON operations.
Modules:
    - sync_playwright: Provides synchronous API for Playwright.
    - json_operations: Contains functions to initialize and finalize JSON operations.
    - playwright_operations: Contains the `run` function to perform web automation tasks.
Functions:
    - main(): Main function to initialize JSON, run Playwright, and finalize JSON.
Usage:
    Run this script directly to execute the main function."""
from json_operations import initialize_json, finalize_json
from playwright_operations import run

def main():
    """
    Main function to initialize JSON, run Playwright, and finalize JSON.

    This function performs the following steps:
    1. Initializes JSON by calling the `initialize_json` function.
    2. Uses the Playwright library to perform web automation tasks by calling the `run` function.
    3. Finalizes JSON by calling the `finalize_json` function.
    """
    print("Initializing JSON...")
    initialize_json()
    with sync_playwright() as playwright:
        print("Running Playwright...")
        run(playwright)
    print("Finalizing JSON...")
    finalize_json()

if __name__ == "__main__":
    print("Starting main.py...")
    main()
    print("main.py finished.")