package sites.exceptions;

import java.io.IOException;

public class SiteBlockedException extends IOException {

    public SiteBlockedException(String s) {
        super(s);
    }
}
