package com.example.serenitea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostNotiActivity extends AppCompatActivity {

    private String PostID, author_id;
    private DatabaseReference mPost, UserRef;
    private String date, content;
    private Integer color, background, font, size;
    private ImageView imageViewAvatar;
    private TextView quote, txtName, likeNum;
    private ImageButton likeBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageViewAvatar = (ImageView)findViewById(R.id.post_avatar);
        quote = (TextView)findViewById(R.id.post_quote);
        txtName = (TextView)findViewById(R.id.post_nickname);

        generatePost();
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

    private void generatePost() {
        author_id = getIntent().getStringExtra("Author");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(author_id);
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String ava_id = snapshot.child("avatar").getValue().toString();
                    int resource = getApplicationContext().getResources().getIdentifier(ava_id, "drawable", getApplicationContext().getPackageName());
                    imageViewAvatar.setImageResource(resource);
                    txtName.setText(snapshot.child("nickname").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        PostID = getIntent().getStringExtra("Post");
        mPost = FirebaseDatabase.getInstance().getReference().child("forum").child(PostID);
        mPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists())
                {
                    content = datasnapshot.child("content").getValue().toString();
                    quote.setText(content);
                    background = Integer.valueOf(String.valueOf(datasnapshot.child("background").getValue()));
                    color = Integer.valueOf(String.valueOf(datasnapshot.child("color").getValue()));
                    font = Integer.valueOf(String.valueOf(datasnapshot.child("font").getValue()));
                    size = Integer.valueOf(String.valueOf(datasnapshot.child("size").getValue()));
                    if (background < 0) {
                        quote.setBackgroundColor(background);
                    } else {
                        quote.setBackgroundResource(background);
                    }
                    if (color != 0) {
                        quote.setTextColor(color);
                    }
                    if (font != 0) {
                        Typeface tf = ResourcesCompat.getFont(getApplicationContext(), font);
                        quote.setTypeface(tf);
                    }
                    if (size != 0) {
                        quote.setTextSize(size);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}