package com.example.serenitea;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private Context context;
    private String fromQuote;

    public NotificationAdapter(List<Notification> notiList, Context context) {
        this.notiList = notiList;
        this.context = context;
//        Collections.sort(this.notiList );
    }

    public class NotiViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewAvatar;
        TextView textViewNotification;
        LinearLayout layoutNotification;

        public NotiViewHolder(View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.notification_avatar);
            textViewNotification = itemView.findViewById(R.id.notification_content);
            layoutNotification = itemView.findViewById(R.id.layout_notification);
        }

        public void setAvatar(String id) {
            int resource = context.getResources().getIdentifier(id, "drawable", context.getPackageName());
            imageViewAvatar.setImageResource(resource);
        }

        public void setContent(String content) {
            textViewNotification.setText(content + " sent you a quote.");
        }

        public void setBackgroundLayout() {
            //đổi màu background của notification mới
            layoutNotification.setBackgroundColor(Color.rgb(245, 194, 197));
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
        fromQuote = notification.getQuote();
        String fromStatus = notification.getStatus();

        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("notification").child(currentUserID);
        UserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.setContent(snapshot.child(fromUserID).child("nickname").getValue().toString());
                    holder.setAvatar(snapshot.child(fromUserID).child("avatar").getValue().toString());
                    if (fromStatus.equals("sent")) holder.setBackgroundLayout();
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
                Intent intent = new Intent(context, QuoteNotificationActivity.class);
                intent.putExtra("Quote", fromQuote);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notiList.size();
    }
}
