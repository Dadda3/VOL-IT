package com.practiceautomation.tests;

import com.practiceautomation.pages.CalendarPage;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Автотесты Practice Automation")
@Feature("Календари (Calendars)")
public class CalendarTest extends BaseTest {

    // ==========================================
    // ПОЗИТИВНЫЕ СЦЕНАРИИ (8 шт.)
    // ==========================================

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-01: Успешная отправка валидной даты в формате YYYY-MM-DD")
    @Description("Проверка сабмита формы при вводе корректной даты текущего года")
    public void testValidDateSubmission() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2026-09-22")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"),
                "Сообщение об успешной отправке не появилось");
    }

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-02: Отправка даты в будущем периоде")
    @Description("Проверка корректной обработки будущей даты планирования")
    public void testFutureDateSubmission() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2027-11-15")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-03: Отправка даты в прошедшем периоде")
    @Description("Проверка корректной обработки ретроспективной даты")
    public void testPastDateSubmission() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2020-05-09")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    @ParameterizedTest(name = "Граничная дата: {0}")
    @ValueSource(strings = {"2026-01-01", "2026-12-31"})
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-04/05: Граничные значения года (первый и последний день)")
    public void testBoundaryDatesSubmission(String boundaryDate) {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate(boundaryDate)
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-06: Ввод даты високосного года (29 февраля)")
    @Description("Проверка корректности валидации существующего дня високосного года (2028-02-29)")
    public void testLeapYearValidDateSubmission() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2028-02-29")
            .submitForm();

        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-07: Отображение подсказки формата ввода")
    @Description("Проверка наличия ориентирующей подсказки YYYY-MM-DD для пользователя")
    public void testFormatHintDisplayed() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage();

        assertEquals("YYYY-MM-DD", page.getFormatHintText(),
                "Текст подсказки формата не соответствует ожидаемому YYYY-MM-DD");
    }

    @Test
    @Story("Позитивные проверки формы календаря")
    @DisplayName("TC-CAL-08: Корректное обновление значения поля при повторном вводе")
    @Description("Проверка очистки и перезаписи значения в инпуте даты")
    public void testClearAndReenterDate() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2025-01-01")
            .clearDateInput()
            .enterDate("2026-10-10");

        assertEquals("2026-10-10", page.getDateInputValue(),
                "Значение в инпуте не обновилось после очистки и повторного ввода");

        page.submitForm();
        assertTrue(page.getSuccessMessageText().contains("Thank you for your response"));
    }

    // ==========================================
    // НЕГАТИВНЫЕ СЦЕНАРИИ (4 шт.)
    // ==========================================

    @Test
    @Story("Негативные проверки формы календаря")
    @DisplayName("TC-CAL-NEG-01: Ввод даты в неверном формате DD-MM-YYYY")
    public void testInvalidDateFormatHyphen() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("22-09-2026")
            .submitForm();

        assertFalse(page.getFieldErrorMessage().isEmpty(),
                "Ожидалась ошибка формата даты (ожидается YYYY-MM-DD)");
    }

    @Test
    @Story("Негативные проверки формы календаря")
    @DisplayName("TC-CAL-NEG-02: Ввод даты с точечным разделителем (YYYY.MM.DD)")
    public void testInvalidDateDelimiterDot() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2026.09.22")
            .submitForm();

        assertFalse(page.getFieldErrorMessage().isEmpty(),
                "Формат с точками не должен приниматься валидатором");
    }

    @Test
    @Story("Негативные проверки формы календаря")
    @DisplayName("TC-CAL-NEG-03: Ввод несуществующей календарной даты (30 февраля)")
    public void testNonExistentDateFebruary() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("2026-02-30")
            .submitForm();

        assertFalse(page.getFieldErrorMessage().isEmpty(),
                "Несуществующая дата должна приводить к ошибке валидации");
    }

    @Test
    @Story("Негативные проверки формы календаря")
    @DisplayName("TC-CAL-NEG-04: Ввод нечисловой строки со спецсимволами")
    public void testArbitraryStringInput() {
        CalendarPage page = new CalendarPage(driver);
        page.openPage()
            .enterDate("test-date-!@#")
            .submitForm();

        assertFalse(page.getFieldErrorMessage().isEmpty(),
                "Строковое значение со спецсимволами должно блокироваться валидатором");
    }
}