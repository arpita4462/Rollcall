package com.atrio.rollcall;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.atrio.rollcall.model.StudentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DailyReport extends AppCompatActivity {
    private static final String TAG = "Data";
    TextView daily_tv;
    private DatabaseReference db_ref,db_ref2;
    private FirebaseDatabase db_instance;
    Intent iget;
    String section,studclass,date,subclass;
    Calendar calander;
    SimpleDateFormat simpledateformat;
    ArrayList<String> rolldata,roll_eng,roll_math,roll_science,roll_gen,roll_hindi;
    Workbook wb;
    Cell c;
    CellStyle cs;
    Sheet sheet1;
    FileOutputStream outStream;
    File sdcard,dir,file;
    String[] permissions;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        daily_tv=(TextView)findViewById(R.id.dailyrep_tv);


        db_instance = FirebaseDatabase.getInstance();
        db_ref = db_instance.getReference("AttendanceList");
        db_ref2 = db_instance.getReference("StudentList");

        iget = getIntent();
        studclass = iget.getStringExtra("studclass");
        section = iget.getStringExtra("section");
        calander = Calendar.getInstance();
        simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        date = simpledateformat.format(calander.getTime());
//        date="17-05-2017";
        rolldata = new ArrayList<String>();
        roll_eng = new ArrayList<>();
        roll_math = new ArrayList<>();
        roll_science = new ArrayList<>();
        roll_gen = new ArrayList<>();
        roll_hindi = new ArrayList<>();
        rolldata.add("RollNo");
        roll_math.add("Maths");
        roll_eng.add("English");
        roll_science.add("Science");
        roll_hindi.add("Hindi");
        roll_gen.add("General Knowledge");

        dialog = new ProgressDialog(DailyReport.this);
        dialog.setMessage("Please Wait....");
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
        permissions = new String[]{
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        checkPermissions();
        subclass = studclass.substring(studclass.indexOf("-") + 1, studclass.length());
        Log.i("subclass",""+subclass);


        //New Workbook
        wb = new HSSFWorkbook();

        c = null;

        //Cell style for header row
        cs = wb.createCellStyle();
        cs.setFillForegroundColor(HSSFColor.LIME.index);
        cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        //New Sheet
        sheet1 = null;
        sheet1 = wb.createSheet(date);
//        sheet1.setDefaultRowHeight((short) 25);
//        sheet1.getRow(0).getCell(0).setCellStyle(cs);

        sdcard = Environment.getExternalStorageDirectory();

        dir = new File(sdcard.getAbsolutePath() + "/RollCall/");
        Log.i("Dir44",""+dir);
        dir.mkdir();
        file = new File(dir,"Attendance-"+subclass+"-"+date+".xls");
        Log.i("Dir44",""+file);

        outStream = null;

        final Query queryRef = db_ref2.child(studclass).child(section).orderByKey();
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Data", Toast.LENGTH_LONG).show();
//
                }else{

                    queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data : dataSnapshot.getChildren()){
                                StudentUser post = data.getValue(StudentUser.class);

                                rolldata.add(post.getRollno());
                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                saveExcelFile(rolldata, "0");




                            }




                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Query  data_eng = db_ref.child(studclass).child(section).child("English").child(date).orderByKey();
                    data_eng.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                String status = data.getValue().toString();
                                roll_eng.add(status);



                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                updateExcelFile(roll_eng,"1");


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Query dailydata = db_ref.child(studclass).child(section).child("Maths").child(date).orderByKey();

                    dailydata.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                String status = data.getValue().toString();
                                roll_math.add(status);

                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                updateExcelFile(roll_math,"2");


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Query daily_hindi = db_ref.child(studclass).child(section).child("Hindi").child(date).orderByKey();

                    daily_hindi.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                String status = data.getValue().toString();
                                roll_hindi.add(status);



                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                updateExcelFile(roll_hindi,"3");


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Query daily_science = db_ref.child(studclass).child(section).child("Science").child(date).orderByKey();

                    daily_science.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                String status = data.getValue().toString();
                                roll_science.add(status);



                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                updateExcelFile(roll_science,"4");


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Query daily_gen = db_ref.child(studclass).child(section).child("General Knowledge").child(date).orderByKey();

                    daily_gen.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot data: dataSnapshot.getChildren()){
                                String status = data.getValue().toString();
                                //Log.i("DataValue22",""+data.getValue());
                                roll_gen.add(status);



                            }

                            String state = Environment.getExternalStorageState();
                            if (Environment.MEDIA_MOUNTED.equals(state)) {

                                updateExcelFile(roll_gen,"5");


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });






                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        dialog.show();





    }


    private boolean updateExcelFile(ArrayList<String> roll_eng, String s) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }
        boolean success = false;

        FileInputStream file = null;
        try {
            file = new FileInputStream(new File("/storage/emulated/0/RollCall/Attendance-"+subclass+"-"+date+".xls"));
            Log.i("outfile2",""+file);
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Cell cell;

            cs = workbook.createCellStyle();
            cs.setFillForegroundColor(HSSFColor.LIME.index);
            cs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            Log.i("sheet77",""+sheet);
            if (s.equals("1")){

                for (int j =0; j< roll_eng.size();j++){
                    //Log.i("c",""+roll_eng.get(j));
                    Row row = sheet.getRow(j);
                    cell = row.createCell(1);
                    cell.setCellValue(roll_eng.get(j));
                    sheet.getRow(0).getCell(1).setCellStyle(cs);
                    sheet.setColumnWidth(1, (15 * 200));
                }
//


            }

            if (s.equals("2")){

                for (int j =0; j< roll_eng.size();j++){
                    Log.i("c",""+roll_eng.get(j));
                    Row row = sheet.getRow(j);
                    cell = row.createCell(2);
                    cell.setCellValue(roll_eng.get(j));
                    sheet.getRow(0).getCell(2).setCellStyle(cs);
                    sheet.setColumnWidth(2, (15 * 200));
                }


            }

            if (s.equals("3")) {

                for (int j = 0; j < roll_eng.size(); j++) {
                    // Log.i("c", "" + roll_eng.get(j));
                    Row row = sheet.getRow(j);
                    cell = row.createCell(3);
                    cell.setCellValue(roll_eng.get(j));
                    sheet.getRow(0).getCell(3).setCellStyle(cs);
                    sheet.setColumnWidth(3, (15 * 200));
                }
            }

            if (s.equals("4")) {

                for (int j = 0; j < roll_eng.size(); j++) {
                    //Log.i("c", "" + roll_eng.get(j));
                    Row row = sheet.getRow(j);
                    cell = row.createCell(4);
                    cell.setCellValue(roll_eng.get(j));
                    sheet.getRow(0).getCell(4).setCellStyle(cs);
                    sheet.setColumnWidth(4, (15 * 200));
                }
            }

            if (s.equals("5")) {

                for (int j = 0; j < roll_eng.size(); j++) {
                    //Log.i("c", "" + roll_eng.get(j));
                    Row row = sheet.getRow(j);
                    cell = row.createCell(5);
                    cell.setCellValue(roll_eng.get(j));
                    sheet.getRow(0).getCell(5).setCellStyle(cs);
                    sheet.setColumnWidth(5, (15 *300));
                }
            }




            file.close();
            FileOutputStream outFile =new FileOutputStream(new File("/storage/emulated/0/RollCall/Attendance-"+subclass+"-"+date+".xls"));
            Log.i("outfile",""+outFile);
            workbook.write(outFile);
            outFile.close();

            Log.w("FileUtils", "Writing file" + outFile);

            success = true;
            File myFile = new File("/storage/emulated/0/RollCall/Attendance-"+subclass+"-"+date+".xls");
            Uri myUri = Uri.fromFile(myFile);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(myUri, "application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                Toast.makeText(DailyReport.this, "No Application Available to View Excel", Toast.LENGTH_SHORT).show();
            }


            dialog.dismiss();
            daily_tv.setText("See Your Daily Report at:"+"/storage/emulated/0/RollCall/Attendance-"+subclass+"-"+date+".xls");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }


    private  boolean saveExcelFile(ArrayList<String> rolldata, String s) {

        // Log.e("Hiii77", "Storage not available or read only");
        // check if available and not read only
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        boolean success = false;


        for (int i =0; i< rolldata.size();i++){
            Log.i("c",""+rolldata.get(i));
            Row row = sheet1.createRow(i);
            c = row.createCell(0);
            c.setCellValue(rolldata.get(i));
            sheet1.getRow(0).getCell(0).setCellStyle(cs);
            sheet1.setColumnWidth(0, (15* 200));

        }


        Log.i("Hii","Executing");
        // get the path to sdcard


        try {
            outStream = new FileOutputStream(file);
            wb.write(outStream);

            Log.w("FileUtils", "Writing file" + file);
            success = true;
        } catch (IOException e) {
            Log.w("FileUtils", "Error writing " + file, e);
        } catch (Exception e) {
            Log.w("FileUtils", "Failed to save file", e);
        } finally {
            try {
                if (null != outStream)
                    outStream.close();
            } catch (Exception ex) {
            }
        }
        return success;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

}

