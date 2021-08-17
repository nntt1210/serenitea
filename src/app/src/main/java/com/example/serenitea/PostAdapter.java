package com.example.serenitea;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, LikeRef, UserRef;
    private Context context;

    public PostAdapter(List<Post> postList, Context context) {
        mAuth = FirebaseAuth.getInstance();
        this.postList = postList;
        this.context = context;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView quote, txtName, likeNum;

        public PostViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.post_avatar);
            quote = itemView.findViewById(R.id.post_quote);
            txtName = itemView.findViewById(R.id.post_nickname);
            likeNum = itemView.findViewById(R.id.like_number);

        }

        public void setAvatar(String id) {
            int resource = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
            imageViewAvatar.setImageResource(resource);
        }

        public void setContent(String content) {
            quote.setText(content);
        }

        public void setName(String name) {
            txtName.setText(name);
        }

        public void setLike(String like) {
            likeNum.setText(like);
        }

//        public void setBackground(Integer background) {
//            quote.setBackgroundResource(background);
//        }
//
//        public void setColor(Integer color) {
//            quote.setTextColor(color);
//        }
//
//        public void setFont(Integer font) {
//            Typeface tf = ResourcesCompat.getFont(context, font);
//            quote.setTypeface(tf);
//        }

        //----------------set cố định background, font, color--------------
        public void setBackground(Integer background) {
            quote.setBackgroundResource(R.drawable.neutral_1);
        }

        @SuppressLint("ResourceAsColor")
        public void setColor(Integer color) {
            quote.setTextColor(R.color.text_friend);
        }

        public void setFont(Integer font) {
            Typeface tf = ResourcesCompat.getFont(context, R.font.open_sans_semibold);
            quote.setTypeface(tf);
        }
//---------------------------------------------------------------------
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_view, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new PostAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.PostViewHolder holder, int position) {

//        String currentUserID = mAuth.getCurrentUser().getUid();
        Post post = postList.get(position);
        String postId = post.postId;
        String user_id = post.getUserId();
        String date = post.getDate();
        String content = post.getContent();
        Integer color = post.getColor();
        Integer background = post.getBackground();
        Integer font = post.getFont();

        holder.setContent(content);
        holder.setColor(color);
        holder.setBackground(background);
        holder.setFont(font);
        UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        LikeRef = FirebaseDatabase.getInstance().getReference().child("likes").child(postId);

        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.setAvatar(snapshot.child("avatar").getValue().toString());
                    holder.setName(snapshot.child("nickname").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        LikeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            String size = "0";

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    size = String.valueOf(snapshot.getChildrenCount());
                    if (snapshot.child(mAuth.getUid()).exists()) {
                        //change Like button

                    }
                }
                holder.setLike(size);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
