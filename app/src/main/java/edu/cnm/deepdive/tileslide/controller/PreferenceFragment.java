package edu.cnm.deepdive.tileslide.controller;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.cnm.deepdive.tileslide.R;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link PreferenceFragment.OnFragmentInteractionListener} interface to handle interaction events.
 * Use the {@link PreferenceFragment#newInstance} factory method to create an instance of this
 * fragment.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    addPreferencesFromResource(R.xml.preferences);
  }



  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }




}
