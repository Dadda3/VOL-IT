package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ModalsPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/modals/";

    private final By simpleModalButton = By.id("simpleModal");
    private final By formModalButton = By.id("formModal");

    private final By simpleModalOverlay = By.id("pum-1318");
    private final By simpleModalContainer = By.id("popmake-1318");
    private final By simpleModalTitle = By.id("pum_popup_title_1318");
    private final By simpleModalContent = By.cssSelector("#popmake-1318 .pum-content p");
    private final By simpleModalCloseButton = By.cssSelector("#popmake-1318 .pum-close");

    private final By formModalOverlay = By.id("pum-674");
    private final By formModalContainer = By.id("popmake-674");
    private final By formModalTitle = By.id("pum_popup_title_674");
    private final By formModalCloseButton = By.cssSelector("#popmake-674 .pum-close");
    private final By nameInput = By.id("g1051-name");
    private final By emailInput = By.id("g1051-email");
    private final By messageTextarea = By.id("contact-form-comment-g1051-message");
    private final By submitButton = By.cssSelector("#popmake-674 button[type='submit'], #popmake-674 input[type='submit']");
    private final By successMessage = By.cssSelector("#pum-674 .contact-form-submission, #pum-674 #contact-form-1051 h4");
    private final By nameError = By.id("g1051-name-text-error-message");
    private final By emailError = By.id("g1051-email-email-error-message");

    public ModalsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу модальных окон")
    public ModalsPage openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Кликнуть по кнопке Simple Modal")
    public ModalsPage clickSimpleModalButton() {
        click(simpleModalButton);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(simpleModalContainer));
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(simpleModalButton));
            wait.until(ExpectedConditions.visibilityOfElementLocated(simpleModalContainer));
        }
        return this;
    }

    @Step("Кликнуть по кнопке Form Modal")
    public ModalsPage clickFormModalButton() {
        click(formModalButton);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(formModalContainer));
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(formModalButton));
            wait.until(ExpectedConditions.visibilityOfElementLocated(formModalContainer));
        }
        return this;
    }

    @Step("Получить заголовок Simple Modal")
    public String getSimpleModalTitle() {
        return getText(simpleModalTitle);
    }

    @Step("Получить текст контента Simple Modal")
    public String getSimpleModalContent() {
        return getText(simpleModalContent);
    }

    @Step("Закрыть Simple Modal кликом по крестику")
    public ModalsPage closeSimpleModal() {
        click(simpleModalCloseButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(simpleModalContainer));
        return this;
    }

    @Step("Получить заголовок Form Modal")
    public String getFormModalTitle() {
        return getText(formModalTitle);
    }

    @Step("Заполнить поле Name: {name}")
    public ModalsPage enterName(String name) {
        type(nameInput, name);
        return this;
    }

    @Step("Заполнить поле Email: {email}")
    public ModalsPage enterEmail(String email) {
        type(emailInput, email);
        return this;
    }

    @Step("Заполнить поле Message: {message}")
    public ModalsPage enterMessage(String message) {
        type(messageTextarea, message);
        return this;
    }

    @Step("Нажать кнопку Submit в Form Modal")
    public ModalsPage submitForm() {
        click(submitButton);
        return this;
    }

    @Step("Получить текст подтверждения отправки формы")
    public String getSuccessMessageText() {
        return getText(successMessage);
    }

    @Step("Получить текст ошибки поля Name")
    public String getNameErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(nameError)).getText().trim();
    }

    @Step("Получить текст ошибки поля Email")
    public String getEmailErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailError)).getText().trim();
    }

    @Step("Закрыть Form Modal кликом по крестику")
    public ModalsPage closeFormModal() {
        click(formModalCloseButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formModalContainer));
        return this;
    }

    @Step("Кликнуть по оверлею (вне области модального окна)")
    public ModalsPage clickModalOverlay(boolean isSimpleModal) {
        By overlayLocator = isSimpleModal ? simpleModalOverlay : formModalOverlay;
        WebElement overlay = find(overlayLocator);
        try {
            new Actions(driver).moveToElement(overlay, 15, 15).click().perform();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {clientX: 15, clientY: 15, bubbles: true}));",
                    overlay
            );
        }
        return this;
    }

    @Step("Проверить видимость Simple Modal")
    public boolean isSimpleModalVisible() {
        List<WebElement> elements = driver.findElements(simpleModalContainer);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    @Step("Проверить видимость Form Modal")
    public boolean isFormModalVisible() {
        List<WebElement> elements = driver.findElements(formModalContainer);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }
}
