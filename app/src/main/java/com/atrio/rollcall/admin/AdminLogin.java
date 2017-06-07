package com.atrio.rollcall.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.MainActivity;
import com.atrio.rollcall.R;
import com.atrio.rollcall.custom.CustomRestpwd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLogin extends AppCompatActivity {
    Button bt_sub;
    EditText et_email,et_pass;
    TextView tv_forgetpwd;
    TextInputLayout inputLayoutName,inputLayoutPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    String email,password;
    private CustomRestpwd customRestpwd;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_pass);
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        bt_sub = (Button) findViewById(R.id.btn_sub);
        et_email = (EditText) findViewById(R.id.input_name);
        et_pass = (EditText) findViewById(R.id.input_pass);
        tv_forgetpwd=(TextView)findViewById(R.id.tv_forgetpwd);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(AdminLogin.this,SelectAdmin.class));
                    finish();
                }
            }
        };
        bt_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = et_email.getText().toString();
                password = et_pass.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    inputLayoutName.setError(getString(R.string.err_msg_email));
                }else  if (TextUtils.isEmpty(password)) {
                    inputLayoutName.setErrorEnabled(false);
                    inputLayoutPassword.setError(getString(R.string.err_msg_password));
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(AdminLogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Log.i("success111", "" + task.isSuccessful());

//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.i("failure", "" + task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                            }

                        }
                    });
                }
            }
        });

        tv_forgetpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRestpwd = new CustomRestpwd(AdminLogin.this);
                customRestpwd.requestWindowFeature(Window.FEATURE_NO_TITLE);
                customRestpwd.show();

            }
        });

    }

    public void onStart() {
        super.onStart();
      mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AdminLogin.this,MainActivity.class));

    }
}
