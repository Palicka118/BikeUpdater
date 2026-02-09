# json_operations.py
import json
import os
import importlib.util

# Get the directory where this script is located
SCRIPTS_DIR = os.path.dirname(os.path.abspath(__file__))

json_file_path = os.path.join(SCRIPTS_DIR, "motorcycles.json")
seen_bikes_file_path = os.path.join(SCRIPTS_DIR, "seen_bikes.json")
favorite_bikes_file_path = os.path.join(SCRIPTS_DIR, "favorite_bikes.json")
JSONdata = []
seen_bikes = set()
favorite_bikes = set()

def initialize_json():
    """
    Initializes the JSON files required for the bike search engine.
    This function performs the following steps:
    1. Checks if the directory for the JSON file exists, and creates it if it does not.
    2. Removes the existing JSON file if it exists and creates a new empty JSON file.
    3. Loads the seen bikes from the seen bikes file if it exists and is valid JSON.
       If the file is empty or invalid, it initializes an empty set for seen bikes.
    4. Creates the seen bikes file if it does not exist.
    Prints status messages to indicate the progress of initialization.
    """
    print("Initializing JSON files (delegating to init_data)...")
    # Try to reuse the init_data helper in the same scripts directory to ensure
    # files exist and contain valid JSON ([]). This avoids duplicating file-creation logic.
    try:
        # local import when running inside scripts package or same directory
        from init_data import create_if_missing
    except Exception:
        # fallback: load module by path
        init_path = os.path.join(os.path.dirname(__file__), "init_data.py")
        spec = importlib.util.spec_from_file_location("init_data", init_path)
        init_mod = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(init_mod)
        create_if_missing = getattr(init_mod, "create_if_missing")

    # Ensure files exist and contain valid JSON defaults
    try:
        create_if_missing(os.path.dirname(__file__))
    except Exception as e:
        print(f"init_data.create_if_missing failed: {e}")

    # Load seen and favorite bikes (safe because init created valid JSON files)
    if os.path.exists(seen_bikes_file_path):
        try:
            with open(seen_bikes_file_path, "r", encoding="utf-8") as file:
                global seen_bikes
                seen_bikes = set(json.load(file) or [])
            print(f"Seen bikes loaded from {seen_bikes_file_path}")
        except json.JSONDecodeError:
            seen_bikes = set()
            print("Seen bikes file is invalid, initializing empty set.")
    else:
        print(f"Seen bikes file missing after init: {seen_bikes_file_path}")

    if os.path.exists(favorite_bikes_file_path):
        try:
            with open(favorite_bikes_file_path, "r", encoding="utf-8") as file:
                global favorite_bikes
                favorite_bikes = set(json.load(file) or [])
            print(f"Favorite bikes loaded from {favorite_bikes_file_path}")
        except json.JSONDecodeError:
            favorite_bikes = set()
            print("Favorite bikes file is invalid, initializing empty set.")
    else:
        print(f"Favorite bikes file missing after init: {favorite_bikes_file_path}")

def finalize_json():
    """
    Finalizes and saves JSON data to specified file paths.
    This function checks if there is new JSON data to save. If there is, it writes the JSON data
    to the specified `json_file_path` and the list of seen bikes to the `seen_bikes_file_path`.
    If there is no new data, it prints a message indicating that there is no new data to save.
    In case of any exceptions during the file operations, it prints an error message.
    Raises:
        Exception: If there is an error while writing data to the files.
    Prints:
        - "Finalizing JSON data..." at the start of the function.
        - "No new data to save." if there is no new JSON data.
        - "Data saved to {json_file_path}" after successfully saving JSON data.
        - "Seen bikes saved to {seen_bikes_file_path}" after successfully saving seen bikes data.
        - "Error saving data to {json_file_path}: {e}" if an exception occurs during file operations.
    """
    print("Finalizing JSON data...")
    if not JSONdata:
        print("No new data to save.")
        return
    try:
        with open(json_file_path, "w") as file:
            file.write(json.dumps(JSONdata, indent=4))
        print(f"Data saved to {json_file_path}")

        with open(favorite_bikes_file_path, "w") as file:
            json.dump(list(favorite_bikes), file, indent=4)
        print(f"Favorite bikes saved to {favorite_bikes_file_path}")
    except Exception as e:
        print(f"Error saving data to {json_file_path}: {e}")



def save(name, make, url, price, year, mileage, image_url):
    """
    Save bike information to a JSON-like structure if the bike hasn't been seen before.
    Args:
        name (str): The name of the bike.
        make (str): The make or brand of the bike.
        url (str): The URL where the bike is listed.
        price (float): The price of the bike.
        year (int): The manufacturing year of the bike.
        mileage (int): The mileage of the bike.
        image_url (str): The URL of the bike's image.
    Returns:
        None
    """
    bike_id = url.split("/")[-1].split(".")[0]  # Extract a unique identifier from the URL
    #if bike_id in seen_bikes:
    #    print(f"Bike {bike_id} already seen, skipping.")
    #    return

    data = {"name": name, "url": url, "price": price, "year": year, "mileage": mileage, "image_url": image_url, "make": make}
    JSONdata.append(data)
    print(f"Data added: {data}")
    print(f"Current JSONdata length: {len(JSONdata)}")



def clear_json_files():
    """
    Clears the contents of the JSON files specified by `json_file_path` and `seen_bikes_file_path`.
    
    This function opens each file in write mode and writes an empty JSON array ("[]") to them, effectively clearing their contents.
    
    Prints messages to indicate the start and completion of the clearing process.
    """
    print("Clearing JSON files...")
    with open(json_file_path, "w") as file:
        file.write("[]")
    with open(seen_bikes_file_path, "w") as file:
        file.write("[]")
    with open(favorite_bikes_file_path, "w") as file:
        file.write("[]")
    print("JSON files cleared.")