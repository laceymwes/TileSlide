package edu.cnm.deepdive.tileslide.controller;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import edu.cnm.deepdive.tileslide.fragment.PuzzleFragment;
import edu.cnm.deepdive.tileslide.R;
import edu.cnm.deepdive.tileslide.fragment.PreferencesFragment;

public class MainActivity extends AppCompatActivity {

  private PreferencesFragment prefFragment;
  private PuzzleFragment puzFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    setContentView(R.layout.activity_main);
    prefFragment = new PreferencesFragment();
    if (savedInstanceState == null) {
      puzFragment = new PuzzleFragment();
      getFragmentManager().beginTransaction()
          .addToBackStack("puzzle")
          .replace(R.id.container, puzFragment)
          .commit();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();

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
        getFragmentManager()
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.container, prefFragment)
            .commit();
    }
    return true;
  }

}
