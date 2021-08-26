package com.example.serenitea;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    Context context;
    ArrayList<Friend> listFriend;
    LayoutInflater inflater;
    public FriendAdapter(Context applicationContext, ArrayList<Friend> listFriend) {
        this.context = applicationContext;
        this.listFriend = listFriend;
        inflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return listFriend.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.friend_view, null); // inflate the layout
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(listFriend.get(i).avatar_id, "drawable", context.getPackageName());
        ImageButton btn = (ImageButton)view.findViewById(R.id.friend_avatar); // get the reference of ImageView
        btn.setImageResource(resourceId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GridView)viewGroup).performItemClick(v,i,0);
            }
        });
        TextView nickname = (TextView)view.findViewById(R.id.friend_nickname);
        nickname.setText(listFriend.get(i).nickname);
        return view;
    }
}
