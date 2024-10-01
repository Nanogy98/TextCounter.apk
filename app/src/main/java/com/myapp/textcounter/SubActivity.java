package com.myapp.textcounter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.UnsupportedEncodingException;

public class SubActivity extends AppCompatActivity {
private TextView LI,LT,PA,BR,EM,EUC,UTF8,UTF16,JIS,SHIFT;
    Buttons bt = new Buttons ();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        Button BK = findViewById (R.id.button_back);
        BK.setOnClickListener (new Back());

        Bundle b = getIntent().getExtras();
        String text = b.getString("text data"),
        lines = b.getString ("text lines");

        LT = findViewById (R.id.text_letters2);
        LI = findViewById (R.id.text_lines2);
        //PA = findViewById (R.id.text_paper2);
        BR = findViewById (R.id.text_break2);
        EM = findViewById (R.id.text_empty2);
        UTF8 = findViewById (R.id.text_utf_8_2);
        UTF16 = findViewById (R.id.text_utf_16_2);
        EUC = findViewById (R.id.text_euc_2);
        SHIFT = findViewById (R.id.text_shift_2);
        //JIS = findViewById (R.id.text_jis_2);

        try {
            LT.setText (bt.Count (text));
            LI.setText (lines);
            //PA.setText ();
            BR.setText (bt.BreakCnt (bt.Count (text),lines));
            EM.setText (bt.EmptyCnt(text));
            UTF8.setText(bt.format (text.getBytes("UTF-8").length));
            UTF16.setText(bt.format (text.getBytes("UTF-16").length));
            EUC.setText(bt.format (text.getBytes("EUC-JP").length));
            SHIFT.setText(bt.format (text.getBytes("SHIFT-JIS").length));
            //JIS.setText(bt.format (text.getBytes("JIS_X0201").length));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    class Back implements View.OnClickListener{
        public void onClick(View v){
            new Intent (SubActivity.this,MainActivity.class);
            /*startActivity(); 推移先のActivityを新しく開始する
             新しくactivityを開始することなく、SubActivityを閉じる*/
            finish ();
        }
    }
}
