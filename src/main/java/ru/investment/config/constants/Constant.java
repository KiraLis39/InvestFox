package ru.investment.config.constants;

import fox.FoxFontBuilder;
import fox.FoxRender;
import fox.VideoMonitor;
import fox.utils.InputAction;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Data
@Slf4j
public final class Constant {
    public static final SimpleDateFormat sdf = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
    public static final VideoMonitor mon = new VideoMonitor();
    public static final FoxRender render = new FoxRender();
    public static final InputAction inAc = new InputAction();
    public static final FoxFontBuilder ffb = new FoxFontBuilder();
    public static final Font fontTableComment = ffb.setFoxFont(FoxFontBuilder.FONT.CORBEL, 12, false);
    public static final Font fontTableSum = ffb.setFoxFont(FoxFontBuilder.FONT.CONSOLAS, 16, true);
    public static final Font fontSidePanel = ffb.setFoxFont(FoxFontBuilder.FONT.SEGOE_UI_SYMBOL, 20, true);
    public static final Font fontTableDataText = ffb.setFoxFont(FoxFontBuilder.FONT.CORBEL, 16, true);
    public static final Font fontEmitentLabel = ffb.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 32, true);
    public static final Font fontFinalSum = ffb.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 26, true);
    public static final String BROKERS_DIR = "./brokers/";
    public static final String SHARES_DIR = "./shares/";
    public static final String BROKER_SAVE_POSTFIX = ".dto";
    public static final int TITLE_WIDTH = 250;
    public static final boolean showTableCellsBorder = false;
    public static Font fontTableSumRow;
    public static Font fontPrimaryHeaders;

    static {
        try {
            Font f = Font.createFont(0, Objects.requireNonNull(Constant.class.getResourceAsStream("/fonts/Lucida Sans Unicode.ttf")));
            int ruf = ffb.register(f, mon.getEnvironment());
            fontTableSumRow = ffb.setFoxFont(ruf, 18, true, mon.getEnvironment());
        } catch (FontFormatException | IOException e) {
            log.error("Не удалось зарегистрировать шрифт '{}'", "Lucida Sans Unicode");
            fontTableSumRow = ffb.setFoxFont(FoxFontBuilder.FONT.ARIAL, 18, true);
        } catch (Exception e) {
            log.error("Не удалось зарегистрировать шрифт '{}': {}", "Lucida Sans Unicode", e.getMessage());
        }

        try {
            Font f = Font.createFont(0, Objects.requireNonNull(Constant.class.getResourceAsStream("/fonts/Propaganda.ttf")));
            int ruf = ffb.register(f, mon.getEnvironment());
            fontPrimaryHeaders = ffb.setFoxFont(ruf, 10, false, mon.getEnvironment());
        } catch (FontFormatException | IOException e) {
            log.error("Не удалось зарегистрировать шрифт '{}'", "Propaganda");
            fontPrimaryHeaders = ffb.setFoxFont(FoxFontBuilder.FONT.ARIAL, 18, true);
        } catch (Exception e) {
            log.error("Не удалось зарегистрировать шрифт '{}': {}", "Propaganda", e.getMessage());
        }
    }

    private Constant() {
    }
}
