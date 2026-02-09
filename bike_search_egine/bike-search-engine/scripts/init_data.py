"""Create JSON data files if they don't already exist.

Usage:
    python scripts/init_data.py

This script will create the following files in the `scripts/` directory if missing:
- favorite_bikes.json     (empty list)
- motorcycles.json        (empty list)
- seen_bikes.json        (empty list)
"""
from __future__ import annotations

import json
import os
import sys
from typing import Any, Dict


BASE_DIR = os.path.dirname(__file__)

DEFAULTS: Dict[str, Any] = {
    "favorite_bikes.json": [],
    "motorcycles.json": [],
    "seen_bikes.json": [],
}


def create_if_missing(base_dir: str = BASE_DIR) -> int:
    """Create missing files and return number of files created."""
    created = 0
    for name, default in DEFAULTS.items():
        path = os.path.join(base_dir, name)
        if not os.path.exists(path):
            try:
                with open(path, "w", encoding="utf-8") as fh:
                    json.dump(default, fh, ensure_ascii=False, indent=2)
                print(f"Created: {path}")
                created += 1
            except OSError as exc:
                print(f"Failed to create {path}: {exc}", file=sys.stderr)
        else:
            print(f"Exists:   {path}")
    return created


if __name__ == "__main__":
    num = create_if_missing()
    if num == 0:
        print("All data files already exist.")
    else:
        print(f"Created {num} data file(s).")
