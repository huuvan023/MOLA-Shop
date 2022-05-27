package com.example.doancuoiky.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancuoiky.GlobalVariable;
import com.example.doancuoiky.R;
import com.example.doancuoiky.activity.ProductDetailActivity;
import com.example.doancuoiky.adapter.ProductSearchAdapter;
import com.example.doancuoiky.adapter.ProductSuggestionAdapter;
import com.example.doancuoiky.adapter.SearchAdapter;
import com.example.doancuoiky.adapter.SearchHistoryAdapter;
import com.example.doancuoiky.modal.Product;
import com.example.doancuoiky.modal.Search;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private ListView lvDataNameProduct,lvHistory;
    private ArrayList<Search> arrayListProductName, arrayListHistory;
    private TextView clearHistory;
    private SearchAdapter adapterData;
    private SearchHistoryAdapter adapterHistory;
    private LinearLayout layoutHistory;
    private View view;
    EditText txtSearch;
    SearchView searchView;
    ArrayList<Product> arraySearch;
    TextView tvNoSearchResultReturn;
    TextView searchKeyword;

    private RecyclerView rcvSearch, rcvSuggestion;
    private LinearLayout layoutSuggestion;
    ProductSearchAdapter searchAdapter;
    ProductSuggestionAdapter adapterSuggestion;
    RelativeLayout layoutResultSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_search, container, false);

        anhXa(view);
        checkData();
        setAdapterSearch();

        clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayListHistory.clear();
                adapterHistory.notifyDataSetChanged();
                checkData();
            }
        });

        searchAdapter.onGotoDetail(new ProductSearchAdapter.IClickGotoProductDetail() {
            @Override
            public void onClickGotoDetail(String idProduct) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),ProductDetailActivity.class);
                intent.putExtra("productDetail",idProduct);
                startActivity(intent);
            }
        });

        adapterSuggestion.onGotoDetail(new ProductSuggestionAdapter.IClickGotoProductDetail() {
            @Override
            public void onClickGotoDetail(String idProduct) {
                Intent intent = new Intent(Objects.requireNonNull(getActivity()).getApplicationContext(),ProductDetailActivity.class);
                intent.putExtra("productDetail",idProduct);
                startActivity(intent);
            }
        });


        setHasOptionsMenu(true);
        return  view;
    }


    private void setDataSearch(String keyword){

        arraySearch.clear();
        for(int i = 0;i < GlobalVariable.arrayProduct.size();i++){
            String strRoot = GlobalVariable.arrayProduct.get(i).getProductName().toLowerCase();
            String srtKeyword = keyword.trim().toLowerCase();
            if(strRoot.contains(srtKeyword)){
                arraySearch.add(GlobalVariable.arrayProduct.get(i));
            }
        }
        if(arraySearch.size() == 0){
            tvNoSearchResultReturn.setVisibility(View.VISIBLE);
            layoutSuggestion.setVisibility(View.GONE);
        }
        else{
            tvNoSearchResultReturn.setVisibility(View.GONE);
            layoutSuggestion.setVisibility(View.VISIBLE);
        }
        searchAdapter.notifyDataSetChanged();

    }

    private void setAdapterSearch(){
        searchAdapter = new ProductSearchAdapter(getContext(), arraySearch);
        rcvSearch.setHasFixedSize(true);
        rcvSearch.setLayoutManager(new GridLayoutManager(getContext(),2));
        rcvSearch.setAdapter(searchAdapter);
    }

    private void anhXa(View view) {

        if(arraySearch == null){
            arraySearch = new ArrayList<>();
        }

        lvDataNameProduct = view.findViewById(R.id.lv_search);
        lvHistory = view.findViewById(R.id.lv_history_search);
        clearHistory = view.findViewById(R.id.tv_delete_history_search_fragment);
        layoutHistory = view.findViewById(R.id.layout_history_search_search_fragment);
        rcvSearch = view.findViewById(R.id.rcv_search);
        tvNoSearchResultReturn = view.findViewById(R.id.tv_no_search_result_return);
        arrayListProductName = new ArrayList<>();
        rcvSuggestion = view.findViewById(R.id.rcv_suggestion_rate_greater_than_4_3);
        layoutSuggestion = view.findViewById(R.id.layout_suggestion_rate_greater_than_4_3);
        layoutResultSearch = view.findViewById(R.id.layout_result_search);
        searchKeyword = view.findViewById(R.id.tv_search_keyword);
        // set data
        for(int i = 0;i < GlobalVariable.arrayProduct.size(); i++){
            arrayListProductName.add(new Search(GlobalVariable.arrayProduct.get(i).getProductName().trim()));
        }

        if(arrayListHistory == null){
            arrayListHistory = new ArrayList<>();
        }


        adapterData = new SearchAdapter(getContext(),R.layout.item_search,arrayListProductName);
        lvDataNameProduct.setAdapter(adapterData);

        adapterHistory = new SearchHistoryAdapter(getContext(),R.layout.item_search,arrayListHistory);
        lvHistory.setAdapter(adapterHistory);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false);
        rcvSuggestion.setLayoutManager(linearLayoutManager);

        adapterSuggestion = new ProductSuggestionAdapter(getContext(),GlobalVariable.arraySuggestion);
        rcvSuggestion.setAdapter(adapterSuggestion);


    }

    // them icon search len thanh toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search,menu);

