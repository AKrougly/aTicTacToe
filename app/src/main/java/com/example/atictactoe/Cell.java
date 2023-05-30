package com.example.atictactoe;

import android.widget.ImageView;

public class Cell {
    static final char CHAR_CROSS = 'X';
    static final char CHAR_CIRCLE = 'O';
    private final int idx;
    private int played;
    private ImageView imageView;
    Cell(int idx, int played, ImageView imageView) {
        this.idx = idx;
        this.played = played;
        this.imageView = imageView;
    }
    boolean isEmpty() {
        return played == 0;
    }
    ImageView getComponent() {
        return imageView;
    }
    void bindComponent(ImageView imageView) {
        this.imageView = imageView;
    }
    int getIdx() {
        return idx;
    }
    int getPlayed() {
        return played;
    }
    void setVal(int played) {
        this.played = played;
    }
}
