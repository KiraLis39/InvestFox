package registry;

import fox.FoxFontBuilder;

import java.awt.*;

public class Registry {
    public static String version = "1.0.2";
    public static Font btnsFont1 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.SEGOE_UI_SYMBOL, 20, true);
    public static Font btnsFont2 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.CORBEL, 16, true);
    public static Font btnsFont3, btnsFont4;

    static {
        int ruf = FoxFontBuilder.addNewFont("Lucida Sans Unicode");
        btnsFont3 = FoxFontBuilder.setFoxFont(ruf, 18, true, GraphicsEnvironment.getLocalGraphicsEnvironment());

        ruf = FoxFontBuilder.addNewFont("Propaganda");
        btnsFont4 = FoxFontBuilder.setFoxFont(ruf, 10, false, GraphicsEnvironment.getLocalGraphicsEnvironment());
    }

    public static Font btnsFont5 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.CORBEL, 12, false);
    public static Font btnsFont6 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.CONSOLAS, 14, true);
    public static Font btnsFont7 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.PAPYRYS, 32, true);
    public static Font btnsFont8 = FoxFontBuilder.setFoxFont(FoxFontBuilder.FONT.PAPYRYS, 24, true);
}
