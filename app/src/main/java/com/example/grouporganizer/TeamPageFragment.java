package com.example.grouporganizer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeamPageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_team_page, container, false);
        BottomNavigationView bottomNav = v.findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.page_1:
                        BlankFragment fragment1 = BlankFragment.newInstance("Messages");
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.team_fragment_container, fragment1)
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

}