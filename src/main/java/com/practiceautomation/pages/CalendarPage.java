package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CalendarPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/calendars/";

    // Локаторы инпута и формы[cite: 1]
    private final By dateInput = By.id("g1065-1-selectorenteradate");
    private final By submitButton = By.cssSelector("button.pushbutton-wide");
    private final By successMessage = By.cssSelector("div.contact-form-submission h4");
    private final By fieldErrorMessage = By.id("g1065-1-selectorenteradate-text-error-message");
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
        input.sendKeys(date);
        return this;
    }

    @Step("Очистить поле ввода даты")
    public CalendarPage clearDateInput() {
        WebElement input = findClickable(dateInput);
        scrollTo(input);
        input.clear();
        return this;
    }

    @Step("Нажать кнопку Submit")
    public CalendarPage submitForm() {
        WebElement btn = findClickable(submitButton);
        scrollTo(btn);
        btn.click();
        return this;
    }

    @Step("Получить текст подтверждения успешной отправки")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }

    @Step("Получить текст ошибки валидации под полем")
    public String getFieldErrorMessage() {
        return getText(fieldErrorMessage);
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