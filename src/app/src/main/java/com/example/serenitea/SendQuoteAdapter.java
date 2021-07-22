package com.example.serenitea;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SendQuoteAdapter extends BaseAdapter {

    final ArrayList<Quote> listSendQuote;

    SendQuoteAdapter(ArrayList<Quote> listSendQuote) {
        this.listSendQuote = listSendQuote;
    }

    @Override
    public int getCount() {
        return listSendQuote.size();
    }

    @Override
    public Object getItem(int position) {
        return listSendQuote.get(position);
    }

    @Override
    public long getItemId(int position) {
        //Trả về một ID của phần
        return listSendQuote.get(position).getQuoteID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convertView là View của phần tử ListView, nếu convertView != null nghĩa là
        //View này được sử dụng lại, chỉ việc cập nhật nội dung mới
        //Nếu null cần tạo mới

        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.send_quote_view, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        Quote quote = (Quote)getItem(position);
        ((TextView)viewProduct.findViewById(R.id.send_quote_content)).setText(String.format("%s", quote.getContent()));


        return viewProduct;
    }
}
