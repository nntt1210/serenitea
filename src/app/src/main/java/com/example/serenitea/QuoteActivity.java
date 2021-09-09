package com.example.serenitea;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuoteActivity extends AppCompatActivity {
    /* Trang in quote ra màn hình
     * Một số hàm:
     * - Dựa vào id trả về trong EmotionActivity, vào database generate quote đó ra màn hình
     * - Tạo phần like bài viết, khi like thì lưu quote vào database --> setLikeButtonQuote()
     * - Các hàm SendUserTo...Activity()
     * */

    private TextView QuoteView;
    private String QuoteID;
    private String Quote;
    private String background, color;
    private ImageButton btnFavorite, btnShare;
    private Boolean btnFavoriteClicked;
    private String curUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference Ref;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private View quoteShare;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        //event click Back
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));
        actionBar.setDisplayHomeAsUpEnabled(true);

        QuoteView = findViewById(R.id.text_quote);
        QuoteView.setMovementMethod(new ScrollingMovementMethod());
        GenerateQuote();

        btnFavoriteClicked = new Boolean(false);
        btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setTag(btnFavoriteClicked);
        isFav(QuoteID);
        btnFavorite.setOnClickListener(v -> {
            if (!QuoteID.isEmpty()) {
                if (((Boolean) btnFavorite.getTag()) == false) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_added);
                    btnFavorite.setTag(new Boolean(true));
                    saveToFavQuote(QuoteID);

                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite);
                    btnFavorite.setTag(new Boolean(false));
                    removeFromFav(QuoteID);
                }
            }
        });


        btnShare = findViewById(R.id.btn_share);
        //init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        btnShare.setOnClickListener(v -> ShareQuoteOnFacebook());
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

    private void GenerateQuote() {
        DatabaseReference mQuote;
        QuoteID = getIntent().getStringExtra("quoteID");
        mQuote = FirebaseDatabase.getInstance().getReference().child("quotes").child(QuoteID);
        mQuote.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
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

    public void setLikeButtonQuote() {

    }

    public void saveToFavQuote(String QuoteID) {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(d);
        Ref = FirebaseDatabase.getInstance().getReference().child("favorite").child(curUser);
        Ref.child(QuoteID).child("date").setValue(formattedDate);
        Ref.child(QuoteID).child("dateSystem").setValue(ServerValue.TIMESTAMP);
    }

    public void removeFromFav(String QuoteID) {
        Ref = FirebaseDatabase.getInstance().getReference().child("favorite").child(curUser);
        Ref.child(QuoteID).removeValue();
    }

    public void isFav(String QuoteID) {
        Ref = FirebaseDatabase.getInstance().getReference().child("favorite").child(curUser);
        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(QuoteID)) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_added);
                    btnFavorite.setTag(new Boolean(true));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void ShareQuoteOnFacebook() {
        //take screen shot
        takeScreenShot();

        //share image
        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            shareDialog.show(content);
        } else {
            Toast.makeText(QuoteActivity.this, "Please log in with your Facebook account first!", Toast.LENGTH_LONG).show();

        }


        //Create callback
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(QuoteActivity.this, "Share successfully!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(QuoteActivity.this, "Share cancel!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(QuoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void takeScreenShot() {
        quoteShare = findViewById(R.id.text_quote);
        bitmap = Screenshot.takeScreenShotOfRootView(quoteShare);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}