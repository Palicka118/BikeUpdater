# json_operations.py
import json
import os

json_file_path = os.path.join("bike_search_egine/bike-search-engine/scripts/motorcycles.json")
JSONdata = []

def initialize_json():
    directory = os.path.dirname(json_file_path)
    if not os.path.exists(directory):
        os.makedirs(directory)
    if os.path.exists(json_file_path):
        os.remove(json_file_path)
    open(json_file_path, "w").close()
    print(f"File initialized: {json_file_path}")

def finalize_json():
    print("Finalizing JSON data...")
    try:
        with open(json_file_path, "w") as file:
            file.write(json.dumps(JSONdata, indent=4))
        print(f"Data saved to {json_file_path}")
    except Exception as e:
        print(f"Error saving data to {json_file_path}: {e}")

def save(name, make, url, price, year, mileage, image_url):
    data = {"name": name, "url": url, "price": price, "year": year, "mileage": mileage, "image_url": image_url, "make": make}
    JSONdata.append(data)
    print(f"Data added: {data}")
    print(f"Current JSONdata length: {len(JSONdata)}")
