package com.example.serenitea;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateQuoteActivity extends Fragment {

    EditText quote;
    RelativeLayout relativeLayout;
    int DefaultColor ;
    BottomNavigationView createNavBar;
    AlertDialog backgroundDialog, textDialog, gradientDialog, fontDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_quote, container, false);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.activity_create_quote);
        quote = (EditText)view.findViewById(R.id.create_quote);

        createNavBar = view.findViewById(R.id.create_nav_bar);
        createNavBar.getMenu().getItem(0).setCheckable(false);
        createNavBar.getMenu().getItem(1).setCheckable(false);
        createNavBar.getMenu().getItem(2).setCheckable(false);

        final Item[] background_items = {
                new Item("Library", R.drawable.ic_image),
                new Item("Color", R.drawable.ic_color),
                new Item("Gradient", R.drawable.ic_gradient)
        };

        final Item[] text_items = {
                new Item("Font", R.drawable.ic_font),
                new Item("Color", R.drawable.ic_color),
                new Item("Size", R.drawable.ic_size),
        };
        final Item[] gradient_items = {
                new Item("Night Fade", R.drawable.sad_1),
                new Item("Rainy Asville", R.drawable.sad_2),
                new Item("Deep Blue", R.drawable.sad_3),
                new Item("Soft Lipstick", R.drawable.sad_4),
                new Item("Happy Unicorn", R.drawable.nervous_1),
                new Item("Sun Veggie", R.drawable.nervous_2),
                new Item("Summer Games", R.drawable.nervous_3),
                new Item("Young Grass", R.drawable.nervous_5),
                new Item("Fly High", R.drawable.angry_1),
                new Item("Happy Fisher", R.drawable.angry_2),
                new Item("Sharp Blues", R.drawable.angry_3),
                new Item("Faraway River", R.drawable.angry_4),
                new Item("New York", R.drawable.neutral_1),
                new Item("Over Sun", R.drawable.neutral_2),
                new Item("Healthy Water", R.drawable.neutral_4),
                new Item("Strict November", R.drawable.neutral_5),
                new Item("Love Kiss", R.drawable.happy_2),
                new Item("Amour Amour", R.drawable.happy_3),
                new Item("Phoenix Start", R.drawable.happy_5)
        };

        final Item[] font_items = {
                new Item("AbrilFatface", R.font.abril_fatface),
                new Item("Economica", R.font.economica),
                new Item("FredokaOne", R.font.fredoka_one),
                new Item("HammersmithOne", R.font.hammersmith_one),
                new Item("HappyMonkey", R.font.happy_monkey),
                new Item("JosefinSlab", R.font.josefin_slab_thin),
                new Item("Kavoon", R.font.kavoon),
                new Item("Lato", R.font.lato_thin),
                new Item("Merriweather", R.font.merriweather_light),
                new Item("Roboto", R.font.roboto),
        };

        ListAdapter background_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                background_items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(background_items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
        ListAdapter text_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                text_items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(text_items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        ListAdapter gradient_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                gradient_items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setText("");
                v.setBackgroundResource(gradient_items[position].icon);

                return v;
            }
        };

        ListAdapter font_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                font_items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(),font_items[position].icon);
                tv.setTypeface(typeface);

                return v;
            }
        };


        AlertDialog.Builder background_builder = new AlertDialog.Builder(getActivity());
        background_builder.setTitle("Background");
        background_builder.setAdapter(background_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        break;
                    case 1:
                        OpenColorPickerDialog(false, 0);
                        break;
                    case 2:
                        OpenGradientPickerDialog();
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog.Builder text_builder = new AlertDialog.Builder(getActivity());
        text_builder.setTitle("Text Style");
        text_builder.setAdapter(text_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        OpenFontPickerDialog();
                        break;
                    case 1:
                        OpenColorPickerDialog(false, 1);
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog.Builder gradient_builder = new AlertDialog.Builder(getActivity());
        gradient_builder.setTitle("Gradient");
        gradient_builder.setAdapter(gradient_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                relativeLayout.setBackgroundResource(gradient_items[item].icon);
            }
        });

        AlertDialog.Builder font_builder = new AlertDialog.Builder(getActivity());
        font_builder.setTitle("Font");
        font_builder.setAdapter(font_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Typeface typeface = ResourcesCompat.getFont(getActivity(),font_items[item].icon);
                quote.setTypeface(typeface);
            }
        });


// create and show the alert dialog
        backgroundDialog = background_builder.create();
        textDialog = text_builder.create();
        gradientDialog = gradient_builder.create();
        fontDialog = font_builder.create();

        createNavBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_background:
                        backgroundDialog.show();
                        return true;

                    case R.id.nav_text_style:
                        textDialog.show();
                        return true;

                    case R.id.nav_share:
                        return true;


                }

                // Default operation you want to perform

                return false;
            }
        });

        return view;
    }

    private void OpenColorPickerDialog(boolean AlphaSupport, int item) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(requireActivity(), DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;
                if (item == 0)
                {
                    relativeLayout.setBackgroundColor(color);
                }
                else if (item == 1)
                {
                    quote.setHintTextColor(color);
                    quote.setTextColor(color);
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

                Toast.makeText(requireActivity(), "Color Picker Closed", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();

    }

    private void OpenGradientPickerDialog() {
        gradientDialog.show();
    }
    private void OpenFontPickerDialog() {
        fontDialog.show();
    }

}