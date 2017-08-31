package com.txt.readerlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.txt.readerlibrary.db.BookList;
import com.txt.readerlibrary.util.Fileutil;
import com.txt.readerlibrary.util.PageFactory;
import com.txt.readerlibrary.utils.DownLoadFile;
import com.txt.readerlibrary.utils.TxtLogUtils;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/25.
 */

public class TxtReader{
    public static Context context;
    public  static  TxtReader txtReader;

    public static TxtReader getTxtReader() {
        return txtReader;
    }

    public TxtReader(Context context) {
        this.context=context;
                LitePal.initialize(context);
        Config.createConfig(context);
        PageFactory.createPageFactory(context);

        init(context);
    }

    public  static TxtReader newInstance(Context context){
        if (txtReader ==null){
            txtReader=new TxtReader(context);
        }
        return txtReader;
    }

    public  static void initlize(Context context){
        TxtReader.newInstance(context);
    }



   public   String yuYinPath;

    public  String getYuYinPath() {
        return yuYinPath;
    }

    public  TxtReader setYuYinPath(String yuYinPath) {
        this.yuYinPath = yuYinPath;
        return  this;
    }

    private void init(Context context){
        this.context=context;
        LitePal.initialize(context);
        Config.createConfig(context);
        PageFactory.createPageFactory(context);
//        initialEnv();
    }

//
//    private  void noPath(final String path,Activity mContext){
//        new AlertDialog.Builder(mContext)
//                .setTitle(mContext.getString(R.string.app_name))
//                .setMessage(path + "文件不存在,是否删除该书本？")
//                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        DataSupport.deleteAll(BookList.class, "bookpath = ?", path);
//                    }
//                }).setCancelable(true).show();
//    }

    List<BookList>bookLists;


    public  void openBookByFilePath(String filePath,Activity mContext ){
        setPath(filePath,mContext);
    }

    private void setPath(final String url, final Activity mContext ){
        bookLists = DataSupport.findAll(BookList.class); //从数据库中查找所有booklist数据
        TxtLogUtils.D("bookLists--->"+bookLists.toString());
        if (bookLists!=null&&bookLists.size()>0){
            for (int i=0;i<bookLists.size();i++){
                if (url.equals(bookLists.get(i).getBookpath())){
                    File file=new File(bookLists.get(i).getBookpath());
                    if (file.exists()){
                        TxtLogUtils.D("存在路径，打开已存在的书");
                        ReadActivity.openBook(bookLists.get(i),mContext);
                        return;
                    }else{
                        TxtLogUtils.D("存在路径,但文件不存在");
//                        noPath(bookLists.get(i).getBookpath(),mContext);
//                        return;
                    }
                }

            }

        }
        saveBookListByPath(url,mContext);

    };
    //保存选择的txt文件
    private void saveBookListByPath(String path,Activity mContext) {
        File file = new File(path);

        if (!file.exists()){
            Toast.makeText(mContext,"文件不存在",Toast.LENGTH_LONG).show();
            return;
        }
//        List<File> files = adapter.getCheckFiles();
//        if (files.size() > 0) {
        List<BookList> bookLists = new ArrayList<BookList>();
//            for (File file : files) {
        bookList = new BookList();
        String bookName = Fileutil.getFileNameNoEx(file.getAbsolutePath());
        bookList.setBookname(bookName);
        bookList.setBookpath(file.getAbsolutePath());
        bookList.setNetUrl(false);
        bookLists.add(bookList);
        ReadActivity.openBook(bookList,mContext);
        mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
        mSaveBookToSqlLiteTask.execute(bookLists);
    }




    public  void openBookByUrl(String url,Activity mContext ){
        setUrl(url,mContext);
    }

    private void setUrl(final String url, final Activity mContext ){
        bookLists = DataSupport.findAll(BookList.class); //从数据库中查找所有booklist数据
        TxtLogUtils.D("bookLists--->"+bookLists.toString());
        if (bookLists!=null&&bookLists.size()>0){
            for (int i=0;i<bookLists.size();i++){
                if (url.equals(bookLists.get(i).getTxtUrl())){
                        ReadActivity.openBook(bookLists.get(i),mContext);
                        return;
                }

            }

        }
                    saveBookList(url,mContext);

        };

    private  BookList bookList;

    private  SaveBookToSqlLiteTask mSaveBookToSqlLiteTask;
    //保存选择的txt文件
    private void saveBookList(String url,Activity mContext) {
//        File file = new File(bookFilePath);

//        List<File> files = adapter.getCheckFiles();
//        if (files.size() > 0) {
        List<BookList> bookLists = new ArrayList<BookList>();
//            for (File file : files) {
        bookList = new BookList();
        String bookName = Fileutil.getFileNameNoEx(DownLoadFile.getUrlName(url));
        bookList.setBookname(bookName);
        bookList.setNetUrl(true);
//        bookList.setBookpath(file.getAbsolutePath());
        bookList.setTxtUrl(url);
        bookLists.add(bookList);
        ReadActivity.openBook(bookList,mContext);
        mSaveBookToSqlLiteTask = new SaveBookToSqlLiteTask();
        mSaveBookToSqlLiteTask.execute(bookLists);
    }

    private class SaveBookToSqlLiteTask extends AsyncTask<List<BookList>, Void, Integer> {
        private static final int FAIL = 0;
        private static final int SUCCESS = 1;
        private static final int REPEAT = 2;
        private BookList repeatBookList;

        @Override
        protected Integer doInBackground(List<BookList>... params) {
            List<BookList> bookLists = params[0];
            for (BookList bookList : bookLists) {
                if (bookList.isNetUrl()) {
                    List<BookList> books = DataSupport.where("txtUrl = ?", bookList.getTxtUrl()).find(BookList.class);
                    if (books.size() > 0) {
                        repeatBookList = bookList;
                        return REPEAT;
                    }
                }else{
                    List<BookList> books = DataSupport.where("bookpath = ?", bookList.getBookpath()).find(BookList.class);
                    if (books.size() > 0) {
                        repeatBookList = bookList;
                        return REPEAT;
                    }
                }
            }

            try {
                DataSupport.saveAll(bookLists);
            } catch (Exception e) {
                e.printStackTrace();
                return FAIL;
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            String msg = "";
            switch (result) {
                case FAIL:
                    msg = "由于一些原因添加书本失败";
                    break;
                case SUCCESS:
//                    msg = "添加书本成功";
//                    setAddFileText(0);
//                    adapter.cancel();
                    break;
                case REPEAT:
                    msg = "书本" + repeatBookList.getBookname() + "重复了";
                    break;
            }

//            Toast.makeText(FileActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

}
