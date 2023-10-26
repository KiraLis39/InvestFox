package ru.investment.entity.sites.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.utils.BrowserUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import static com.codeborne.selenide.Selenide.$;

@Getter
@Setter
@Slf4j
public abstract class AbstractSite {
    private String name;
    private String source;
    private String url;
    private boolean isActive;
    private ShareDTO dto = ShareDTO.builder().build();

    protected void buildUrl(String url_) {
        this.url = url_;
        log.debug("ССЫЛКА: " + this.url);
    }

    public Document getDoc() {
        try {
            HttpConnection conn = (HttpConnection) Jsoup.newSession();
            conn.url(this.url);
            conn.followRedirects(true); // default true
            conn.ignoreContentType(true);
            conn.ignoreHttpErrors(true); // default false
            conn.maxBodySize(4194304); // default 2097152
            conn.timeout(15_000); // default 30_000
            conn.userAgent(HttpConnection.DEFAULT_UA);

            Document result = conn.get();
            if (conn.response().statusCode() != 200) {
                throw new HttpStatusException("Abstract site: Request status here not OK", conn.response().statusCode(), this.url);
            }
            return result;
        } catch (HttpStatusException hse) {
            switch (hse.getStatusCode()) {
                case 403 -> log.error("Доступ запрещен: " + this.url);
                case 404 -> log.error("Page not found: " + this.url);
                default ->
                        log.error("Код ошибки " + hse.getStatusCode() + ": " + this.url + " (" + hse.getMessage() + ").");
            }
        } catch (SocketTimeoutException ste) {
            log.error("Не дождались ответа: " + this.url);
        } catch (MalformedURLException | UnsupportedMimeTypeException | IllegalArgumentException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            log.error("Сайт не отвечает: " + this.url);
        } catch (Exception e) {
            log.error("Some other exception caught: {}", e.getMessage());
            throw e;
        }

        return null;
    }

    public abstract ShareDTO task() throws Exception;

    protected boolean checkPageAvailable() {
        if (BrowserUtil.isPageNotFound()) {
            log.error("isPageNotFound");
            return false;
        }
        if (BrowserUtil.isPageNotExists()) {
            log.error("isPageNotExists");
            return false;
        }
        if (BrowserUtil.isPageNotAvailable()) {
            log.error("isPageNotAvailable");
            return false;
        }
        if (BrowserUtil.isTechnicalWorks()) {
            log.error("isTechnicalWorks");
            return false;
        }
        if ($("*").text().contains("403 ERROR")) {
            log.error("Спалились (403)...");
            return false;
        }
        return true;
    }
}
