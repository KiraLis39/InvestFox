package ru.investment.gui.abstracts;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.config.constants.Constant;
import ru.investment.entity.dto.BrokerDTO;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractBroker extends JPanel {

    public void exportData(BrokerDTO dto) {
        try {
            dirsExistCheck();
            String savePath = Constant.BROKERS_DIR + dto.getName() + Constant.BROKER_SAVE_POSTFIX;
            ObjectMapperConfig.getMapper().writeValue(new File(savePath), dto);
        } catch (Exception e) {
            log.error("Broker data export is failed: {}", e.getMessage());
        }
    }

    public BrokerDTO importData(Path toDtoPath, String name) {
        try {
            dirsExistCheck();
            if (Files.notExists(Paths.get(Constant.BROKERS_DIR + name + Constant.BROKER_SAVE_POSTFIX))) {
                ObjectMapperConfig.getMapper().writeValue(
                        new File(Constant.BROKERS_DIR + name + Constant.BROKER_SAVE_POSTFIX),
                        BrokerDTO.builder().name(name).build());
            }
            return ObjectMapperConfig.getMapper().readValue(toDtoPath.toFile(), BrokerDTO.class);
        } catch (MismatchedInputException npe) {
            log.error("Load failed: {}", npe.getMessage());
        } catch (Exception e) {
            log.error("Exception here: {}", e.getMessage());
        }
        return new BrokerDTO(); // return clazz.getDeclaredConstructor().newInstance()
    }

    public void dirsExistCheck() {
        // deprecated: теперь всё сохраняется с БД, но может понадобиться для импорта\экспорта
        try {
            if (!new File(Constant.BROKERS_DIR).exists()) {
                Files.createDirectory(Paths.get(Constant.BROKERS_DIR));
            }
        } catch (IOException e) {
            log.error("Ошибка при проверке существования директории сохранения брокеров: {}", e.getMessage());
        }
    }

    public abstract BrokerDTO getSavingData();
}
