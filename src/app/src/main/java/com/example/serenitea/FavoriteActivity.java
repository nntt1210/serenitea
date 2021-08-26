package com.example.serenitea;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriteActivity extends Fragment {
    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, FavoriteQuoteRef, QuoteRef;
    String currentUserId;
    private RecyclerView favoriteList;
    private TextView quoteView;
    private Dialog quoteDialog;
    private View share_view;
    private Bitmap bitmap;
    CallbackManager callbackManager;
    private TextView quoteShare;
    private ShareDialog share_dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        share_view = inflater.inflate(R.layout.share_quote_view, null);
        quoteShare = (TextView)share_view.findViewById(R.id.text_share_quote);
        return inflater.inflate(R.layout.activity_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();

        RootRef = FirebaseDatabase.getInstance().getReference();
        FavoriteQuoteRef = RootRef.child("favorite").child(currentUserId);
        QuoteRef = RootRef.child("quotes");


        favoriteList = (RecyclerView) getView().findViewById(R.id.list_favorite);
        favoriteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        DisplayAllFavoriteQuote();

        callbackManager = CallbackManager.Factory.create();
        share_dialog = new ShareDialog(getActivity());
    }

    private void DisplayAllFavoriteQuote() {

        FirebaseRecyclerOptions<Quote> options =
                new FirebaseRecyclerOptions.Builder<Quote>()
                        .setQuery(FavoriteQuoteRef, Quote.class)
                        .build();

        FirebaseRecyclerAdapter<Quote, FavoriteActivity.FavoriteViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Quote, FavoriteViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FavoriteActivity.FavoriteViewHolder holder, int position, Quote model) {
                String each_quote_id = getRef(position).getKey();

                FavoriteQuoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String each_date = snapshot.child(each_quote_id).child("date").getValue().toString();
                            holder.setDate(each_date);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                QuoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String content_each_quote = snapshot.child(each_quote_id).child("content").getValue().toString();
                            final String background = snapshot.child(each_quote_id).child("background").getValue().toString();
                            final String color = snapshot.child(each_quote_id).child("color").getValue().toString();
                            holder.setContent(content_each_quote);
                            holder.setBackground(background);
                            holder.setColor(color);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder quote_builder = new AlertDialog.Builder(getActivity());
                        if(share_view.getParent() != null) {
                            ((ViewGroup)share_view.getParent()).removeView(share_view);
                        }
                        quote_builder.setView(share_view);
                        quoteView = (TextView)share_view.findViewById(R.id.text_share_quote);
                        quoteView.setText(holder.getContent());
                        final String color = holder.getColor();
                        final String background = holder.getBackground();
                        quoteView.setTextColor(Color.parseColor(color));
                        int resourceId = getResources().getIdentifier(background, "drawable", getActivity().getApplicationContext().getPackageName());
                        quoteView.setBackgroundResource(resourceId);
                        quoteDialog = quote_builder.create();
                        quoteDialog.show();
                        Window window = quoteDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                });
                //click event on each quote
                holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Are you sure you want to delete this quote?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFavoriteQuote(each_quote_id, currentUserId);
                            }
                        }).setNegativeButton("Cancel", null);
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });

                holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder share_builder = new AlertDialog.Builder(getActivity());
                        share_builder.setTitle("Share on Facebook");
                        share_builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ShareQuoteOnFacebook();
                            }
                        });
                        if(share_view.getParent() != null) {
                            ((ViewGroup)share_view.getParent()).removeView(share_view);
                        }
                        share_builder.setView(share_view);
                        quoteShare.setText(holder.getContent());
                        final String color = holder.getColor();
                        final String background = holder.getBackground();
                        quoteShare.setTextColor(Color.parseColor(color));
                        int resourceId = getResources().getIdentifier(background, "drawable", getActivity().getApplicationContext().getPackageName());
                        quoteShare.setBackgroundResource(resourceId);
                        quoteDialog = share_builder.create();
                        quoteDialog.show();
                        Window window = quoteDialog.getWindow();
                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                });
            }

            @NonNull
            @Override
            public FavoriteActivity.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_view, parent, false);
                FavoriteActivity.FavoriteViewHolder favHolder = new FavoriteActivity.FavoriteViewHolder(view);
                return favHolder;
            }
        };
        favoriteList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    private void ShareQuoteOnFacebook() {

        //take screen shot
        takeScreenShot();

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        if (ShareDialog.canShow(SharePhotoContent.class)) {
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            share_dialog.show(content);
        } else {
            Toast.makeText(getActivity(), "Please log in with your Facebook account first!", Toast.LENGTH_LONG).show();

        }


        //Create callback
        share_dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(getActivity(), "Share successfully!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Share cancel!", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

    }


    private void takeScreenShot() {
        bitmap = Screenshot.takeScreenShotOfRootView(quoteShare);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContentQuote;
        TextView textViewFavoriteDate;
        ImageButton deleteBtn, shareBtn;
        String background, color, content;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContentQuote = itemView.findViewById(R.id.favorite_content);
            textViewFavoriteDate = itemView.findViewById(R.id.favorite_date);
            deleteBtn = itemView.findViewById(R.id.favorite_delete);
            shareBtn = itemView.findViewById(R.id.favorite_share);
        }

        public void setContent(String content) {
            this.content = content;
            textViewContentQuote.setText(content);
        }

        public void setDate(String date) {
            textViewFavoriteDate.setText(date);
        }

        public void setBackground(String background) {
            this.background = background;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getContent() {
            return this.content;
        }

        public String getColor() {
            return this.color;
        }

        public String getBackground() {
            return this.background;
        }

    }

    public void deleteFavoriteQuote(String idQuote, String currentUserId) {
        DatabaseReference QuoteRemove = FirebaseDatabase.getInstance().getReference().child("favorite").child(currentUserId).child(idQuote);
        QuoteRemove.removeValue();
    }
}