package com.example.serenitea;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private DatabaseReference RootRef, LikeRef, UserRef;
    private Context context;
    Boolean likeChecker = false;
    private String saveCurrentDate;
    private Bitmap bitmap;
    CallbackManager callbackManager;
    private TextView quoteShare;
    private ShareDialog share_dialog;

    public PostAdapter(List<Post> postList, Context context, Activity activity) {
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        RootRef = FirebaseDatabase.getInstance().getReference();
        LikeRef = FirebaseDatabase.getInstance().getReference().child("likes");
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        this.postList = postList;
        this.context = context;

        share_dialog = new ShareDialog(activity);

        callbackManager = CallbackManager.Factory.create();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView quote, txtName, likeNum;
        ImageButton likeBtn, shareBtn, delete_btn;
        int countLikes = 0;

        public PostViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.post_avatar);
            quote = itemView.findViewById(R.id.post_quote);
            txtName = itemView.findViewById(R.id.post_nickname);
            likeNum = itemView.findViewById(R.id.like_number);
            likeBtn = itemView.findViewById(R.id.btn_post_like);
            shareBtn = itemView.findViewById(R.id.btn_post_share);
            delete_btn=itemView.findViewById(R.id.btn_post_delete);
            delete_btn.setEnabled(false);
            delete_btn.setVisibility(itemView.INVISIBLE);

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

                                intent.putExtra("VIEW_AUTHOR", 1);
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

        public void ShareQuoteOnFacebook() {

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
//                Toast.makeText(activity, "Please log in with your Facebook account first!", Toast.LENGTH_LONG).show();

            }


            //Create callback
            share_dialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
//                    Toast.makeText(getActivity(context), "Share successfully!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onCancel() {
//                    Toast.makeText(getActivity(context), "Share cancel!", Toast.LENGTH_LONG).show();

                }

                @Override
                public void onError(FacebookException error) {
//                    Toast.makeText(getActivity(context), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

        }


        public void takeScreenShot() {
            bitmap = Screenshot.takeScreenShotOfRootView(quote);
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

        if (currentUserId.equals(user_id))
        {
            holder.delete_btn.setEnabled(true);
            holder.delete_btn.setVisibility(View.VISIBLE);

        }
        //click delete
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),postId,Toast.LENGTH_SHORT).show();
                DeletePost(postId);
            }
        });
        //click Like
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LikeAPost(postId, user_id);

            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ShareQuoteOnFacebook();
            }
        });

    }



//    @Override
//    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//    }

    private void NotificateToPostOwner(String postOwner, String postId) {
        if (!postOwner.equals(currentUserId)) {
            String data_ref = "notification/" + postOwner;
            DatabaseReference notiKey = RootRef.child("notification").child(postOwner).push();
            String noti_push_id = notiKey.getKey();

            //get date
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            saveCurrentDate = currentDate.format(calendar.getTime());

            Map notiMap = new HashMap();
            notiMap.put("date", saveCurrentDate);
            notiMap.put("quote", "0");
            notiMap.put("from", currentUserId);
            notiMap.put("status", "sent");
            notiMap.put("type", 2);
            notiMap.put("postId", postId);

            Map detailNotiMap = new HashMap();
            detailNotiMap.put(data_ref + "/" + noti_push_id, notiMap);

            RootRef.updateChildren(detailNotiMap);
        }
    }

    private void LikeAPost(String postId, String userId) {
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

                        NotificateToPostOwner(userId, postId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void DeletePost(String PostId)
    {
        DatabaseReference PostRef= RootRef.child("forum");
        PostRef.child(PostId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                Toast.makeText(context,"Post Deleted",Toast.LENGTH_SHORT).show();
            }
        });
        LikeRef.child(PostId).removeValue();
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
