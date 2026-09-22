package com.practiceautomation.tests;

import com.practiceautomation.pages.ModalsPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Автотесты Practice Automation")
@Feature("Модальные окна (Modals)")
public class ModalsTest extends BaseTest {

    // ==========================================
    // ПОЗИТИВНЫЕ СЦЕНАРИИ (9 шт.)
    // ==========================================

    @Test
    @Story("Простое модальное окно")
    @DisplayName("TC-MOD-01: Открытие Simple Modal и проверка содержимого")
    @Description("Проверка отображения заголовка и текста простого информационного модального окна")
    public void testOpenSimpleModal() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickSimpleModalButton();

        assertTrue(page.isSimpleModalVisible(), "Simple Modal должно быть видимым");
        assertEquals("Simple Modal", page.getSimpleModalTitle());
        assertEquals("Hi, I’m a simple modal.", page.getSimpleModalContent());
    }

    @Test
    @Story("Простое модальное окно")
    @DisplayName("TC-MOD-02: Закрытие Simple Modal по клику на крестик")
    @Description("Проверка успешного скрытия окна при клике на кнопку закрытия")
    public void testCloseSimpleModalViaButton() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickSimpleModalButton()
            .closeSimpleModal();

        assertFalse(page.isSimpleModalVisible(), "Simple Modal должно быть закрыто");
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-03: Открытие Form Modal и валидация заголовка")
    public void testOpenFormModal() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton();

        assertTrue(page.isFormModalVisible(), "Form Modal должно быть видимым");
        assertEquals("Modal Containing A Form", page.getFormModalTitle());
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-04: Успешная отправка формы с заполнением только обязательного поля (Name)")
    public void testSubmitFormWithMandatoryFieldOnly() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .enterName("Dmitry")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"),
                "Сообщение об успехе не отобразилось");
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-05: Успешная отправка формы при полном заполнении полей")
    public void testSubmitFormWithAllFields() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .enterName("Test User")
            .enterEmail("testuser@example.com")
            .enterMessage("Regular automation message test.")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-06: Заполнение Message элементами Automation Tools через запятую (Требование №5)")
    @Description("Сбор списка инструментов с помощью Selenium, объединение через запятую и заполнение поля Message в Form Modal")
    public void testSubmitFormWithMessageFromAutomationTools() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage();

        String automationToolsString = page.fetchAutomationToolsJoined();
        assertFalse(automationToolsString.isEmpty(), "Список инструментов Automation Tools не должен быть пустым");

        page.clickFormModalButton()
            .enterName("QA Automation Specialist")
            .enterEmail("qa@practiceautomation.com")
            .enterMessage(automationToolsString)
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"),
                "Форма со списком Automation Tools должна успешно отправиться");
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-07: Закрытие Form Modal по клику на крестик")
    public void testCloseFormModalViaButton() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .closeFormModal();

        assertFalse(page.isFormModalVisible(), "Form Modal должно быть закрыто");
    }

    @Test
    @Story("Модальное окно с формой")
    @DisplayName("TC-MOD-08: Повторное открытие Form Modal после закрытия")
    @Description("Проверка сохранения работоспособности триггеров и отсутствия блокировки DOM")
    public void testReopenFormModalAfterClose() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .closeFormModal();

        assertFalse(page.isFormModalVisible(), "Form Modal должно быть скрыто после закрытия");

        page.clickFormModalButton();
        assertTrue(page.isFormModalVisible(), "Form Modal должно открываться повторно");
    }

    @Test
    @Story("Жизненный цикл окон")
    @DisplayName("TC-MOD-09: Последовательное открытие и закрытие обоих окон без перезагрузки")
    public void testSequentialModalsInteraction() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickSimpleModalButton()
            .closeSimpleModal();
        assertFalse(page.isSimpleModalVisible());

        page.clickFormModalButton()
            .closeFormModal();
        assertFalse(page.isFormModalVisible());
    }

    // ==========================================
    // НЕГАТИВНЫЕ СЦЕНАРИИ (4 шт.)
    // ==========================================

    @Test
    @Story("Негативные проверки формы")
    @DisplayName("TC-MOD-NEG-01: Отправка формы с пустым обязательным полем Name")
    public void testSubmitFormWithEmptyName() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .submitForm();

        assertFalse(page.getNameErrorMessage().isEmpty(),
                "Ожидалось сообщение об обязательности поля Name");
    }

    @Test
    @Story("Негативные проверки формы")
    @DisplayName("TC-MOD-NEG-02: Ввод некорректного адреса в поле Email")
    public void testSubmitFormWithInvalidEmail() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .enterName("Dmitry")
            .enterEmail("plainaddress_without_at")
            .submitForm();

        assertFalse(page.getEmailErrorMessage().isEmpty(),
                "Ожидалась ошибка валидации формата Email");
    }

    @Test
    @Story("Негативные проверки модальных окон")
    @DisplayName("TC-MOD-NEG-03: Попытка отправки полностью пустой формы")
    public void testSubmitCompletelyEmptyForm() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickFormModalButton()
            .submitForm();

        assertTrue(page.isFormModalVisible(), "Модальное окно не должно закрываться при ошибке сабмита");
        assertFalse(page.getNameErrorMessage().isEmpty());
    }

    @Test
    @Story("Негативные проверки модальных окон")
    @DisplayName("TC-MOD-NEG-04: Проверка запрета закрытия по клику на оверлей")
    @Description("В настройках Popup Maker close_on_overlay_click: false, окно не должно закрываться при клике вне его области")
    public void testModalDoesNotCloseOnOverlayClick() {
        ModalsPage page = new ModalsPage(driver);
        page.openPage()
            .clickSimpleModalButton()
            .clickModalOverlay(true);

        assertTrue(page.isSimpleModalVisible(), "Модальное окно не должно закрываться при клике на оверлей");
    }
}