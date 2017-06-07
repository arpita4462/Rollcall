package com.atrio.rollcall.teacher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.adapter.RecyclerAdapter;
import com.atrio.rollcall.model.StudentUser;
import com.atrio.rollcall.sendmail.SendMail;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class AttendanceActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button btn_sub;
    TextView tv_name, tv_date, tv_time;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    Dialog dialog;
    public String date, time, teacher_name,mail_id, roll_abs;
    RecyclerView mRecyclerView;
    public static ArrayList<String> my2mail,mail_list,list_abs;
    public static ArrayList<StudentUser> studentlist;
    ProgressDialog pDialog;
    public static String class1, section, subject;
    FirebaseRecyclerAdapter mFirebaseAdapter;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference mdatacallAttendance, mDatabaseReference, allreference;
    long totalstudent;
    RecyclerAdapter adapter;
    public static int mSelectedItem = -1;
    DataSnapshot items,item_data;
    ArrayList<StudentUser> store_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv_name = (TextView) toolbar.findViewById(R.id.tv_name);
        tv_date = (TextView) toolbar.findViewById(R.id.tv_date);
        tv_time = (TextView) toolbar.findViewById(R.id.tv_time);

        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpledateformat.format(calander.getTime());
        simpledateformat = new SimpleDateFormat("hh:mm aaa");
        time = simpledateformat.format(calander.getTime());
        tv_date.setText(date);
        tv_time.setText(time);
        Intent i = getIntent();
        class1 = i.getStringExtra("class");
        section = i.getStringExtra("section");
        subject = i.getStringExtra("subject");
        teacher_name = i.getStringExtra("teach_name");
        studentlist = new ArrayList<>();
        mail_list = new ArrayList<>();
        my2mail = new ArrayList<String>();
        list_abs = new ArrayList<>();

        store_list = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        pDialog = new ProgressDialog(AttendanceActivity.this);
        pDialog.setMessage("Please wait..");
        pDialog.setCanceledOnTouchOutside(false);


//        pDialog.show();

        btn_sub = (Button) findViewById(R.id.bt_send);
        tv_name.setText(teacher_name);



        ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        } else {

            mDatabaseReference = database.getReference("StudentList");
            pDialog.show();
            Query query = mDatabaseReference.child(class1).child(section).orderByKey();

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getChildrenCount() == 0) {
                        pDialog.dismiss();
                        // totalstudent=dataSnapshot.getChildrenCount();
                        Toast toast = Toast.makeText(AttendanceActivity.this, "No Student Present", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                    } else {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            StudentUser post = data.getValue(StudentUser.class);
                            post.setFirst_name(post.first_name);
                            post.setLast_name(post.last_name);
                            post.setRollno(post.rollno);
                            post.setStud_class(class1);
                            post.setStud_sub(subject);
                            post.setStud_sec(section);
                            post.setDate(date);

                            studentlist.add(post);

                        }

                        adapter = new RecyclerAdapter(AttendanceActivity.this, studentlist);
                        mRecyclerView.setAdapter(adapter);
                        pDialog.dismiss();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


        btn_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                allreference = FirebaseDatabase.getInstance().getReference("StudentList");
                mdatacallAttendance = FirebaseDatabase.getInstance().getReference("AttendanceList");


                Query countdata = allreference.child(class1).child(section).orderByKey();

                countdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Log.i("ChCount222", "" + dataSnapshot.getChildrenCount());
                        for (DataSnapshot data :dataSnapshot.getChildren()){
                            StudentUser user_info = data.getValue(StudentUser.class);
                            user_info.setEmailid(user_info.getEmailid());
                            user_info.setRollno(user_info.getRollno());
                            store_list.add(user_info);

                        }

                        final long alldata = dataSnapshot.getChildrenCount();


                        Query presentData = mdatacallAttendance.child(class1).child(section).child(subject).child(date).orderByKey();
                        presentData.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Log.i("daata876", "" + dataSnapshot.getChildrenCount());
                                long listData = dataSnapshot.getChildrenCount();



                                if (alldata != listData) {

                                    store_list.clear();

                                    Toast.makeText(AttendanceActivity.this, "Mark All The Student Attendance", Toast.LENGTH_SHORT).show();

                                } else {




                                    Iterator<DataSnapshot> item = dataSnapshot.getChildren().iterator();

                                    while (item.hasNext()){
                                        items = item.next();
                                        String absdata = items.getValue().toString();
                                        if (absdata.equals("absent")) {
                                            roll_abs = items.getKey();
                                            my2mail.add(roll_abs);
                                            Log.i("list88",""+store_list.size());
                                            // Log.i("rollno43",""+roll_abs);
                                            //Log.i("rollno43",""+my2mail);
                                        }


                                    }

                                    for (int j= 0;j<store_list.size();j++){
                                           /* Log.i("rll88",""+store_list.get(j).getEmailid());
                                            Log.i("jl88",""+store_list.get(j).getRollno());
*/
                                        for (int y=0;y<my2mail.size();y++){
                                            // Log.i("yl88",""+my2mail.get(y));
                                            if (store_list.get(j).getRollno().equals(my2mail.get(y))){
                                                //Log.i("mail55",""+store_list.get(j).getEmailid());
                                                list_abs.add(store_list.get(j).getEmailid());

                                            }


                                        }



                                    }
                                    // Log.i("rollno33",""+list_abs.size());
                                    //Log.i("rollno34",""+list_abs);
                                    sendmail(v,list_abs);
                                    Toast.makeText(AttendanceActivity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();


                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }



    private void sendmail(View v, final ArrayList<String> my2mai1l) {
        dialog = new Dialog(v.getContext());
        dialog.setContentView(R.layout.custom_mail_dialog);
        dialog.setTitle("Alert...");
        dialog.show();
        Button bt_yes = (Button) dialog.findViewById(R.id.btn_yes);
        Button bt_no = (Button) dialog.findViewById(R.id.btn_no);
        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("childrollno", "" + my2mai1l);
                String email = "info@atriodata.com";
                String mail_subject = "Attendance Remark";
                String message = "Your little one is absent on " + date + " " + time + " in " + subject + " class.";

                SendMail sm = new SendMail(v.getContext(), email, mail_subject, message, my2mai1l);

                sm.execute();

                dialog.dismiss();
            }
        });


        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }






}

