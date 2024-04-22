package com.myapp.textcounter;

import android.text.Editable;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class Buttons extends AppCompatActivity {
    //テキストのカウント
    private int textLen;

    //edittextの文字列の長さを取得するメソッド
    public void setCount(Editable text){this.textLen = text.length();}
    //edittextの文字列の長さを返すメソッド
    public int getCount(){ return this.textLen; }

    //edittextの文字列の長さが0であるか判断するメソッド
    public boolean setJudge(){
        if(textLen == 0){return true;}
        else {return false;}
    }
    //Int型からstring型に変換するメソッド
    //textLenの値から変換
    public String format(){return Integer.toString (textLen);}
    //引数から変換
    public String formatter(int lines){
        return Integer.toString (lines);
    }



}
