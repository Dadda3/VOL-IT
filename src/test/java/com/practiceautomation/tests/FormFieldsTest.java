package com.practiceautomation.tests;

import com.practiceautomation.pages.FormFieldsPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Epic("UI Автотесты Practice Automation")
@Feature("Форма (Form Fields)")
public class FormFieldsTest extends BaseTest {

    @Test
    @Story("Заполнение Message списком Automation Tools")
    @DisplayName("TC-FORM-01: Заполнение Message элементами Automation Tools через запятую")
    @Description("Сбор списка инструментов с раздела Automation Tools, объединение через запятую и заполнение поля Message")
    public void testFillMessageWithAutomationTools() {
        FormFieldsPage page = new FormFieldsPage(driver);
        page.openPage();

        String tools = page.getAutomationToolsJoined();
        assertFalse(tools.isEmpty(), "Список Automation Tools не должен быть пустым");

        page.enterName("QA Automation")
            .enterEmail("qa@example.com")
            .enterMessage(tools)
            .submitForm();

        String response = page.getSuccessMessageText();
        assertTrue(response.contains("Thank you") || response.contains("received") || response.contains("Message"),
                "Форма должна успешно отправиться. Фактический ответ: " + response);
    }
}