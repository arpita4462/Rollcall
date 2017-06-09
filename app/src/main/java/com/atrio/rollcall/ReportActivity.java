package com.atrio.rollcall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.model.StudentUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;


public class ReportActivity extends AppCompatActivity {
    TextView eng_tv,maths_tv,sci_tv,hindi_tv,gk_tv,total_tv,name_tv;
    String section,studclass,st_roll,st_from,st_to,mathpre,engpre,hindipre,gkpre,scipre;
    private DatabaseReference db_ref,db_ref2;
    private FirebaseDatabase db_instance;
    ProgressDialog dialog;
    public  double math_total,math_total_present,math_count = 0,math_cal_total,
            eng_total,eng_total_present,eng_count = 0,eng_cal_total,
            hindi_total,hindi_total_present,hindi_count = 0,hindi_cal_total,
            sci_total,sci_total_present,sci_count = 0,sci_cal_total,
            gk_total,gk_total_present,gk_count = 0,gk_cal_total,
            total,cal_total;
    public DecimalFormat df;


    Intent iget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        eng_tv = (TextView) findViewById(R.id.eng_tv);
        maths_tv = (TextView) findViewById(R.id.math_tv);
        sci_tv = (TextView) findViewById(R.id.sci_tv);
        hindi_tv = (TextView) findViewById(R.id.hindi_tv);
        gk_tv = (TextView) findViewById(R.id.gk_tv);
        total_tv = (TextView) findViewById(R.id.total_tv);
        name_tv = (TextView) findViewById(R.id.tv_name);

        iget = getIntent();
        studclass = iget.getStringExtra("studclass");
        section = iget.getStringExtra("section");
        st_roll = iget.getStringExtra("rollno");
        st_from = iget.getStringExtra("from");
        st_to = iget.getStringExtra("to");

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("AttendanceList");
        db_ref2 = db_instance.getReference("StudentList");
        df=  new DecimalFormat("####0.00");

        dialog = new ProgressDialog(ReportActivity.this);
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        Query queryRef = db_ref2.child(studclass).child(section).orderByChild("rollno").equalTo(st_roll);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.show();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                StudentUser post = snapshot.getValue(StudentUser.class);
                dialog.dismiss();
                name_tv.setText(post.getFirst_name()+" "+post.getLast_name());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();

            }
        });
        dialog.show();
        Query qmath= db_ref.child(studclass).child(section).child("Maths").orderByKey().startAt(st_from).endAt(st_to);

        qmath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    math_total_present = dataSnapshot.getChildrenCount();
                        for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                            mathpre=noteSnapshot.child(st_roll).getValue().toString();
                        if (mathpre.equals("present")){
                            math_count++;
                        }
                        }
                    math_total = math_count;
                    math_cal_total = (math_total/math_total_present)*100;
                    passvalue(math_cal_total);
                    dialog.dismiss();
                    if (math_cal_total==0){
                        maths_tv.setText("0 %");}
                    else {
                        maths_tv.setText(df.format(math_cal_total)+" "+"%");
                    }
                }
                else {
                    maths_tv.setText("0 %");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
        Query qeng= db_ref.child(studclass).child(section).child("English").orderByKey().startAt(st_from).endAt(st_to);
        qeng.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    eng_total_present = dataSnapshot.getChildrenCount();
                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                        engpre=noteSnapshot.child(st_roll).getValue().toString();
                        if (engpre.equals("present")){
                            eng_count++;
                        }
                    }
                    eng_total = eng_count;
                    eng_cal_total = (eng_total/eng_total_present)*100;
                    passvalue(eng_cal_total);

                    dialog.dismiss();
                    if (eng_cal_total==0){
                        eng_tv.setText("0 %");}
                    else {
                        eng_tv.setText(df.format(eng_cal_total)+" "+"%");
                    }
                }
                else {
                    eng_tv.setText("0 %");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
        Query qhindi= db_ref.child(studclass).child(section).child("Hindi").orderByKey().startAt(st_from).endAt(st_to);

        qhindi.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    hindi_total_present = dataSnapshot.getChildrenCount();
                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                        hindipre=noteSnapshot.child(st_roll).getValue().toString();
                        if (hindipre.equals("present")){
                            hindi_count++;
                        }

                    }
                    hindi_total = hindi_count;
                    hindi_cal_total = (hindi_total/hindi_total_present)*100;
                    passvalue(hindi_cal_total);
                    dialog.dismiss();
                    if (hindi_cal_total == 0) {
                        hindi_tv.setText("0 %");
                    } else {
                        hindi_tv.setText(df.format(hindi_cal_total)+" "+"%");
                    }
                }else {
                    hindi_tv.setText("0 %");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
        Query qscience= db_ref.child(studclass).child(section).child("Science").orderByKey().startAt(st_from).endAt(st_to);

        qscience.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    sci_total_present = dataSnapshot.getChildrenCount();
                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                        scipre=noteSnapshot.child(st_roll).getValue().toString();
                        if (scipre.equals("present")){
                            sci_count++;
                        }
                    }
                    sci_total = sci_count;
                    sci_cal_total = (sci_total/sci_total_present)*100;
                    passvalue(sci_cal_total);
                    dialog.dismiss();
                    if (sci_cal_total==0){
                        sci_tv.setText("0 %");}
                    else {
                        sci_tv.setText(df.format(sci_cal_total)+" "+"%");
                    }}
                else {
                    sci_tv.setText("0 %");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
        Query qgk= db_ref.child(studclass).child(section).child("General Knowledge").orderByKey().startAt(st_from).endAt(st_to);
        qgk.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0){
                    gk_total_present = dataSnapshot.getChildrenCount();
                    for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                        gkpre=noteSnapshot.child(st_roll).getValue().toString();
                        if (gkpre.equals("present")){
                            gk_count++;
                        }
                    }

                    gk_total = gk_count;
                    gk_cal_total = (gk_total/gk_total_present)*100;
                    passvalue(gk_cal_total);
                    dialog.dismiss();
                    if (gk_cal_total==0){
                        gk_tv.setText("0 %");}
                    else {
                        gk_tv.setText(df.format(gk_cal_total)+" "+"%");
                    }
                }else {
                    gk_tv.setText("0 %");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void passvalue(double totaldata) {
        total=math_cal_total+eng_cal_total+hindi_cal_total+sci_cal_total+gk_cal_total;
        cal_total=(total/5);
        Log.i("total11",""+cal_total);
        total_tv.setText(df.format(cal_total)+" "+"%");
    }
}
