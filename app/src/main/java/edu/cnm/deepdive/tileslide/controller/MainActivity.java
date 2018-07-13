package edu.cnm.deepdive.tileslide.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  private static int PUZZLE_SIZE = 3;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesCounter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    movesCounter = findViewById(R.id.moves_counter);
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    tileGrid.setOnItemClickListener(this);
    createPuzzle();
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    int row = position / PUZZLE_SIZE;
    int col = position % PUZZLE_SIZE;
    frame.move(row, col);
    adapter.notifyDataSetChanged();
    movesCounter.setText(getString(R.string.moves_counter) + frame.getMoves());
  }

  private void createPuzzle() {
    frame = new Frame(PUZZLE_SIZE, new Random());
    adapter = new FrameAdapter(this, frame);
    tileGrid.setAdapter(adapter);
  }
}
