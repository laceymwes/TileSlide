package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  public static final String TILE_NUMS_KEY = "tileNums";
  public static final String START_NUMS_KEY = "startNums";
  private static int PUZZLE_SIZE = 3;
  private static final int PREFERENCE_REQUEST_CODE = 1;

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesCounter;
  private Button resetButton;
  private ImageButton androidButton;
  private ImageButton r2d2Button;
  private PreferenceFragment pFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    pFragment = new PreferenceFragment();
    movesCounter = findViewById(R.id.moves_counter);
    resetButton = findViewById(R.id.reset_button);
    resetButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        adapter.notifyDataSetChanged();
        updateMoves();
      }
    });
    androidButton = findViewById(R.id.android);
    r2d2Button = findViewById(R.id.r2d2);
    androidButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (adapter.getImageID() != R.drawable.android_puzzle) {
          setAdapter(R.drawable.android_puzzle);
        }
      }
    });
    r2d2Button.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (adapter.getImageID() != R.drawable.r2d2_puzzle) {
          setAdapter(R.drawable.r2d2_puzzle);
        }
      }
    });
    tileGrid = findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(PUZZLE_SIZE);
    tileGrid.setOnItemClickListener(this);
    createPuzzle(savedInstanceState);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.preferences:
        Intent intent = new Intent(this, PreferenceFragment.class);
        startActivityForResult(intent, PREFERENCE_REQUEST_CODE);
  }
    return true;
  }

  // Finish implementation
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    String numTiles = getPreferences(this.MODE_PRIVATE).getString("num_tiles", null);
    String puzzleImage = getPreferences(this.MODE_PRIVATE).getString("puzzle_image", null);
    if (requestCode == PREFERENCE_REQUEST_CODE) {
      if (numTiles != null) {
        PUZZLE_SIZE = Integer.parseInt(numTiles);
      }
      if (puzzleImage != null) {
        switch (puzzleImage.toLowerCase()) {
          case "android":

        }
      }
    }
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    int row = position / PUZZLE_SIZE;
    int col = position % PUZZLE_SIZE;
    updateGrid(row, col);
  }

  private void createPuzzle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      frame = new Frame(
          PUZZLE_SIZE,
          // frame.tiles[][]
          savedInstanceState
          .getIntegerArrayList(TILE_NUMS_KEY)
          .toArray(new Integer[0]),
          // frame.start[][]
          savedInstanceState.getIntegerArrayList(START_NUMS_KEY)
          .toArray(new Integer[0]),
          new Random()
      );
    } else {
      frame = new Frame(PUZZLE_SIZE, new Random());
    }
    setAdapter(R.drawable.android_puzzle);
  }

  private void setAdapter(int puzzleID) {
    adapter = new FrameAdapter(this, frame, puzzleID);
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
    ArrayList<Integer> tileNums = new ArrayList<>();
    Tile[][] source = frame.getTiles();
    for (int row = 0; row < source.length; row++) {
      for (int col = 0; col < source[0].length; col++) {
          tileNums.add((source[row][col] == null)?
              null : (source[row][col].getNumber()));
      }
    }
    ArrayList<Integer> startNums = new ArrayList<>();
    Tile[][] start = frame.getStart();
    for (int row = 0; row < start.length; row++) {
      for (int col = 0; col < start[0].length; col++) {
        startNums.add((start[row][col] == null)?
            null : (start[row][col].getNumber()));
      }
    }
    outState.putIntegerArrayList(TILE_NUMS_KEY, tileNums);
    outState.putIntegerArrayList(START_NUMS_KEY, startNums);
    super.onSaveInstanceState(outState);
  }
}
