package com.example.serenitea;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FavoriteListViewAdapter extends BaseAdapter {

    final ArrayList<Quote> listFavorite;

    FavoriteListViewAdapter(ArrayList<Quote> listFavorite) {
        this.listFavorite = listFavorite;
    }

    @Override
    public int getCount() {
        return listFavorite.size();
    }

    @Override
    public Object getItem(int position) {
        return listFavorite.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listFavorite.get(position).quoteID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.favorite_view, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        Quote quote = (Quote)getItem(position);
        ((TextView)viewProduct.findViewById(R.id.favorite_content)).setText(String.format("%s", quote.content));
        ((TextView)viewProduct.findViewById(R.id.favorite_date)).setText(String.format("%s", quote.date));


        return viewProduct;
    }
}
