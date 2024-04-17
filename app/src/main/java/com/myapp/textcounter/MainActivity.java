package com.myapp.textcounter;




import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Build;
import java.util.Objects;
import static androidx.appcompat.app.AppCompatDelegate.*;
//AppcompatActivityクラスに継承
public class MainActivity extends AppCompatActivity {
    //フィールド
    private EditText editText;
    private TextView textView;
    Buttons bt = new Buttons();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //API28以降はシステムの設定に依存させる
        if(Build.VERSION.SDK_INT>=28){
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM); // アプリ全体に適用
        }else{//API28以下はダークテーマ無効化
            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO);
        }

        //テキスト入力
        editText = findViewById(R.id.edit_text);
        //テキスト表示
        textView  = findViewById(R.id.text_view);
        //使用するボタンの定義
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
        //view初期表示
        textView.setText("0文字");
    }

   class ButtonCount implements View.OnClickListener {
        //トーストメッセージ
       Context toastMsg = getApplicationContext();
        public void onClick(View view) {
            bt.setCount(editText.getText());
            if(bt.setJudge(bt.getCount ())){
                Toast.makeText(toastMsg, R.string.notext, Toast.LENGTH_SHORT).show();
            }
            textView.setText(bt.getCount() + "文字");
        }
    }

    class ButtonDelete implements View.OnClickListener {
        public void onClick(View view){
            editText.getText().clear();
            textView.setText(editText.getText().length() + "文字");
        }
    }

    class ButtonCopy implements View.OnClickListener {
        //クリップボードにコピー | クリップボードマネージャー
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Context toastMsg = getApplicationContext();
        public void onClick(View view) {
            bt.setCount(editText.getText());
            if(bt.setJudge(bt.getCount ())){
                Toast.makeText(toastMsg, R.string.notext, Toast.LENGTH_SHORT).show();
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
        }
    }

    class ButtonPaste implements View.OnClickListener {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        public void onClick(View view) {
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
        }
    }
}