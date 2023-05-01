package ru.investment.gui.components.other;

import java.nio.file.Path;
import java.time.LocalDateTime;


public class AlarmItemCycle {
    private final String time;
    private final Path track;
    private LocalDateTime startTime;

    public AlarmItemCycle(String time, Path path) {
        this.time = time;
        this.track = path;
        resetStartTime();
    }

    public String getTime() {
        return time;
    }

    public Path getTrack() {
        return track;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void resetStartTime() {
        startTime = LocalDateTime.now();
    }

    public String toString() {
        return "[" + time + "]  " + track.toFile().getName() + "";
    }

    public boolean isCycled() {
        return true;
    }

    public boolean isWasPlayed() {
        return false;
    }

    public void wasPlayed(boolean b) {
    }
}
