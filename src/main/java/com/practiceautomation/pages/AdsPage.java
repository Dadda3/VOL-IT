package com.practiceautomation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdsPage extends BasePage {
    public static final String PAGE_URL = "https://practice-automation.com/ads/";

    // Локаторы основного контента страницы
    private final By pageTitle = By.tagName("h1");
    private final By countdownText = By.xpath("//p[contains(text(),'An ad will appear')]");
    private final By videoTutorialLink = By.linkText("how to handle ads in test automation");

    // Локаторы рекламного модального окна (Popup Maker ID: 1272)
    private final By adOverlay = By.id("pum-1272");
    private final By adContainer = By.id("popmake-1272");
    private final By adTitle = By.id("pum_popup_title_1272");
    private final By adContent = By.cssSelector("#popmake-1272 .pum-content p");
    private final By adCloseButton = By.cssSelector("#popmake-1272 .pum-close");

    public AdsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открыть страницу с рекламными окнами")
    public AdsPage openPage() {
        open(PAGE_URL);
        return this;
    }

    @Step("Ожидать автоматического появления рекламного окна (таймаут: {timeoutSeconds} сек.)")
    public AdsPage waitForAdToAppear(int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(adContainer));
        return this;
    }

    @Step("Проверить, отображается ли реклама прямо сейчас")
    public boolean isAdVisible() {
        List<WebElement> elements = driver.findElements(adContainer);
        return !elements.isEmpty() && elements.get(0).isDisplayed();
    }

    @Step("Получить заголовок рекламного окна")
    public String getAdTitle() {
        return getText(adTitle);
    }

    @Step("Получить текст тела рекламного окна")
    public String getAdBodyText() {
        return getText(adContent);
    }

    @Step("Закрыть рекламное окно кликом по крестику")
    public AdsPage closeAd() {
        click(adCloseButton);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(adOverlay));
        return this;
    }

    @Step("Получить заголовок страницы (H1)")
    public String getPageTitleText() {
        return getText(pageTitle);
    }

    @Step("Получить текст обратного отсчета со страницы")
    public String getCountdownText() {
        return getText(countdownText);
    }

    @Step("Проверить доступность и кликабельность ссылки на странице")
    public boolean isTutorialLinkClickable() {
        return wait.until(ExpectedConditions.elementToBeClickable(videoTutorialLink)).isDisplayed();
    }

    @Step("Проверить наличие класса темы оформления у рекламного оверлея")
    public boolean hasThemeClass(String themeClassName) {
        return find(adOverlay).getAttribute("class").contains(themeClassName);
    }

    @Step("Кликнуть по оверлею вне области рекламного окна")
    public AdsPage clickOverlayOutsideAd() {
        WebElement overlay = find(adOverlay);
        new Actions(driver).moveToElement(overlay, 10, 10).click().perform();
        return this;
    }

    @Step("Нажать клавишу ESCAPE")
    public AdsPage pressEscapeKey() {
        new Actions(driver).sendKeys(Keys.ESCAPE).perform();
        return this;
    }

    @Step("Обновить страницу (F5)")
    public AdsPage refreshPage() {
        driver.navigate().refresh();
        return this;
    }
}