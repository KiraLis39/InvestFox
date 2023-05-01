package ru.investment.gui.components.other;

import java.nio.file.Path;


public class AlarmItem {
    private final String time;
    private final Path track;
    private boolean wasPlayed = false;

    public AlarmItem(String time, Path path) {
        this.time = time;
        this.track = path;
    }

    public String getTime() {
        return time;
    }

    public Path getTrack() {
        return track;
    }

    public boolean isCycled() {
        return false;
    }

    @Override
    public String toString() {
        return "(" + time + ")  " + track.toFile().getName() + "";
    }

    public void wasPlayed(boolean wasPlayed) {
        this.wasPlayed = wasPlayed;
    }

    public boolean isWasPlayed() {
        return this.wasPlayed;
    }
}
