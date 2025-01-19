# json_operations.py
import json
import os

json_file_path = os.path.join("bike_search_egine/bike-search-engine/scripts/motorcycles.json")
seen_bikes_file_path = os.path.join("bike_search_egine/bike-search-engine/scripts/seen_bikes.json")
JSONdata = []
seen_bikes = set()

def initialize_json():
    print("Initializing JSON files...")
    directory = os.path.dirname(json_file_path)
    if not os.path.exists(directory):
        os.makedirs(directory)
    if os.path.exists(json_file_path):
        os.remove(json_file_path)
    open(json_file_path, "w").close()
    print(f"File initialized: {json_file_path}")

    if os.path.exists(seen_bikes_file_path):
        try:
            with open(seen_bikes_file_path, "r") as file:
                global seen_bikes
                seen_bikes = set(json.load(file))
            print(f"Seen bikes loaded from {seen_bikes_file_path}")
        except json.JSONDecodeError:
            seen_bikes = set()
            print(f"Seen bikes file is empty or invalid, initializing empty set.")
    else:
        open(seen_bikes_file_path, "w").close()
        print(f"Seen bikes file created: {seen_bikes_file_path}")

def finalize_json():
    print("Finalizing JSON data...")
    if not JSONdata:
        print("No new data to save.")
        return
    try:
        with open(json_file_path, "w") as file:
            file.write(json.dumps(JSONdata, indent=4))
        print(f"Data saved to {json_file_path}")

        with open(seen_bikes_file_path, "w") as file:
            json.dump(list(seen_bikes), file, indent=4)
        print(f"Seen bikes saved to {seen_bikes_file_path}")
    except Exception as e:
        print(f"Error saving data to {json_file_path}: {e}")

def save(name, make, url, price, year, mileage, image_url):
    bike_id = url.split("/")[-1].split(".")[0]  # Extract a unique identifier from the URL
    if bike_id in seen_bikes:
        print(f"Bike {bike_id} already seen, skipping.")
        return

    data = {"name": name, "url": url, "price": price, "year": year, "mileage": mileage, "image_url": image_url, "make": make}
    JSONdata.append(data)
    seen_bikes.add(bike_id)