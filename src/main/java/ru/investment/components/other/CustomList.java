package ru.investment.components.other;

import javax.swing.*;


public class CustomList<AlarmItem> extends JList<AlarmItem> {
    private final PlayPane owner;

    public CustomList(ListModel<AlarmItem> dlm, PlayPane owner) {
        super(dlm);
        this.owner = owner;
    }

//    public int getPlayedRowIndex() {
//        return owner.getPlayedIndex();
//    }

    public int[] getSelectedIndexes() {
        return getSelectedIndices();
    }
}
