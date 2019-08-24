package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.utils.DatePickerUtls;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReportsActivity extends AppCompatActivity {
    int mdYear;
    int mdMonth;
    int mdDay;
    String strDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        //Start Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        ImageView backArrow = toolbar.findViewById(R.id.toolbar_back_arrow);
        title.setText("Reports");
        title.setPadding(0, 0, 60, 0);
        ImageView logout=(ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReportsActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Alert");
                dialog.setMessage("Are you sure you want to Logout?" );
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                })
                        .setNegativeButton("No ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                                dialog.dismiss();
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });
        AppCompatButton datefrom= findViewById(R.id.datefrom);
        AppCompatButton todate= findViewById(R.id.todate);
        AppCompatButton generatereport= findViewById(R.id.generatereport);
        final AppCompatTextView selectedfromdate= findViewById(R.id.selectedfromdate);
        final AppCompatTextView selecteddateto= findViewById(R.id.selecteddateto);
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strDate = sdf.format(c.getTime());
        selectedfromdate.setText(""+strDate);
        selecteddateto.setText(""+strDate);
        datefrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mdYear = c1.get(Calendar.YEAR);
                mdMonth = c1.get(Calendar.MONTH);
                mdDay = c1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd2 = new DatePickerDialog(ReportsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                                selectedfromdate.setText(DatePickerUtls.convert(year, monthOfYear, dayOfMonth));

                            }
                        }, mdYear, mdMonth, mdDay);
                try{
                    Log.v("","Welcome Back");
                    dpd2.getDatePicker().setMinDate(getDateFromString("2019-01-01").getTime());
                    dpd2.getDatePicker().setMaxDate(getDateFromString("2030-01-01").getTime());


                }catch (NullPointerException n){
                    n.printStackTrace();
                }
                dpd2.show();
            }
        });
        todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c1 = Calendar.getInstance();
                mdYear = c1.get(Calendar.YEAR);
                mdMonth = c1.get(Calendar.MONTH);
                mdDay = c1.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd2 = new DatePickerDialog(ReportsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {

                                selecteddateto.setText(DatePickerUtls.convert(year, monthOfYear, dayOfMonth));

                            }
                        }, mdYear, mdMonth, mdDay);
                try{
                    Log.v("","Welcome Back");
                    dpd2.getDatePicker().setMinDate(getDateFromString("2019-01-01").getTime());
                    dpd2.getDatePicker().setMaxDate(getDateFromString("2030-01-01").getTime());


                }catch (NullPointerException n){
                    n.printStackTrace();
                }
                dpd2.show();
            }
        });
        generatereport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Fromdate=selectedfromdate.getText().toString().trim();
                String Todate=selecteddateto.getText().toString().trim();
                if (Fromdate.equalsIgnoreCase("")||Todate.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Please Select Dates",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(getApplicationContext(),ReportsDataActivity.class);
                    intent.putExtra("Fromdate",Fromdate);
                    intent.putExtra("Todate",Todate);
                    startActivity(intent);
                }


            }
        });
    }
    private Date getDateFromString(String dateStr){
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
