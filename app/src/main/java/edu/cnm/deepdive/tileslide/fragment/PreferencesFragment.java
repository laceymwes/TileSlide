package edu.cnm.deepdive.tileslide.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import edu.cnm.deepdive.tileslide.R;


public class PreferencesFragment extends PreferenceFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    addPreferencesFromResource(R.xml.preferences);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    MenuItem item = menu.findItem(R.id.preferences);
    item.setVisible(false);
    item.setEnabled(false);
    super.onCreateOptionsMenu(menu, inflater);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }
}
