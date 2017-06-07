package com.atrio.rollcall.teacher;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        Log.i("data1",""+email);

            }
        });


    }

    private void login() {

       // Log.i("comingtoLOgin",""+correct_email);
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
        //Log.i("q4",""+queryRef);

   queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
       @Override
       public void onDataChange(DataSnapshot dataSnapshot) {

           //Log.i("CountData",""+dataSnapshot.getChildrenCount());

           if (dataSnapshot.getChildrenCount() == 0){

               dialog.dismiss();
               inputLayoutName.setError(getString(R.string.err_msg_email));
               requestFocus(et_email);
             //  Toast.makeText(getApplicationContext(),"Something went wrong. You entered InCorrect Email ", Toast.LENGTH_SHORT).show();

           }else {

               inputLayoutName.setErrorEnabled(false);
               for (DataSnapshot noteSnapshot: dataSnapshot.getChildren()) {
                   TeacherUser post = noteSnapshot.getValue(TeacherUser.class);
                   correct_email = post.getEmail_id();
                   correct_pwd = post.getPassword();
                   teacher_name=post.getName();
                  /* Log.i("data2",""+correct_email);
                   Log.i("data21",""+correct_pwd);*/
               }

               if (!pass.equals(correct_pwd)){
                   dialog.dismiss();
                   inputLayoutPassword.setError(getString(R.string.err_msg_password));
                   requestFocus(et_pass);
               }else{
                   dialog.dismiss();
                   Intent iSelect = new Intent(TeacherActivity.this,SelectActivity.class);
                   iSelect.putExtra("teacher_name",teacher_name);
                   Log.i("teachername1",""+teacher_name);
                   startActivity(iSelect);
                   finish();

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
        //Log.i("Print99",""+parent_email);
        if (email.isEmpty() || !isValidEmail(email)) {
//            Log.i("data3",""+email);
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
