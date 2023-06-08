package com.example.atictactoe;

import android.util.Log;

import java.util.Arrays;
import java.util.stream.IntStream;

public class Game {
    static final char CHAR_CROSS = 'X';
    static final char CHAR_CIRCLE = 'O';
    private int playerNo;
    private char playerImg;
    private int winner;
    private int[] board;
    private final int[][] WINNING_COMBOS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},    //rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},    //columns
            {0, 4, 8}, {2, 4, 6}    //cross
    };
    Game(int playerNo, char playerImg, int[] board) {
        initGame(playerNo, playerImg, board);
    }
    Game(Game game) {
        initGame(game.getPlayerNo(), game.getPlayerImg(), game.getBoard());
    }
    void initGame(int playerNo, char playerImg, int[] board) {
        this.playerNo = playerNo;
        this.playerImg = playerImg;
        if (board == null)
            initBoard();
        else
            this.board = board.clone();
        this.winner = checkWinner();
    }
    void initBoard() {
        board = new int[MainActivity.BOARD_SIZE];
        for (int i = 0; i < MainActivity.BOARD_SIZE; i++) {
            board[i] = 0;
        }
    }
    public int getWinner() {
        return winner;
    }
    public int getPlayerNo() {
        return playerNo;
    }
    public char getPlayerImg() {
        return playerImg;
    }
    public int[] getBoard() {
        return board;
    }
    public boolean isEmptyCell(int cell) {
        return board[cell] == 0;
    }
    public boolean noWinner() {
        return winner == 0;
    }
    public boolean isWinner(int winner) {
        return winner == 1 || winner == 2;
    }
    public boolean hasWinner() { return isWinner(winner); }
    public boolean isDraw() {
        return winner == -1;
    }
    public void togglePlayer() {
        playerNo = playerNo % 2 + 1;
        playerImg = playerImg == CHAR_CROSS ? CHAR_CIRCLE : CHAR_CROSS;
    }
    public int[] getEmptyCells() {
        return IntStream.range(0, board.length)
                .filter(i -> isEmptyCell(i))
                .toArray();
    }
    int hasWon(int[] threeCells) {
        int played = board[threeCells[1]];
        return board[threeCells[0]] == played &&
                played == board[threeCells[2]] ? played : 0;
    }
    int checkWinner() {
        if (isWinner(winner))
            return winner;
        int[] emptyCells = getEmptyCells();

        if (emptyCells.length > 4) return 0; // Game cannot be won until the 5th move

        int combosLeft = WINNING_COMBOS.length;

        do {
            winner = hasWon(WINNING_COMBOS[combosLeft - 1]);
            combosLeft--;
        } while ((winner == 0) && (combosLeft > 0));
        // -1 for draw, 0 for no win, 1 or 2 for winning player
        return (winner == 0) && emptyCells.length == 0 ? -1 : winner;
    }
    void makeMove(int cell) {
        if (board[cell] == 0) {
            board[cell] = playerNo;
            winner = checkWinner();
            togglePlayer();
        }
    }
    void resetGame(int playerNo, char playerImg) {
        this.playerNo = playerNo;
        this.playerImg = playerImg;
        winner = 0;
        Arrays.fill(board, 0);
    }
}
