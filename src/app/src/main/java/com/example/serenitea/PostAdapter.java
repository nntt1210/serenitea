package com.example.serenitea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference databaseReference, LikeRef, UserRef;
    private Context context;
    Boolean likeChecker = false;
    private String saveCurrentDate;

    public PostAdapter(List<Post> postList, Context context) {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        this.postList = postList;
        this.context = context;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView quote, txtName, likeNum;
        ImageButton likeBtn;
        int countLikes = 0;

        public PostViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.post_avatar);
            quote = itemView.findViewById(R.id.post_quote);
            txtName = itemView.findViewById(R.id.post_nickname);
            likeNum = itemView.findViewById(R.id.like_number);
            likeBtn = itemView.findViewById(R.id.btn_post_like);

        }

        public void sendData(String id) {
            imageViewAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PersonProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("USER_ID", id);
                    DatabaseReference UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(id);
                    UserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String uname, udob, uava, ugender;
                                int ucot;
                                uname = snapshot.child("nickname").getValue().toString();
                                udob = snapshot.child("dob").getValue().toString();
                                uava = snapshot.child("avatar").getValue().toString();
                                ugender = snapshot.child("gender").getValue().toString();
                                ucot = Integer.parseInt(snapshot.child("tea").getValue().toString());

                                intent.putExtra("VIEW_AUTHOR", 0);
                                intent.putExtra("AUTHOR_AVATAR", uava);
                                intent.putExtra("AUTHOR_NICKNAME", uname);
                                intent.putExtra("AUTHOR_DOB", udob);
                                intent.putExtra("AUTHOR_CUP_OF_TEA", ucot);
                                intent.putExtra("AUTHOR_GENDER", ugender);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

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

        public void setBackground(Integer background) {
            if (background < 0) {
                quote.setBackgroundColor(background);
            } else {
                quote.setBackgroundResource(background);
            }
        }

        public void setColor(Integer color) {
            quote.setTextColor(color);
        }

        public void setFont(Integer font) {
            Typeface tf = ResourcesCompat.getFont(context, font);
            quote.setTypeface(tf);
        }

        public void setSize(Integer size) {
            quote.setTextSize(size);
        }

        public void setLikeButtonStatus(final String PostKey) {
            LikeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    likeNum.setText(Integer.toString(countLikes));
                    if (snapshot.exists()) {
                        if (snapshot.child(PostKey).hasChild(currentUserId)) {
                            countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                            likeNum.setText(Integer.toString(countLikes));
//                        set filled Like button
                            likeBtn.setImageResource(R.drawable.ic_like_added);
                        } else {
                            countLikes = (int) snapshot.child(PostKey).getChildrenCount();
                            likeNum.setText(Integer.toString(countLikes));
//                        set outlined Like button
                            likeBtn.setImageResource(R.drawable.ic_like);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
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
        Post post = postList.get(position);
        String postId = post.postId;
        String user_id = post.getUserId();
        String content = post.getContent();
        Integer color = post.getColor();
        Integer background = post.getBackground();
        Integer font = post.getFont();
        Integer size = post.getSize();

//        holder.setAuthor(user_id);
        holder.sendData(user_id);

        holder.setLikeButtonStatus(postId);

        holder.setContent(content);
        if (color != 0) {
            holder.setColor(color);
        }
        if (background != 0) {
            holder.setBackground(background);
        }
        if (font != 0) {
            holder.setFont(font);
        }
        if (size != 0) {
            holder.setSize(size);
        }
        UserRef = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
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

        //click Like
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeChecker = true;

                LikeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (likeChecker.equals(true)) {
                            //user already liked
                            if (snapshot.child(postId).hasChild(currentUserId)) {
                                LikeRef.child(postId).child(currentUserId).removeValue();
                                likeChecker = false;
                            } else {//still not like
                                LikeRef.child(postId).child(currentUserId).setValue(true);
                                likeChecker = false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
