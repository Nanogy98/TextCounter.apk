package com.myapp.textcounter;

import android.os.Build;
import android.text.Editable;
public class Buttons  {
    //テキストのカウント
    //edittextの文字列の長さを取得するメソッド
    public String Count(Editable text){
        return Integer.toString (text.length());
    }
    //edittextの文字列の長さが0であるか判断するメソッド
    public boolean Judge(Editable s){
        int L = s.toString ().length();
        if(L == 0){
            return true;
        }
        else {
            return false;
        }
    }

    //Int型からstring型に変換するメソッド
    public String format(int lines){
        return Integer.toString (lines);
    }
    //OS Version Check
    public boolean ChkOS(int ver){
        if(Build.VERSION.SDK_INT<=ver ){
            return true;
        }else{
            return false;
        }
    }
}
