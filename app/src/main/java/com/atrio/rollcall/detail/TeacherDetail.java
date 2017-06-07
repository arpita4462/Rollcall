package com.atrio.rollcall.detail;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.model.TeacherUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TeacherDetail extends AppCompatActivity {

    String tech_id;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    EditText emp_id,name_et,empid_et,email_et,pass_et,mob_et,gender_et;
    Button updatebtn,editbtn,showbtn;
    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_detail);
        showbtn=(Button)findViewById(R.id.btn_show);
        emp_id=(EditText)findViewById(R.id.empid_et);
        name_et=(EditText)findViewById(R.id.name_tv);
        email_et=(EditText)findViewById(R.id.email_tv);
        empid_et=(EditText)findViewById(R.id.empid_et1);
        pass_et=(EditText)findViewById(R.id.pass_tv);
        mob_et=(EditText)findViewById(R.id.mob_tv);
        gender_et=(EditText)findViewById(R.id.gender_tv);
        updatebtn=(Button) findViewById(R.id.btn_update);
        editbtn=(Button)findViewById(R.id.btn_edit);
        editbtn.setEnabled(false);
        updatebtn.setEnabled(false);

        name_et.setEnabled(false);
        empid_et.setEnabled(false);
        pass_et.setEnabled(false);
        email_et.setEnabled(false);
        mob_et.setEnabled(false);
        gender_et.setEnabled(false);

        ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }else{ db_instance = FirebaseDatabase.getInstance();
            db_ref = db_instance.getReference("TeacherList");

            dialog = new ProgressDialog(TeacherDetail.this);
            dialog.setMessage("Please Wait....");
            dialog.setCanceledOnTouchOutside(false);
            showbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (emp_id.getText().toString().trim().length() < 1) {
                        emp_id.setError("Enter Employee ID");
                        emp_id.requestFocus();
                    }else {
                        tech_id=emp_id.getText().toString().toLowerCase();
                        findTeacherById(tech_id);
                        dialog.show();



                    }
                }


            });
            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    name_et.requestFocus();
                    name_et.setEnabled(true);
                    pass_et.setEnabled(true);
                    empid_et.setEnabled(false);
                    email_et.setEnabled(true);
                    mob_et.setEnabled(true);
                    gender_et.setEnabled(true);

                    updatebtn.setEnabled(true);
                    updatebtn.setBackgroundResource(R.drawable.btn);

                }
            });

            updatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validate()) {
                        dialog.show();
                        tech_id = emp_id.getText().toString().toLowerCase();


                        Query queryRef = db_ref.orderByChild("emp_ID").equalTo(tech_id);
                        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    TeacherUser user = child.getValue(TeacherUser.class);
                                    String newName = name_et.getText().toString();
//                                    String newempid = empid_et.getText().toString();
                                    String newemail = email_et.getText().toString();
                                    String newpass = pass_et.getText().toString();
                                    String newmob = mob_et.getText().toString();
                                    String newgender = gender_et.getText().toString();

                                    child.getRef().child("name").setValue(newName);
//                                    child.getRef().child("emp_ID").setValue(newempid);
                                    child.getRef().child("email_id").setValue(newemail);
                                    child.getRef().child("password").setValue(newpass);
                                    child.getRef().child("mobile").setValue(newmob);
                                    child.getRef().child("gender").setValue(newgender);
                                    user.setName(newName);
//                                    user.setEmp_ID(newempid);
                                    user.setEmail_id(newemail);
                                    user.setPassword(newpass);
                                    user.setMobile(newmob);
                                    user.setGender(newgender);

                                    name_et.setEnabled(false);
                                    empid_et.setEnabled(false);
                                    pass_et.setEnabled(false);
                                    email_et.setEnabled(false);
                                    mob_et.setEnabled(false);
                                    gender_et.setEnabled(false);
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();

                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(), "Error In Register", Toast.LENGTH_LONG).show();


                            }
                        });
                    }
                }
            });




        }


}

    private boolean validate() {
// check whether the field is empty or not
        if (name_et.getText().toString().trim().length() < 1) {
            name_et.setError("Please Fill This Field");
            name_et.requestFocus();
            return false;
        }else if (empid_et.getText().toString().trim().length() < 1) {
            empid_et.setError("Please Fill This Field");
            empid_et.requestFocus();
            return false;


    }  else if (email_et.getText().toString().trim().length() < 1 || isEmailValid(email_et.getText().toString()) == false) {
            empid_et.setError("Invalid Email Address");
            email_et.requestFocus();
            return false;
        }else if (gender_et.getText().toString().trim().length() < 1) {
            gender_et.setError("Please Fill This Field");
            gender_et.requestFocus();
            return false;


    }else if (mob_et.getText().toString().trim().length() < 1 ||mob_et.getText().toString().trim().length() >12 ||mob_et.getText().toString().trim().length()<10 ) {
            mob_et.setError("Please Fill This Field");
            mob_et.requestFocus();
            return false;

        }else if (pass_et.getText().toString().trim().length() < 1) {
            pass_et.setError("Please Fill This Field");
            pass_et.requestFocus();
            return false;


    } else
            return true;

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
    private void findTeacherById(String tech_id) {

//        dialog.show();
        tech_id=emp_id.getText().toString().toLowerCase();
        Query queryRef = db_ref.orderByChild("emp_ID").equalTo(tech_id);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid Employee ID", Toast.LENGTH_LONG).show();
                }else {
                    dialog.dismiss();
                    editbtn.setEnabled(true);
                    editbtn.setBackgroundResource(R.drawable.btn);
                    updatebtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
//                if (snapshot.hasChildren()) {
                TeacherUser post = snapshot.getValue(TeacherUser.class);
                name_et.setText(post.getName());
                empid_et.setText(post.getEmp_ID());
                email_et.setText(post.getEmail_id());
                pass_et.setText(post.getPassword());
                mob_et.setText(post.getMobile());
                gender_et.setText(post.getGender());
//                } else {
//                    Toast.makeText(getApplicationContext(), "Invalid Employee ID", Toast.LENGTH_LONG).show();
//
//                }
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


    }


}
