package ru.investment.config.constants;

import fox.FoxFontBuilder;
import fox.FoxRender;
import fox.VideoMonitor;
import fox.utils.InputAction;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Getter
@Setter
@Slf4j
public final class Constant {
    public static final SimpleDateFormat SDF = new SimpleDateFormat("MM.dd.yyyy HH:mm:ss");
    public static final VideoMonitor MON = new VideoMonitor();
    public static final FoxRender RENDER = new FoxRender();
    public static final InputAction IN_AC = new InputAction();
    public static final FoxFontBuilder FFB = new FoxFontBuilder();
    public static final Font FONT_TABLE_COMMENT = FFB.setFoxFont(FoxFontBuilder.FONT.CORBEL, 12, false);
    public static final Font FONT_TABLE_SUM = FFB.setFoxFont(FoxFontBuilder.FONT.CONSOLAS, 16, true);
    public static final Font FONT_SIDE_PANEL = FFB.setFoxFont(FoxFontBuilder.FONT.SEGOE_UI_SYMBOL, 20, true);
    public static final Font FONT_TABLE_DATA_TEXT = FFB.setFoxFont(FoxFontBuilder.FONT.CORBEL, 16, true);
    public static final Font FONT_EMITENT_LABEL = FFB.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 32, true);
    public static final Font FONT_FINAL_SUM = FFB.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 26, true);
    public static final String BROKERS_DIR = "./brokers/";
    public static final String SHARES_DIR = "./shares/";
    public static final String BROKER_SAVE_POSTFIX = ".dto";
    public static final int TITLE_WIDTH = 250;
    public static final boolean SHOW_TABLE_CELLS_BORDER = false;
    public static final int SCHEDULER_POOL_SIZE = 3;
    public static final long NEXT_STATUS_SWITCH_MINUTES = 10;
    @Getter
    private static Font fontTableSumRow;
    @Getter
    private static Font fontPrimaryHeaders;

    static {
        try {
            Font f = Font.createFont(0, Objects.requireNonNull(Constant.class.getResourceAsStream("/fonts/Lucida Sans Unicode.ttf")));
            int ruf = FFB.register(f, MON.getEnvironment());
            fontTableSumRow = FFB.setFoxFont(ruf, 18, true, MON.getEnvironment());
        } catch (FontFormatException | IOException e) {
            log.error("Не удалось зарегистрировать шрифт '{}'", "Lucida Sans Unicode");
            fontTableSumRow = FFB.setFoxFont(FoxFontBuilder.FONT.ARIAL, 18, true);
        } catch (Exception e) {
            log.error("Не удалось зарегистрировать шрифт '{}': {}", "Lucida Sans Unicode", e.getMessage());
        }

        try {
            Font f = Font.createFont(0, Objects.requireNonNull(Constant.class.getResourceAsStream("/fonts/Propaganda.ttf")));
            int ruf = FFB.register(f, MON.getEnvironment());
            fontPrimaryHeaders = FFB.setFoxFont(ruf, 10, false, MON.getEnvironment());
        } catch (FontFormatException | IOException e) {
            log.error("Не удалось зарегистрировать шрифт '{}'", "Propaganda");
            fontPrimaryHeaders = FFB.setFoxFont(FoxFontBuilder.FONT.ARIAL, 18, true);
        } catch (Exception e) {
            log.error("Не удалось зарегистрировать шрифт '{}': {}", "Propaganda", e.getMessage());
        }
    }

    private Constant() {
    }
}
