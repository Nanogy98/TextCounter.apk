package com.myapp.textcounter;

import android.text.Editable;

public class Buttons  {
    //テキストのカウント
    private int textLen;
    public void setCount(Editable text){this.textLen = text.length();}
    public int getCount(){ return this.textLen; }

    public boolean setJudge(int len){
        if(len == 0){
            return true;
        }
        else {
            return false;
        }
    }
}
