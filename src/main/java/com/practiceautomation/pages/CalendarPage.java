package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CalendarPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/calendars/";

    private final By dateInput = By.cssSelector("#g1065-1-selectorenteradate, input[name*='selectorenteradate']");
    private final By submitButton = By.cssSelector(
            "button.pushbutton-wide, input.pushbutton-wide, input[type='submit'], button[type='submit']"
    );
    private final By successMessage = By.cssSelector(
            ".contact-form-submission, #contact-form-1065 h4, .contact-form-success, .form-success"
    );
    private final By fieldErrorMessage = By.cssSelector(
            "#g1065-1-selectorenteradate-text-error-message, .contact-form-error, [id*='selectorenteradate'][id*='error'], .form-error"
    );
    private final By formatHint = By.id("g1065-1-selectorenteradate-text-format");

    public CalendarPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу с календарями")
    public CalendarPage openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Ввести дату в поле: {date}")
    public CalendarPage enterDate(String date) {
        WebElement input = findClickable(dateInput);
        scrollTo(input);
        input.clear();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        input.sendKeys(date);
        return this;
    }

    @Step("Очистить поле ввода даты")
    public CalendarPage clearDateInput() {
        WebElement input = findClickable(dateInput);
        scrollTo(input);
        input.clear();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        return this;
    }

    @Step("Нажать кнопку Submit")
    public CalendarPage submitForm() {
        click(submitButton);
        return this;
    }

    @Step("Получить текст подтверждения успешной отправки")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }

    @Step("Проверить, отображается ли подтверждение успешной отправки")
    public boolean isSuccessMessageDisplayed() {
        List<WebElement> elements = driver.findElements(successMessage);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    @Step("Получить текст ошибки валидации под полем")
    public String getFieldErrorMessage() {
        try {
            WebElement input = driver.findElement(dateInput);
            String validationMsg = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].validationMessage;", input);
            if (validationMsg != null && !validationMsg.trim().isEmpty()) {
                return validationMsg.trim();
            }
        } catch (Exception ignored) {}

        try {
            return new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(fieldErrorMessage))
                    .getText().trim();
        } catch (Exception e) {
            List<WebElement> errors = driver.findElements(fieldErrorMessage);
            for (WebElement el : errors) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    return el.getText().trim();
                }
            }
            return "";
        }
    }

    @Step("Получить текст подсказки формата")
    public String getFormatHintText() {
        return getText(formatHint);
    }

    @Step("Получить значение атрибута 'value' поля даты")
    public String getDateInputValue() {
        return find(dateInput).getAttribute("value");
    }
}
