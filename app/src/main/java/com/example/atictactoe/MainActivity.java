package com.example.atictactoe;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    static final int BOARD_SIZE = 9;
    private TextView playerOneScore;
    private TextView playerTwoScore;
    private final ImageView[] viewBoard = new ImageView[BOARD_SIZE];
    private int initPlayerNo = 1;
    private char initPlayerImg = Game.CHAR_CROSS;
    private boolean autoPlay = false;
    Button buttonAutoPlay;
    Game game;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerOneScore = findViewById(R.id.player_1_score);
        playerTwoScore = findViewById(R.id.player_2_score);
        setPlayerOneScore("0");
        setPlayerTwoScore("0");
        game = new Game(initPlayerNo, initPlayerImg, null);

        Button resetButton = findViewById(R.id.Reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
        buttonAutoPlay = findViewById(R.id.mega_brain);
        buttonAutoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAutoPlay();
            }
        });
        disableButtonAutoPlay();

        for (int i = 0; i < BOARD_SIZE; i++) {
            try {
                ImageView img = (ImageView) findViewById(
                    getResources().getIdentifier(
                        "buttonImage" + i, "id", getPackageName()
                    )
                );
                viewBoard[i] = img;
                int cell = i;
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //makeMove(Arrays.asList(viewBoard).indexOf(view));
                        //makeMove(Arrays.asList(viewBoard).stream().filter(item -> item == view).findFirst().orElse(null));
                        makeMove(cell);
                    }
                });
            } catch (Exception e) {
                Log.e("ATicTacToe", "Bad cell:" + i);
            }
        }
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
    void displayIncreasedScore(int player) {
        if (player == 1) {
            setPlayerOneScore(String.valueOf(Integer.parseInt(getPlayerOneScore()) + 1));
        } else if (player == 2) {
            setPlayerTwoScore(String.valueOf(Integer.parseInt(getPlayerTwoScore()) + 1));
        }
    }
    void makeMove(int cell) {
        if (game.isEmptyCell(cell) && game.noWinner()) {
            viewBoard[cell].setImageResource(game.getPlayerImg() == Game.CHAR_CROSS ? R.drawable.cross : R.drawable.circle);
            game.makeMove(cell);
            if (game.hasWinner()) {
                displayIncreasedScore(game.getWinner());
                Toast.makeText(this, "Player:" + game.getWinner() + " won", LENGTH_SHORT).show();
            }
            if (game.isDraw())
                Toast.makeText(this, "Draw", LENGTH_SHORT).show();

            if (!autoPlay && game.getPlayerNo() == 2 && game.noWinner()) {
                enableButtonAutoPlay();
            }
            if (autoPlay && game.getPlayerNo() == 2 && game.noWinner()) {
                makeMove(new Solver().complay(game));
            }
        }
    }
    void resetGame() {
        game.resetGame(initPlayerNo, initPlayerImg);
        Arrays.stream(viewBoard).forEach(cell -> {
            if (cell != null)
                cell.setImageDrawable(null);
        });
    }
    void enableButtonAutoPlay() {
        //buttonAutoPlay.setAlpha(.5f);
        buttonAutoPlay.setEnabled(true);
        buttonAutoPlay.setClickable(true);
    }
    void disableButtonAutoPlay() {
        //buttonAutoPlay.setAlpha(.5f);
        buttonAutoPlay.setEnabled(false);
        buttonAutoPlay.setClickable(false);
    }
    void setAutoPlay() {
        disableButtonAutoPlay();
        autoPlay = true;
        if (game.getPlayerNo() == 2 && game.noWinner()) {
            makeMove(new Solver().complay(game));
        }
    }
}