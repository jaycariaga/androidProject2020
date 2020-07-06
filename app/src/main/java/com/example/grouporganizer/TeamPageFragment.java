package com.example.grouporganizer;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeamPageFragment extends Fragment {
    //FragmentManager fm;
    MessageFragment messageFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final String teamID = getArguments().getString("entryId");
        final View v = inflater.inflate(R.layout.fragment_team_page, container, false);
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_navigation);

        //moves back to home_page class fragments
        final TextView teamDisp = v.findViewById(R.id.teamnameDisp);
        teamDisp.setText(loadTeamName());
        final TextView homepage_switch = v.findViewById(R.id.homepage_switch);
        homepage_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Home_page homePage = new Home_page();
                getActivity().onBackPressed();
            }
        });


        //message fragment instantiated for initially played fragment
        messageFragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("entryId", teamID);
        messageFragment.setArguments(args);
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.team_fragment_container, messageFragment)
                .commit();

        //Navigation Bar Handling
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.team_fragment_container, messageFragment)
                                .commit();
                        break;
                    case R.id.page_2:
                        BlankFragment fragment2 = BlankFragment.newInstance("Data");
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.team_fragment_container, fragment2)
                                .commit();
                        break;
                    case R.id.page_3:
                        BlankFragment fragment3 = BlankFragment.newInstance("Search");
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.team_fragment_container, fragment3)
                                .commit();
                        break;

                    case R.id.page_4:
                        BlankFragment fragment4 = BlankFragment.newInstance("Tasks");
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.team_fragment_container, fragment4)
                                .commit();
                        break;
                }
                return true;
            }
        });

        return v;
    }

    private String loadTeamName() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String data = sharedPreferences.getString("teamname", "");
        return data;
    }

}