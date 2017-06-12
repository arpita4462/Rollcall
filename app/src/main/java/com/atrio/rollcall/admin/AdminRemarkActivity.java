package com.atrio.rollcall.admin;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.R;
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

public class AdminRemarkActivity extends AppCompatActivity {
    String class_name,section;
    Button bt_all,bt_indivi,bt_send,bt_class,btn_all;
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
        setContentView(R.layout.activity_admin_activity);
        sp_class = (Spinner) findViewById(R.id.admin_sp_class);
        sp_sec = (Spinner) findViewById(R.id.admin_sp_sec);
        actv = (AutoCompleteTextView) findViewById(R.id.admin_autoCompleteTextView1);
        et_remark =(EditText)findViewById(R.id.admin_et_remark);
        tv_name =(TextView) findViewById(R.id.admin_tv_name);
        tv_sec =(TextView) findViewById(R.id.admin_tv_sec);
        tv_class =(TextView) findViewById(R.id.admin_tv_class);
        bt_class = (Button)findViewById(R.id.admin_btn_class);
        bt_indivi = (Button)findViewById(R.id.admin_btn_indi);
        btn_all =(Button)findViewById(R.id.btn_admin_all);
        bt_send = (Button)findViewById(R.id.admin_btn_send_remark);

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("StudentList");

        clicked=false;
        bt_send.setEnabled(false);
        progressDialog = new ProgressDialog(AdminRemarkActivity.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(AdminRemarkActivity.this,
                R.array.class_name, android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(AdminRemarkActivity.this,
                R.array.section, android.R.layout.simple_spinner_item);


        adpter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_class.setAdapter(adpter_class);
        sp_sec.setAdapter(adapter_sec);


        tv_class.setVisibility(View.GONE);
        sp_class.setVisibility(View.INVISIBLE);
        tv_sec.setVisibility(View.GONE);
        sp_sec.setVisibility(View.INVISIBLE);
        tv_name.setVisibility(View.GONE);
        actv.setVisibility(View.GONE);


        btn_all.setOnClickListener(new View.OnClickListener() {

       @Override
       public void onClick(View view) {
           i =1;
           clicked=true;
           btn_all.setBackgroundResource(R.drawable.btn);
           bt_class.setBackgroundResource(R.drawable.disable_btn);
           bt_indivi.setBackgroundResource(R.drawable.disable_btn);
           tv_class.setVisibility(View.GONE);
           sp_class.setVisibility(View.INVISIBLE);
           sp_sec.setVisibility(View.INVISIBLE);
           tv_sec.setVisibility(View.GONE);
           sp_sec.setVisibility(View.INVISIBLE);
           tv_name.setVisibility(View.GONE);
           actv.setVisibility(View.GONE);

           // Queery for All Sudent
           progressDialog.show();
           Query quer_allid = db_ref.orderByKey();
           quer_allid.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                   mailall = new ArrayList<String>();

                   if (dataSnapshot.getChildrenCount()!=0) {
                       for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                           for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                               for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                   StudentUser user = dataSnapshot3.getValue(StudentUser.class);
                                   mailall.add(user.getEmailid());
                               }
                           }
                       }
                       progressDialog.dismiss();
                   }else{
                       progressDialog.dismiss();

                       Toast.makeText(getApplicationContext(), "There is no Student in selected Class", Toast.LENGTH_LONG).show();

                   }

               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });




       }
   });

        bt_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                clicked=true;
                mailall = new ArrayList<String>();
                btn_all.setBackgroundResource(R.drawable.disable_btn);
                bt_class.setBackgroundResource(R.drawable.btn);
                bt_indivi.setBackgroundResource(R.drawable.disable_btn);
                tv_class.setVisibility(View.VISIBLE);
                sp_class.setVisibility(View.VISIBLE);
                sp_sec.setVisibility(View.VISIBLE);
                tv_sec.setVisibility(View.GONE);
                sp_sec.setVisibility(View.INVISIBLE);
                tv_name.setVisibility(View.GONE);
                actv.setVisibility(View.GONE);

                sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        class_name = parent.getItemAtPosition(position).toString();
                        bt_class.performClick();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

              class_name =   sp_class.getSelectedItem().toString();

                progressDialog.show();

                Query queryall = db_ref.child(class_name).orderByKey();

                queryall.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {
                                for (DataSnapshot data2 : data.getChildren()) {
                                    StudentUser post = data2.getValue(StudentUser.class);
                                    mailall.add(post.getEmailid());

                                }

                            }
                            progressDialog.dismiss();

                        } else {
                            progressDialog.dismiss();

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

                bt_indivi.setBackgroundResource(R.drawable.btn);
                bt_class.setBackgroundResource(R.drawable.disable_btn);
                btn_all.setBackgroundResource(R.drawable.disable_btn);
                tv_class.setVisibility(View.VISIBLE);
                sp_class.setVisibility(View.VISIBLE);
                tv_sec.setVisibility(View.VISIBLE);
                sp_sec.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                actv.setVisibility(View.VISIBLE);
                actv.requestFocus();
                sp_sec.getSelectedItem().toString();

                sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        class_name = parent.getItemAtPosition(position).toString();
                        bt_indivi.performClick();
                        actv.setText("");

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                sp_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        section = parent.getItemAtPosition(position).toString();
                        bt_indivi.performClick();
                        actv.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                class_name = sp_class.getSelectedItem().toString();
                section = sp_sec.getSelectedItem().toString();

                    progressDialog.show();

                    Query query_key = db_ref.child(class_name).child(section).orderByKey();


                    query_key.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getChildrenCount() != 0) {
                                list_student_info = new ArrayList<>();
                                list_data = new ArrayList<>();

                                Iterator<DataSnapshot> item = dataSnapshot.getChildren().iterator();

                                while (item.hasNext()) {
                                    DataSnapshot items = item.next();
                                    String key00 = items.getKey();
                                    String name = items.getValue().toString();
                                    StudentUser user = items.getValue(StudentUser.class);
                                    String stud_info = user.first_name + " " + user.last_name + "-" + user.getRollno();

                                    user.setFirst_name(user.first_name);
                                    user.setLast_name(user.last_name);
                                    user.setEmailid(user.emailid);
                                    user.setRollno(user.rollno);

                                    list_student_info.add(stud_info);
                                    list_data.add(user);

                                }
                                sendData(list_student_info, list_data);
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                adapter.clear();
                                Toast.makeText(getApplicationContext(), "There is no Student in selected Class", Toast.LENGTH_SHORT).show();
                            }

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
                                String email ="priyas7715@gmail.com";

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
                        Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_LONG).show();

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
//                                Log.i("childrollno", "" + mailall);
//                                Log.i("childrollno", "" + mailall.size());

                                String email ="priyas7715@gmail.com";
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

                mailall = new ArrayList<>();

                for (int i=0;i<list_data.size();i++){
                    if (list_data.get(i).getRollno().equals(data1) ){
                        mailall.add(list_data.get(i).getEmailid());

                    }
                }

            }
        });



    }


}
