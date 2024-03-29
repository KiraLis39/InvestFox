package ru.investment.utils;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.investment.config.constants.ParserMessages;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;

@Slf4j
@UtilityClass
public class BrowserUtils {

    // для локального не-headless тестирования:
    public static void closeRegionModal() {
        try {
            Selenide.sleep(500);
            if ($(".modal .modal-content .modal-footer").exists()) {
                $(".input-group").$x("button").click();
            }
        } catch (Exception ignoreIfAbsent) {
            log.debug("Искомое модальное окно не найдено");
        }
    }

    public static void closeWindow() {
        Selenide.closeWindow();
    }

    public static void closeAndClearAll() {
        try {
            closeWindow();
        } catch (Exception e) {
            log.warn("Can`t close the browser window.");
        }

        log.info("\n* Выполняется чистка проекта... *");
        try {
            if (webdriver().driver().hasWebDriverStarted()) {
                log.debug("Clear browser cookies...");
                webdriver().driver().clearCookies();
            }
        } catch (Throwable t) {
            log.debug("Не удалось очистить куки браузера: " + t.getMessage());
        }
        closeDriver();
        log.info("* Чистка проекта завершена *\n");
    }

    private static void closeDriver() {
        try {
            if (webdriver().driver().hasWebDriverStarted()) {
                log.debug("Closing browser driver...");
                closeWebDriver();
            }
        } catch (Throwable t) {
            log.warn("Не удалось закрыть веб-драйвер: " + t.getMessage());
        }
    }

    /**
     * Метод проверки на существование и работоспособности вкладки дополнительной информации заказчика.
     *
     * @return доступность дополнительной вкладки информации
     */
    public static boolean isPageNotAvailable() {
        $x("/html/body").shouldBe(Condition.visible);
        if ($x("/html/body").text().startsWith(ParserMessages.PAGE_NOT_ACCEPTABLE)) {
            return true;
        }
        SelenideElement availableElement = $x("/html/body/table/tbody/tr/td/table");
        return (availableElement.exists() && availableElement.text().contains(ParserMessages.PAGE_NOT_ACCEPTABLE));
    }

    public static boolean isTechnicalWorks() {
        return $(".alert-text__item").exists() && $(".alert-text__item")
                .text().contains(ParserMessages.TECHNICAL_WORKS);
    }

    public static boolean isPageNotExists() {
        return $x("/html/body")
                .text().contains(ParserMessages.PAGE_NOT_EXISTS);
    }

    public static boolean isPageNotFound() {
        return $x("/html/body")
                .text().contains(ParserMessages.PAGE_NOT_FOUND);
    }

    public static synchronized boolean openNewBrowser() {
        int openTries = 3;
        boolean isSuccess;
        do {
            isSuccess = true;
            openTries--;
            try {
                // open the browser instant:
                open();
            } catch (Exception e) {
                log.warn("Selenide open exception: {}", e.getMessage());
                isSuccess = false;
                Selenide.sleep(500);
            }
        } while (!isSuccess && openTries > 0);
        return isSuccess;
    }
}
