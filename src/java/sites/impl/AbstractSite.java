package sites.impl;

import dto.ShareDTO;
import lombok.Data;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import sites.exceptions.SiteBlockedException;
import sites.interfaces.iSite;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

@Data
public abstract class AbstractSite implements iSite {
    public ShareDTO dto = new ShareDTO();
    protected String name;
    protected String source;
    protected String url;
    protected boolean isActive;

    protected void buildUrl(String url) {
        this.url = url;
        System.out.println("ССЫЛКА: " + this.url);
    }

    protected Document getDoc() {
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
                case 403 -> System.err.println("Доступ запрещен: " + url);
                case 404 -> System.err.println("Page not found: " + url);
                default -> System.err.println("Код ошибки " + hse.getStatusCode() + ": " + url + " (" + hse.getMessage() + ").");
            }
        } catch (SiteBlockedException sbe) {
            System.err.println("Возможно сайт заблокирован: " + url);
        } catch (SocketTimeoutException ste) {
            System.err.println("Не дождались ответа: " + url);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (UnsupportedMimeTypeException ume) {
            ume.printStackTrace();
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (IOException ioe) {
            System.err.println("Сайт не отвечает: " + url);
        } catch (Exception e) {
            throw e;
        }

        return null;
    }
}
