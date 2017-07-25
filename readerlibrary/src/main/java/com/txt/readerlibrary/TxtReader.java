package com.txt.readerlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.txt.readerlibrary.db.BookList;
import com.txt.readerlibrary.util.Fileutil;
import com.txt.readerlibrary.util.PageFactory;
import com.txt.readerlibrary.utils.DownLoadFile;
import com.txt.readerlibrary.utils.LogUtils;

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


    private String mSampleDirPath;

    public static final String SAMPLE_DIR_NAME = "baiduTTS";

    public static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
    public static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
    public static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
    public static final String LICENSE_FILE_NAME = "temp_license";
    public static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
    public static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
    public static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";

    private static final int PRINT = 0;
    private static final int UI_CHANGE_INPUT_TEXT_SELECTION = 1;
    private static final int UI_CHANGE_SYNTHES_TEXT_SELECTION = 2;





    private void init(Context context){
        this.context=context;
        LitePal.initialize(context);
        Config.createConfig(context);
        PageFactory.createPageFactory(context);
        initialEnv();
    }

    public String getTTPath(){
        return mSampleDirPath;
    }

    private void initialEnv() {
        if (mSampleDirPath == null) {
            String sdcardPath = Environment.getExternalStorageDirectory().toString();
            mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
            Log.i("cc---->","mSampleDirPath--->"+mSampleDirPath);
        }
        makeDir(mSampleDirPath);
        copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
//        copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_FEMALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_SPEECH_MALE_MODEL_NAME);
        copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
                + ENGLISH_TEXT_MODEL_NAME);
    }

    private void makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
            Log.i("cc-->","创建文件---1"+file.toString());
        }
        Log.i("cc-->","创建文件---》2"+file.toString());
    }

    /**
     * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
     *
     * @param isCover 是否覆盖已存在的目标文件
     * @param source
     * @param dest
     */
    private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
        File file = new File(dest);

        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            Log.i("cc","xcdd");
            try {
                is = context.getResources().getAssets().open(source);
                Log.i("cc","is--->"+is.toString());
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
                Log.i("cc","获取Assets值");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("cc","获取Assets异常1");
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("cc","获取Assets异常2");
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private  void noPath(final String path,Activity mContext){
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage(path + "文件不存在,是否删除该书本？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(BookList.class, "bookpath = ?", path);
                    }
                }).setCancelable(true).show();
    }

    List<BookList>bookLists;



    public  void openBook(String url,Activity mContext ){
        setUrl(url,mContext);
    }

    private void setUrl(final String url, final Activity mContext ){
        bookLists = DataSupport.findAll(BookList.class); //从数据库中查找所有booklist数据
        LogUtils.D("bookLists--->"+bookLists.toString());
        if (bookLists!=null&&bookLists.size()>0){
            for (int i=0;i<bookLists.size();i++){
                if (url.equals(bookLists.get(i).getTxtUrl())){
                    File file=new File(bookLists.get(i).getBookpath());
                    if (file.exists()){
                        LogUtils.D("存在路径，打开已存在的书");
                        ReadActivity.openBook(bookLists.get(i),mContext);
                        return;
                    }else{
                        LogUtils.D("存在路径,但文件不存在");
                        noPath(bookLists.get(i).getBookpath(),mContext);
                        return;
                    }
                }

            }

        }
        DownLoadFile.downloadFile(mContext, url, new DownLoadFile.FilePathCallBack() {
            @Override
            public void getFilePath(String path) {
                if (path!=null){
                    LogUtils.D("获取到文件路径");
                    saveBookList(url,path,mContext);
                }else{
                    LogUtils.D("未获取到文件路径");
                }
            }
        });
    }
    private  BookList bookList;

    private  SaveBookToSqlLiteTask mSaveBookToSqlLiteTask;
    //保存选择的txt文件
    private void saveBookList(String url,String bookFilePath,Activity mContext) {
        File file = new File(bookFilePath);

//        List<File> files = adapter.getCheckFiles();
//        if (files.size() > 0) {
        List<BookList> bookLists = new ArrayList<BookList>();
//            for (File file : files) {
        bookList = new BookList();
        String bookName = Fileutil.getFileNameNoEx(file.getName());
        bookList.setBookname(bookName);
        bookList.setBookpath(file.getAbsolutePath());
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
                List<BookList> books = DataSupport.where("bookpath = ?", bookList.getBookpath()).find(BookList.class);
                if (books.size() > 0) {
                    repeatBookList = bookList;
                    return REPEAT;
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
