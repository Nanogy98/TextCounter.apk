package com.myapp.textcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Build;
import android.view.View;
import java.util.Objects;
import static androidx.appcompat.app.AppCompatDelegate.*;

public class MainActivity extends AppCompatActivity {//継承
    //フィールド
    private EditText editText;
    private TextView textLetter,textLines;
    Buttons bt = new Buttons();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API28以降はシステムの設定に依存させる
        if(Build.VERSION.SDK_INT>=28){
            setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM); // アプリ全体に適用
        }else{//API28以下はダークテーマ無効化
            setDefaultNightMode(MODE_NIGHT_NO);
        }

        //使用するアイテム(オブジェクト)の定義
        editText = findViewById(R.id.edit_text);
        textLetter  = findViewById(R.id.text_letters2);
        textLines = findViewById(R.id.text_lines2);

        //テキストカウント
        Button button_cnt = findViewById(R.id.button_cnt);
        button_cnt.setOnClickListener(new ButtonCount());
        //テキスト削除
        Button button_del = findViewById(R.id.button_del);
        button_del.setOnClickListener( new ButtonDelete());
        //テキストコピー
        Button button_cpy = findViewById(R.id.button_cpy);
        button_cpy.setOnClickListener(new ButtonCopy());
        //テキストペースト
        Button button_pst = findViewById(R.id.button_pst);
        button_pst.setOnClickListener(new ButtonPaste());
    }

     class ButtonCount implements View.OnClickListener {
        public void onClick(View view) {
            bt.setCount(editText.getText());
            if(bt.setJudge()){
                Toast.makeText(getApplicationContext(), R.string.noText, Toast.LENGTH_SHORT).show();//トーストメッセージ
            }
            textLetter.setText(bt.format());
            textLines.setText(bt.formatter(editText.getLineCount()));
        }
    }

    class ButtonDelete implements View.OnClickListener {
        public void onClick(View view){
            editText.getText().clear();
            textLetter.setText(bt.formatter(editText.getText().length()));
            textLines.setText(bt.formatter(editText.getLineCount()));
        }
    }

    class ButtonCopy implements View.OnClickListener {
        //クリップボードマネージャー
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        public void onClick(View view) {
            bt.setCount(editText.getText());
            if(bt.setJudge()){
                Toast.makeText(getApplicationContext(), R.string.noText, Toast.LENGTH_SHORT).show();
            }else{
                // Editのテキストを取得
                ClipData clip = ClipData.newPlainText(null, editText.getText());
                //クリップボードにセット
                clipboard.setPrimaryClip(clip);
            }
            //OS 12L以下かつ、edittext内の文字の長さが0でない時だけバブルを出す
            if(Build.VERSION.SDK_INT<=32 && bt.getCount() !=0) {
                Toast.makeText(getApplicationContext(), R.string.copy, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ButtonPaste implements View.OnClickListener {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        public void onClick(View view) {
            try {
                //グリップボートのデータの位置     0番目
                ClipData.Item item = Objects.requireNonNull (clipboard.getPrimaryClip ( )).getItemAt (0);
                CharSequence pasteData = item.getText ( );
                editText.setText (pasteData);
                textLetter.setText (pasteData.length ( ));
                textLines.setText(bt.formatter(editText.getLineCount()));
            } catch (Exception e) {
                return;
            }
        }
    }

}