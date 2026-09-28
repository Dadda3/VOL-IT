package com.practiceautomation.tests;

import com.practiceautomation.pages.AdsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Автотесты Practice Automation")
@Feature("Рекламные окна (Ads)")
public class AdsTest extends BaseTest {

    @Test
    @Story("Автоматическое появление рекламы")
    @DisplayName("TC-ADS-01: Автоматическое открытие рекламного окна по таймеру")
    @Description("Проверка появления окна popmake-1272 после отсчета таймера задержки")
    public void testAdAppearsAfterDelay() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15);

        assertTrue(page.isAdVisible(), "Рекламное окно должно отобразиться после задержки");
    }

    @Test
    @Story("Контент рекламного окна")
    @DisplayName("TC-ADS-02: Валидация заголовка рекламного окна")
    public void testAdTitleContent() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15);

        assertEquals("Hi", page.getAdTitle(), "Заголовок рекламы должен быть 'Hi'");
    }

    @Test
    @Story("Контент рекламного окна")
    @DisplayName("TC-ADS-03: Валидация содержимого рекламы")
    public void testAdBodyContent() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15);

        assertEquals("I am an ad.", page.getAdBodyText(), "Текст рекламы должен быть 'I am an ad.'");
    }

    @Test
    @Story("Закрытие рекламного окна")
    @DisplayName("TC-ADS-04: Закрытие рекламного окна кликом по крестику")
    public void testCloseAdViaButton() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15)
            .closeAd();

        assertFalse(page.isAdVisible(), "Рекламное окно должно быть закрыто после клика по крестику");
    }

    @Test
    @Story("Доступность контента")
    @DisplayName("TC-ADS-05: Доступность контента страницы до появления рекламного окна")
    public void testContentVisibleBeforeAd() {
        AdsPage page = new AdsPage(driver);
        page.openPage();

        assertEquals("Ads", page.getPageTitleText());
        assertTrue(page.getCountdownText().contains("An ad will appear in"),
                "Текст обратного отсчета должен содержать сообщение о появлении рекламы");
    }

    @Test
    @Story("Доступность контента")
    @DisplayName("TC-ADS-06: Восстановление кликабельности элементов после закрытия рекламы")
    public void testInteractionRestoredAfterAdClosed() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15)
            .closeAd();

        assertTrue(page.isTutorialLinkClickable(),
                "После закрытия рекламы ссылки на странице должны быть кликабельны");
    }

    @Test
    @Story("Стилизация рекламы")
    @DisplayName("TC-ADS-07: Проверка применения темы оформления cutting-edge")
    public void testAdThemeApplied() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15);

        assertTrue(page.hasThemeClass("pum-theme-cutting-edge"),
                "Оверлей должен содержать класс темы оформления 'pum-theme-cutting-edge'");
    }

    @Test
    @Story("Жизненный цикл рекламы")
    @DisplayName("TC-ADS-08: Повторное появление рекламы после перезагрузки страницы (F5)")
    public void testAdReappearsOnReload() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15)
            .closeAd();

        assertFalse(page.isAdVisible());

        page.refreshPage()
            .waitForAdToAppear(15);

        assertTrue(page.isAdVisible(), "После перезагрузки страницы реклама должна появиться снова");
    }

    @Test
    @Story("Негативные проверки показа рекламы")
    @DisplayName("TC-ADS-NEG-01: Рекламное окно скрыто сразу после загрузки страницы")
    @Description("Проверка, что реклама не появляется мгновенно, а ожидает таймер")
    public void testAdNotDisplayedImmediately() {
        AdsPage page = new AdsPage(driver);
        page.openPage();

        assertFalse(page.isAdVisible(),
                "Реклама не должна отображаться сразу в первую секунду после открытия страницы");
    }

    @Test
    @Story("Негативные проверки закрытия рекламы")
    @DisplayName("TC-ADS-NEG-02: Запрет закрытия рекламы по клику на оверлей")
    public void testAdDoesNotCloseOnOverlayClick() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15)
            .clickOverlayOutsideAd();

        assertTrue(page.isAdVisible(),
                "Рекламное окно не должно закрываться при клике на оверлей (close_on_overlay_click: false)");
    }

    @Test
    @Story("Негативные проверки закрытия рекламы")
    @DisplayName("TC-ADS-NEG-03: Запрет закрытия рекламы по нажатию клавиши ESC")
    public void testAdDoesNotCloseOnEscape() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15)
            .pressEscapeKey();

        assertTrue(page.isAdVisible(),
                "Рекламное окно не должно закрываться по клавише ESC (close_on_esc_press: false)");
    }

    @Test
    @Story("Негативные проверки жизненного цикла")
    @DisplayName("TC-ADS-NEG-04: Отсутствие самозакрытия рекламы со временем")
    public void testAdDoesNotSelfDismiss() {
        AdsPage page = new AdsPage(driver);
        page.openPage()
            .waitForAdToAppear(15);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}

        assertTrue(page.isAdVisible(),
                "Реклама должна оставаться открытой, пока пользователь явно не нажмёт кнопку закрытия");
    }
}
