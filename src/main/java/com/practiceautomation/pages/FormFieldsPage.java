package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class FormFieldsPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/form-fields/";

    private final By nameInput = By.cssSelector("#name-input, #name");
    private final By emailInput = By.cssSelector("#email, input[type='email']");
    private final By messageTextarea = By.cssSelector("#message, textarea");
    private final By submitButton = By.id("submit-btn");

    private final By automationToolsItems = By.xpath(
            "//label[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'automation tools')]" +
            "/following-sibling::ul[1]//label | " +
            "//label[contains(translate(text(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'automation tools')]" +
            "/following-sibling::ul[1]//li | " +
            "//input[@name='g1103-whatareyourautomationtools[]']/following-sibling::label | " +
            "//input[contains(@id, 'automation-tools')]/following-sibling::label"
    );

    public FormFieldsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу Form Fields")
    public FormFieldsPage openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Получить список Automation Tools в виде строки через запятую средствами Selenium")
    public String getAutomationToolsJoined() {
        wait.until(ExpectedConditions.presenceOfElementLocated(automationToolsItems));
        List<WebElement> items = driver.findElements(automationToolsItems);

        return items.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .distinct()
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

    @Step("Получить текст подтверждения отправки (JS Alert или DOM)")
    public String getSuccessMessageText() {
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();
            alert.accept();
            return alertText;
        } catch (Exception ignored) {
            List<WebElement> domMessages = driver.findElements(By.cssSelector(
                    "div.contact-form-submission, #contact-form-feedback-submit, .wpcf7-response-output"
            ));
            for (WebElement el : domMessages) {
                if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                    return el.getText().trim();
                }
            }
            return "";
        }
    }
}
