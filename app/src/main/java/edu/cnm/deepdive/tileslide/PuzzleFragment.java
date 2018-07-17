package edu.cnm.deepdive.tileslide;


import static android.content.Context.MODE_PRIVATE;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.cnm.deepdive.tileslide.controller.PreferencesFragment;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class PuzzleFragment extends Fragment implements AdapterView.OnItemClickListener,
  SharedPreferences.OnSharedPreferenceChangeListener {


  public static final String TILE_NUMS_KEY = "tileNums";
  public static final String START_NUMS_KEY = "startNums";
  private static int puzzleSize = 3;
  private static int puzzleImageID = R.drawable.android_puzzle;
  private static final String NUM_TILES_PREF_KEY = "num_tiles";
  private static final String PUZZLE_IMAGE_PREF_KEY = "puzzle_image";

  private Frame frame;
  private FrameAdapter adapter;
  private GridView tileGrid;
  private TextView movesCounter;
  private Button resetButton;
  private ImageButton androidButton;
  private ImageButton r2d2Button;
  private SharedPreferences preferences;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setDefaults();
  }

  private void setDefaults() {
    preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    preferences.registerOnSharedPreferenceChangeListener(this);
    if (preferences.getString(PUZZLE_IMAGE_PREF_KEY, null) != null) {
      puzzleSize = Integer.parseInt(preferences.getString(NUM_TILES_PREF_KEY, null));
      puzzleImageID = (preferences
          .getString(PUZZLE_IMAGE_PREF_KEY, null).toLowerCase().equals("android")) ?
          R.drawable.android_puzzle : R.drawable.r2d2_puzzle;
    }
  }

  public PuzzleFragment() {
    // Required empty public constructor
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    movesCounter = view.findViewById(R.id.moves_counter);
    resetButton = view.findViewById(R.id.reset_button);
    resetButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        frame.reset();
        adapter.notifyDataSetChanged();
        updateMoves();
      }
    });
    tileGrid = view.findViewById(R.id.tile_grid);
    tileGrid.setNumColumns(puzzleSize);
    tileGrid.setOnItemClickListener(this);
    createPuzzle(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_puzzle, container, false);
  }

  private void setAdapter(int puzzleID) {
    adapter = new FrameAdapter(getActivity(), frame, puzzleID);
    tileGrid.setAdapter(adapter);
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    int row = position / puzzleSize;
    int col = position % puzzleSize;
    updateGrid(row, col);
  }

  private void createPuzzle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      frame = new Frame(
          puzzleSize,
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
      frame = new Frame(puzzleSize, new Random());
    }
    setAdapter(puzzleImageID);
  }

  private void updateGrid(int row, int col) {
    if(frame.move(row, col)) {
      adapter.notifyDataSetChanged();
      updateMoves();
      if (frame.hasWon()) {
        won();
      }
    } else {
      Toast.makeText(getActivity(), getString(R.string.illegal_move), Toast.LENGTH_LONG).show();
    }
  }

  private void updateMoves() {
    movesCounter.setText(String.format(getString(R.string.moves_counter), frame.getMoves()));
  }

  private void won() {
    Toast.makeText(getActivity(), getString(R.string.win), Toast.LENGTH_LONG).show();
    tileGrid.setOnItemClickListener(null);
    resetButton.setEnabled(false);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
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

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    switch (key.toLowerCase()) {
      case "num_tiles":
        puzzleSize = Integer.parseInt(sharedPreferences.getString(key, null));
        break;
      case "puzzle_image":
        if (sharedPreferences.getString(key, null).toLowerCase().equals("android")) {
          puzzleImageID = R.drawable.android_puzzle;
        } else {
          puzzleImageID = R.drawable.r2d2_puzzle;
        }
    }
    createPuzzle(null);
  }
}
