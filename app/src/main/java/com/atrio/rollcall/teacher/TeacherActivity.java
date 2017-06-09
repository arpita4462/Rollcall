package com.atrio.rollcall.teacher;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.atrio.rollcall.R;
import com.atrio.rollcall.RemarkAcitvity;
import com.atrio.rollcall.model.TeacherUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TeacherActivity extends AppCompatActivity {

    Button bt_sub;
    EditText et_email,et_pass;
    TextInputLayout inputLayoutName,inputLayoutPassword;
    FirebaseDatabase database;
    DatabaseReference mDatabaseReference;
    String pass,email,correct_email,correct_pwd,teacher_name;
    ProgressDialog dialog;
    Dialog selectdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectdialog = new Dialog(TeacherActivity.this);
        selectdialog.setContentView(R.layout.remark_attandance_select);
        setContentView(R.layout.activity_teacher);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        bt_sub = (Button)findViewById(R.id.btn_sub);
        et_email = (EditText)findViewById(R.id.input_name);
        et_pass = (EditText)findViewById(R.id.input_pass);
        database = FirebaseDatabase.getInstance();
        mDatabaseReference = database.getReference("TeacherList");
        dialog = new ProgressDialog(TeacherActivity.this);
        dialog.setMessage("Please Wait....");

        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = et_email.getText().toString().trim();
                pass = et_pass.getText().toString().trim();
                login();
            }
        });
    }
    private void login() {
        if (!validateEmail()) {
            return;
        }else if (!validatePassword()) {
            Log.i("elseif",""+correct_email);
            return;
        }else {
            validateUser();
        }
    }
    private void validateUser() {
        dialog.show();
        Query queryRef = mDatabaseReference.orderByChild("email_id").equalTo(email);

   queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {
           if (dataSnapshot.getChildrenCount() == 0){
               dialog.dismiss();
               inputLayoutName.setError(getString(R.string.err_msg_email));
               requestFocus(et_email);
           }else {

               inputLayoutName.setErrorEnabled(false);
               for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                   TeacherUser post = noteSnapshot.getValue(TeacherUser.class);
                   correct_email = post.getEmail_id();
                   correct_pwd = post.getPassword();
                   teacher_name=post.getName();
               }
               if (!pass.equals(correct_pwd)){
                   dialog.dismiss();
                   inputLayoutPassword.setError(getString(R.string.err_msg_password));
                   requestFocus(et_pass);
               }else{
                   dialog.dismiss();
                   selectdialog.show();
                   Button bt_atten = (Button) selectdialog.findViewById(R.id.btn_atten);
                   Button bt_remark = (Button) selectdialog.findViewById(R.id.btn_remark);
                   bt_atten.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           Intent iSelect = new Intent(TeacherActivity.this,SelectActivity.class);
                           iSelect.putExtra("teacher_name",teacher_name);
                           Log.i("teachername1",""+teacher_name);
                           startActivity(iSelect);
                           selectdialog.dismiss();
                       }

                   });
                   bt_remark.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {

                           Intent i=new Intent(TeacherActivity.this, RemarkAcitvity.class);
                           startActivity(i);
                           selectdialog.dismiss();

                       }
                   });

               }
           }

       }

       @Override
       public void onCancelled(DatabaseError databaseError) {

       }
   });
    }

    private boolean validatePassword() {

        if (pass.isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(et_pass);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }
        return true;
    }
    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validateEmail() {
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutName.setError(getString(R.string.err_msg_email));
            requestFocus(et_email);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }
        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
