package com.practiceautomation.extensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

public class AllureScreenshotExtension implements TestWatcher {

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        WebDriver driver = getDriver(context);
        if (driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Скриншот при падении теста", "image/png",
                    new ByteArrayInputStream(screenshot), ".png");
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