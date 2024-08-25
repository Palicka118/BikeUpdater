from playwright.sync_api import Playwright, sync_playwright, expect
import json

yamaha = ["yamaha-yzf-r6", "yamaha-yzf-r1", "yamaha-yzf-r3", "yamaha-yzf-r7"]


def save(nazev, url):
    f = open("results.txt", "r")
    if(f.read().__contains__(nazev + " ---- " +  url)):
        f.close
    else:
        f.close()
        f = open("results.txt", "a")
        f.write(nazev + " ---- " + url + "\n")
        f.close()
    print(nazev, url)


def run(playwright: Playwright) -> None:
    


    browser = playwright.chromium.launch(headless=False)
    context = browser.new_context()
    page = context.new_page()

    LISTSECTION =page.locator("//*[@id=\"content\"]/div/ul")
    heading = page.locator("//*[@id=\"content\"]/div/div[2]/div")

    page.goto("https://www.motorkari.cz/motobazar/motorky/")
    page.wait_for_load_state("load")

    page.locator("#znacka").select_option("yamaha")
    page.locator("#model").select_option("yamaha-yzf-r6")
    page.wait_for_load_state("load")
    page.get_by_role("button", name="Hledat inzeráty").click()
    page.wait_for_load_state("load")

    rows = LISTSECTION.get_by_role("listitem")
    count = rows.count()
    for i in range(count):
        rows.nth(i).get_by_role("heading").get_by_role("link").click()
        page.wait_for_load_state("load")


        headings = heading.get_by_role("heading")
        name = headings.nth(0).text_content()
        url = page.url
        save(name, url)
        page.go_back()
        page.wait_for_load_state("load")
    f = open("results.txt", "a")
    f.write("------------------------------------------------------------------------------ \n")
    f.close()
    page.wait_for_timeout(5000)


    # ----------------------------------------------------------------------------
    context.close()
    browser.close()


with sync_playwright() as playwright:
    run(playwright)
