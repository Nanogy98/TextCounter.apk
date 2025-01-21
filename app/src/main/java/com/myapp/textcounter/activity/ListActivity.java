package com.myapp.textcounter.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import java.util.LinkedList;

import com.myapp.textcounter.Buttons;
import com.myapp.textcounter.R;
import com.myapp.textcounter.db.InputHistory;

public class ListActivity extends AppCompatActivity {

    static Buttons bt = new Buttons();

    ListView listView;
    ArrayAdapter<String> adapter;
    //リストタッチされてからダイアログに出力する際の受け渡し用
    static String item1= null,item2= null,item3= null,item4 = null;
    static LinkedList<String>  //dbから取り出した各データを個々にリストのインスタンスを生成する
            text_list = new  LinkedList<String>( ),
            line_list = new  LinkedList<String>( ) ,
            break_list = new  LinkedList<String>( ) ,
            empty_list = new  LinkedList<String>( ) ;

    private InputHistory helper;
    private SQLiteDatabase db;
    static Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*前回のタイミングでリスト内にデータが残っているため、クリアしないでそのまま使いまわすと、
        データが上積みされて、押したpositionの位置とリストの要素の位置がズレて、タッチしたリストと
        違うデータが取り出されてしまうので、格納する前のタイミングで初期化する*/

        text_list.clear ();
        line_list.clear ();
        break_list.clear ();
        empty_list.clear ();
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_list);
        //dbのヘルパーのインスタンスがnullの場合はインスタンスを作成する
        if(helper == null){ helper = new InputHistory(getApplicationContext());}
        //dbが作成されていない場合は
        if(db == null){db = helper.getReadableDatabase();}

        Button back = findViewById (R.id.button_back);
        back.setOnClickListener (new Back ());
        Button delAll = findViewById(R.id.button_del);
        delAll.setOnClickListener(new Delete());

        listView = (ListView)findViewById(R.id.textList);
        listView.setOnItemClickListener(new ViewEvent());//リストにクリックイベントを登録

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        cursor = db.query("textData", new String[] {"date","description","line","break","empty"},
                null,null,null,null,null);
        /*listviewに格納する際に必要
        listviewに1列目から最後の列までを出力するためにまず、
        データを取り出すためのカーソルを一番最初の列に移動させてる*/
        cursor.moveToFirst();
        //iがカーソルのする数(レコードの数)になるまで出力を行う
        for (int i = 0 ;i<cursor.getCount();i++){
            /*listActivityを開いた時点でdbからlinkedListに
            １レコードずつ配列に１要素ずつ格納されているlistviewで
            リストがタッチされた際に格納されたlinkedListからデータ
            を参照して、ダイアログに渡している。*/
            text_list.add(cursor.getString(1));
            line_list.add(cursor.getString(2));
            break_list.add(cursor.getString(3));
            empty_list.add(cursor.getString(4));
            //データを取り出しアダプターに格納
            if(cursor.getString(1).length() >15) {
                adapter.add(cursor.getString(0) + "\n" + cursor.getString(1).substring(0, 15)+"...");
            }else{
                adapter.add(cursor.getString(0) + "\n" + cursor.getString(1));
            }
            //次の行に移動
            cursor.moveToNext();
        }
        //for文の処理が終わってから初めてlistviewにadapterの情報を反映させる
        listView.setAdapter(adapter);
        cursor.close();
    }

    public class ViewEvent implements ListView.OnItemClickListener{
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listView = (ListView) parent;
            //リストからデータを取り出す際にリストをタッチした際のpositionでリストの要素を指定して、データを取り出す。
            //取り出したデータをダイアログで出力するために、変数itemにデータをセットする
            item1 = text_list.get(position);
            item2 = line_list.get(position);
            item3 = break_list.get(position);
            item4 = empty_list.get(position);
            //タッチした際に定義したダイアログのクラスを呼び出す
            new TextListDetail_DialogFragment().show(getSupportFragmentManager(), "my_dialog2");
        }
    }
    public static class TextListDetail_DialogFragment extends DialogFragment {
        @NonNull @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View myDia = inflater.inflate(R.layout.dialog_list, null);
            TextView letter = myDia.findViewById(R.id.text_letters2),//myDia.findViewById(R.id.text_letters),
                    line   = myDia.findViewById(R.id.text_lines2),
                    breaks = myDia.findViewById(R.id.text_break2),
                    empty  = myDia.findViewById(R.id.text_empty2),
                    scroll = myDia.findViewById(R.id.textViewer);
            letter.setText(  bt.Count(item1));
            line.setText(item2);
            breaks.setText(item3);
            empty.setText(item4);
            scroll.setText(item1);

            /* listviewが開始したタイミング(onCreate内)の時しかデータを格納
            * してないので出力した時点でクリアしてしまうと今度、listviewで別の
            * リスト をタッチした際に空になったlinkedListからデータを参照する
            * こととなりNPEが発生してしまうので、ダイアログを出力した時点での
            * クリアは行わない */
            return new AlertDialog.Builder(requireActivity()).setView(myDia).setPositiveButton(R.string.button_bck, (dialog, which) -> {}).create();
        }
    }

    //データ全削除
    class Delete implements View.OnClickListener{public void onClick(View v){adapter.clear();bt.textDelete(db,"textData");}}
    class Back implements View.OnClickListener{public void onClick(View v){new Intent (ListActivity.this,MainActivity.class);finish ();}}
}

