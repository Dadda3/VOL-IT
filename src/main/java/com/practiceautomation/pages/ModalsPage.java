package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.stream.Collectors;

public class ModalsPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/modals/";
    public static final String FORM_FIELDS_URL = "https://practice-automation.com/form-fields/";

    // Триггеры модальных окон
    private final By simpleModalButton = By.id("simpleModal");
    private final By formModalButton = By.id("formModal");

    // Simple Modal (ID: 1318)
    private final By simpleModalOverlay = By.id("pum-1318");
    private final By simpleModalContainer = By.id("popmake-1318");
    private final By simpleModalTitle = By.id("pum_popup_title_1318");
    private final By simpleModalContent = By.cssSelector("#popmake-1318 .pum-content p");
    private final By simpleModalCloseButton = By.cssSelector("#popmake-1318 .pum-close");

    // Form Modal (ID: 674)
    private final By formModalOverlay = By.id("pum-674");
    private final By formModalContainer = By.id("popmake-674");
    private final By formModalTitle = By.id("pum_popup_title_674");
    private final By formModalCloseButton = By.cssSelector("#popmake-674 .pum-close");
    private final By nameInput = By.id("g1051-name");
    private final By emailInput = By.id("g1051-email");
    private final By messageTextarea = By.id("contact-form-comment-g1051-message");
    private final By submitButton = By.cssSelector("#jp-form-99ddf6d0cf76daf0c77607b1cc134112e4314c10 button[type='submit']");
    private final By successMessage = By.cssSelector("#pum-674 .contact-form-submission h4");
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
        wait.until(ExpectedConditions.visibilityOfElementLocated(simpleModalContainer));
        return this;
    }

    @Step("Кликнуть по кнопке Form Modal")
    public ModalsPage clickFormModalButton() {
        click(formModalButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(formModalContainer));
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
        // Ждем скрытия всего полноэкранного оверлея
        wait.until(ExpectedConditions.invisibilityOfElementLocated(simpleModalOverlay));
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
        click(formModalCloseButton);
        // Ждем скрытия всего полноэкранного оверлея
        wait.until(ExpectedConditions.invisibilityOfElementLocated(formModalOverlay));
        return this;
    }

    @Step("Кликнуть по оверлею (вне области модального окна)")
    public ModalsPage clickModalOverlay(boolean isSimpleModal) {
        By overlayLocator = isSimpleModal ? simpleModalOverlay : formModalOverlay;
        WebElement overlay = find(overlayLocator);
        new Actions(driver).moveToElement(overlay, 10, 10).click().perform();
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

    @Step("Получить список инструментов из раздела Automation Tools со страницы Form Fields (Требование №5)")
    public String fetchAutomationToolsJoined() {
        driver.get(FORM_FIELDS_URL);
        
        By toolsLocator = By.xpath("//label[contains(text(),'Automation tools')]/parent::*//input[@type='checkbox']/following-sibling::label | " +
                                   "//li[contains(@class,'checkbox')]//label");
        
        List<WebElement> toolElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(toolsLocator));
        String result = toolElements.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .collect(Collectors.joining(", "));

        driver.get(PAGE_URL);
        return result;
    }
}