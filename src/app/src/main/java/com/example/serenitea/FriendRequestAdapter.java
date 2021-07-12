package com.example.serenitea;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FriendRequestAdapter extends BaseAdapter {

    final ArrayList<String> listFriendRequest;

    FriendRequestAdapter(ArrayList<String> listFriendRequest) {
        this.listFriendRequest = listFriendRequest;
    }

    @Override
    public int getCount() {
        return listFriendRequest.size();
    }

    @Override
    public Object getItem(int position) {
        return listFriendRequest.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.friend_request_view, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        String nickname = (String)getItem(position);
        ((TextView)viewProduct.findViewById(R.id.friend_request_nickname)).setText(String.format("%s", nickname));


        return viewProduct;
    }
}
