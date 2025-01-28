package com.assignment.testcases;

import com.crm.qa.base.TestBase;
import com.crm.qa.pages.HomePage;
import com.crm.qa.util.TestUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class assignment extends TestBase {
  HomePage homePage;
  TestUtil testUtil;

  String sheetName = "contacts";

  public assignment() {
    super();
  }

  @BeforeMethod
  public void setUp() {
    testUtil = new TestUtil();
    homePage = new HomePage();
  }

  @Test()
  public void verifyInvalidInputForSourceAndDestinationCity() {
    homePage.clearDefaultData();
    homePage.inputSourceCity("jnvd", false);
    homePage.inputDestinationCity("sdjnvbj", false);
    Assert.assertEquals(
        homePage.getErrorMessage(),
        "No results for this search, try something else.",
        "Error Message didn't match");
    homePage.clickOnSearchButton();
    Assert.assertEquals(
        homePage.getInvalidInputErrorMessage(),
        "Add an airport or city",
        "Error Message didn't match");
  }

  @Test()
  public void verifyValidInputForSourceAndDestinationCity() {
    homePage.clearDefaultData();
    String url = homePage.getCurrentUrl();
    homePage.inputSourceCity("dehradun", true);
    homePage.inputDestinationCity("DEL", true);
    homePage.clickOnSearchButton();
    Assert.assertFalse(url.equalsIgnoreCase(driver.getCurrentUrl()));
  }

  @Test()
  public void verifyFlightResultsForValidInput() {
    homePage.clearDefaultData();
    homePage.inputSourceCity("DEL", true);
    homePage.inputDestinationCity("BOM", true);
    homePage.selectDate("15", "May", "2025");
    homePage.clickOnSearchButton();
    String selectedDate = homePage.getSelectedDate();
    Assert.assertTrue(selectedDate.contains("11"));
    homePage.selectDate("16", "May", "2025");
    homePage.clickOnSearchButton();
    selectedDate = homePage.getSelectedDate();
    Assert.assertTrue(selectedDate.contains("16"));
  }
}
