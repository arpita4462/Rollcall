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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.ReportActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CustomReport extends Dialog implements View.OnClickListener {

    Context mycontext;
    public String class_name,section,st_roll,st_from,st_to;
    Button bt_sub;
    EditText et_rollno,et_from,et_to;
    DatePickerDialog datePickerDialog;
    ProgressDialog dialog;


    public CustomReport(Context context) {
        super(context);
        mycontext=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_report);
        Spinner sp_class = (Spinner) findViewById(R.id.sp_class);
        Spinner sp_sec = (Spinner) findViewById(R.id.sp_sec);
        et_rollno =(EditText)findViewById(R.id.et_roll);
        et_from =(EditText)findViewById(R.id.tv_from);
        et_to =(EditText)findViewById(R.id.tv_to);

        bt_sub = (Button)findViewById(R.id.bt_submit);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);
        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(getContext(),
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
        et_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                                String monthname =  new SimpleDateFormat("MMM").format(dateCalendar.getTime());
                               String simpledateformat = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
//                                et_from.setText(simpledateformat.format(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                et_from.setText(simpledateformat);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        et_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                                String monthname =  new SimpleDateFormat("MMM").format(dateCalendar.getTime());
                                String simpledateformat = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
//                                et_from.setText(simpledateformat.format(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                                et_to.setText(simpledateformat);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    dialog.show();
                    st_roll=et_rollno.getText().toString().toUpperCase();
                    st_from=et_from.getText().toString();
                    st_to=et_to.getText().toString();
                    Intent idetail = new Intent(getContext(), ReportActivity.class);
                    idetail.putExtra("studclass",class_name);
                    idetail.putExtra("section",section);
                    idetail.putExtra("rollno", st_roll);
                    idetail.putExtra("from", st_from);
                    idetail.putExtra("to", st_to);
                    getContext().startActivity(idetail);
                    et_rollno.setText("");

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

        }else if (et_rollno.getText().toString().trim().length()<1) {
            et_rollno.setError("Please Fill This Field");
            et_rollno.requestFocus();
            return false;

        }else if (et_from.getText().toString().trim().length()<1){
            et_from.setError("Please Fill This Field");
            et_from.requestFocus();
            return false;
        }else  if (et_to.getText().toString().trim().length()<1){
            et_from.setError("Please Fill This Field");
            et_from.requestFocus();
            return false;

        }

        return true;
    }
    @Override
    public void onClick(View v) {

    }
}
