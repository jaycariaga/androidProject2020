package com.example.grouporganizer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class TeamActivity extends Fragment  {

    //dropdown box for actions are here instead of a resource file because being an admin changes the actions...
    //private Spinner spinner;
    private static final String[] paths = {"Choose One", "Poke User", "POST a Task", "Leave Team"};
    Button showActions;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.team_page, container, false);

        showActions = (Button) v.findViewById(R.id.showTeamAction);
        showActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View dialogview = inflater.inflate(R.layout.team_menu_spinner, null);
                builder.setTitle("Pick an Action");
                final Spinner spinner = (Spinner) dialogview.findViewById(R.id.spinner);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 1:
                                Toast.makeText(getContext(), "poked", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(getContext(), "goes to task page", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getContext(), "api to leave team", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                builder.setPositiveButton("Perform Action", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!spinner.getSelectedItem().toString().equalsIgnoreCase(adapter.getItem(0))){
                            Toast.makeText(getContext(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                        else{
                            Toast.makeText(getContext(), "Must choose one to perform action.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "Action Cancelled", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //displays popup box
                builder.setView(dialogview);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return v;
    }

}
