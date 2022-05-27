package com.example.doancuoiky.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doancuoiky.R;
import com.example.doancuoiky.modal.Search;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter implements Filterable {

    private IClickOnCopyKeySearch iClickOnCopyKeySearch;
    private IClickOnSearchByKeyword iClickOnSearchByKeyword;

    // dung interface de callback su kien ra ben ngoai -> Fragment Cart
    public interface IClickOnCopyKeySearch{
        // dinh nghia cho method muon xu ly
        void onClickCopyKeySearch(String product_name); // truyền vào index -> vị trí cần xóa
    }

    public interface IClickOnSearchByKeyword{
        void onSearchByKeyword(String product_name);
    }

    public void onCopyKeySearch(SearchAdapter.IClickOnCopyKeySearch listener){
        this.iClickOnCopyKeySearch = listener;
    }

    public void onSearchByKeyword(SearchAdapter.IClickOnSearchByKeyword listener){
        this.iClickOnSearchByKeyword = listener;
    }

    Context myContext;
    int myLayout;
    List<Search> arraySearchData;
    ImageView ivCopy;
    TextView valueSearch;
    CustomFilter filter;
    List<Search> filterList;

    public SearchAdapter(Context context,int layout,List<Search> array){
        myContext = context;
        myLayout = layout;
        arraySearchData = array;
        this.filterList = array;
    }

    @Override
    public int getCount() {
        if(arraySearchData == null) return 0;
        return arraySearchData.size();
    }

    @Override
    public Object getItem(int i) {
        return arraySearchData.get(i);
//        return null;
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
                notifyDataSetChanged();

                iClickOnCopyKeySearch.onClickCopyKeySearch(arraySearchData.get(i).getTitle());
            }
        });

        // gan gia tri
        valueSearch.setText(arraySearchData.get(i).getTitle());

        valueSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iClickOnSearchByKeyword.onSearchByKeyword(arraySearchData.get(i).getTitle());
            }
        });

        return view;
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new CustomFilter();
        }
        return filter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults filterResults = new FilterResults();

            if(charSequence == null || charSequence.length() == 0 ){
                filterResults.count = filterList.size();
                filterResults.values = filterList;
            }
            else{
                String searchStr = charSequence.toString().toLowerCase();
                List<Search> filters = new ArrayList<>();

                for(int i = 0;i < filterList.size();i++){
                    if(filterList.get(i).getTitle().toLowerCase().contains(searchStr)){
                        Search s = new Search(filterList.get(i).getTitle());
                        filters.add(s);
                    }
                }
                filterResults.count = filters.size();
                filterResults.values = filters;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            arraySearchData = (List<Search>) filterResults.values;

            notifyDataSetChanged();
        }
    }
}
