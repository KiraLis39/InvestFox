package ru.investment.sites.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import ru.investment.dto.ShareDTO;
import ru.investment.sites.exceptions.SiteBlockedException;

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
    private ShareDTO dto = new ShareDTO();

    protected void buildUrl(String url) {
        this.url = url;
        log.debug("ССЫЛКА: " + this.url);
    }

    public Document getDoc() {
        try {
            HttpConnection conn = (HttpConnection) Jsoup.newSession();
//            conn.newRequest();
            conn.url(url);
            conn.followRedirects(true); // default true
            conn.ignoreContentType(true);
            conn.ignoreHttpErrors(true); // default false
            conn.maxBodySize(4194304); // default 2097152
            conn.timeout(15_000); // default 30_000
            conn.userAgent(HttpConnection.DEFAULT_UA);
//            conn.referrer("http://www.google.com");

//            HttpURLConnection httpConn = (HttpURLConnection) conn;
//            statusCode = conn.getResponseCode();
//            statusMessage = conn.getResponseMessage();
//            contentType = conn.getContentType();
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
        } catch (SiteBlockedException sbe) {
            log.error("Возможно сайт заблокирован: " + url);
        } catch (SocketTimeoutException ste) {
            log.error("Не дождались ответа: " + url);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (UnsupportedMimeTypeException ume) {
            ume.printStackTrace();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (IOException ioe) {
            log.error("Сайт не отвечает: " + url);
        } catch (Exception e) {
            log.error("Some other exception caught: {}", e.getMessage());
            throw e;
        }

        return null;
    }

    public abstract ShareDTO task() throws Exception;
}
