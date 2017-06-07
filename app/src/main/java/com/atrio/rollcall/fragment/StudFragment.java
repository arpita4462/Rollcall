package com.atrio.rollcall.fragment;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.model.StudentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudFragment extends Fragment {
    Button submitButton;
    EditText nametv, classtv, emailtv, addresstv, dobtv, mobiletv,parent_tv,et_last_name;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    private Spinner spin_class,spin_sec;
    public String class_name,section,subclass;
//    private  int rollcount=1;
    DatePickerDialog datePickerDialog;
    AlertDialog.Builder builder;
    AlertDialog alert;
    ProgressDialog dialog;



    //private String studentID,stud_Detail;
    String first_name,last_name,studclass,rollno, parents_name,emailid,per_address,gender,dob,mob,sub_class,lastrollno;
    public StudFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view1 = inflater.inflate(R.layout.fragment_stud, container, false);
        nametv = (EditText) view1.findViewById(R.id.firstName);
        et_last_name = (EditText)view1.findViewById(R.id.et_last_name);
        spin_class = (Spinner)view1.findViewById(R.id.spin_class);
        spin_sec = (Spinner)view1.findViewById(R.id.spin_sec);
        parent_tv = (EditText) view1.findViewById(R.id.parent);
//        rollnotv = (EditText) view1.findViewById(R.id.rollno);
        emailtv = (EditText) view1.findViewById(R.id.email);
        addresstv = (EditText) view1.findViewById(R.id.home);
        dobtv = (EditText) view1.findViewById(R.id.dob);
        mobiletv = (EditText) view1.findViewById(R.id.mobno);
        radioSexGroup = (RadioGroup) view1.findViewById(R.id.radioSex);

        ArrayAdapter<CharSequence> adpter_class = ArrayAdapter.createFromResource(getActivity(),R.array.class_name, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter_sec = ArrayAdapter.createFromResource(getActivity(),R.array.section, android.R.layout.simple_spinner_item);

        adpter_class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_sec.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spin_class.setAdapter(adpter_class);
        spin_sec.setAdapter(adapter_sec);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);

        submitButton = (Button) view1.findViewById(R.id.submit);
//        submitButton.setEnabled(false);

        spin_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                class_name = adapterView.getItemAtPosition(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        spin_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                section = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        addresstv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch( View view, MotionEvent event) {
                if (view.getId() == R.id.input_layout_home) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction()&MotionEvent.ACTION_MASK){
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                        case MotionEvent.ACTION_DOWN:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });


        addresstv.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
//                    Toast.makeText(HelloFormStuff.this, edittext.getText(), Toast.LENGTH_SHORT).show();
                    return true;
                }
