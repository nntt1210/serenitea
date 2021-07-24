package com.example.serenitea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotiViewHolder> {
    private List<Notification> notiList;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference, UserRef;

    public NotificationAdapter(List<Notification> notiList) {
        this.notiList = notiList;
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewNotification;

        public NotiViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.notification_avatar);
            textViewNotification = itemView.findViewById(R.id.notification_content);
        }
        public void setAvatar(String id) {
            //int resource = getResources().getIdentifier(id, "drawable", getPackageName());
            //imageViewAvatar.setImageResource(resource);
        }
        public void setContent(String content) {
            textViewNotification.setText(content + " sent you a quote.");
        }
    }

    @NonNull
    @Override
    public NotiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_view, parent, false);
        mAuth = FirebaseAuth.getInstance();
        return new NotiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotiViewHolder holder, int position) {

        String currentUserID = mAuth.getCurrentUser().getUid();
        Notification notification = notiList.get(position);

        String fromUserID = notification.getFrom();
        String fromDate = notification.getDate();
        String fromTime = notification.getTime();
        String fromQuote = notification.getQuote();

        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(currentUserID);
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    holder.setContent(snapshot.child(fromUserID).child("nickname").getValue().toString());
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

            }
        });
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }
}
