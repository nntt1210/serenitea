package com.example.serenitea;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationAdapter extends BaseAdapter {

    final ArrayList<String> listNotification;

    NotificationAdapter(ArrayList<String> listNotification) {
        this.listNotification = listNotification;
    }

    @Override
    public int getCount() {
        return listNotification.size();
    }

    @Override
    public Object getItem(int position) {
        return listNotification.get(position);
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
            viewProduct = View.inflate(parent.getContext(), R.layout.notification_view, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        String nickname = (String)getItem(position);
        ((TextView)viewProduct.findViewById(R.id.notification_content)).setText(String.format("%s sent you a quote.", nickname));


        return viewProduct;
    }
}
