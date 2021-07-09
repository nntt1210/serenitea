package com.example.serenitea;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoriteActivity extends Fragment {

    ArrayList<Quote> listFavorite;
    FavoriteListViewAdapter favoriteListViewAdapter;
    ListView listViewFavorite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_favorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listFavorite = new ArrayList<>();
        listFavorite.add(new Quote(1, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
        listFavorite.add(new Quote(2, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));
        listFavorite.add(new Quote(3, "When you can't control what's happening, challenge yourself to control the way you respond. That's where your power is.", "02/07/2021"));

        favoriteListViewAdapter = new FavoriteListViewAdapter(listFavorite);

        listViewFavorite = getView().findViewById(R.id.list_favorite);
        listViewFavorite.setAdapter(favoriteListViewAdapter);
    }


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_favorite);
//
//
//
////        //Lắng nghe bắt sự kiện một phần tử danh sách được chọn
////        listViewProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Product product = (Product) productListViewAdapter.getItem(position);
////                //Làm gì đó khi chọn sản phẩm (ví dụ tạo một Activity hiện thị chi tiết, biên tập ..)
////                Toast.makeText(MainActivity.this, product.name, Toast.LENGTH_LONG).show();
////            }
////        });
////
////
////        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (listProduct.size() > 0) {
////                    //Xoá phần tử đầu tiên của danh sách
////                    int productpost = 0;
////                    listProduct.remove(productpost);
////                    //Thông báo cho ListView biết dữ liệu đã thay đổi (cập nhật, xoá ...)
////                    productListViewAdapter.notifyDataSetChanged();
////                }
////            }
////        });
//    }
}