package com.practiceautomation.extensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class AllureScreenshotExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            WebDriver driver = getDriver(context);
            if (driver != null) {
                try {
                    byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment("Скриншот при падении теста", "image/png",
                            new ByteArrayInputStream(screenshot), ".png");
                } catch (Exception e) {
                    System.err.println("Не удалось прикрепить скриншот в Allure: " + e.getMessage());
                }
            }
        }
    }

    private WebDriver getDriver(ExtensionContext context) {
        Object testInstance = context.getRequiredTestInstance();
        Class<?> clazz = testInstance.getClass();

        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField("driver");
                field.setAccessible(true);
                return (WebDriver) field.get(testInstance);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
