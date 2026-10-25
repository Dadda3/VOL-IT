package com.practiceautomation.extensions;

import com.practiceautomation.tests.BaseTest;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class AllureScreenshotExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            Object testInstance = context.getRequiredTestInstance();
            if (testInstance instanceof BaseTest baseTest) {
                WebDriver driver = baseTest.getDriver();
                if (driver != null) {
                    try {
                        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                        Allure.addAttachment("Скриншот при падении теста", "image/png",
                                new ByteArrayInputStream(screenshot), ".png");
                    } catch (Exception e) {
                        System.err.println("Не удалось прикрепить скриншот в Allure: " + e.getMessage());
                    }

                    try {
                        String pageSource = driver.getPageSource();
                        if (pageSource != null) {
                            Allure.addAttachment("Исходный HTML код страницы (Page Source)", "text/html",
                                    new ByteArrayInputStream(pageSource.getBytes(StandardCharsets.UTF_8)), ".html");
                        }
                    } catch (Exception ignored) {}
                }
            }
        }
    }
}