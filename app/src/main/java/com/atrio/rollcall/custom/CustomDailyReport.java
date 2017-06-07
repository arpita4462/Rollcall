package com.atrio.rollcall.custom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.rollcall.DailyReport;
import com.atrio.rollcall.R;

public class CustomDailyReport extends Dialog implements View.OnClickListener {

    Context mycontext;
    public String class_name,section;
    Button bt_sub;
    DatePickerDialog datePickerDialog;
    ProgressDialog dialog;


    public CustomDailyReport(Context context) {
        super(context);
        mycontext=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_dailyreport);
        Spinner sp_class = (Spinner) findViewById(R.id.sp_class);
        Spinner sp_sec = (Spinner) findViewById(R.id.sp_sec);

        bt_sub = (Button)findViewById(R.id.bt_submit);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(getContext(),
                R.array.class_name, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(getContext(),
                R.array.section, android.R.layout.simple_spinner_item);


        adpter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        sp_class.setAdapter(adpter_class);
        sp_sec.setAdapter(adapter_sec);
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                class_name = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                section = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    dialog.show();

                    Intent idetail = new Intent(getContext(), DailyReport.class);
                    idetail.putExtra("studclass",class_name);
                    idetail.putExtra("section",section);
                    getContext().startActivity(idetail);

                    dialog.dismiss();
                }
            }
        });

    }

    private boolean validate() {


        if (class_name.equals("Select class")){
            Toast toast = Toast.makeText(getContext(),"Please select your class", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;
        }else if (section.equals("Select section")){
            Toast toast = Toast.makeText(getContext(),"Please select your section", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;

        }

        return true;
    }
    @Override
    public void onClick(View v) {

    }
}
