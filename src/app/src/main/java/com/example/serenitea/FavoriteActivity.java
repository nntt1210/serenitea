package com.example.serenitea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FavoriteActivity extends Fragment {

//    ArrayList<Quote> listFavorite;
//    FavoriteListViewAdapter favoriteListViewAdapter;
//    ListView listViewFavorite;

    private FirebaseAuth mAuth;
    private DatabaseReference RootRef, FavoriteQuoteRef, QuoteRef;
    String currentUserId;
    private RecyclerView favoriteList;
    private String receiverID = "FRIEND_ID", quoteID;
    private String saveCurrentDate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
                QuoteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            final String content_each_quote = snapshot.child(each_quote_id).child("content").getValue().toString();
                            holder.setContent(content_each_quote);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


                //click event on each quote
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), each_quote_id, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @NonNull
            @Override
            public FavoriteActivity.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.send_quote_view, parent, false);
                FavoriteActivity.FavoriteViewHolder favHolder = new FavoriteActivity.FavoriteViewHolder(view);
                return favHolder;
            }
        };
        //Toast.makeText(SendQuoteActivity.this, "hello", Toast.LENGTH_LONG).show();
        favoriteList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView textViewContentQuote;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContentQuote = itemView.findViewById(R.id.send_quote_content);
        }

        public void setQuoteID(int id) {
        }

        public void setContent(String content) {
            textViewContentQuote.setText(content);
        }

        public void setDate(String date) {
        }
    }

}