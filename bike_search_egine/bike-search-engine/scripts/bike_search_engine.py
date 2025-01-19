from playwright.sync_api import Playwright, sync_playwright, expect
import json
import os

# Define the path to the JSON file
json_file_path = os.path.join("bike_search_egine/bike-search-engine/scripts/motorcycles.json")
# List of motorcycle makes and models
makes = ["bmw", "triumph"]
yamaha = ["yamaha-yzf-r6", "yamaha-yzf-r6r", "yamaha-yzf-r1", "yamaha-yzf-r3", "yamaha-yzf-r7"]
kawasaki = ["kawasaki-zx-6r-ninja", "kawasaki-zx-10r-ninja", "kawasaki-er-6f", "kawasaki-z900", "kawasaki-z-1000", "kawasaki-ninja-650", "kawasaki-zx-4r-ninja"]
suzuki = ["suzuki-gsx-8r", "suzuki-gsx-r-1000", "suzuki-gsx-r-1100","suzuki-gsx-r-1300-hayabusa", "suzuki-gsx-r-600", "suzuki-gsx-r-750", "suzuki-gsx-s1000"]
bmw = ["bmw-s-1000-rr"]
triumph = ["triumph-daytona-660", "triumph-daytona-675"]
aprilia = ["aprilia-rs-660", "aprilia-rsv-4", "aprilia-rsv-4-factory", "aprilia-rsv-4-rf", "aprilia-rsv-4-rr", "aprilia-rsv-4-1100-factory", "aprilia-rsv-4-1100-rr", "aprilia-rsv-4-1100-rf"]
honda = ["honda-cbr-600rr", "honda-cbr-650r", "honda-cbr-1000rr-fireblade", "honda-cbr-500-r"]
ducati = ["ducati-1098", "ducati-1098-s", "ducati-1198", "ducati-1198-s", "ducati-1199-panigale", "ducati-1299-panigale", "ducati-750-sport", "ducati-848", "ducati-848-evo", "ducati-851", "ducati-959-panigale", "ducati-996", "ducati-998", "ducati-panigale-v2", "ducati-panigale-v4", "ducati-panigale-v4-r", "ducati-streetfighter-1098-s", "ducati-streetfighter-848-s", "ducati-streetfighter-v2", "ducati-streetfighter-v4", "ducati-streetfighter-v4-s", "ducati-supersport"]
JSONdata = []

def initialize_json():
    directory = os.path.dirname(json_file_path)
    if not os.path.exists(directory):
        os.makedirs(directory)
    # Delete the file if it exists
    if os.path.exists(json_file_path):
        os.remove(json_file_path)
    # Create a new empty file
    open(json_file_path, "w").close()
    print(f"File initialized: {json_file_path}")

def finalize_json():
    with open(json_file_path, "w") as file:  # Changed "a" to "w" to overwrite the file
        file.write(json.dumps(JSONdata, indent=4))

def save(name, make, url, price, year, mileage, image_url):
    data = {"name": name, "url": url, "price": price, "year": year, "mileage": mileage, "image_url": image_url, "make": make}
    JSONdata.append(data)

def checkMotorcycleModel(make, models, LISTSECTION, page):
    for model in models:
        try:
            page.locator("#znacka").select_option(make)
            page.locator("#model").select_option(model)
            page.wait_for_load_state("load")
            page.get_by_role("button", name="Hledat inzeráty").click()
            page.wait_for_load_state("load")

            rows = LISTSECTION.get_by_role("listitem")
            count = rows.count()
            for i in range(count):
                try:
                    image_url = rows.nth(i).locator("div.thumb img").get_attribute("src")
                    name = rows.nth(i).locator("div.description h3").text_content().strip()

                    price_value = rows.nth(i).locator('td:has-text("Kč")')
                    if price_value.count() > 0:
                        price_value = price_value.text_content().strip()

                    year_value = rows.nth(i).locator('p:has-text("Ročník:") strong').first.text_content().strip()
                    mileage_locator = rows.nth(i).locator('td:has-text("km")')
                    if mileage_locator.count() > 0:
                        mileage_value = mileage_locator.text_content().strip()
                    else:
                        mileage_value = "N/A"
                    # Locate the photo elements and extract their src attributes
                    url = rows.nth(i).locator("div.thumb a").get_attribute("href")
                    save(name, make, url, price_value, year_value, mileage_value, image_url)
                except Exception as e:
                    print(f"Error processing listing {i} for model {model}: {e}")
                    continue
        except Exception as e:
            print(f"Error selecting model {model} for make {make}: {e}")
            continue

def checkMotorcycleMake(LISTSECTION, page):
    for make in makes:
        if make == "yamaha":
            checkMotorcycleModel("yamaha", yamaha, LISTSECTION, page)
        elif make == "kawasaki":
            checkMotorcycleModel("kawasaki", kawasaki, LISTSECTION, page)
        elif make == "bmw":
            checkMotorcycleModel("bmw", bmw, LISTSECTION, page)
        elif make == "suzuki":
            checkMotorcycleModel("suzuki", suzuki, LISTSECTION, page)
        elif make == "triumph":
            checkMotorcycleModel("triumph", triumph, LISTSECTION, page)
        elif make == "honda":
            checkMotorcycleModel("honda", honda, LISTSECTION, page)
        elif make == "ducati":
            checkMotorcycleModel("ducati", ducati, LISTSECTION, page)
        elif make == "aprilia":
            checkMotorcycleModel("aprilia", aprilia, LISTSECTION, page)

def run(playwright: Playwright) -> None:
    browser = playwright.chromium.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()

    LISTSECTION = page.locator("//*[@id=\"content\"]/div/ul")

    page.goto("https://www.motorkari.cz/motobazar/motorky/")
    page.wait_for_load_state("load")

    initialize_json()
    checkMotorcycleMake(LISTSECTION, page)
    finalize_json()
    # ----------------------------------------------------------------------------
    context.close()
    browser.close()

with sync_playwright() as playwright:
    run(playwright)