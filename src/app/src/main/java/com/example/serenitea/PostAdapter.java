package com.example.serenitea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{

    private List<Post> postList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, UserRef;
    private Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
//        Collections.sort(this.notiList );
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView quote, txtName;

        public PostViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.post_avatar);
            quote = itemView.findViewById(R.id.post_quote);
            txtName = itemView.findViewById(R.id.post_nickname);

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
            quote.setBackgroundResource(background);
        }

        public void setColor(Integer color) {
            quote.setTextColor(color);
        }

        public void setFont(Integer font) {
            Typeface tf = ResourcesCompat.getFont(context, font);
            quote.setTypeface(tf);
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

//        String currentUserID = mAuth.getCurrentUser().getUid();
        Post post = postList.get(position);

        String user_id = post.getUserId();
        String name = post.getName();
        String avatar = post.getAvatar();
        String date = post.getDate();
        String content = post.getContent();
        Integer color = post.getColor();
        Integer background = post.getBackground();
        Integer font = post.getFont();

        holder.setAvatar(avatar);
        holder.setName(name);
        holder.setContent(content);
        holder.setColor(color);
        holder.setBackground(background);
        holder.setFont(font);

//        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(currentUserID);
//        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    holder.setContent(snapshot.child(fromUserID).child("nickname").getValue().toString());
//                    holder.setAvatar(snapshot.child(fromUserID).child("avatar").getValue().toString());
//                    if (fromStatus.equals("sent")) holder.setBackgroundLayout();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
