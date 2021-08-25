package com.example.serenitea;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateQuoteActivity extends Fragment {

    private EditText quote;
    private RelativeLayout relativeLayout;
    private int DefaultColor;
    private BottomNavigationView createNavBar;
    private Dialog backgroundDialog, textDialog, gradientDialog, fontDialog, sizeDialog, shareDialog;
    ShareDialog share_dialog;
    private String content, date;
    private Integer background = 0, color = 0, font = 0, size = 0;
    private String saveCurrentDate;
    private FirebaseAuth mAuth;
    private DatabaseReference PostRef;
    private Bitmap bitmap;
    CallbackManager callbackManager;
    private View quoteShare;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_create_quote, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.activity_create_quote);
        quote = (EditText) view.findViewById(R.id.create_quote);

        createNavBar = view.findViewById(R.id.create_nav_bar);
        createNavBar.getMenu().getItem(0).setCheckable(false);
        createNavBar.getMenu().getItem(1).setCheckable(false);
        createNavBar.getMenu().getItem(2).setCheckable(false);

        final Item[] background_items = {
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

        final Item[] share_items = {
                new Item("Serenitea", R.drawable.logo_dialog),
                new Item("Facebook", R.drawable.ic_facebook)
        };

        ListAdapter background_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                background_items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

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
                text_items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

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
                gradient_items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setText("");
                v.setBackgroundResource(gradient_items[position].icon);

                return v;
            }
        };

        ListAdapter font_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                font_items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                Typeface typeface = ResourcesCompat.getFont(getActivity(), font_items[position].icon);
                tv.setTypeface(typeface);

                return v;
            }
        };

        ListAdapter share_adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                share_items) {
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(share_items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };


        AlertDialog.Builder background_builder = new AlertDialog.Builder(getActivity());
        background_builder.setTitle("Background");
        background_builder.setAdapter(background_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        OpenColorPickerDialog(false, 0);
                        break;
                    case 1:
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
                        OpenSizePickerDialog();
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
                background = gradient_items[item].icon;
            }
        });

        AlertDialog.Builder font_builder = new AlertDialog.Builder(getActivity());
        font_builder.setTitle("Font");
        font_builder.setAdapter(font_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Typeface typeface = ResourcesCompat.getFont(getActivity(), font_items[item].icon);
                font = font_items[item].icon;
                quote.setTypeface(typeface);
            }
        });

        AlertDialog.Builder share_builder = new AlertDialog.Builder(getActivity());
        share_builder.setTitle("Share");
        share_builder.setAdapter(share_adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        getData();
                        break;
                    case 1:

                        break;
                    default:
                        break;
                }
            }
        });


        backgroundDialog = background_builder.create();
        textDialog = text_builder.create();
        gradientDialog = gradient_builder.create();
        fontDialog = font_builder.create();
        shareDialog = share_builder.create();


        final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Size");

        LinearLayout linear = new LinearLayout(getActivity());

        linear.setOrientation(LinearLayout.VERTICAL);

        SeekBar seekbar = new SeekBar(getActivity());

        SeekBar.OnSeekBarChangeListener yourSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                size = progress;
                quote.setTextSize(progress);
            }
        };

        seekbar.setOnSeekBarChangeListener(yourSeekBarListener);

        linear.addView(seekbar);
        alert.setView(linear);
        sizeDialog = alert.create();

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
                        shareDialog.show();
                        return true;


                }

                // Default operation you want to perform

                return false;
            }
        });

//        setData();

        return view;
    }


    private void getData() {
        content = quote.getText().toString();
        color = quote.getCurrentTextColor();

        SaveInDatabase(content, color, background, font, size);
    }

    private void SaveInDatabase(String content, Integer color, Integer background, Integer font, Integer size) {
        mAuth = FirebaseAuth.getInstance();
        PostRef = FirebaseDatabase.getInstance().getReference().child("forum");
        DatabaseReference postKey = PostRef.push();
        String post_push_id = postKey.getKey();

        //get date time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        saveCurrentDate = currentDate.format(calendar.getTime());

        Map postMap = new HashMap();
        postMap.put("date", saveCurrentDate);
//        sendMap.put("time", saveCurrentTime);
        postMap.put("content", content);
        postMap.put("color", color);
        postMap.put("background", background);
        postMap.put("font", font);
        postMap.put("size", size);
        postMap.put("userId", mAuth.getUid());

        Map detailSendMap = new HashMap();
        detailSendMap.put(post_push_id, postMap);

        PostRef.updateChildren(detailSendMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Create quote successfully!", Toast.LENGTH_LONG).show();
                } else {
                    String message = task.getException().getMessage();
                    Toast.makeText(getActivity(), "Error" + message, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void OpenColorPickerDialog(boolean AlphaSupport, int item) {

        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(requireActivity(), DefaultColor, AlphaSupport, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {

                DefaultColor = color;
                if (item == 0) {
                    background = color;
                    relativeLayout.setBackgroundColor(color);
                } else if (item == 1) {
                    quote.setHintTextColor(color);
                    quote.setTextColor(color);
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {

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

    private void OpenSizePickerDialog() {
        sizeDialog.show();
    }

    private void ShareQuoteOnFacebook() {
        //take screen shot
//        takeScreenShot();

        //share image
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            share_dialog.show(content);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Please log in with your Facebook account first!", Toast.LENGTH_LONG).show();

        }


        //Create callback
        share_dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity().getApplicationContext(), "Share successfully!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Share cancel!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

//    private View createQuoteView() {
//        TextView share_quote;
//        share_quote.setText(quote.getText());
//    }
//
//    private void takeScreenShot() {
//        quoteShare = findViewById(R.id.text_quote);
//        bitmap = Screenshot.takeScreenShotOfRootView(quoteShare);
//    }

}