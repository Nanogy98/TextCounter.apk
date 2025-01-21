package com.myapp.textcounter.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import static androidx.appcompat.app.AppCompatDelegate.*;
import java.util.Objects;

import com.myapp.textcounter.Buttons;
import com.myapp.textcounter.R;
import com.myapp.textcounter.db.*;

public class MainActivity extends AppCompatActivity {//継承

    //フィールド onCreate内で呼び出すために、ここで各クラスを宣言
    static Buttons bt ;
    public static EditText e;
    TextView textLetter ,textLines;
    ClipboardManager clipboard;

    InputHistory history;
    SQLiteDatabase historyDB;

    ConfigData mode;
    static SQLiteDatabase modeDB;
    static Cursor cursorTheme , cursor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dbを作成
        bindDB();
        //保存したテーマの状態に応じて反映
        ViewTheme();

        //Activity起動時リソースのidに呼び出して、フィールドに代入
        bt = new Buttons();
        e = findViewById(R.id.edit_text);
        textLetter = findViewById (R.id.text_letters2);
        textLines = findViewById (R.id.text_lines2);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        View (bt.Count (e.getText ( )), bt.intFormat(e.getLineCount ()));

        //テキストカウント
        Button button_cnt = findViewById(R.id.button_cnt);
        button_cnt.setOnClickListener(new ButtonCount());
        //詳細ボタン
        Button button_detail = findViewById(R.id.button_detail);
        button_detail.setOnClickListener(new ButtonDetail());
        //テキスト削除
        Button button_del = findViewById(R.id.button_del);
        button_del.setOnClickListener(new ButtonDelete());
        //テキストコピー
        Button button_cpy = findViewById(R.id.button_cpy);
        button_cpy.setOnClickListener(new ButtonCopy());
        //テキストペースト
        Button button_pst = findViewById(R.id.button_pst);
        button_pst.setOnClickListener(new ButtonPaste());
        //TextWatcherスイッチ
        SwitchCompat Watcher = findViewById(R.id.watcherSwitch);
        Watcher.setOnCheckedChangeListener(new swWatcher());

