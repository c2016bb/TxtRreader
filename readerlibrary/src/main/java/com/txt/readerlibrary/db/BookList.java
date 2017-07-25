package com.txt.readerlibrary.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/12/27.
 */
public class BookList extends DataSupport implements Serializable{
    private int id;
    private String bookname; //书名
    private String bookpath;  //书的路径
    private long begin;
    private String charset;//编码格式
    private String txtUrl; //txt文件网络路径

    public String getTxtUrl() {
        return txtUrl;
    }

    public void setTxtUrl(String txtUrl) {
        this.txtUrl = txtUrl;
    }

    @Override
    public String toString() {
        return "BookList{" +
                "id=" + id +
                ", bookname='" + bookname + '\'' +
                ", bookpath='" + bookpath + '\'' +
                ", begin=" + begin +
                ", charset='" + charset + '\'' +
                ", txtUrl='" + txtUrl + '\'' +
                '}';
    }

    public String getBookname() {
        return this.bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookpath() {
        return this.bookpath;
    }

    public void setBookpath(String bookpath) {
        this.bookpath = bookpath;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
