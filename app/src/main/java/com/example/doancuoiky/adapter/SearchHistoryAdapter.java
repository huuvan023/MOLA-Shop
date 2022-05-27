package com.example.doancuoiky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.doancuoiky.R;
import com.example.doancuoiky.modal.Search;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter  {

    private IClickOnCopyKeySearch iClickOnCopyKeySearch;
    private IClickOnSearchByKeyword iClickOnSearchByKeyword;

    // dung interface de callback su kien ra ben ngoai -> Fragment Cart
    public interface IClickOnCopyKeySearch{
        // dinh nghia cho method muon xu ly
        void onClickCopyKeySearch(int index); // truyền vào index -> vị trí cần xóa
    }

    public interface IClickOnSearchByKeyword{
        void onSearchByKeyword(int index);
    }

    public void onCopyKeySearch(SearchHistoryAdapter.IClickOnCopyKeySearch listener){
        this.iClickOnCopyKeySearch = listener;
    }

    public void onSearchByKeyword(SearchHistoryAdapter.IClickOnSearchByKeyword listener){
        this.iClickOnSearchByKeyword = listener;
    }

    Context myContext;
    int myLayout;
    List<Search> arraySearchData;
    ImageView ivCopy;
    TextView valueSearch;

    public SearchHistoryAdapter(Context context,int layout,List<Search> array){
        myContext = context;
        myLayout = layout;
        arraySearchData = array;
    }

    @Override
    public int getCount() {
        if(arraySearchData == null) return 0;
        return arraySearchData.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout,null);

        // anhs xa va gan gia tri
        valueSearch = view.findViewById(R.id.value_search );
        ivCopy = view.findViewById(R.id.icon_copy_to_search);


        // nút copy từ khóa tìm kiếm
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                iClickOnCopyKeySearch.onClickCopyKeySearch(i);
            }
        });

        // gan gia tri
        valueSearch.setText(arraySearchData.get(i).getTitle());

        valueSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickOnSearchByKeyword.onSearchByKeyword(i);
            }
        });

        return view;
    }

}
