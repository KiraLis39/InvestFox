package ru.investment.gui.abstracts;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import ru.investment.config.ObjectMapperConfig;
import ru.investment.config.constants.Constant;
import ru.investment.entity.dto.BrokerDTO;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Data
@Slf4j
@EqualsAndHashCode(callSuper = false)
public abstract class AbstractBroker extends JPanel {
    private BrokerDTO dto;

    public void exportData() {
        try {
            Path savePath = Paths.get(Constant.BROKERS_DIR + dto.getName() + Constant.BROKER_SAVE_POSTFIX);
            dirsExistCheck(savePath, dto.getName());
            ObjectMapperConfig.getMapper().writeValue(savePath.toFile(), dto);
        } catch (Exception e) {
            log.error("Broker data export is failed: {}", e.getMessage());
        }
    }

    public void importData(String name) {
        try {
            Path brokerPath = Paths.get(Constant.BROKERS_DIR + name + Constant.BROKER_SAVE_POSTFIX);
            dirsExistCheck(brokerPath, name);
            setDto(ObjectMapperConfig.getMapper().readValue(brokerPath.toFile(), BrokerDTO.class));
            return;
        } catch (MismatchedInputException npe) {
            log.error("Load failed: {}", npe.getMessage());
        } catch (Exception e) {
            log.error("Exception here: {}", e.getMessage());
        }
        setDto(BrokerDTO.builder().name(getName()).build()); // return clazz.getDeclaredConstructor().newInstance()
    }

    public void dirsExistCheck(Path brokerPath, String name) {
        // deprecated: теперь всё сохраняется с БД, но может понадобиться для импорта\экспорта
        try {
            if (Files.notExists(brokerPath.getParent())) {
                Files.createDirectory(brokerPath.getParent());
            }
            if (Files.notExists(brokerPath)) {
                ObjectMapperConfig.getMapper().writeValue(brokerPath.toFile(), BrokerDTO.builder().name(name).build());
            }
        } catch (IOException e) {
            log.error("Ошибка при проверке существования директории сохранения брокеров: {}", e.getMessage());
        }
    }

    public abstract BrokerDTO getSavingData();
}
