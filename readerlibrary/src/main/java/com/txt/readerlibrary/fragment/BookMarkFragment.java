package com.txt.readerlibrary.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.txt.readerlibrary.R;
import com.txt.readerlibrary.adapter.MarkAdapter;
import com.txt.readerlibrary.base.TxtBaseFragment;
import com.txt.readerlibrary.db.BookMarks;
import com.txt.readerlibrary.util.PageFactory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by Administrator on 2016/8/31 0031.
 */
public class BookMarkFragment extends TxtBaseFragment {
    public static final String ARGUMENT = "argument";
    public  static  final  String  IS_NET_URL_MARK_FRAGMENT="IS_URL_MARK";
    ListView lv_bookmark;

    private String bookpath;
    private String mArgument;
    private List<BookMarks> bookMarksList;
    private MarkAdapter markAdapter;
    private PageFactory pageFactory;
   private  boolean isNetUrl;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_bookmark;
    }

    @Override
    protected void initData(View view) {
        lv_bookmark=(ListView) view.findViewById(R.id.lv_bookmark);
        pageFactory = PageFactory.getInstance();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookpath = bundle.getString(ARGUMENT);
            isNetUrl=bundle.getBoolean(IS_NET_URL_MARK_FRAGMENT);
        }
        bookMarksList = new ArrayList<>();
        if (isNetUrl) {
            bookMarksList = DataSupport.where("txtUrl= ?", bookpath).find(BookMarks.class);
        }else{
            bookMarksList = DataSupport.where("bookpath= ?", bookpath).find(BookMarks.class);
        }
        markAdapter = new MarkAdapter(getActivity(), bookMarksList);
        lv_bookmark.setAdapter(markAdapter);
    }

    private Dialog deleteDialog;
    @Override
    protected void initListener() {
        lv_bookmark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pageFactory.changeChapter(bookMarksList.get(position).getBegin());
                if (deleteDialog==null||!deleteDialog.isShowing()) {
                    getActivity().finish();
                }
            }
        });
        lv_bookmark.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
              deleteDialog=new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("是否删除书签？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog!=null) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.delete(BookMarks.class, bookMarksList.get(position).getId());//
                                bookMarksList.clear();
                                if (isNetUrl) {
                                    bookMarksList.addAll(DataSupport.where("txtUrl = ?", bookpath).find(BookMarks.class));
                                }else{
                                    bookMarksList.addAll(DataSupport.where("bookpath = ?", bookpath).find(BookMarks.class));
                                }
                                markAdapter.notifyDataSetChanged();
                            }
                        }).setCancelable(true).show();

                return false;
            }
        });
    }

    /**
     * 用于从Activity传递数据到Fragment
     *
     * @param bookpath
     * @return
     */
    public static BookMarkFragment newInstance(String bookpath,boolean isNetUrl) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, bookpath);
        bundle.putBoolean(IS_NET_URL_MARK_FRAGMENT,isNetUrl);
        BookMarkFragment bookMarkFragment = new BookMarkFragment();
        bookMarkFragment.setArguments(bundle);
        return bookMarkFragment;
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