        /*ディスプレイの密度を取得し、密度が2.75以下の場合は

        */
        System.out.println(getResources().getDisplayMetrics());
        if(getResources().getDisplayMetrics().density> 2.75){
            bt.setLayoutDp(this, e, 335f, 300f);
            bt.setLayoutDp(this, Watcher, 95f, 50f);
        }
    }

    class ButtonDetail implements View.OnClickListener {
        @Override public void onClick(View v) {new LengthDialogFragment().show(getSupportFragmentManager(), "my_dialog1");}
    }
    public static class LengthDialogFragment extends DialogFragment {
        @NonNull @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View myDia = inflater.inflate(R.layout.dialog_signin, null);
            EditText setMaxNum = myDia.findViewById(R.id.editSetLength);
            TextView letter = myDia.findViewById(R.id.text_letters2),
                    line   = myDia.findViewById(R.id.text_lines2),
                    breaks = myDia.findViewById(R.id.text_break2),
                    empty  = myDia.findViewById(R.id.text_empty2);
            letter.setText(bt.Count(e.getText()));
            line.setText(bt.line (e));
            breaks.setText(bt.BreakCnt(bt.Count(e.getText ()), bt.line (e)));
            empty.setText(bt.EmptyCnt(bt.format2(e.getText())));
            InputFilter[] maxText = new InputFilter[1];//要素１のインスタンスを生成
            return new AlertDialog.Builder(requireActivity())
                    .setView(myDia)
                    .setPositiveButton(R.string.button_bck, (dialog, which) -> {
                        if (setMaxNum.length() == 0) {
                            e.setFilters(new InputFilter[0]);//要素0番目のデータを空のインスタンスを生成
                        } else {
                            try {
                                maxText[0] = new InputFilter.LengthFilter(Integer.parseInt(bt.format2(setMaxNum.getText())));
                                e.setFilters(maxText);
                            } catch (Exception ex) {
                                //int型以外のデータがセットされようとした場合も空のインスタンスを生成(上限を設定しない)
                                e.setFilters(new InputFilter[0]);
                            }
                        }
                    }).create();
        }
    }
    class ButtonCount implements View.OnClickListener {
        public void onClick(View view) {
            if(bt.Judge(e.getText())) {toast(R.string.burble_message_noText);}//テキストが空の場合
            else{Add();View(bt.Count(e.getText()),bt.line(e));}
        }
    }
    /*TextEvent型のTWにTextEventクラスのインスタンスを生成させることで、
    TWでaddイベントを呼ぶと、removeする際に同じ場所(TW)を参照することでwatcherが切れる*/
    class swWatcher implements CompoundButton.OnCheckedChangeListener{
        TextEvent TW = new TextEvent ();
        public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked) {
            if (isChecked) {e.addTextChangedListener (TW);}
            else{e.removeTextChangedListener (TW);}
        }
    }
    class TextEvent implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int A, int B, int C) {}
        public void onTextChanged(CharSequence charSequence, int A, int B, int C) {}
        public void afterTextChanged(Editable editable) {
            if(!bt.Judge(e.getText())){
                Add();
                View (bt.Count (e.getText ( )), bt.intFormat(e.getLineCount ( )));
            }
        }
    }

    class ButtonDelete implements View.OnClickListener {
        public void onClick(View view){e.getText().clear();View(bt.Count(e.getText()),bt.line(e));}}

    class ButtonCopy implements View.OnClickListener {
        public void onClick(View view) {
            if(bt.Judge(e.getText ())){toast(R.string.burble_message_noText);}
            else{clipboard.setPrimaryClip(ClipData.newPlainText(null, e.getText ()));}//クリップボードにセット
            //Android 12L以下かつ、edittext内の文字の長さが0でない時だけバブルを出す
            if(Build.VERSION.SDK_INT <= 32 && !bt.Judge (e.getText ())) {toast(R.string.burble_message_copy);}
        }
    }

    class ButtonPaste implements View.OnClickListener {
        public void onClick(View view) {
            //グリップボートのデータの位置  0番目
            ClipData.Item item =Objects.requireNonNull (clipboard.getPrimaryClip ( )).getItemAt (0);
            System.out.println(item);
            String pasteData = bt.format2(item.getText());
            //item内が空でない(つまりtrue)の時だけテキストボックスに入れる
            if(!bt.Judge(item)){e.setText (pasteData); View(bt.Count(pasteData),bt.line(e));}
        }
    }

    public void Add(){
        bt.textInsert(historyDB,"textData",new String[] {"date","description","line","break","empty"},new String[]{bt.getNowDate (),
                bt.format2 (e.getText ( )),bt.line(e),bt.BreakCnt(bt.Count(e.getText ()), bt.line (e)),bt.EmptyCnt(bt.format2(e.getText()))});}
    public void toast(int resId){Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();}
    public void View(String a, String b){textLetter.setText (a);textLines.setText(b);}

    void ViewTheme(){
        cursorTheme = modeDB.query("themeModeData", new String[] {"flag"},null,null,null,null,null);
        if (cursorTheme.moveToFirst()){
            if(cursorTheme.getString(0).equals("MODE_NIGHT_NO")){
                setDefaultNightMode(MODE_NIGHT_NO);
            }else if(cursorTheme.getString(0).equals("MODE_NIGHT_YES")){
                setDefaultNightMode(MODE_NIGHT_YES);
            }
            else{setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);}
        }
        cursorTheme.close();
    }
    void bindDB(){
        //dbのファイルが無いない場合は作成する
        if(mode == null){ mode = new ConfigData(getApplicationContext());}
        if(history == null){ history = new InputHistory(getApplicationContext());}
        //dbのインスタンスが無い場合は作成する | 履歴を出力するだけなので、読み込みモードで呼び出す
        if(historyDB == null){ historyDB = history.getReadableDatabase();}
        //設定をを読み書きするだけなので、読み書きモードで呼び出す
        if(modeDB == null){ modeDB = mode.getWritableDatabase();}
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {// オプションメニューのアイテムが選択されたときに呼び出されるメソッド
        switch (item.getItemId()) {
            case R.id.item1:
                Intent I;
                I = new Intent(MainActivity.this, ListActivity.class);
                startActivity(I);
                return true;
            case R.id.item2:
                new ThemeDialogFragment().show(getSupportFragmentManager(), "my_dialog");
                return true;
            case R.id.item3:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static class ThemeDialogFragment extends DialogFragment {
        @NonNull @Override public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            cursor1 = modeDB.query("themeModeData", new String[] {"flag"},null,null,null,null,null);
            int itemChoice = 2;
            if(cursor1.moveToFirst()){
                if(cursor1.getString(0).equals("MODE_NIGHT_NO")){
                    itemChoice = 0;
                } else if (cursor1.getString(0).equals("MODE_NIGHT_YES")) {
                    itemChoice = 1;
                }
            }
            cursor1.close();
            return  new AlertDialog.Builder(requireActivity())
                    .setTitle(R.string.dialog_theme_title)
                    .setSingleChoiceItems(new String[]{"ホワイト","ダーク","自動"}, itemChoice, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    bt.changeTheme(modeDB,cursor1,"MODE_NIGHT_NO");
                                    setDefaultNightMode(MODE_NIGHT_NO);
                                    break;
                                case 1:
                                    bt.changeTheme(modeDB,cursor1,"MODE_NIGHT_YES");
                                    setDefaultNightMode(MODE_NIGHT_YES);
                                    break;
                                case 2:
                                    bt.changeTheme(modeDB,cursor1,"MODE_NIGHT_FOLLOW_SYSTEM");
                                    setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM);
                                    break;
                            }
                        }
                    }).create();
        }
    }
}