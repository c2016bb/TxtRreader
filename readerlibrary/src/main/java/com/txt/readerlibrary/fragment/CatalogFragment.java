package com.txt.readerlibrary.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.txt.readerlibrary.R;
import com.txt.readerlibrary.adapter.CatalogueAdapter;
import com.txt.readerlibrary.base.BaseFragment;
import com.txt.readerlibrary.db.BookCatalogue;
import com.txt.readerlibrary.util.PageFactory;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class CatalogFragment extends BaseFragment {
    public static final String ARGUMENT = "argument";
    ListView lv_catalogue;

    private PageFactory pageFactory;
    ArrayList<BookCatalogue> catalogueList = new ArrayList<>();


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_catalog;
    }

    @Override
    protected void initData(View view) {
        lv_catalogue=(ListView) view.findViewById(R.id.lv_catalogue);

        pageFactory = PageFactory.getInstance();
        catalogueList.addAll(pageFactory.getDirectoryList());
        CatalogueAdapter catalogueAdapter = new CatalogueAdapter(getContext(), catalogueList);
        catalogueAdapter.setCharter(pageFactory.getCurrentCharter());
        lv_catalogue.setAdapter(catalogueAdapter);

        catalogueAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        lv_catalogue.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageFactory.changeChapter(catalogueList.get(position).getBookCatalogueStartPos());
                getActivity().finish();
            }
        });
    }

    /**
     * 用于从Activity传递数据到Fragment
     *
     * @param bookpath
     * @return
     */
    public static CatalogFragment newInstance(String bookpath) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, bookpath);
        CatalogFragment catalogFragment = new CatalogFragment();
        catalogFragment.setArguments(bundle);
        return catalogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
