package com.atrio.rollcall.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.atrio.rollcall.R;
import com.atrio.rollcall.model.StudentUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.atrio.rollcall.teacher.AttendanceActivity.subject;

/**
 * Created by Admin on 30-05-2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private Context c;
    ArrayList<StudentUser> arr=new ArrayList<>();

    public RecyclerAdapter(Context context, ArrayList<StudentUser> arr_qty) {
        this.arr = arr_qty;
        this.c = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.child_name = arr.get(position).getFirst_name();
        holder.child_rollno = arr.get(position).getRollno();
        holder.tv_name.setText("Name: " + arr.get(position).getFirst_name()+" "+arr.get(position).getLast_name());
        holder.tv_rollno.setText("RollNo: " + arr.get(position).getRollno());
        final String class1 = arr.get(position).getStud_class();
        final String section = arr.get(position).getStud_sec();
        final  String sub = arr.get(position).getStud_sub();
        final  String datedata = arr.get(position).getDate();
        holder.list_stud.add("0");





        holder.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_present:
                        String status = "present";
                        holder.db_refprevious.child(class1).child(section).child(sub).child(datedata).child(holder.child_rollno).setValue(status);
                        break;
                    case R.id.radio_absent:
                        status = "absent";
                        holder.db_refprevious.child(class1).child(section).child(subject).child(datedata).child(holder.child_rollno).setValue(status);
                        break;
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return arr.size();
    }
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivProfile;
        public TextView tv_name;
        public TextView tv_rollno;
        public RadioGroup radioGroup1;
        public RadioButton btn_pre,btn_abs;
        public String name,datedata;

        Calendar calander;
        SimpleDateFormat simpledateformat;

        public DatabaseReference db_refprevious;
        public FirebaseDatabase db_instance;
        public String child_name,child_rollno;
        public  ArrayList<String> list_stud;


        public MyViewHolder(View itemView) {
            super(itemView);

            ivProfile = (ImageView) itemView.findViewById(R.id.ivProfile);
            tv_name = (TextView) itemView.findViewById(R.id.tv_Name);
            tv_rollno = (TextView) itemView.findViewById(R.id.tv_Rollno);
            btn_pre = (RadioButton) itemView.findViewById(R.id.radio_present);
            btn_abs = (RadioButton) itemView.findViewById(R.id.radio_absent);
            radioGroup1 = (RadioGroup) itemView.findViewById(R.id.radioGroup1);
            db_instance = FirebaseDatabase.getInstance();
            db_refprevious=db_instance.getReference("AttendanceList");
            calander = Calendar.getInstance();
            simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
            datedata = simpledateformat.format(calander.getTime()).toString();
            list_stud = new ArrayList<>();


        }
    }
}

