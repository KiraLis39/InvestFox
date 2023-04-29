package ru.investment.utils;

import fox.FoxFontBuilder;
import fox.utils.InputAction;
import lombok.Data;

import java.awt.*;

@Data
public final class Constant {
    public static final InputAction inAc = new InputAction();
    public static final FoxFontBuilder ffb = new FoxFontBuilder();

    public static final Font btnsFont1 = ffb.setFoxFont(FoxFontBuilder.FONT.SEGOE_UI_SYMBOL, 20, true);
    public static final Font btnsFont2 = ffb.setFoxFont(FoxFontBuilder.FONT.CORBEL, 16, true);
    public static final Font btnsFont5 = ffb.setFoxFont(FoxFontBuilder.FONT.CORBEL, 12, false);
    public static final Font btnsFont6 = ffb.setFoxFont(FoxFontBuilder.FONT.CONSOLAS, 14, true);
    public static final Font btnsFont7 = ffb.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 32, true);
    public static final Font btnsFont8 = ffb.setFoxFont(FoxFontBuilder.FONT.BAHNSCHRIFT, 24, true);
    public static final Font btnsFont3, btnsFont4;
    public static final String BROKERS_DIR = "./brokers/";

    static {
        int ruf = ffb.addNewFont("Lucida Sans Unicode");
        btnsFont3 = ffb.setFoxFont(ruf, 18, true, GraphicsEnvironment.getLocalGraphicsEnvironment());

        ruf = ffb.addNewFont("Propaganda");
        btnsFont4 = ffb.setFoxFont(ruf, 14, false, GraphicsEnvironment.getLocalGraphicsEnvironment());
    }

    private Constant() {
    }
}
