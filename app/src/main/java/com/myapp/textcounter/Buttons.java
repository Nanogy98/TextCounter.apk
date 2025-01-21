package com.myapp.textcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Buttons extends AppCompatActivity {
    //edittextの文字列の長さを取得するメソッド
    public String Count(Object text) {return intFormat(text.toString().length ( ));}
    //edittextのテキストの行数を取得するメソッド
    public String line(EditText text) {return text.length()==0 ? intFormat(text.getLineCount()-1) : intFormat(text.getLineCount());}
    //edittextの改行抜きでテキストの文字数を取得するメソッド
    public String BreakCnt(String letters,String lines) {return format2((Integer.parseInt(letters) - Integer.parseInt(lines)));}
    //空白抜きでテキストの文字数をカウント
    public String EmptyCnt(String letters) {
        //受けとった文章の中に改行が含まれていたら切り取るさらに全角及び半角が含まれていたら切り取る
       return intFormat(letters.replaceAll("\n","").replaceAll(" ","").replaceAll("　","").replaceAll(" ","").length());
    }
    //edittextの文字列の長さが空であるか判断するメソッド
    public boolean Judge(Object l) {return l.toString().isEmpty();}//空の場合だけtrueを返す
     //  Editable、EditText、型をString型に変換するメソッド || Intをstring型に変換するメソッド
    public String format2(Object j){return  String.valueOf(j);}
    public String intFormat(int lines) {return String.valueOf (lines);}


    public static String getNowDate(){return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));}

    /**DB関係**/
    public void textInsert(SQLiteDatabase db, String tableName, String[] values, String[] datas){
        ContentValues value = new ContentValues();
        for(int i =0 ; i<datas.length;i++){value.put(values[i], datas[i]);}
        db.insert(tableName, null, value);}
    public void textDelete(SQLiteDatabase db, String tableName){db.delete(tableName,null,null);}
    public void changeTheme(SQLiteDatabase db, Cursor cursor,String flag) {
        //一番先頭のレコードデータの有無を判別
        if (cursor.moveToFirst()) {configUpdate(db, flag);} //ある場合はupdate
        else {configInsert(db, flag);}//データが空の場合insert
    }
    public void configInsert(SQLiteDatabase db, String mode) {
        ContentValues values = new ContentValues();
        values.put("flag", mode);
        db.insert("themeModeData", null, values);
    }
    public void configUpdate(SQLiteDatabase db1,String mode) {
        ContentValues values = new ContentValues();
        values.put("flag", mode);
        db1.update("themeModeData",values, null,null);
    }

    /**view関係**/
    public float dpToPx(Context context, float dp) {return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());}
    public void setLayoutDp(Context context, View Item, float widthDp, float heightDp) {
        // LayoutParamsを取得して設定
        ViewGroup.LayoutParams layoutParams = Item.getLayoutParams();
        layoutParams.width = (int) dpToPx(context, widthDp);// dpからpxに変換
        layoutParams.height = (int) dpToPx(context, heightDp);
        // LayoutParamsを引数のViewに設定する
        Item.setLayoutParams(layoutParams);
    }
}