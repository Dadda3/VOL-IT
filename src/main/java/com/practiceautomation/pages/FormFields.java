package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class FormFieldsPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/form-fields/";

    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By messageTextarea = By.id("message");
    private final By submitButton = By.id("submit-btn");
    private final By successMessage = By.cssSelector("div.contact-form-submission h4");

    // ВАЖНО: проверьте XPath по реальному DOM страницы /form-fields/
    // Если "Automation Tools" — это заголовок <p> или <h2> перед списком <ul>, то подойдёт:
    private final By automationToolsLabels =
            By.xpath("//*[contains(text(),'Automation Tools') or contains(text(),'Automation tools')]/following::ul[1]//label");

    public FormFieldsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу Form Fields")
    public FormFieldsPage openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Получить список Automation Tools в виде строки через запятую")
    public String getAutomationToolsJoined() {
        List<WebElement> tools = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(automationToolsLabels)
        );
        return tools.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(", "));
    }

    @Step("Заполнить поле Name: {name}")
    public FormFieldsPage enterName(String name) {
        type(nameInput, name);
        return this;
    }

    @Step("Заполнить поле Email: {email}")
    public FormFieldsPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Заполнить поле Message: {message}")
    public FormFieldsPage enterMessage(String message) {
        type(messageTextarea, message);
        return this;
    }

    @Step("Нажать Submit")
    public FormFieldsPage submitForm() {
        click(submitButton);
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }
}