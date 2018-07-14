package edu.cnm.deepdive.tileslide.controller;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  public static final String TILE_NUMS_KEY = "tileNums";
  private static int PUZZLE_SIZE = 3;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesCounter;
  private Button resetButton;
  private ArrayList<Integer> tileNums;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    movesCounter = findViewById(R.id.moves_counter);
    resetButton = findViewById(R.id.reset_button);
    resetButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        updateMoves();
      }
    });
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    tileGrid.setOnItemClickListener(this);
    createPuzzle(savedInstanceState);
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    int row = position / PUZZLE_SIZE;
    int col = position % PUZZLE_SIZE;
    updateGrid(row, col);
  }

  private void createPuzzle(Bundle savedInstanceState) {
    frame = new Frame(PUZZLE_SIZE, new Random());
    if (savedInstanceState != null) {
      frame.setTiles(
          savedInstanceState
          .getIntegerArrayList(TILE_NUMS_KEY)
          .toArray(new Integer[0])
      );
    }
    adapter = new FrameAdapter(this, frame);
    tileGrid.setAdapter(adapter);
  }

  private void updateGrid(int row, int col) {
    if(frame.move(row, col)) {
      adapter.notifyDataSetChanged();
      updateMoves();
      if (frame.hasWon()) {
        won();
      }
    } else {
      Toast.makeText(this, getString(R.string.illegal_move), Toast.LENGTH_LONG).show();
    }
  }

  private void updateMoves() {
    movesCounter.setText(String.format(getString(R.string.moves_counter), frame.getMoves()));
  }

  private void won() {
    Toast.makeText(this, getString(R.string.win), Toast.LENGTH_LONG).show();
    tileGrid.setOnItemClickListener(null);
    resetButton.setEnabled(false);
  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    Tile[][] source = frame.getTiles();
    tileNums = new ArrayList<>();
    for (int row = 0; row < source.length; row++) {
      for (int col = 0; col < source[0].length; col++) {
          tileNums.add((source[row][col] == null)?
              null : (source[row][col].getNumber()));
      }
    }
    outState.putIntegerArrayList(TILE_NUMS_KEY,tileNums);
    super.onSaveInstanceState(outState);
  }
}
