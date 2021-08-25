package com.example.serenitea;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class PostNotiActivity extends AppCompatActivity {

    private String PostID, author_id;
    private DatabaseReference mPost, UserRef, LikeRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String date, content;
    private Integer color, background, font, size;
    private ImageView imageViewAvatar;
    private TextView quote, txtName, likeNum;
    private ImageButton likeBtn;
    boolean likeChecker = false;
    int countLikes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageViewAvatar = (ImageView) findViewById(R.id.post_avatar);
        quote = (TextView) findViewById(R.id.post_quote);
        txtName = (TextView) findViewById(R.id.post_nickname);
        likeBtn = (ImageButton) findViewById(R.id.btn_post_like);
        likeNum = (TextView) findViewById(R.id.like_number) ;

        generatePost();

        //click like event
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeAPost(PostID, author_id);
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
                if (datasnapshot.exists()) {
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

        //set num
        LikeRef = FirebaseDatabase.getInstance().getReference().child("likes").child(PostID);
        LikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    countLikes = (int) snapshot.getChildrenCount();
                    if (snapshot.hasChild(currentUserId)) {
//                        set filled Like button
                        likeBtn.setImageResource(R.drawable.ic_like_added);
                    } else {
//                        set outlined Like button
                        likeBtn.setImageResource(R.drawable.ic_like);
                    }
                }
                likeNum.setText(Integer.toString(countLikes));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void LikeAPost(String postId, String userId) {
        likeChecker = true;

        LikeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (likeChecker) {
                    //user already liked
                    if (snapshot.hasChild(currentUserId)) {
                        LikeRef.child(currentUserId).removeValue();
                        likeChecker = false;
                    } else {//still not like
                        LikeRef.child(currentUserId).setValue(true);
                        likeChecker = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}