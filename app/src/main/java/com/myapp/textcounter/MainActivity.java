package com.myapp.textcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.os.Bundle;
import android.text.Editable;
import java.util.Objects;
import static androidx.appcompat.app.AppCompatDelegate.*;

public class MainActivity extends AppCompatActivity {//継承
    //フィールド
    private TextView textLetter,textLines;
    private EditText editText;
    Buttons bt = new Buttons();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //使用アイテムの定義
        editText = findViewById(R.id.edit_text);
        textLetter  = findViewById(R.id.text_letters2);
        textLines = findViewById(R.id.text_lines2);

        //テキストカウント
        Button button_cnt = findViewById(R.id.button_cnt);
        button_cnt.setOnClickListener(new ButtonCount());
        //テキスト削除
        Button button_del = findViewById(R.id.button_del);
        button_del.setOnClickListener(new ButtonDelete());
        //テキストコピー
        Button button_cpy = findViewById(R.id.button_cpy);
        button_cpy.setOnClickListener(new ButtonCopy());
        //テキストペースト
        Button button_pst = findViewById(R.id.button_pst);
        button_pst.setOnClickListener(new ButtonPaste());

        //スイッチの イベントを作成
        //Switch toggleSwitch = findViewById(R.id.TG_switch);
        ToggleButton toggleSwitch = findViewById(R.id.TG_switch);
        toggleSwitch.setOnCheckedChangeListener(new onCheckedChangeListener());

        //起動時にシステムのダークモードがYESの場合はtrue(ダークモードオン)
        if(getDefaultNightMode() == MODE_NIGHT_YES||getDefaultNightMode()==MODE_NIGHT_AUTO_BATTERY){
            toggleSwitch.setChecked(true);
        }
        //そうでない場合はfalse(無効)
        else if(getDefaultNightMode()== MODE_NIGHT_NO){
            toggleSwitch.setChecked(false);
        }
    }

    // トグルスイッチを押した時に処理するクラス。
    class onCheckedChangeListener implements CompoundButton.OnCheckedChangeListener{
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                setDefaultNightMode(MODE_NIGHT_YES);
            }
            else {
                setDefaultNightMode(MODE_NIGHT_NO);
            }
        }
    }
     class ButtonCount implements View.OnClickListener {
        EditText e = editText;
        public void onClick(View view) {
            if(bt.Judge(e.getText())){toast();}
            View(bt.Count(e.getText()),bt.format(e.getLineCount()));
        }
    }

    class ButtonDelete implements View.OnClickListener {
        EditText e = editText;
        public void onClick(View view){
            e.getText().clear();
            View(bt.Count(e.getText()),bt.format(e.getLineCount()));
        }
    }

    class ButtonCopy implements View.OnClickListener {
        //クリップボードマネージャー
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Editable e = editText.getText();
        public void onClick(View view) {
            if(bt.Judge(e)){toast();}
            else{
                // Editのテキストを取得
                ClipData clip = ClipData.newPlainText(null, e);
                //クリップボードにセット
                clipboard.setPrimaryClip(clip);
            }
            //OS 12L以下かつ、edittext内の文字の長さが0でない時だけバブルを出す
            if(bt.ChkOS(32) && e.length() !=0) {
                Toast.makeText(getApplicationContext(), R.string.copy, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ButtonPaste implements View.OnClickListener {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        EditText e = editText;
        public void onClick(View view) {
            try {
                //グリップボートのデータの位置     0番目
                ClipData.Item item = Objects.requireNonNull (clipboard.getPrimaryClip ( )).getItemAt (0);
                String pasteData = item.getText().toString();
                e.setText (pasteData);
                View(bt.format(pasteData.length()),bt.format(e.getLineCount()));
            }
            catch (Exception e) {System.out.println(e); return;}
        }
    }
    void toast(){Toast.makeText(getApplicationContext(), R.string.noText, Toast.LENGTH_SHORT).show();}
    void View(String a,String b){
        textLetter.setText (a);
        textLines.setText(b);
    }
}