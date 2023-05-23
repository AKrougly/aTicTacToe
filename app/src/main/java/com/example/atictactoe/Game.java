package com.example.atictactoe;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

class Btn {
    static final char CHAR_SPACE = ' ';
    static final char CHAR_CROSS = 'X';
    static final char CHAR_CIRCLE = 'O';
    private final int idx;
    private char val;
    private ImageView imageView;
    Btn(int idx, char val, ImageView imageView) {
        this.idx = idx;
        this.val = val;
        this.imageView = imageView;
    }
    boolean isEmpty() {
        return val == CHAR_SPACE;
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
    char getVal() {
        return val;
    }
    void setVal(char val) {
        this.val = val;
    }
}
public class Game {
    private final MainActivity mainActivity;
    Button resetGame;
    private int playerNo;
    private char playerImg;
    private boolean thereIsAWinner;
    private Btn[] btnArray = new Btn[] {
            new Btn(0, ' ', null),
            new Btn(1, ' ', null),
            new Btn(2, ' ', null),
            new Btn(3, ' ', null),
            new Btn(4, ' ', null),
            new Btn(5, ' ', null),
            new Btn(6, ' ', null),
            new Btn(7, ' ', null),
            new Btn(8, ' ', null)
    };
    private List<Btn> btnList = Arrays.asList(btnArray);
    private int[][] winningPositions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},    //rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},    //columns
            {0, 4, 8}, {2, 4, 6}    //cross
    };
    Game(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        playerNo = 0;
        playerImg = Btn.CHAR_CROSS;
        thereIsAWinner = false;
        resetGame = (Button)mainActivity.findViewById(R.id.Reset);
        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        for(Btn btn : btnList) {
            try {
                ImageView img = (ImageView) mainActivity.findViewById(mainActivity.getResources().getIdentifier("buttonImage" + btn.getIdx(), "id", mainActivity.getPackageName()));
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        btn.bindComponent(img);
                        makeMove(btn);
                    }
                });
            } catch(Exception e) {
                Log.e("ATicTacToe", "Bad btn:" + btn.getIdx());
            }
        }
    }
    void makeMove(Btn btn) {
        if (!thereIsAWinner && btn.isEmpty()) {
            btn.getComponent().setImageResource(playerImg == Btn.CHAR_CROSS ? R.drawable.cross : R.drawable.circle);
            btn.setVal(playerImg == Btn.CHAR_CROSS ? Btn.CHAR_CIRCLE : Btn.CHAR_CROSS);
            thereIsAWinner = checkWinner();
            if (!thereIsAWinner) {
                playerNo = playerNo == 0 ? 1 : 0;
                playerImg = playerImg == Btn.CHAR_CROSS ? Btn.CHAR_CIRCLE : Btn.CHAR_CROSS;
            }
        }
    }
    boolean checkWinner() {
        boolean res = false;

        for(int[] winingPosition: winningPositions) {
            if (!btnArray[winingPosition[0]].isEmpty() &&
                btnArray[winingPosition[0]].getVal() == btnArray[winingPosition[1]].getVal() &&
                btnArray[winingPosition[1]].getVal() == btnArray[winingPosition[2]].getVal()) {
                res = true;
            }
        }
        return res;
    }
    void resetGame() {
        playerNo = 0;
        playerImg = Btn.CHAR_CROSS;
        thereIsAWinner = false;
        for(int i = 0; i < btnArray.length; i++) {
            btnArray[i].setVal(Btn.CHAR_SPACE);
            if (btnArray[i].getComponent() != null)
                btnArray[i].getComponent().setImageDrawable(null);
            btnArray[i].bindComponent(null);
        }
    }
}
