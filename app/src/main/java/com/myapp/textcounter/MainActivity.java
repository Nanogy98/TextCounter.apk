package com.myapp.textcounter;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Build;

import java.util.Objects;

//AppcompatActivityクラスに継承
public class MainActivity extends AppCompatActivity {
    //フィールド
    private EditText editText;
    private TextView textView;
    private int MODE_NIGHT_YES;
    Buttons bt = new Buttons();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES); // アプリ全体に適用
        //テキスト入力
        editText = findViewById(R.id.edit_text);
        //テキスト表示
        textView = findViewById(R.id.text_view);
        //テキストカウントボタン
        Button button_cnt = findViewById(R.id.button_cnt);
        //テキスト削除ボタン
        Button button_del = findViewById(R.id.button_del);
        //テキストコピー
        Button button_cpy = findViewById(R.id.button_cpy);
        //テキストペースト
        Button button_pst = findViewById(R.id.button_pst);
        //view初期表示
        textView.setText("0文字");

        //クリップボードにコピー | クリップボード クラス
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        //トーストメッセージ
        Context toastMsg = getApplicationContext();

        //テキストペースト
        button_pst.setOnClickListener(v -> {
            CharSequence pasteData;
            try {
                //グリップボートのデータの位置     0番目
                ClipData.Item item = Objects.requireNonNull (clipboard.getPrimaryClip ( )).getItemAt (0);
                pasteData = item.getText ( );
                editText.setText (pasteData);
                textView.setText (pasteData.length ( ) + "文字");
            } catch (Exception e) {
                return;
            }
        });

        //テキストのカウント
        button_cnt.setOnClickListener(v -> {
            bt.setCount(editText.getText());
            if(bt.getCount()==0){
                Toast.makeText(toastMsg, "テキストが入力されていません", Toast.LENGTH_SHORT).show();
            }
            textView.setText(bt.getCount() + "文字");
        });

        //テキスト削除
        button_del.setOnClickListener(v -> {
            editText.getText().clear();
            textView.setText(editText.getText().length() + "文字");
        });

        //テキストコピー
        button_cpy.setOnClickListener(v -> {
            bt.setCount(editText.getText());
            if(bt.getCount()==0){
                Toast.makeText(toastMsg, "テキストが入力されていません", Toast.LENGTH_SHORT).show();
            }else{
                // Editのテキストを取得
                ClipData clip = ClipData.newPlainText(null, editText.getText());
                //クリップボードにセット
                clipboard.setPrimaryClip(clip);
            }
            //OS 12L以下かつ、edittext内の文字の長さが0でない時だけバブルを出す
            if(Build.VERSION.SDK_INT<=32 && bt.getCount() !=0) {
                Toast.makeText(toastMsg, "コピーしました", Toast.LENGTH_SHORT).show();
            }
        });
    }
}