//                dobtv.requestFocus();

                return false;
            }
        });
        dobtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(getActivity(),android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dobtv.setText(dayOfMonth + "/"+ (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        db_instance = FirebaseDatabase.getInstance();

            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (isConnected()) {
                        if (validate()){


                            db_ref = db_instance.getReference("StudentList");

                        int selectedId = radioSexGroup.getCheckedRadioButtonId();
                        radioSexButton = (RadioButton) view1.findViewById(selectedId);
                        first_name = nametv.getText().toString();
                        last_name = et_last_name.getText().toString();
                        parents_name = parent_tv.getText().toString();
                        emailid = emailtv.getText().toString();
                        per_address = addresstv.getText().toString();
                        dob = dobtv.getText().toString();
                        mob = mobiletv.getText().toString();
                        gender = radioSexButton.getText().toString();
//                        rollno = rollnotv.getText().toString();
/*
                            subclass = class_name.substring(class_name.indexOf("-") + 1, class_name.length());
                            rollno=subclass + "-1";
                            Log.i("q2553", "" + rollno);*/



                            dialog.show();
//                        Query readqery = db_ref.child(class_name).child(section).orderByChild("rollno").limitToLast(1);
                            Query readqery = db_ref.child(class_name).child(section).orderByKey();

                       /*     Log.i("q23", "" + class_name);
                        Log.i("q23", "" + section);
                        Log.i("q23", "" + readqery);*/

                        readqery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                               if(dataSnapshot.getChildrenCount()==0){
                                    subclass = class_name.substring(class_name.indexOf("-") + 1, class_name.length());

                                    rollno=subclass+"-001";
                                    createSdutentDetail(first_name, last_name, studclass, rollno, parents_name, emailid, per_address, dob, mob, gender, class_name, section);
                                    nametv.setText("");
                                    et_last_name.setText("");
//                       classtv.setText("");
//                                    rollnotv.setText(rollno);
                                    parent_tv.setText("");
                                    emailtv.setText("");
                                    addresstv.setText("");
                                    dobtv.setText("");
                                    mobiletv.setText("");
                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();
                                    showrollno(rollno);
                                    alert.show();
                                }else {

                                        long countchild = dataSnapshot.getChildrenCount();
//                                        lastrollno = (String) child.child("rollno").getValue();
//int rollplus = (Integer.parseInt(lastrollno.substring(lastrollno.indexOf("-") + 1, lastrollno.length())) + 1);
                                        countchild++;
                                        Log.i("subclass", "" + countchild);
                                        subclass = class_name.substring(class_name.indexOf("-") + 1, class_name.length());
                                        Log.i("rollnodisplay", "" + subclass + "-" + countchild);
                                        rollno=subclass + "-" +String.format("%03d",countchild);
//                                    rollno=rollnotv.getText().toString();
                                   Log.i("rollformat", "" + rollno);


                                        dialog.show();
                                        createSdutentDetail(first_name, last_name, studclass, rollno, parents_name, emailid, per_address, dob, mob, gender, class_name, section);
                                        nametv.setText("");
                                        et_last_name.setText("");
//                       classtv.setText("");
//                                        rollnotv.setText(rollno);
                                        parent_tv.setText("");
                                        emailtv.setText("");
                                        addresstv.setText("");
                                        dobtv.setText("");
                                        mobiletv.setText("");
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();
                                        showrollno(rollno);
                                        alert.show();


                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(getContext(), "Rollno Error", Toast.LENGTH_LONG).show();

                            }
                        });

                    } }}
            });

         return view1;

    }

    private void showrollno(String rollno) {
       /* builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Your Roll No is ");
        builder.setMessage(rollno);
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(getContext(),"Yes is clicked",Toast.LENGTH_LONG).show();
                    }
                });*/

        TextView title = new TextView(getActivity());
        // You Can Customise your Title here
        title.setText("Your Roll No is");
        //title.setBackgroundColor(Color.DKGRAY);
//        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(getResources().getColor(R.color.colorPrimary));



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(title);
        builder.setMessage(rollno);
        builder.setCancelable(false);
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

            }

        });

        alert = builder.show();
        TextView messageText = (TextView)alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        messageText.setTextColor(Color.RED);
        title.setTextSize(20);

    }

    public boolean isConnected(){

        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

            return false;
        }
        else
            return true;
    }

    private void createSdutentDetail(String first_name, String last_name, String studclass, String rollno, String pname, String emailid, String per_address, String dob, String mob, String gender,String class_name,String section ) {

       StudentUser studentUser=new StudentUser();

       studentUser.setFirst_name(first_name);
       studentUser.setLast_name(last_name);
       studentUser.setDob(dob);
       studentUser.setSection(section);
       studentUser.setStudclass(class_name);
       studentUser.setRollno(rollno);
       studentUser.setParents_name(pname);
       studentUser.setEmailid(emailid);
       studentUser.setPer_address(per_address);
       studentUser.setMob(mob);
       studentUser.setGender(gender);


       db_ref.child(class_name).child(section).child(rollno).setValue(studentUser);
    }


    private boolean validate() {
// check whether the field is empty or not
        if (nametv.getText().toString().trim().length() < 1) {
            nametv.setError("Please Fill This Field");
            nametv.requestFocus();
            return false;

        } else if (et_last_name.getText().toString().trim().length() < 1){
            et_last_name.setError("Please Fill This Field");
            et_last_name.requestFocus();
            return false;

        }

        else if (class_name.equals("Select class")) {
            Toast.makeText(getContext(), "Select Class", Toast.LENGTH_LONG).show();
            return false;


        }else if (section.equals("Select section")){
            Toast.makeText(getContext(), "Select Section", Toast.LENGTH_LONG).show();
            return false;
/*

        } else if (rollnotv.getText().toString().trim().length() < 1) {
            rollnotv.setError("Please Fill This Field");
            rollnotv.requestFocus();
            return false;
*/

        } else if (parent_tv.getText().toString().trim().length() < 1) {
            parent_tv.setError("Please Fill This Field");
            parent_tv.requestFocus();
            return false;
        }
        else if (emailtv.getText().toString().trim().length() < 1 || isEmailValid(emailtv.getText().toString()) == false) {
            emailtv.setError("Invalid Email Address");
            emailtv.requestFocus();
            return false;

        }else if (addresstv.getText().toString().trim().length() < 1) {
            addresstv.setError("Please Fill This Field");
            addresstv.requestFocus();
            return false;

        } else if (dobtv.getText().toString().trim().length() < 1) {
            dobtv.setError("Please Fill This Field");
            dobtv.requestFocus();
            return false;

        } else if (mobiletv.getText().toString().trim().length() < 1 ||mobiletv.getText().toString().trim().length() >12 ||mobiletv.getText().toString().trim().length()<10 ) {
            mobiletv.setError("Please Fill This Field");
            mobiletv.requestFocus();
            return false;

        }  else
            return true;

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
     boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}