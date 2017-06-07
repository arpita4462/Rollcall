package com.atrio.rollcall.detail;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.model.StudentUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class StudentDetail extends AppCompatActivity {

    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    String section,studclass,st_roll;
    TextView showTextView;
    EditText name_et,class_et,roll_et,guard_et,email_et,address_et,dob_et,mob_et,gender_et;
    Button updatebtn,editbtn;
    Intent iget;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        showTextView=(TextView)findViewById(R.id.show_tv);
        name_et=(EditText)findViewById(R.id.name_tv);
        class_et=(EditText)findViewById(R.id.class_tv);
        roll_et=(EditText)findViewById(R.id.roll_tv);
        guard_et=(EditText)findViewById(R.id.parent_tv);
        email_et=(EditText)findViewById(R.id.email_tv);
        address_et=(EditText)findViewById(R.id.add_tv);
        dob_et=(EditText)findViewById(R.id.dob_tv);
        mob_et=(EditText)findViewById(R.id.mob_tv);
        gender_et=(EditText)findViewById(R.id.gender_tv);
        updatebtn=(Button) findViewById(R.id.btn_update);
        editbtn=(Button)findViewById(R.id.btn_edit);

        name_et.setEnabled(false);
        class_et.setEnabled(false);
        roll_et.setEnabled(false);
        guard_et.setEnabled(false);
        email_et.setEnabled(false);
        address_et.setEnabled(false);
        dob_et.setEnabled(false);
        mob_et.setEnabled(false);
        gender_et.setEnabled(false);
        updatebtn.setEnabled(false);

        iget=getIntent();
        studclass=iget.getStringExtra("studclass");
        section=iget.getStringExtra("section");
        st_roll=iget.getStringExtra("rollno");

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("StudentList");


        dialog = new ProgressDialog(StudentDetail.this);
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);
            findstudentbyname(st_roll);
        dialog.show();

        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name_et.requestFocus();
                name_et.setEnabled(true);
                class_et.setEnabled(true);
                roll_et.setEnabled(false);
                guard_et.setEnabled(true);
                email_et.setEnabled(true);
                address_et.setEnabled(true);
                dob_et.setEnabled(true);
                mob_et.setEnabled(true);
                gender_et.setEnabled(true);
                updatebtn.setEnabled(true);
                updatebtn.setBackgroundResource(R.drawable.btn);

            }
        });

        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager connMgr = (ConnectivityManager)getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo == null) {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }else {
                    if (validate()) {
                        updatestudentbyrollno(st_roll);
                        dialog.show();
                    }
                }

                         }
        });


        dob_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getApplication(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dob_et.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
    }

    private void updatestudentbyrollno(String st_roll) {
        Query queryRef = db_ref.child(studclass).child(section).orderByChild("rollno").equalTo(st_roll);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    StudentUser user=child.getValue(StudentUser.class);
                    String newName= name_et.getText().toString();
                    String newFname=newName.substring(0,newName.indexOf(" "));
                    String newLname=newName.substring(newName.indexOf(" ")+1);
                    String newclass= class_et.getText().toString();
                    String newroll= roll_et.getText().toString();
                    String newguard= guard_et.getText().toString();
                    String newemail= email_et.getText().toString();
                    String newaddress= address_et.getText().toString();
                    String newmob= mob_et.getText().toString();
                    String newgender= gender_et.getText().toString();

                    child.getRef().child("fname").setValue(newFname);
                    child.getRef().child("lname").setValue(newLname);
                    child.getRef().child("studclass").setValue(newclass);
                    child.getRef().child("rollno").setValue(newroll);
                    child.getRef().child("emailid").setValue(newemail);
                    child.getRef().child("parents_name").setValue(newguard);
                    child.getRef().child("per_address").setValue(newaddress);
                    child.getRef().child("mob").setValue(newmob);
                    child.getRef().child("gender").setValue(newgender);
                    user.setFirst_name(newFname);
                    user.setLast_name(newLname);
                    user.setStudclass(newclass);
                    user.setRollno(newroll);
                    user.setParents_name(newguard);
                    user.setEmailid(newemail);
                    user.setPer_address(newaddress);
                    user.setMob(newmob);
                    user.setGender(newgender);


                    name_et.setEnabled(false);
                    class_et.setEnabled(false);
                    roll_et.setEnabled(false);
                    guard_et.setEnabled(false);
                    email_et.setEnabled(false);
                    address_et.setEnabled(false);
                    dob_et.setEnabled(false);
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

    private void findstudentbyname(String st_roll) {
        // Query queryRef1 = db_ref.orderByKey();
        Query queryRef = db_ref.child(studclass).child(section).orderByChild("rollno").equalTo(st_roll);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_LONG).show();
//
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
                StudentUser post = snapshot.getValue(StudentUser.class);
                name_et.setText(post.getFirst_name()+" "+post.getLast_name());
                class_et.setText(post.getStudclass());
                roll_et.setText(post.getRollno());
                guard_et.setText(post.getParents_name());
                email_et.setText(post.getEmailid());
                address_et.setText(post.getPer_address());
                dob_et.setText(post.getDob());
                mob_et.setText(post.getMob());
                gender_et.setText(post.getGender());
                dialog.dismiss();
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
            // ....
        });


    }
    private boolean validate() {
// check whether the field is empty or not
        if (name_et.getText().toString().trim().length() < 1) {
            name_et.setError("Please Fill This Field");
            name_et.requestFocus();
            return false;

        }

        else if (class_et.equals("Select class")) {
            Toast.makeText(getApplicationContext(), "Select Class", Toast.LENGTH_LONG).show();
            return false;


        }else if (section.equals("Select section")){
            Toast.makeText(getApplicationContext(), "Select Section", Toast.LENGTH_LONG).show();
            return false;

        } else if (roll_et.getText().toString().trim().length() < 1) {
            roll_et.setError("Please Fill This Field");
            roll_et.requestFocus();
            return false;

        } else if (guard_et.getText().toString().trim().length() < 1) {
            guard_et.setError("Please Fill This Field");
            guard_et.requestFocus();
            return false;
        }
        else if (email_et.getText().toString().trim().length() < 1 || isEmailValid(email_et.getText().toString()) == false) {
            email_et.setError("Invalid Email Address");
            email_et.requestFocus();
            return false;

        }else if (address_et.getText().toString().trim().length() < 1) {
            address_et.setError("Please Fill This Field");
            address_et.requestFocus();
            return false;

        } else if (dob_et.getText().toString().trim().length() < 1) {
            dob_et.setError("Please Fill This Field");
            dob_et.requestFocus();
            return false;

        }else if (gender_et.getText().toString().trim().length() < 1) {
            gender_et.setError("Please Fill This Field");
            gender_et.requestFocus();
            return false;


        } else if (mob_et.getText().toString().trim().length() < 1 ||mob_et.getText().toString().trim().length() >12 ||mob_et.getText().toString().trim().length()<10 ) {
            mob_et.setError("Please Fill This Field");
            mob_et.requestFocus();
            return false;

        }  else
            return true;

    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
}
