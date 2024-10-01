package com.myapp.textcounter;

import android.os.Build;
import android.text.Editable;
import android.widget.EditText;

public class Buttons  {

    //edittextのテキストの行数を取得するメソッド
    public String line(EditText text) {return format(text.getLineCount());}
    //edittextの文字列の長さを取得するメソッド
    public String Count(Editable text) {return format(text.length ( ));}
    //SubActivity用
    public String Count(String text) {return format(text.length ( ));}
    //edittextの改行抜きでテキストの文字数を取得するメソッド
    public String BreakCnt(String letters,String lines) {return format((Integer.parseInt(letters) - Integer.parseInt(lines))+1);}
    //空白抜きでテキストの文字数をカウント
    public String EmptyCnt(String letters) {
        //全角が含まれる場合半角に置き換え、その置き換えた半角を””に置き換えて、その際のdataの長さを返す
        if(letters.contains("　")){
          String data = letters.replaceAll("　", " ");//全角=>半角
            return format(data.replaceAll(" ","").length());//半角=>空
        }
        //全角が含まれない場合
        return format(letters.replaceAll(" ","").length());
    }

    //Intをstring型に変換するメソッド
    public String format(int lines) {return String.valueOf (lines);}
    //edittextの文字列の長さが0であるか判断するメソッド
    public boolean Judge(Editable l) {return Integer.parseInt (Count (l)) == 0;}//0の場合だけtrueを返す
    //OS Version Check
    public boolean ChkOS(int v) {return Build.VERSION.SDK_INT <= v;}//osが引数v以下の場合だけtrueを返す

   /*public boolean Judge(Editable l) {
        if (Integer.parseInt (Count (l)) == 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean ChkOS(int v) {
        if (Build.VERSION.SDK_INT <= v) {
            return true;
        } else {
            return false;
        }
    }*/
}