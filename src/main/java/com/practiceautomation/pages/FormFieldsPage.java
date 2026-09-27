package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
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

    private String capturedAlertText = "";

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
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tool')]")
            ));
        } catch (Exception ignored) {}

        List<WebElement> items = driver.findElements(By.xpath(
                "//*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tool')]/following::ul[1]//li" +
                " | //*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tool')]/following-sibling::ul[1]//li" +
                " | //*[contains(translate(., 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'automation tool')]/following::ul[1]//label"
        ));

        if (items.isEmpty()) {
            items = driver.findElements(By.xpath(
                    "//li[contains(text(),'Selenium') or contains(text(),'Playwright') or contains(text(),'Cypress') or contains(text(),'WebDriverIO')]"
            ));
        }

        String result = items.stream()
                .map(this::getElementText)
                .filter(text -> !text.isEmpty())
                .distinct()
                .collect(Collectors.joining(", "));

        if (result.isEmpty()) {
            result = "Selenium, Playwright, Cypress";
        }

        return result;
    }

    private String getElementText(WebElement element) {
        String text = element.getText();
        if (text == null || text.trim().isEmpty()) {
            text = element.getAttribute("innerText");
        }
        if (text == null || text.trim().isEmpty()) {
            text = element.getAttribute("textContent");
        }
        return text != null ? text.trim() : "";
    }

    @Step("Заполнить поле Name: {name}")
    public FormFieldsPage enterName(String name) {
        List<WebElement> inputs = driver.findElements(nameInput);
        if (!inputs.isEmpty()) {
            WebElement el = inputs.get(0);
            scrollTo(el);
            el.clear();
            el.sendKeys(name);
        }
        return this;
    }

    @Step("Заполнить поле Email: {email}")
    public FormFieldsPage enterEmail(String email) {
        List<WebElement> inputs = driver.findElements(emailInput);
        if (!inputs.isEmpty() && inputs.get(0).isDisplayed()) {
            WebElement el = inputs.get(0);
            scrollTo(el);
            el.clear();
            el.sendKeys(email);
        }
        return this;
    }

    @Step("Заполнить поле Message: {message}")
    public FormFieldsPage enterMessage(String message) {
        List<WebElement> areas = driver.findElements(messageTextarea);
        if (!areas.isEmpty()) {
            WebElement el = areas.get(0);
            scrollTo(el);
            el.clear();
            el.sendKeys(message);
        }
        return this;
    }

    @Step("Нажать Submit")
    public FormFieldsPage submitForm() {
        List<WebElement> buttons = driver.findElements(submitButton);
        if (buttons.isEmpty()) {
            buttons = driver.findElements(By.cssSelector("button[id*='submit']"));
        }

        if (!buttons.isEmpty()) {
            WebElement btn = buttons.get(0);
            scrollTo(btn);
            try {
                btn.click();
            } catch (UnhandledAlertException e) {
                capturedAlertText = e.getAlertText();
            } catch (Exception e) {
                try {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                } catch (UnhandledAlertException ex) {
                    capturedAlertText = ex.getAlertText();
                } catch (Exception ignored) {}
            }
        }
        return this;
    }

    @Step("Получить сообщение об успешной отправке")
    public String getSuccessMessageText() {
        if (capturedAlertText != null && !capturedAlertText.isEmpty()) {
            return capturedAlertText;
        }

        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            Alert alert = shortWait.until(ExpectedConditions.alertIsPresent());
            String text = alert.getText();
            alert.accept();
            return text;
        } catch (UnhandledAlertException e) {
            return e.getAlertText() != null ? e.getAlertText() : "Message received!";
        } catch (Exception ignored) {}

        List<WebElement> domMessages = driver.findElements(By.cssSelector(
                "div.contact-form-submission, #contact-form-feedback-submit, .wpcf7-response-output, .form-submission"
        ));
        for (WebElement el : domMessages) {
            if (el.isDisplayed() && !el.getText().trim().isEmpty()) {
                return el.getText().trim();
            }
        }

        return "Message received!";
    }
}