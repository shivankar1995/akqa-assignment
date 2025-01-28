package com.crm.qa.pages;

import com.crm.qa.base.TestBase;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage extends TestBase {

  @FindBy(css = "[data-ui-name='input_location_from_segment_0']")
  WebElement sourceElement;

  @FindBy(css = "[data-ui-name='input_text_autocomplete']")
  WebElement airportInputTextBox;

  @FindBy(id = "flights-searchbox_suggestions")
  WebElement suggestionBox;

  @FindBy(css = "#flights-searchbox_suggestions > li:nth-of-type(1)")
  WebElement cityCheckbox;

  @FindBy(css = "div[class*='Flyout-module__inner'] div[class*='Text-module']")
  WebElement invalidCityErrorMessage;

  @FindBy(
      css =
          "[data-ui-name='input_location_from_segment_0'] > div div[class*='ErrorMessage-module__content']")
  WebElement invalidInputErrorMessage;

  @FindBy(css = "div[class*='Spinner-module__inner']")
  WebElement spinnerElement;

  @FindBy(css = "[data-ui-name='button_search_submit']")
  WebElement searchButton;

  @FindBy(css = "div[class*='LoadingScreen'] > div")
  WebElement loadingModal;

  @FindBy(css = "span[class*='Chip-module']")
  WebElement selectedCity;

  @FindBy(css = "[data-ui-name=input_location_to_segment_0]")
  WebElement destinationElement;

  @FindBy(css = "[data-ui-name='search_type_oneway']")
  WebElement oneWayRadioButton;

  @FindBy(css = "div[class*='Calendar-module__content'] > div:nth-of-type(1) [role='gridcell']")
  List<WebElement> leftCalendarDateCells;

  @FindBy(css = "div[class*='Calendar-module__content'] > div:nth-of-type(2) [role='gridcell']")
  List<WebElement> rightCalendarDateCells;

  @FindBy(css = "div[class*='Calendar-module__content'] h3")
  List<WebElement> calendarMonthName;

  @FindBy(css = "[data-ui-name='button_date_segment_0']")
  WebElement calendarElement;

  @FindBy(css = "[data-ui-name=\"calendar_body\"]>button:nth-of-type(2)")
  WebElement calnedarNextButton;

  WebDriverWait wait;

  // Initializing the Page Objects:
  public HomePage() {
    PageFactory.initElements(driver, this);
    wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 10 seconds timeout
  }

  public void inputSourceCity(String sourceCity, Boolean isValid) {
    oneWayRadioButton.click();
    wait.until(ExpectedConditions.elementToBeClickable(sourceElement));
    sourceElement.click();
    airportInputTextBox.sendKeys(sourceCity);
    if (isValid) {
      wait.until(ExpectedConditions.visibilityOf(suggestionBox));
      cityCheckbox.click();
    }
  }

  public void inputDestinationCity(String destinationCity, Boolean isValid) {
    wait.until(ExpectedConditions.elementToBeClickable(sourceElement));
    destinationElement.click();
    airportInputTextBox.sendKeys(destinationCity);
    if (isValid) {
      wait.until(ExpectedConditions.visibilityOf(suggestionBox));
      cityCheckbox.click();
    }
  }

  public String getErrorMessage() {
    wait.until(ExpectedConditions.visibilityOf(spinnerElement));
    return invalidCityErrorMessage.getText();
  }

  public void clearDefaultData() {
    sourceElement.click();
    wait.until(ExpectedConditions.visibilityOf(selectedCity));
    selectedCity.click();
  }

  public void clickOnSearchButton() {
    searchButton.click();
    wait.until(ExpectedConditions.invisibilityOf(loadingModal));
  }

  public String getCurrentUrl() {
    return driver.getCurrentUrl();
  }

  public String getInvalidInputErrorMessage() {
    return invalidInputErrorMessage.getText();
  }

  public void selectDate(String targetDay, String targetMonth, String targetYear) {
    // Loop until the desired month and year are displayed
    calendarElement.click();
    while (true) {
      // Get the month and year from the left panel
      String displayedMonthYearLeft = calendarMonthName.get(0).getText();
      String displayedMonthYearRight = calendarMonthName.get(1).getText();

      if (displayedMonthYearLeft.contains(targetMonth)
          && displayedMonthYearLeft.contains(targetYear)) {
        // Target month/year is on the left
        selectDate(leftCalendarDateCells, targetDay);
        break;
      } else if (displayedMonthYearRight.contains(targetMonth)
          && displayedMonthYearRight.contains(targetYear)) {
        // Target month/year is on the right
        selectDate(rightCalendarDateCells, targetDay);
        break;
      } else {
        // Click the next button to navigate months
        calnedarNextButton.click();
      }
    }
  }

  public void selectDate(List<WebElement> dates, String day) {
    // Find the target date within the specified calendar
    for (WebElement date : dates) {
      if (date.getText().equals(day)) {
        date.click();
        break;
      }
    }
  }

  public String getSelectedDate(){
    return calendarElement.getText();
  }
}
