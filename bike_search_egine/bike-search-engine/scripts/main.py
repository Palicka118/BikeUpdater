from playwright.sync_api import sync_playwright
from json_operations import initialize_json, finalize_json
from playwright_operations import run

def main():
    print("Initializing JSON...")
    initialize_json()
    with sync_playwright() as playwright:
        print("Running Playwright...")
        run(playwright)
    print("Finalizing JSON...")
    finalize_json()

if __name__ == "__main__":
    main()