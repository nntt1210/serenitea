package com.example.serenitea;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateQuoteActivity extends Fragment {

    Button button;
    RelativeLayout relativeLayout;
    int DefaultColor ;
    BottomNavigationView createNavBar;
    AlertDialog backgroundDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_quote, container, false);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.activity_create_quote);

        createNavBar = view.findViewById(R.id.create_nav_bar);
        createNavBar.getMenu().getItem(0).setCheckable(false);

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose an animal");

        // add a list
        String[] animals = {"horse", "cow", "camel", "sheep", "goat"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // horse
                    case 1: // cow
                    case 2: // camel
                    case 3: // sheep
                    case 4: // goat
                }
            }
        });

// create and show the alert dialog
        backgroundDialog = builder.create();

        createNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_background:
                        backgroundDialog.show();
                        return true;

                    case R.id.nav_text_style:
                        return true;

                    case R.id.nav_share:
                        return true;


                }

                // Default operation you want to perform

                return false;
            }
        });

//        button = (Button)view.findViewById(R.id.button);

//        DefaultColor = ContextCompat.getColor(getActivity().getApplicationContext(), R.color.theme);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                OpenColorPickerDialog(false);
//
//            }
//        });
        return view;
    }

//    private void OpenColorPickerDialog(boolean AlphaSupport) {
//
//        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(getActivity().getApplicationContext(), DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
//
//                DefaultColor = color;
//
//                relativeLayout.setBackgroundColor(color);
//            }
//
//            @Override
//            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
//
//                Toast.makeText(getActivity().getApplicationContext(), "Color Picker Closed", Toast.LENGTH_SHORT).show();
//            }
//        });
//        ambilWarnaDialog.show();
//
//    }

}