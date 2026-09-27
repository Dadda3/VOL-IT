package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
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
    private final By submitButton = By.cssSelector("#popmake-674 button[type='submit']");
    private final By successMessage = By.cssSelector("#pum-674 .contact-form-submission");
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
        WebElement btn = findClickable(simpleModalButton);
        scrollTo(btn);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(300))
                .until(d -> {
                    if (isSimpleModalVisible()) {
                        return true;
                    }
                    try {
                        btn.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                    }
                    return isSimpleModalVisible();
                });
        try {
            wait.until(ExpectedConditions.elementToBeClickable(simpleModalCloseButton));
        } catch (Exception ignored) {}
        return this;
    }

    @Step("Кликнуть по кнопке Form Modal")
    public ModalsPage clickFormModalButton() {
        WebElement btn = findClickable(formModalButton);
        scrollTo(btn);

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(300))
                .until(d -> {
                    if (isFormModalVisible()) {
                        return true;
                    }
                    try {
                        btn.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                    }
                    return isFormModalVisible();
                });
        try {
            wait.until(ExpectedConditions.elementToBeClickable(formModalCloseButton));
        } catch (Exception ignored) {}
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
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(simpleModalCloseButton));

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(300))
                .until(d -> {
                    if (!isSimpleModalVisible()) {
                        return true;
                    }
                    try {
                        closeBtn.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
                    }
                    if (isSimpleModalVisible()) {
                        try {
                            ((JavascriptExecutor) driver).executeScript(
                                    "if (window.jQuery && jQuery('#pum-1318').length) { jQuery('#pum-1318').popmake('close'); }"
                            );
                        } catch (Exception ignored) {}
                    }
                    return !isSimpleModalVisible();
                });

        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(simpleModalOverlay));
        } catch (Exception ignored) {}
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
        return getText(nameError);
    }

    @Step("Получить текст ошибки поля Email")
    public String getEmailErrorMessage() {
        return getText(emailError);
    }

    @Step("Закрыть Form Modal кликом по крестику")
    public ModalsPage closeFormModal() {
        WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(formModalCloseButton));

        new WebDriverWait(driver, Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(300))
                .until(d -> {
                    if (!isFormModalVisible()) {
                        return true;
                    }
                    try {
                        closeBtn.click();
                    } catch (Exception e) {
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeBtn);
                    }
                    if (isFormModalVisible()) {
                        try {
                            ((JavascriptExecutor) driver).executeScript(
                                    "if (window.jQuery && jQuery('#pum-674').length) { jQuery('#pum-674').popmake('close'); }"
                            );
                        } catch (Exception ignored) {}
                    }
                    return !isFormModalVisible();
                });

        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(formModalOverlay));
        } catch (Exception ignored) {}
        return this;
    }

    @Step("Кликнуть по оверлею (вне области модального окна)")
    public ModalsPage clickModalOverlay(boolean isSimpleModal) {
        By overlayLocator = isSimpleModal ? simpleModalOverlay : formModalOverlay;
        WebElement overlay = find(overlayLocator);
        try {
            new Actions(driver).moveToLocation(10, 10).click().perform();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].dispatchEvent(new MouseEvent('click', {clientX: 10, clientY: 10, bubbles: true}));",
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