package edu.cnm.deepdive.tileslide.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
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
import edu.cnm.deepdive.tileslide.PuzzleFragment;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.model.Frame;
import edu.cnm.deepdive.tileslide.model.Tile;
import edu.cnm.deepdive.tileslide.view.FrameAdapter;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

  public static final String TILE_NUMS_KEY = "tileNums";
  public static final String START_NUMS_KEY = "startNums";
  private static int puzzleSize = 3;
  private static int puzzleImageID = R.drawable.android_puzzle;
  private static final int PREFERENCE_REQUEST_CODE = 1;
  private static final String NUM_TILES_PREF_KEY = "num_tiles";
  private static final String PUZZLE_IMAGE_PREF_KEY = "puzzle_image";



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
    getSupportFragmentManager().beginTransaction()
        .addToBackStack(null)
        .replace(R.id.container, new PuzzleFragment())
        .commit();
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
        getFragmentManager().beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, new PreferenceFragment())
            .commit();

    }
    return true;
  }
}
