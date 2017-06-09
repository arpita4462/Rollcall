package com.atrio.rollcall.teacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.rollcall.R;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    String item_sub;
    Button bt_sub;
    String section,class_name;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        Spinner sp_class = (Spinner) findViewById(R.id.sp_class);
        Spinner sp_sub = (Spinner) findViewById(R.id.sp_sub);
        Spinner sp_sec = (Spinner) findViewById(R.id.sp_sec);
        bt_sub = (Button)findViewById(R.id.bt_submit);

        dialog = new ProgressDialog(SelectActivity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);
        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(SelectActivity.this,R.array.class_name, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(SelectActivity.this,R.array.section, android.R.layout.simple_spinner_item);

        adpter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_class.setAdapter(adpter_class);
        sp_sec.setAdapter(adapter_sec);
        sp_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                section = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                class_name = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        List<String> sub = new ArrayList<String>();
        sub.add("Maths");
        sub.add("English");
        sub.add("Hindi");
        sub.add("Science");
        sub.add("Computer");
        sub.add("Social Science");
        sub.add("General Knowledge");

        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sub);
        subAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_sub.setAdapter(subAdapter);
        sp_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_sub = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
     bt_sub.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             if (class_name.equals("Select class")){
                 Toast toast = Toast.makeText(SelectActivity.this,"Please select your class", Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 0, 0);
                 toast.show();
             }else if (section.equals("Select section")){
                 Toast toast = Toast.makeText(SelectActivity.this,"Please select your section", Toast.LENGTH_LONG);
                 toast.setGravity(Gravity.CENTER, 0, 0);
                 toast.show();

             }else{
                 Intent teach=getIntent();
                 String teach_name=teach.getStringExtra("teacher_name");
                 dialog.show();
                 Intent isend = new Intent(SelectActivity.this,AttendanceActivity.class);
                 isend.putExtra("class",class_name);
                 isend.putExtra("subject",item_sub);
                 isend.putExtra("section",section);
                 isend.putExtra("teach_name",teach_name);
                 startActivity(isend);
                 dialog.dismiss();
             }


         }
     });

    }
}
