package com.atrio.rollcall;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.model.StudentUser;
import com.atrio.rollcall.sendmail.SendMail;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class RemarkAcitvity extends AppCompatActivity {
    String class_name,section,email;
    Button bt_all,bt_indivi,bt_send;
    private AutoCompleteTextView actv;
    EditText et_remark;
    Spinner sp_class,sp_sec;
    TextView tv_sec,tv_name,tv_class;
    Dialog dialog;
    ProgressDialog progressDialog;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    ArrayList<String> mailall;
    ArrayList<String> list_student_info;
    ArrayAdapter<String> adapter;
    ArrayList<StudentUser> list_data;
    boolean clicked;
    int i=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark_acitvity);

        sp_class = (Spinner) findViewById(R.id.sp_class);
        sp_sec = (Spinner) findViewById(R.id.sp_sec);
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        et_remark =(EditText)findViewById(R.id.et_remark);
        tv_name =(TextView) findViewById(R.id.tv_name);
        tv_sec =(TextView) findViewById(R.id.tv_sec);
        tv_class =(TextView) findViewById(R.id.tv_class);
        bt_all = (Button)findViewById(R.id.btn_all);
        bt_indivi = (Button)findViewById(R.id.btn_indi);
        bt_send = (Button)findViewById(R.id.btn_send_remark);
        tv_class.setVisibility(View.GONE);
        sp_class.setVisibility(View.INVISIBLE);
        tv_sec.setVisibility(View.GONE);
        sp_sec.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.GONE);
        actv.setVisibility(View.GONE);

        clicked=false;
        bt_send.setEnabled(false);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("StudentList");


        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(RemarkAcitvity.this,
                R.array.class_name, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(RemarkAcitvity.this,
                R.array.section, android.R.layout.simple_spinner_item);


        adpter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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



        bt_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                clicked=true;
                mailall = new ArrayList<String>();
                bt_all.setBackgroundResource(R.drawable.btn);
                bt_indivi.setBackgroundResource(R.drawable.disable_btn);
                tv_class.setVisibility(View.VISIBLE);
                sp_class.setVisibility(View.VISIBLE);
                sp_sec.setVisibility(View.VISIBLE);
                tv_sec.setVisibility(View.GONE);
                sp_sec.setVisibility(View.INVISIBLE);
                tv_name.setVisibility(View.GONE);
                actv.setVisibility(View.GONE);

                sp_sec.getSelectedItem().toString();
                progressDialog.show();
                Query queryall = db_ref.child(class_name).orderByKey();

                queryall.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0){
                            for (DataSnapshot data :dataSnapshot.getChildren()) {
                                for (DataSnapshot data2 :data.getChildren()) {
                                    StudentUser post = data2.getValue(StudentUser.class);
                                    mailall.add(post.getEmailid());
                                }

                            }
                            progressDialog.dismiss();

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "There is no Student in selected Class", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        bt_indivi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked=true;
                i=2;
                list_student_info = new ArrayList<>();
                list_data = new ArrayList<>();


                bt_indivi.setBackgroundResource(R.drawable.btn);
                bt_all.setBackgroundResource(R.drawable.disable_btn);
                tv_class.setVisibility(View.VISIBLE);
                sp_class.setVisibility(View.VISIBLE);
                tv_sec.setVisibility(View.VISIBLE);
                sp_sec.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                actv.setVisibility(View.VISIBLE);
                actv.requestFocus();
                sp_sec.getSelectedItem().toString();

                progressDialog.show();
               Query query_key = db_ref.child(class_name).child(section).orderByKey();


                query_key.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterator<DataSnapshot> item = dataSnapshot.getChildren().iterator();

                        while (item.hasNext()){
                            DataSnapshot items = item.next();
                            String key00= items.getKey();
                            String name = items.getValue().toString();
                            Log.i("key88",""+key00);
                            Log.i("Value88",""+name);
                            StudentUser user = items.getValue(StudentUser.class);
                            String stud_info = user.first_name+" "+user.last_name+"-"+user.getRollno();

                            user.setFirst_name(user.first_name);
                            user.setLast_name(user.last_name);
                            user.setEmailid(user.emailid);
                            user.setRollno(user.rollno);

                            list_student_info.add(stud_info);
                            list_data.add(user);

                        }
                        sendData(list_student_info,list_data);
progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        et_remark.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.toString().trim().length()==0){
                    bt_send.setEnabled(false);
                    bt_send.setBackgroundResource(R.drawable.disable_btn);

                } else {
                    bt_send.setEnabled(true);
                    bt_send.setBackgroundResource(R.drawable.btn);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (i!=2){
                        if (clicked==true) {

                            dialog = new Dialog(view.getContext());
                            dialog.setContentView(R.layout.custom_mail_dialog);
                            dialog.setTitle("Alert...");
                            dialog.show();
                            Button bt_yes = (Button) dialog.findViewById(R.id.btn_yes);
                            Button bt_no = (Button) dialog.findViewById(R.id.btn_no);
                            bt_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("childrollno", "" + mailall);
                                    Log.i("childrollno", "" + mailall.size());

                                    for (int i = 0; i < 1; i++) {

                                        email = mailall.get(i);
                                    }

                                    String mail_subject = "Student Remark";
                                    String message = "" + et_remark.getText().toString();

                                    SendMail sendmailall = new SendMail(v.getContext(), email, mail_subject, message, mailall);

                                    sendmailall.execute();

                                    dialog.dismiss();
                                }
                            });


                            bt_no.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(), "Select Either All or Indivisual", Toast.LENGTH_LONG).show();

                        }

                    }else {
                        if (clicked == true && actv.getText().toString().length() == 0) {
                            actv.setError("Select Student");
                            actv.requestFocus();
                        } else {
                            dialog = new Dialog(view.getContext());
                            dialog.setContentView(R.layout.custom_mail_dialog);
                            dialog.setTitle("Alert...");
                            dialog.show();
                            Button bt_yes = (Button) dialog.findViewById(R.id.btn_yes);
                            Button bt_no = (Button) dialog.findViewById(R.id.btn_no);
                            bt_yes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.i("childrollno", "" + mailall);
                                    Log.i("childrollno", "" + mailall.size());

                                    for (int i = 0; i < 1; i++) {

                                        email = mailall.get(i);
                                    }

                                    String mail_subject = "Student Remark";
                                    String message = "" + et_remark.getText().toString();

                                    SendMail sendmailall = new SendMail(v.getContext(), email, mail_subject, message, mailall);

                                    sendmailall.execute();

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
            }
        });
    }

    private void sendData(ArrayList<String> list_student_info, final ArrayList<StudentUser> list_data) {
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list_student_info);
        actv.setAdapter(adapter);
        actv.setThreshold(1);
        actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String data = parent.getItemAtPosition(position).toString();
                String data1 = data.substring(data.indexOf("-")+1,data.length());
                for (int i=0;i<list_data.size();i++){
                    if (list_data.get(i).getRollno().equals(data1) ){
                    }
                }

            }
        });



    }


}
