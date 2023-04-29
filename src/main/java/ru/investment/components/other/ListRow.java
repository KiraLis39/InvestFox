package ru.investment.components.other;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Slf4j
public class ListRow extends JPanel implements Comparator<ListRow> {
    private final PlayPane owner;
    private final String text;
    private final Path trackPath;
    private BufferedImage icon;
    private int count;
    private boolean isFall = false;

    public ListRow(PlayPane owner, int count, File iconFile, Path trackPath) {
        this.owner = owner;
        try {
            icon = ImageIO.read(iconFile);
        } catch (IOException e) {
//            log.info(ListRow.class, Out.LEVEL.WARN, "Icon of each tracks line not found!");
            e.printStackTrace();
        }

        this.trackPath = trackPath;
        this.text = trackPath.toFile().getName();
        this.count = count;

//        setFont(ru.investment.registry.Registry.trackSelectedFont);
    }

    public BufferedImage getImIcon() {
        return icon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compare(ListRow o1, ListRow o2) {
        return o1.count > o2.count ? 1 : -1;
    }

    public Path getPath() {
        return trackPath;
    }

    public String getDuration() throws IOException {
        if (Files.notExists(trackPath)) {
            return "Файл не найден";
        }

        Header h = null;
        FileInputStream file = null;
        try {
            file = new FileInputStream(trackPath.toFile());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        Bitstream bitstream = new Bitstream(file);
        try {
            h = bitstream.readFrame();
        } catch (BitstreamException ex) {
            ex.printStackTrace();
        }

//        int size = h.calculate_framesize();
//        float ms_per_frame = h.ms_per_frame();
//        int maxSize = h.max_number_of_frames(10000);
//        float t = h.total_ms(size);

        long tn = 0;
        try {
            tn = file.getChannel().size();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        int min = h.min_number_of_frames(500);
        log.info("Chanel: " + file.getChannel().size() + ": " + min + ": " + (h.total_ms((int) tn) / 1000 / 60));

        String hrs = String.format("%.0f", h.total_ms((int) tn) / 1000f / 60f / 60f);
        String mins = String.format("%.0f", h.total_ms((int) tn) / 1000f / 60f);
        String secs = String.format("%2.0f", h.total_ms((int) tn) / 10000f);

        return (hrs.length() < 2 ? "0" + hrs : hrs) + ":" + (mins.length() < 2 ? "0" + mins : mins) + ":" + secs;
    }

    public PlayPane getOwner() {
        return owner;
    }

    public boolean isFall() {
        return this.isFall;
    }

    public void setFall(boolean isFall) {
        this.isFall = isFall;
    }
}
