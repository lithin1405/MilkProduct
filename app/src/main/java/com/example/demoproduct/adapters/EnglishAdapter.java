package com.example.demoproduct.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.demoproduct.R;
import com.example.demoproduct.activities.InvoicePrintActivity;
import com.example.demoproduct.activities.ReportsInvoiceActivity;
import com.example.demoproduct.model.CustomerReports;

import java.util.ArrayList;

public class EnglishAdapter extends RecyclerView.Adapter<EnglishAdapter.ViewHolder> {


    private LayoutInflater mInflater;
    private ArrayList<CustomerReports> totaldatalist;
    private CustomerReports topicList;
    private Context mcontext;


    // data is passed into the constructor
    public EnglishAdapter(Context context, ArrayList<CustomerReports> totaldatalist) {
        this.totaldatalist = totaldatalist;
        this.mInflater = LayoutInflater.from(context);
        this.mcontext=context;
    }

    // inflates the row layout from xml when needed
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.english_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        topicList=totaldatalist.get(position);
        holder.quantity.setText(String.valueOf(topicList.getQty()));
        holder.invoiceno.setText(String.valueOf(topicList.getInvNo()));
        holder.invoicedate.setText(String.valueOf(topicList.getInvDate()));
        holder.customername.setText(String.valueOf(topicList.getCustName()));
        holder.commission.setText(String.valueOf(topicList.getComm()));
        holder.netamount.setText(String.valueOf(topicList.getNetAmount()));

        holder.invoiceno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topicList=totaldatalist.get(position);
                Log.v("","topicList.getInvNo()"+topicList.getInvNo());
                Intent intent=new Intent(mcontext, ReportsInvoiceActivity.class);
                intent.putExtra("InvoiceNo",topicList.getInvNo());
                mcontext.startActivity(intent);

            }
        });

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return totaldatalist.size();
    }


    // stores and recycles views as they are scrolled off screen
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView invoiceno,invoicedate,customername,commission,netamount,quantity;

        ViewHolder(View itemView) {
            super(itemView);
            invoiceno = itemView.findViewById(R.id.invoiceno);
            invoicedate = itemView.findViewById(R.id.invoicedate);
            customername = itemView.findViewById(R.id.customername);
            commission = itemView.findViewById(R.id.commission);
            netamount = itemView.findViewById(R.id.netamount);
            quantity = itemView.findViewById(R.id.quantity);

        }


    }


}