//        // auto focus search
        menu.findItem(R.id.ic_search_toolbar).expandActionView();
        layoutSuggestion.setVisibility(View.GONE);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                //expand

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                //Collapse
                layoutSuggestion.setVisibility(View.GONE);
                return true;
            }
        };

        menu.findItem(R.id.ic_search_toolbar).setOnActionExpandListener(onActionExpandListener);

        searchView = (SearchView) menu.findItem(R.id.ic_search_toolbar).getActionView();

        txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint("Nhập tên sản phẩm bạn muốn tìm kiếm");

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);


        adapterHistory.onCopyKeySearch(new SearchHistoryAdapter.IClickOnCopyKeySearch() {
            @Override
            public void onClickCopyKeySearch(int index) {

                String text = arrayListHistory.get(index).getTitle();
                if(menu.findItem(R.id.ic_search_toolbar).expandActionView()){
                    txtSearch.setText(text);
                }
            }
        });

        adapterHistory.onSearchByKeyword(new SearchHistoryAdapter.IClickOnSearchByKeyword() {
            @Override
            public void onSearchByKeyword(int index) {
                String text = arrayListHistory.get(index).getTitle();
                if(menu.findItem(R.id.ic_search_toolbar).expandActionView()){
                    txtSearch.setText(text);

                    checkAddHistory(text);
                    txtSearch.clearFocus();
                    lvDataNameProduct.setVisibility(View.GONE);
                    hideKeyboard(view);
                    checkFocus();

                    showDataSearchReturn(text);
                }
            }
        });

        adapterData.onSearchByKeyword(new SearchAdapter.IClickOnSearchByKeyword() {
            @Override
            public void onSearchByKeyword(String product_name) {
                if(menu.findItem(R.id.ic_search_toolbar).expandActionView()){
                    txtSearch.setText(product_name);

                    checkAddHistory(product_name);
                    txtSearch.clearFocus();
                    lvDataNameProduct.setVisibility(View.GONE);
                    hideKeyboard(view);
                    checkFocus();

                    showDataSearchReturn(product_name);
                }
            }
        });

        adapterData.onCopyKeySearch(new SearchAdapter.IClickOnCopyKeySearch() {
            @Override
            public void onClickCopyKeySearch(String product_name) {
                if(menu.findItem(R.id.ic_search_toolbar).expandActionView()){
                    txtSearch.setText(product_name);
                }
            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                checkAddHistory(query);
                txtSearch.clearFocus();
                lvDataNameProduct.setVisibility(View.GONE);
                hideKeyboard(view);
                checkFocus();

                showDataSearchReturn(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() > 0){
                    lvDataNameProduct.setVisibility(View.VISIBLE);
                    adapterData.getFilter().filter(newText);
                    layoutHistory.setVisibility(View.INVISIBLE);
                }
                else{
                    lvDataNameProduct.setVisibility(View.INVISIBLE);
                    layoutResultSearch.setVisibility(View.GONE);
                    tvNoSearchResultReturn.setVisibility(View.GONE);
                    checkData();
                }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    private void checkFocus() {

        txtSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if(txtSearch.getText().toString().length() == 0){
                        lvDataNameProduct.setVisibility(View.GONE);
                    }else{
                        lvDataNameProduct.setVisibility(View.VISIBLE);
                    }
                    layoutResultSearch.setVisibility(View.GONE);
                    layoutSuggestion.setVisibility(View.GONE);
                } else {
                    lvDataNameProduct.setVisibility(View.GONE);

                }
            }
        });
    }

    private void showDataSearchReturn(String keyword) {
        layoutResultSearch.setVisibility(View.VISIBLE);
        searchKeyword.setText(keyword);

        setDataSearch(keyword);
    }

    private void checkAddHistory(String query){
        boolean flag = true;
        for(int i = 0;i < arrayListHistory.size();i++){
            if(arrayListHistory.get(i).getTitle().equals(query)){
                flag = false;
                return;
            }
        }
        if(flag){
            Search newHistory = new Search(query);
            arrayListHistory.add(newHistory);
            adapterHistory.notifyDataSetChanged();
        }
    }

    private void checkData(){
        if(arrayListHistory.size() <= 0){
            layoutHistory.setVisibility(View.INVISIBLE);
        }
        else{
            layoutHistory.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}














