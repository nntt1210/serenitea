package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

public class AvatarActivity extends AppCompatActivity {
    GridView simpleGrid;
    Integer avatar;
    final int logos[] = {R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9,
            R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12, R.drawable.avatar_13, R.drawable.avatar_14, R.drawable.avatar_15};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avatar);

        avatar = (Integer)getIntent().getIntExtra("EDIT_AVATAR", 0);
        if (avatar == 1) {
            //event click Back
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("");
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        else {
            getSupportActionBar().hide();
        }

        simpleGrid = (GridView) findViewById(R.id.simpleGridView); // init GridView
        // Create an object of CustomAdapter and set Adapter to GirdView
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), logos);
        simpleGrid.setAdapter(customAdapter);
        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(avatar == 1) {
                    SharedPreferences sp = getSharedPreferences("EDIT", 0);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("EDIT_AVATAR",logos[position]);
                    editor.commit();
                    finish();
                }
                else {
                    if(view.getId() == R.id.btn) {
                        Intent intent = new Intent(AvatarActivity.this, SetupActivity.class);
                        intent.putExtra("AVATAR", logos[position]);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SharedPreferences sp = getSharedPreferences("EDIT", 0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("EDIT_AVATAR", -1);
                editor.commit();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}