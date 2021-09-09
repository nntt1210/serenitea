package com.example.serenitea;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuoteNotificationActivity extends AppCompatActivity {

    private TextView QuoteView;
    private String Quote;
    private ImageButton btnFavorite;
    private String background, color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        QuoteView = (TextView)findViewById(R.id.text_quote);
        QuoteView.setMovementMethod(new ScrollingMovementMethod());
        GenerateQuote();

        Boolean btnFavoriteClicked;
        btnFavoriteClicked = new Boolean(false);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setTag(btnFavoriteClicked);
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Boolean)btnFavorite.getTag()) == false)
                {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_added);
                    btnFavorite.setTag(new Boolean(true));
                }
                else
                {
                    btnFavorite.setImageResource(R.drawable.ic_favorite);
                    btnFavorite.setTag(new Boolean(false));
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void GenerateQuote ()
    {
        DatabaseReference mQuote;
        String QuoteID;
        QuoteID = getIntent().getStringExtra("Quote");
        mQuote = FirebaseDatabase.getInstance().getReference().child("quotes").child(QuoteID);
        mQuote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    Quote = datasnapshot.child("content").getValue().toString();
                    background = datasnapshot.child("background").getValue().toString();
                    color = datasnapshot.child("color").getValue().toString();
                    QuoteView.setText(Quote);
                    QuoteView.setTextColor(Color.parseColor(color));
                    int resourceId = getResources().getIdentifier(background, "drawable", getApplicationContext().getPackageName());
                    QuoteView.setBackgroundResource(resourceId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public void setLikeButtonQuote(){

    }
}