package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class FormFields extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/form-fields/";

    private final By nameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By messageTextarea = By.id("message");
    private final By submitButton = By.id("submit-btn");
    private final By successMessage = By.cssSelector("div.contact-form-submission, #contact-form-feedback-submit");

    private final By automationToolsSection = By.xpath(
            "//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tools')]"
    );
    private final By automationToolsLabels = By.xpath(
            "//*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tools')]/following::ul[1]//label" +
            " | //*[contains(translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tools')]/following-sibling::ul[1]//label" +
            " | //input[@type='checkbox' and contains(@name, 'automation')]/following-sibling::label"
    );

    public FormFields(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу Form Fields")
    public FormFields openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Получить список Automation Tools в виде строки через запятую")
    public String getAutomationToolsJoined() {
        wait.until(ExpectedConditions.presenceOfElementLocated(automationToolsSection));
        List<WebElement> tools = driver.findElements(automationToolsLabels);

        if (tools.isEmpty()) {
            tools = driver.findElements(By.xpath(
                    "//label[contains(text(),'Selenium') or contains(text(),'Playwright') or contains(text(),'Cypress') or contains(text(),'WebDriverIO')]"
            ));
        }

        return tools.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .distinct()
                .collect(Collectors.joining(", "));
    }

    @Step("Заполнить поле Name: {name}")
    public FormFields enterName(String name) {
        type(nameInput, name);
        return this;
    }

    @Step("Заполнить поле Email: {email}")
    public FormFields enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Заполнить поле Message: {message}")
    public FormFields enterMessage(String message) {
        type(messageTextarea, message);
        return this;
    }

    @Step("Нажать Submit")
    public FormFields submitForm() {
        click(submitButton);
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }
}
