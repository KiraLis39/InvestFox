package gui;

import com.fasterxml.jackson.core.JsonProcessingException;
import core.NetProcessor;
import javax.swing.*;
import java.io.IOException;

public class WorkFrame extends JFrame {


    public WorkFrame() {
        try {
            NetProcessor.testPrint("MOEX-UPRO");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
