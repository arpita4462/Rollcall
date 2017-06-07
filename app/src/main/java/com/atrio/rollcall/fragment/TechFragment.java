package com.atrio.rollcall.fragment;


import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atrio.rollcall.R;
import com.atrio.rollcall.model.TeacherUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TechFragment extends Fragment {
    Button submitButton;
    EditText nametv, pwdtv, empidtv, emailtv, mobiletv;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private DatabaseReference db_ref;
    private FirebaseDatabase db_instance;
    String name,emailid,gender,pwd,mob,empID;
    ProgressDialog dialog;

    public TechFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tech, container, false);
        nametv = (EditText) view.findViewById(R.id.firstName);
        pwdtv = (EditText) view.findViewById(R.id.pwd);
        empidtv = (EditText) view.findViewById(R.id.empid);
        emailtv = (EditText) view.findViewById(R.id.email);
        mobiletv = (EditText) view.findViewById(R.id.mobno);
        radioSexGroup = (RadioGroup) view.findViewById(R.id.radioSex);
        submitButton = (Button) view.findViewById(R.id.submit);

        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("TeacherList");
//        Log.i("KeyID",""+empID);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isConnected()) {
                 if (validate()){

                        int selectedId = radioSexGroup.getCheckedRadioButtonId();
                        radioSexButton = (RadioButton) view.findViewById(selectedId);
                        name=nametv.getText().toString().trim();
                        emailid=emailtv.getText().toString().trim();
                        pwd=pwdtv.getText().toString().trim();
                        mob=mobiletv.getText().toString().trim();
                        gender=radioSexButton.getText().toString().trim();
                        empID=empidtv.getText().toString().trim().toLowerCase();

                     dialog.show();
                     Query queryRef = db_ref.orderByChild("emp_ID").equalTo(empID);

                     queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             if (dataSnapshot.getChildrenCount() == 0) {
                                 createTeacherDetail(name,empID,emailid,mob,pwd,gender);


                                 nametv.setText("");
                                 emailtv.setText("");
                                 mobiletv.setText("");
                                 empidtv.setText("");
                                 pwdtv.setText("");
                                 empidtv.requestFocus();
                                 empidtv.setText("");
                                 dialog.dismiss();
                                 Toast.makeText(getContext(), "SuccessFully Register", Toast.LENGTH_LONG).show();
                             }else {


                                 Log.i("empid1",""+empID);
                                 dialog.dismiss();
                                 Toast.makeText(getContext(), "This Employee ID is already present", Toast.LENGTH_LONG).show();
                                 empidtv.setText("");

                             }
                         }

                         @Override
                         public void onCancelled(DatabaseError databaseError) {

                         }
                     });
                    }
                }
            }
        });
        return view;
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
            return false;
        }
        else
            return true;
    }

    private void createTeacherDetail(String name, final String empID, String emailid, String mob,String pwd, String gender) {

        TeacherUser teacherUser=new TeacherUser();

        teacherUser.setName(name);
        teacherUser.setEmail_id(emailid);
        teacherUser.setEmp_ID(empID);
        teacherUser.setMobile(mob);
        teacherUser.setPassword(pwd);
        teacherUser.setGender(gender);

        db_ref.child(empID).setValue(teacherUser);
    }

    private boolean validate() {
// check whether the field is empty or not

        if (empidtv.getText().toString().trim().length() < 1) {
            empidtv.setError("Please Fill This Field");
            empidtv.requestFocus();
            return false;
        }else if (nametv.getText().toString().trim().length() < 1) {
                nametv.setError("Please Fill This Field");
                nametv.requestFocus();
                return false;


        }  else if (emailtv.getText().toString().trim().length() < 1 || isEmailValid(emailtv.getText().toString()) == false) {
            emailtv.setError("Invalid Email Address");
            emailtv.requestFocus();
            return false;

        }else if (mobiletv.getText().toString().trim().length() < 1 ||mobiletv.getText().toString().trim().length() >12 ||mobiletv.getText().toString().trim().length()<10 ) {
            mobiletv.setError("Please Fill This Field");
            mobiletv.requestFocus();
            return false;


             }else if (pwdtv.getText().toString().trim().length() < 1) {
            pwdtv.setError("Please Fill This Field");
            pwdtv.requestFocus();
            return false;

        }  else
            return true;

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }
}