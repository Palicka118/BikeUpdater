from playwright.sync_api import Playwright
"""
This module contains functions to scrape motorcycle listings from a website using Playwright.
Functions:
    process_listing(rows, i, make, model):
        Processes a single motorcycle listing and saves the details.
    checkMotorcycleModel(make, models, LISTSECTION, page):
        Iterates over a list of motorcycle models for a given make and processes each listing.
    checkMotorcycleMake(LISTSECTION, page):
        Iterates over a dictionary of motorcycle makes and their respective models, and processes each listing.
    run(playwright: Playwright) -> None:
        Main function to start the Playwright browser, navigate to the website, and initiate the scraping process.
"""
from json_operations import save

yamaha = ["yamaha-yzf-r6", "yamaha-yzf-r6r", "yamaha-yzf-r1", "yamaha-yzf-r3", "yamaha-yzf-r7"]
kawasaki = ["kawasaki-zx-6r-ninja", "kawasaki-zx-10r-ninja", "kawasaki-er-6f", "kawasaki-z900", "kawasaki-z-1000", "kawasaki-ninja-650", "kawasaki-zx-4r-ninja"]
suzuki = ["suzuki-gsx-8r", "suzuki-gsx-r-1000", "suzuki-gsx-r-1100","suzuki-gsx-r-1300-hayabusa", "suzuki-gsx-r-600", "suzuki-gsx-r-750", "suzuki-gsx-s1000"]
bmw = ["bmw-s-1000-rr"]
triumph = ["triumph-daytona-660", "triumph-daytona-675"]
aprilia = ["aprilia-rs-660", "aprilia-rsv-4", "aprilia-rsv-4-factory", "aprilia-rsv-4-rf", "aprilia-rsv-4-rr", "aprilia-rsv-4-1100-factory", "aprilia-rsv-4-1100-rr", "aprilia-rsv-4-1100-rf"]
honda = ["honda-cbr-600rr", "honda-cbr-650r", "honda-cbr-1000rr-fireblade", "honda-cbr-500-r"]
ducati = ["ducati-1098", "ducati-1098-s", "ducati-1198", "ducati-1198-s", "ducati-1199-panigale", "ducati-1299-panigale", "ducati-750-sport", "ducati-848", "ducati-848-evo", "ducati-851", "ducati-959-panigale", "ducati-996", "ducati-998", "ducati-panigale-v2", "ducati-panigale-v4", "ducati-panigale-v4-r", "ducati-streetfighter-1098-s", "ducati-streetfighter-848-s", "ducati-streetfighter-v2", "ducati-streetfighter-v4", "ducati-streetfighter-v4-s", "ducati-supersport"]

def process_listing(rows, i, make, model):
    """
    Processes a single listing from the provided rows and extracts relevant information.
    Args:
        rows (Locator): The Playwright locator object containing the rows of listings.
        i (int): The index of the row to process.
        make (str): The make of the bike.
        model (str): The model of the bike.
    Extracted Information:
        - Image URL
        - Name
        - Price (in Kč or EUR)
        - Year
        - Mileage
        - URL
    The extracted information is then saved using the `save` function.
    Raises:
        Exception: If there is an error processing the listing, it prints an error message with the listing index and model.
    """
    try:
        image_url = rows.nth(i).locator("div.thumb img").get_attribute("src")
        name = rows.nth(i).locator("div.description h3").text_content().strip()

        price_value = rows.nth(i).locator('td:has-text("Kč")')
        if price_value.count() > 0:
            price_value = price_value.text_content().strip()
        else: 
            price_value = rows.nth(i).locator('td:has-text("EUR")')
            if price_value.count() > 0:
                price_value = price_value.text_content().strip()
            else: price_value = "N/A"

        year_value = rows.nth(i).locator('p:has-text("Ročník:") strong').first.text_content().strip()
        mileage_locator = rows.nth(i).locator('td:has-text("km")')
        mileage_value = mileage_locator.text_content().strip() if mileage_locator.count() > 0 else "N/A"
        url = rows.nth(i).locator("div.thumb a").get_attribute("href")
        save(name, make, url, price_value, year_value, mileage_value, image_url)
    except Exception as e:
        print(f"Error processing listing {i} for model {model}: {e}")

def checkMotorcycleModel(make, models, LISTSECTION, page):
    """
    Selects a motorcycle make and model from dropdowns on a webpage, searches for listings, 
    and processes each listing found.
    Args:
        make (str): The make of the motorcycle to select.
        models (list): A list of motorcycle models to select.
        LISTSECTION (Locator): The locator for the section containing the list of search results.
        page (Page): The Playwright page object representing the browser page.
    Raises:
        Exception: If there is an error selecting a model or processing the listings.
    """
    for model in models:
        try:
            page.locator("#znacka").select_option(make)
            page.locator("#model").select_option(model)
            page.wait_for_load_state("load")
            page.get_by_role("button", name="Hledat inzeráty").click()
            page.wait_for_load_state("load")

            rows = LISTSECTION.get_by_role("listitem")
            for i in range(rows.count()):
                process_listing(rows, i, make, model)
        except Exception as e:
            print(f"Error selecting model {model} for make {make}: {e}")

def checkMotorcycleMake(LISTSECTION, page):
    """
    Checks the motorcycle make and calls the function to check the motorcycle model.

    Args:
        LISTSECTION (str): The section of the list to check.
        page (playwright.sync_api.Page): The Playwright page object to interact with the web page.

    Returns:
        None
    """
    make_models = {
        "yamaha": yamaha,
        "kawasaki": kawasaki,
        "bmw": bmw,
        "suzuki": suzuki,
        "triumph": triumph,
        "honda": honda,
        "ducati": ducati,
        "aprilia": aprilia
    }
    for make in make_models:
        checkMotorcycleModel(make, make_models[make], LISTSECTION, page)

def run(playwright: Playwright) -> None:
    """
    Launches a Playwright browser instance, navigates to a motorcycle marketplace,
    and performs operations to check motorcycle makes.
    Args:
        playwright (Playwright): The Playwright instance to use for browser automation.
    Returns:
        None
    """
    print("Starting Playwright...")
    browser = playwright.chromium.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()

    LISTSECTION = page.locator("//*[@id=\"content\"]/div/ul")

    page.goto("https://www.motorkari.cz/motobazar/motorky/")
    page.wait_for_load_state("load")

    checkMotorcycleMake(LISTSECTION, page)
    context.close()
    browser.close()
    print("Playwright finished.")
