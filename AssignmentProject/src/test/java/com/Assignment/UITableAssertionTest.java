package com.Assignment;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class UITableAssertionTest {
	WebDriver driver;

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver-win64\\chromedriver.exe");

		// Initialize the WebDriver
		driver = new ChromeDriver();

		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		// Enter the URL
		driver.get("https://testpages.herokuapp.com/styled/tag/dynamic-table.html");

		driver.findElement(By.xpath("//summary[text() = 'Table Data']")).click();
		WebElement element = driver.findElement(By.xpath("//textarea[@id = 'jsondata']"));
		element.clear();
		element.click();

		// Enter the given data in the table
		element.sendKeys(
				"[{\"name\" : \"Bob\", \"age\" : 20, \"gender\": \"male\"}, {\"name\": \"George\", \"age\" : 42, \"gender\": \"male\"}, {\"name\": \"Sara\", \"age\" : 42, \"gender\": \"female\"}, {\"name\": \"Conor\", \"age\" : 40, \"gender\": \"male\"}, {\"name\": \"Jennifer\", \"age\" : 42, \"gender\": \"female\"}]");

		driver.findElement(By.xpath("//button[@id = 'refreshtable']")).click();
	}

	@Test
	public void assertUITableData() {

		// Define the expected data
		String[][] expectedData = 
				{ { "Bob", "20", "male" },
				{ "George", "42", "male" }, 
				{ "Sara", "42", "female" },
				{ "Conor", "40", "male" }, 
				{ "Jennifer", "42", "female" } };

		WebElement table = driver.findElement(By.xpath("//table[@id='dynamictable']"));

		// Get the rows of the table
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		SoftAssert softAssert = new SoftAssert();

		for (int i = 1; i < rows.size(); i++) { // Start at 1 to skip the header row
			WebElement row = rows.get(i);
			List<WebElement> columns = row.findElements(By.tagName("td"));

			for (int j = 0; j < columns.size(); j++) {
				String actualData = columns.get(j).getText();
				String expectedValue = expectedData[i - 1][j];

				softAssert.assertEquals(actualData, expectedValue, "Actual data doesn't match with expected data");
			}
		}

		softAssert.assertAll();
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}
}