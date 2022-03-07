package sites.impl;

import dto.ShareDTO;
import org.jsoup.nodes.Document;
import sites.interfaces.iSite;

import java.io.IOException;

public abstract class AbstractSite implements iSite {
    protected String name;
    protected String source;
    protected boolean isActive;
    public ShareDTO dto = new ShareDTO();
    public Document doc;

    @Override
    public abstract ShareDTO task() throws IOException;

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {return isActive;}

    public String getName() {return name;}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
