package com.atrio.rollcall.admin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.atrio.rollcall.MainActivity;
import com.atrio.rollcall.R;
import com.atrio.rollcall.custom.CustomDailyReport;
import com.atrio.rollcall.custom.CustomDetail;
import com.atrio.rollcall.custom.CustomReport;
import com.atrio.rollcall.detail.TeacherDetail;
import com.google.firebase.auth.FirebaseAuth;

public class SelectAdmin extends AppCompatActivity {

    Button btn_reg,btn_detail, btn_report, btn_dailyreport,btn_timetable;
    private CustomDetail customDetail;
    private CustomReport customReport;
    private CustomDailyReport customDailyReport;
    private Dialog dialog;
    private ImageButton img_button;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        customReport = new CustomReport(SelectAdmin.this);
        customReport.requestWindowFeature(Window.FEATURE_NO_TITLE);

        customDailyReport = new CustomDailyReport(SelectAdmin.this);
        customDailyReport.requestWindowFeature(Window.FEATURE_NO_TITLE);

        customDetail = new CustomDetail(SelectAdmin.this);
        customDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_admin);
        btn_reg =(Button)findViewById(R.id.btn_reg);
        btn_detail=(Button)findViewById(R.id.btn_detail);
        btn_report =(Button)findViewById(R.id.btn_report);
        btn_dailyreport =(Button)findViewById(R.id.btn_dailyreport);
        img_button = (ImageButton)findViewById(R.id.img_logout);
        btn_timetable=(Button)findViewById(R.id.btn_timetable);


        img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Toast.makeText(getApplicationContext(),"Successfully Logout",Toast.LENGTH_SHORT).show();
                Intent imov = new Intent(SelectAdmin.this,AdminLogin.class);
                imov.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(imov);
                finish();




            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(SelectAdmin.this,AdminActivity.class);
                startActivity(i);

            }
        });
        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customReport.show();

            }
        });

        btn_dailyreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDailyReport.show();
            }
        });
        btn_detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.activity_detail);
                Button bt_stud = (Button) dialog.findViewById(R.id.btn_stud);
                Button bt_tech = (Button) dialog.findViewById(R.id.btn_tech);
                bt_tech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(SelectAdmin.this, TeacherDetail.class);
                        startActivity(i);
                        dialog.dismiss();


                         }

                });


                bt_stud.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        customDetail.show();
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });


        btn_timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SelectAdmin.this, TimeTableActivity.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SelectAdmin.this,MainActivity.class));

    }

    @Override
    protected void onPause() {
        super.onPause();
        customDetail.dismiss();
//       dialog.dismiss();

    }
}

