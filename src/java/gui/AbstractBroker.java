package gui;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import dto.Broker;
import dto.BrokerDTO;
import fox.Out;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import utils.JsonMapper;

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
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@JsonSerialize
public abstract class AbstractBroker extends JPanel implements Broker {

    public AbstractBroker(LayoutManager layout) {
        super(layout);
    }

    @Override
    public void saveDto(BrokerDTO dto) throws IOException {
        try {
            dirsExistCheck();
        } catch (IOException e) {
            throw e;
        }
        Out.Print(PortfelPane.class, Out.LEVEL.INFO, "Saving broker " + dto.getName());

        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream("./brokers/" + dto.getName() + ".dto"))) {
            String saved = JsonMapper.getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dto);
            osw.write(saved);
        } catch (InvalidDefinitionException ide) {
            System.err.println("Save failed: " + ide.getMessage());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public BrokerDTO loadDto(Path dto, String name) {
        try {
            dirsExistCheck();
            if (Files.notExists(Paths.get("./brokers/" + name + ".dto"))) {
                try {
                    Files.createFile(Paths.get("./brokers/" + name + ".dto"));
                } catch (IOException e) {
                    throw e;
                }
            }
            return JsonMapper.getMapper().readValue(dto.toFile(), BrokerDTO.class);
        } catch (MismatchedInputException npe) {
            System.err.println("Load failed: " + npe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new BrokerDTO(); // return clazz.getDeclaredConstructor().newInstance()
    }

    @Override
    public void dirsExistCheck() throws IOException {
        if (!new File("./brokers/").exists()) {
            Files.createDirectory(Paths.get("./brokers/"));
        }
    }
}
