package com.example.atictactoe;

import java.util.Objects;

public class CellScore {
    private final int move;
    private final int score;
    CellScore(int move, int score) {
        this.move = move;
        this.score = score;
    }
    public int getMove() {
        return move;
    }
    public int getScore() {
        return score;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellScore ms = (CellScore) o;
        return move == ms.move && score == ms.score;
    }
    @Override
    public int hashCode() {
        return Objects.hash(move, score);
    }
    @Override
    public String toString() {
        return "m:" + move + " s:" + score;
    }
}
