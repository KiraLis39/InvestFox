package ru.investment.gui;

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.investment.dto.BrokerDTO;
import ru.investment.utils.Constant;
import ru.investment.utils.JsonMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
public abstract class AbstractBroker extends JPanel {

    protected AbstractBroker(LayoutManager layout) {
        super(layout);
    }

    public void saveDto(BrokerDTO dto) {
        try {
            dirsExistCheck();
        } catch (IOException e) {
            log.error("Exception here: {}", e.getMessage());
        }
        log.info("Saving broker " + dto.getName());

        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(Constant.BROKERS_DIR + dto.getName() + ".ru.investment.dto"))) {
            String saved = JsonMapper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dto);
            osw.write(saved);
        } catch (InvalidDefinitionException ide) {
            log.error("Save failed: {}", ide.getMessage());
        } catch (Exception e) {
            log.error("Save failed: {}", e.getMessage());
        }
    }

    public BrokerDTO loadDto(Path toDtoPath, String name) {
        try {
            dirsExistCheck();
            if (Files.notExists(Paths.get(Constant.BROKERS_DIR + name + ".ru.investment.dto"))) {
                try {
                    JsonMapper.getMapper().writeValue(new File(Constant.BROKERS_DIR + name + ".ru.investment.dto"),
                            BrokerDTO.builder().name(name).build());
                } catch (IOException e) {
                    log.error("Exception here: {}", e.getMessage());
                }
            }
            return JsonMapper.getMapper().readValue(toDtoPath.toFile(), BrokerDTO.class);
        } catch (MismatchedInputException npe) {
            log.error("Load failed: {}", npe.getMessage());
        } catch (Exception e) {
            log.error("Exception here: {}", e.getMessage());
        }

        return new BrokerDTO(); // return clazz.getDeclaredConstructor().newInstance()
    }

    public void dirsExistCheck() throws IOException {
        if (!new File(Constant.BROKERS_DIR).exists()) {
            Files.createDirectory(Paths.get(Constant.BROKERS_DIR));
        }
    }
}
