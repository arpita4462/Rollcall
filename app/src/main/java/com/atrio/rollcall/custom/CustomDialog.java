package com.atrio.rollcall.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.atrio.rollcall.R;
import com.atrio.rollcall.admin.AdminLogin;
import com.atrio.rollcall.admin.SelectAdmin;
import com.atrio.rollcall.teacher.TeacherActivity;

/**
 * Created by Admin on 28-03-2017.
 */

public class CustomDialog extends Dialog implements View.OnClickListener {
    public LinearLayout ll_student,ll_teacher,ll_Parent;
    Context mycontext;

    public CustomDialog(Context context) {
        super(context);
        mycontext = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        ll_Parent = (LinearLayout)findViewById(R.id.ll_parent);
        ll_student = (LinearLayout)findViewById(R.id.ll_admin);
        ll_teacher = (LinearLayout)findViewById(R.id.ll_teacher);
        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.slide);
        ll_Parent.startAnimation(animation1);
        ll_student.setOnClickListener(this);
        ll_teacher.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

            if (view.getId() == R.id.ll_admin){
                Intent iadmin = new Intent(getContext(),AdminLogin.class);
                getContext().startActivity(iadmin);

            }else {
                Intent iteacher = new Intent(getContext(),TeacherActivity.class);
                getContext().startActivity(iteacher);


        }


    }


}
