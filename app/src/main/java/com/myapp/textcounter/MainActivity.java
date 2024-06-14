package com.myapp.textcounter;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.View;
import android.os.Bundle;
import java.util.Objects;
import static androidx.appcompat.app.AppCompatDelegate.*;

public class MainActivity extends AppCompatActivity {//継承

    //フィールドonCreate内で呼び出すために、privateで宣言
    private Buttons bt ;
    private EditText e;
    private TextView textLetter ,textLines;
    private ClipboardManager clipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Activity起動時リソースのidに呼び出して、フィールドに代入
        bt = new Buttons();
        e = findViewById(R.id.edit_text);
        textLetter = findViewById (R.id.text_letters2);
        textLines = findViewById (R.id.text_lines2);
        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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

        //チェックボックス(TextWachar)
        CheckBox CB = findViewById(R.id.checkBox);
        CB.setOnCheckedChangeListener (new Check());

        //スイッチ(テーマ変更) Activity開始時にクラスを呼ぶ
        //Switch toggleSwitch = findViewById(R.id.TG_switch);
        ToggleButton toggleSwitch = findViewById(R.id.TG_switch);
        toggleSwitch.setOnCheckedChangeListener(new Theme());
    }

    static class Theme implements CompoundButton.OnCheckedChangeListener{
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                setDefaultNightMode(MODE_NIGHT_YES);
            }
            else {
                setDefaultNightMode(MODE_NIGHT_NO);
            }
        }
    }

    /*
    TextEvent型のTWにTextEventクラスのobj生成させることで、
    TWでaddイベントを呼ぶと、removeする際に同じ場所(TW)を参照することでwatcherが切れる
    */
    class Check implements CompoundButton.OnCheckedChangeListener{
        TextEvent TW = new TextEvent ();
        public void onCheckedChanged(CompoundButton buttonView1, boolean isChecked) {
            View (bt.Count (e.getText ( )), bt.format (e.getLineCount ( )));
            if (isChecked) {
                e.addTextChangedListener (TW);
            }else{
                e.removeTextChangedListener (TW);
            }
        }
    }
    class TextEvent implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int A, int B, int C) {}
        public void onTextChanged(CharSequence charSequence, int A, int B, int C) {}
        public void afterTextChanged(Editable editable) {
            View (bt.Count (e.getText ( )), bt.format (e.getLineCount ( )));
        }
    }

    class ButtonCount implements View.OnClickListener {
        public void onClick(View view) {
            if (bt.Judge (e.getText ( ))) {toast ( R.string.noText);}
            View (bt.Count (e.getText ( )), bt.format (e.getLineCount ( )));
        }
    }

    class ButtonDelete implements View.OnClickListener {
        public void onClick(View view){
            e.getText().clear();
            View(bt.Count(e.getText()),bt.format(e.getLineCount()));
        }
    }

    class ButtonCopy implements View.OnClickListener {
        public void onClick(View view) {
            if(bt.Judge(e.getText ())){toast(R.string.noText);}
            else{
                ClipData clip = ClipData.newPlainText(null, e.getText ());
                clipboard.setPrimaryClip(clip);//クリップボードにセット
            }
            //OS 12L以下かつ、edittext内の文字の長さが0でない時だけバブルを出す
            if(bt.ChkOS(32) && e.length() !=0) {toast(R.string.copy);}
        }
    }

    class ButtonPaste implements View.OnClickListener {
        public void onClick(View view) {
            try {//グリップボートのデータの位置  0番目
                ClipData.Item item = Objects.requireNonNull (clipboard.getPrimaryClip ( )).getItemAt (0);
                String pasteData = item.getText().toString();
                e.setText (pasteData);
                View(bt.format(pasteData.length()),bt.format(e.getLineCount()));
            }
            catch (Exception e) {System.out.println(e); return;}
        }
    }
    void toast(int id){Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();}
    void View(String a,String b){textLetter.setText (a);textLines.setText(b);}
}