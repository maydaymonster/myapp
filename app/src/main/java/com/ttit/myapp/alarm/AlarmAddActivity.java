package com.ttit.myapp.alarm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ttit.myapp.R;

import java.util.Calendar;

public class AlarmAddActivity extends Activity {
    private Button conButton;
    private EditText etTitle, etText;
    private String title, text;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private TextView tv_date;
    private TextView tv_time;

    private Calendar c;

    private AlarmDateBase myDataBase;
    private AlarmNote note;
    private int id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_add);
        init();
        myDataBase=new AlarmDateBase(this);
        //将曾经编辑的内容显示出来
        id=getIntent().getIntExtra("id",0);
        if(id!=0){
            note = myDataBase.getTitleandContent(id);
            etTitle.setText(note.getTitle());
            etText.setText(note.getContent());
            Log.d("wode", note.getDate());
            String str_date=note.getDate().substring(0, note.getDate().indexOf(" "));
            tv_date.setText(str_date);
            String str_time=note.getDate().substring(note.getDate().indexOf(" ")+1);
            tv_time.setText(str_time);
        }

        //日期按钮监听器
        Button dButton = (Button) findViewById(R.id.bt_date);
        dButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlarmAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        updatedate();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button tButton = (Button) findViewById(R.id.bt_time);
        tButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AlarmAddActivity.this,new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour=hourOfDay;
                        mMinute=minute;
                        updatetime();
                    }
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),false).show();
            }
        });


        conButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave();
            }
        });

    }

    //初始化控件
    private void init() {
        conButton = (Button) findViewById(R.id.add_bt_confirm);
        etTitle = (EditText) findViewById(R.id.add_et_title);
        etText = (EditText) findViewById(R.id.add_et_note);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_time = (TextView) findViewById(R.id.tv_time);

        c=Calendar.getInstance();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlarmAddActivity.this, PlanActivity.class);
        startActivity(intent);
        AlarmAddActivity.this.finish();
    }

    private String format(int x)
    {
        String s=""+x;
        if(s.length()==1) s="0"+s;
        return s;
    }
    //tv更新日期
    private void updatedate()
    {
        tv_date.setText(new StringBuilder().append(mYear).append("/")
                .append(format(mMonth + 1)).append("/")
                .append(format(mDay)).append(" "));
    }
    private void updatetime()
    {
        tv_time.setText(new StringBuilder().append(mHour).append(":")
                .append(format(mMinute)).append(" "));
    }

    private void isSave()
    {
        String times = MyTime.getTime(mYear,mMonth+1,mDay,mHour,mMinute);
        String titles=etTitle.getText().toString();
        String texts=etText.getText().toString();
        //修改数据
        if (id!=0) {
            note = new AlarmNote(id,titles, texts, times);
            myDataBase.toUpdate(note);
            Intent intent = new Intent(AlarmAddActivity.this, PlanActivity.class);
            startActivity(intent);
            AlarmAddActivity.this.finish();
        }
        //新建一条
        else {
            if(titles!=null||texts!=null){
                note = new AlarmNote(titles, texts, times);
                myDataBase.toInsert(note);
            }

            //返回mainActivity
            Intent intent = new Intent(AlarmAddActivity.this, PlanActivity.class);
            startActivity(intent);

            AlarmAddActivity.this.finish();
        }
    }


}