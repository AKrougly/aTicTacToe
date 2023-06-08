package com.example.atictactoe;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Solver {
    int[] CORNERS = new int[] { 0, 2, 6, 8 };
    int MAX_DEPTH = 9;
    Solver () { }
    int score(Game game, int depth) {
        if (game.isDraw() || game.noWinner()) return 0;
        return depth + (game.getWinner() == 1 ? MAX_DEPTH : -MAX_DEPTH);
    }
    CellScore minimax(Game game, int depth) {
        if (depth >= MAX_DEPTH) throw new RuntimeException();
        if (game.isDraw() || game.hasWinner()) return new CellScore(-1, score(game, depth));

        ArrayList<CellScore> cellScoreArray = new ArrayList<>();
        Arrays.stream(game.getEmptyCells()).forEach(
            cell -> {
                Game cloneGame = new Game(game);
                cloneGame.makeMove(cell);
                CellScore cellScore = new CellScore(cell, minimax(cloneGame, depth + 1).getScore());
                cellScoreArray.add(cellScore);
            }
        );
        return
            game.getPlayerNo() == 1 ?
                cellScoreArray
                .stream()
                .max(Comparator.comparing(CellScore::getScore))
                .orElseThrow(NoSuchElementException::new) :
                cellScoreArray
                .stream()
                .min(Comparator.comparing(CellScore::getScore))
                .orElseThrow(NoSuchElementException::new);
    }
    int getOpeningMove(Game game) {
        // Play center or random corner
        return game.getBoard()[4] == 0 ? 4 : CORNERS[(int) Math.floor(Math.random() * 4)];
    }
    int complay(Game game) {
        return game.getEmptyCells().length == 8 ? getOpeningMove(game) : minimax(game, 0).getMove();
    }
}
