package ru.investment.entity.sites.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import ru.investment.entity.dto.ShareDTO;
import ru.investment.exceptions.root.ParsingException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

@Data
@Slf4j
public abstract class AbstractSite {
    protected String name;
    protected String source;
    protected String url;
    protected boolean isActive;
    private ShareDTO dto = ShareDTO.builder().build();

    protected void buildUrl(String url) {
        this.url = url;
        log.debug("ССЫЛКА: " + this.url);
    }

    public Document getDoc() {
        try {
            HttpConnection conn = (HttpConnection) Jsoup.newSession();
            conn.url(url);
            conn.followRedirects(true); // default true
            conn.ignoreContentType(true);
            conn.ignoreHttpErrors(true); // default false
            conn.maxBodySize(4194304); // default 2097152
            conn.timeout(15_000); // default 30_000
            conn.userAgent(HttpConnection.DEFAULT_UA);

            Document result = conn.get();
            if (conn.response().statusCode() != 200) {
                throw new HttpStatusException("Abstract site: Request status here not OK", conn.response().statusCode(), url);
            }
            return result;
        } catch (HttpStatusException hse) {
            switch (hse.getStatusCode()) {
                case 403 -> log.error("Доступ запрещен: " + url);
                case 404 -> log.error("Page not found: " + url);
                default -> log.error("Код ошибки " + hse.getStatusCode() + ": " + url + " (" + hse.getMessage() + ").");
            }
        } catch (SocketTimeoutException ste) {
            log.error("Не дождались ответа: " + url);
        } catch (MalformedURLException | UnsupportedMimeTypeException | IllegalArgumentException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            log.error("Сайт не отвечает: " + url);
        } catch (Exception e) {
            log.error("Some other exception caught: {}", e.getMessage());
            throw e;
        }

        return null;
    }

    public abstract ShareDTO task() throws ParsingException;
}
