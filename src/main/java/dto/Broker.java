package dto;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;

public interface Broker extends Serializable {
    void saveDto(BrokerDTO dto) throws IOException;

    BrokerDTO loadDto(Path dto, String name);

    void dirsExistCheck() throws IOException;
}
