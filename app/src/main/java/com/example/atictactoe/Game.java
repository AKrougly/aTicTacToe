package com.example.atictactoe;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class Game {
    private int winner;
    private int playerNo;
    private char playerImg;
    private final TextView playerOneScore;
    private final TextView playerTwoScore;
    private final Cell[] cellArray = new Cell[] {
            new Cell(0, 0, null),
            new Cell(1, 0, null),
            new Cell(2, 0, null),
            new Cell(3, 0, null),
            new Cell(4, 0, null),
            new Cell(5, 0, null),
            new Cell(6, 0, null),
            new Cell(7, 0, null),
            new Cell(8, 0, null)
    };
    private final List<Cell> cellList = Arrays.asList(cellArray);
    private final int[][] WINNING_COMBOS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},    //rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},    //columns
            {0, 4, 8}, {2, 4, 6}    //cross
    };
    Game(MainActivity mainActivity) {
        playerOneScore = mainActivity.findViewById(R.id.player_1_score);
        playerTwoScore = mainActivity.findViewById(R.id.player_2_score);
        setPlayerOneScore("0");
        setPlayerTwoScore("0");
        Button resetButton = mainActivity.findViewById(R.id.Reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });

        for(Cell cell : cellList) {
            try {
                ImageView img = (ImageView) mainActivity.findViewById(
                    mainActivity.getResources().getIdentifier(
                        "buttonImage" + cell.getIdx(), "id", mainActivity.getPackageName()
                    )
                );
                cell.bindComponent(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        makeMove(cell);
                    }
                });
            } catch (Exception e) {
                Log.e("ATicTacToe", "Bad cell:" + cell.getIdx());
            }
        }
        resetGame();
    }
    String getPlayerOneScore() {
        return playerOneScore.getText().toString();
    }
    void setPlayerOneScore(String str) {
        playerOneScore.setText(str);
    }
    String getPlayerTwoScore() {
        return playerTwoScore.getText().toString();
    }
    void setPlayerTwoScore(String str) {
        playerTwoScore.setText(str);
    }
    void incScore() {
        if (winner == 1) {
            setPlayerOneScore(String.valueOf(Integer.parseInt(getPlayerOneScore()) + 1));
        } else {
            setPlayerTwoScore(String.valueOf(Integer.parseInt(getPlayerTwoScore()) + 1));
        }
    }
    boolean validMove(Cell cell) {
        return cell.isEmpty() && cell.getIdx() >= 0 && cell.getIdx() < cellArray.length;
    }
    Cell[] getEmptyCells() {
        return cellList
                .stream()
                .filter(cell -> cell.isEmpty())
                .toArray(Cell[]::new);
    };
    boolean hasWinner() {
        return winner == 1 || winner == 2;
    }
    final void togglePlayer() {
        playerNo = playerNo % 2 + 1;
        playerImg = playerImg == Cell.CHAR_CROSS ? Cell.CHAR_CIRCLE : Cell.CHAR_CROSS;
    }
    int hasWon(int[] threeCells) {
        int played = cellArray[threeCells[1]].getPlayed();
        return cellArray[threeCells[0]].getPlayed() == played &&
            played == cellArray[threeCells[2]].getPlayed() ? played : 0;
    };
    int getWinner() {
        Cell[] emptyCells = getEmptyCells();

        if (emptyCells.length > 4) return 0; // Game cannot be won until the 5th move

        int combosLeft = WINNING_COMBOS.length;

        do {
            winner = hasWon(WINNING_COMBOS[combosLeft - 1]);
            combosLeft--;
        } while ((winner == 0) && (combosLeft > 0));
        // -1 for draw, 0 for no win, 1 or 2 for winning player
        return (winner == 0) && emptyCells.length == 0 ? -1 : winner;
    };
    void makeMove(Cell cell) {
        if (validMove(cell) && !hasWinner()) {
            cell.getComponent().setImageResource(playerImg == Cell.CHAR_CROSS ? R.drawable.cross : R.drawable.circle);
            cell.setVal(playerNo);
            winner = getWinner();
            if (!hasWinner())
                togglePlayer();
            else
                incScore();
        }
    }
    void resetGame() {
        playerNo = 1;
        playerImg = Cell.CHAR_CROSS;
        winner = 0;
        for(Cell cell : cellList) {
            cell.setVal(winner);
            if (cell.getComponent() != null)
                cell.getComponent().setImageDrawable(null);
        }
    }
}
