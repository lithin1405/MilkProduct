package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.database.DatabaseHelper;
import com.example.demoproduct.model.Customers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellProductsActivity extends AppCompatActivity {
    private AppCompatEditText mstdprice,mtmprice,mgoldprice,mdtmprice,mvijsmprice,mmmprice,mmodsmprice,mcmprice,
            mhunmlcups,mscurdprice,mfourcups,mbcurdprice,mbmilkprice,mlassiprice,midlyprice,mdosaprice;
    private AppCompatEditText vstdprice,vtmprice,vgoldprice,vdtmprice,vvijsmprice,vmmprice,vmodsmprice,vcmprice,
            vhunmlcups,vscurdprice,vfourcups,vbcurdprice,vbmilkprice,vlassiprice,vidlyprice,vdosaprice;
    private AppCompatTextView stdprice,tmprice,goldprice,dtmprice,vijsmprice,mmprice,modsmprice,cmprice,hunmlcups
            ,scurdprice,fourcups,bcurdprice,bmilkprice,lassiprice,idlyprice,dosaprice;
    private AppCompatTextView totsstdprice,tottmprice,totgoldprice,totdtmprice,totvijsmprice,totmmprice,totmodsmprice,totcmprice,
    tothuncups,totvscurdprice,totfourcups,totbcurdprice,totbmilkprice,totlassiprice,totalidlyprice,totaldosaprice;
    private AppCompatTextView tvtotal;
    private AppCompatButton appCompatButtonPreview,appCompatButtoncancel;
    private AppCompatAutoCompleteTextView autocustomernames;
//    Commission=0.00;CustomerId=18;CustomerName=SRINU;TMCommission=0.00
    ArrayList<String> CommissionList;
    ArrayList<String> CustomerIdList;
    ArrayList<String> CustomerNameList ;
    ArrayList<String> TMCommissionList;
    private DatabaseHelper databaseHelper;
    private Customers user;
    List<Customers> customeridlist;
    String Customerid="";
    String CustomerName="";
    String Commission="";
    String TMCommission="";
    List<Customers> customerslist;
    ArrayList<Customers> customersnameslist;
    private ArrayAdapter<String> branchadapter;
    Customers fbranch;
    HashMap<String, Customers> hmFundingBranch;
    ArrayList<String>  fundbranchlist=new ArrayList<String>();
    ArrayList<Customers> branchlist;
    public List<String> fundingbranchlist = new ArrayList<String>();




    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_products);
        //Start Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView backArrow =(ImageView) toolbar.findViewById(R.id.toolbar_back_arrow);
        title.setText("Sell Products");
        title.setPadding(0, 0, 60, 0);
        ImageView logout=(ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(SellProductsActivity.this);
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
        SharedPreferences sp = getApplicationContext().getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
        int AgentId = sp.getInt("AgentId", 0);
        Log.v("","AgentId"+AgentId);
        String AgentName=sp.getString("AgentName",null);
        Log.v("","AgentName"+AgentName);
        CommissionList=new ArrayList<>();
        CustomerIdList=new ArrayList<>();
        CustomerNameList=new ArrayList<>();
        TMCommissionList=new ArrayList<>();
        customeridlist=new ArrayList<>();
        customerslist=new ArrayList<>();
        customersnameslist=new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        user = new Customers();
        String Mstd = getIntent().getStringExtra("Mstd");
        String Vstd = getIntent().getStringExtra("Vstd");
        String Totstd = getIntent().getStringExtra("Totstd");
        String Mtm = getIntent().getStringExtra("Mtm");
        String Vtm = getIntent().getStringExtra("Vtm");
        String Tottm = getIntent().getStringExtra("Tottm");
        String Mgold = getIntent().getStringExtra("Mgold");
        String Vgold = getIntent().getStringExtra("Vgold");
        String Totgold = getIntent().getStringExtra("Totgold");
        String Mdtm = getIntent().getStringExtra("Mdtm");
        String Vdtm = getIntent().getStringExtra("Vdtm");
        String Totdtm = getIntent().getStringExtra("Totdtm");
        String Mvij = getIntent().getStringExtra("Mvij");
        String Vvij = getIntent().getStringExtra("Vvij");
        String TotVij = getIntent().getStringExtra("TotVij");
        String Mmm = getIntent().getStringExtra("Mmm");
        String Vmm = getIntent().getStringExtra("Vmm");
        String Totvm = getIntent().getStringExtra("Totvm");
        String Mmodel = getIntent().getStringExtra("Mmodel");
        String Vmodel = getIntent().getStringExtra("Vmodel");
        String Totmodel = getIntent().getStringExtra("Totmodel");
        String Mcow = getIntent().getStringExtra("Mcow");
        String Vcow = getIntent().getStringExtra("Vcow");
        String TotCow = getIntent().getStringExtra("TotCow");
        final String Mhun = getIntent().getStringExtra("Mhun");
        String Vhun = getIntent().getStringExtra("Vhun");
        String Tothun = getIntent().getStringExtra("Tothun");
        String Mcurd = getIntent().getStringExtra("Mcurd");
        String Vcurd = getIntent().getStringExtra("Vcurd");
        String Totcurd = getIntent().getStringExtra("Totcurd");
        final String Mfour = getIntent().getStringExtra("Mfour");
        String Vfour = getIntent().getStringExtra("Vfour");
        String Totfour = getIntent().getStringExtra("Totfour");
        final String Mbcurd = getIntent().getStringExtra("Mbcurd");
        String Vbcurd = getIntent().getStringExtra("Vbcurd");
        String Totbcurd = getIntent().getStringExtra("Totbcurd");
        String Mbmilk = getIntent().getStringExtra("Mbmilk");
        String Vbmilk = getIntent().getStringExtra("Vbmilk");
        String Totbmilk = getIntent().getStringExtra("Totbmilk");
        String Mlassi = getIntent().getStringExtra("Mlassi");
        String Vlassi = getIntent().getStringExtra("Vlassi");
        String TotLassi = getIntent().getStringExtra("TotLassi");
        String Midly = getIntent().getStringExtra("Midly");
        String Vidly = getIntent().getStringExtra("Vidly");
        String TotIdly = getIntent().getStringExtra("TotIdly");
        String Mdosa = getIntent().getStringExtra("Mdosa");
        String Vdosa = getIntent().getStringExtra("Vdosa");
        String TotDosa = getIntent().getStringExtra("TotDosa");
        String TotalAmount = getIntent().getStringExtra("TotalAmount");
        Customerid = getIntent().getStringExtra("Customerid");
        CustomerName = getIntent().getStringExtra("CustomerName");
        Commission = getIntent().getStringExtra("Commission");
        TMCommission = getIntent().getStringExtra("TMCommission");


         appCompatButtonPreview=(AppCompatButton)findViewById(R.id.appCompatButtonPreview);
         autocustomernames=(AppCompatAutoCompleteTextView) findViewById(R.id.autocustomernames);

          mstdprice=(AppCompatEditText)findViewById(R.id.mstdprice);
          vstdprice=(AppCompatEditText)findViewById(R.id.vstdprice);

         mtmprice=(AppCompatEditText)findViewById(R.id.mtmprice);
         vtmprice=(AppCompatEditText)findViewById(R.id.vtmprice);

         mgoldprice=(AppCompatEditText)findViewById(R.id.mgoldprice);
         vgoldprice=(AppCompatEditText)findViewById(R.id.vgoldprice);

         mdtmprice=(AppCompatEditText)findViewById(R.id.mdtmprice);
         vdtmprice=(AppCompatEditText)findViewById(R.id.vdtmprice);

         mvijsmprice=(AppCompatEditText)findViewById(R.id.mvijsmprice);
         vvijsmprice=(AppCompatEditText)findViewById(R.id.vvijsmprice);

         mmmprice=(AppCompatEditText)findViewById(R.id.mmmprice);
         vmmprice=(AppCompatEditText)findViewById(R.id.vmmprice);

         mmodsmprice=(AppCompatEditText)findViewById(R.id.mmodsmprice);
         vmodsmprice=(AppCompatEditText)findViewById(R.id.vmodsmprice);

         mcmprice=(AppCompatEditText)findViewById(R.id.mcmprice);
         vcmprice=(AppCompatEditText)findViewById(R.id.vcmprice);

         mhunmlcups=(AppCompatEditText)findViewById(R.id.mhunmlcups);
         vhunmlcups=(AppCompatEditText)findViewById(R.id.vhunmlcups);

         mscurdprice=(AppCompatEditText)findViewById(R.id.mscurdprice);
         vscurdprice=(AppCompatEditText)findViewById(R.id.vscurdprice);

         mfourcups=(AppCompatEditText)findViewById(R.id.mfourcups);
         vfourcups=(AppCompatEditText)findViewById(R.id.vfourcups);

         mbcurdprice=(AppCompatEditText)findViewById(R.id.mbcurdprice);
         vbcurdprice=(AppCompatEditText)findViewById(R.id.vbcurdprice);

         mbmilkprice=(AppCompatEditText)findViewById(R.id.mbmilkprice);
         vbmilkprice=(AppCompatEditText)findViewById(R.id.vbmilkprice);

         mlassiprice=(AppCompatEditText)findViewById(R.id.mlassiprice);
         vlassiprice=(AppCompatEditText)findViewById(R.id.vlassiprice);

        midlyprice=(AppCompatEditText)findViewById(R.id.midlyprice);
        vidlyprice=(AppCompatEditText)findViewById(R.id.vidlyprice);

        mdosaprice=(AppCompatEditText)findViewById(R.id.mdosaprice);
        vdosaprice=(AppCompatEditText)findViewById(R.id.vdosaprice);

        stdprice=(AppCompatTextView)findViewById(R.id.stdprice);
        totsstdprice=(AppCompatTextView)findViewById(R.id.totsstdprice);

         tmprice=(AppCompatTextView)findViewById(R.id.tmprice);
         tottmprice=(AppCompatTextView)findViewById(R.id.tottmprice);

         goldprice=(AppCompatTextView)findViewById(R.id.goldprice);
         totgoldprice=(AppCompatTextView)findViewById(R.id.totgoldprice);

         dtmprice=(AppCompatTextView)findViewById(R.id.dtmprice);
         totdtmprice=(AppCompatTextView)findViewById(R.id.totdtmprice);

         vijsmprice=(AppCompatTextView)findViewById(R.id.vijsmprice);
         totvijsmprice=(AppCompatTextView)findViewById(R.id.totvijsmprice);

         mmprice=(AppCompatTextView)findViewById(R.id.mmprice);
         totmmprice=(AppCompatTextView)findViewById(R.id.totmmprice);

         modsmprice=(AppCompatTextView)findViewById(R.id.modsmprice);
         totmodsmprice=(AppCompatTextView)findViewById(R.id.totmodsmprice);

         cmprice=(AppCompatTextView)findViewById(R.id.cmprice);
         totcmprice=(AppCompatTextView)findViewById(R.id.totcmprice);

         hunmlcups=(AppCompatTextView)findViewById(R.id.hunmlcups);
         tothuncups=(AppCompatTextView)findViewById(R.id.tothuncups);

         scurdprice=(AppCompatTextView)findViewById(R.id.scurdprice);
         totvscurdprice=(AppCompatTextView)findViewById(R.id.totvscurdprice);

         fourcups=(AppCompatTextView)findViewById(R.id.fourcups);
         totfourcups=(AppCompatTextView)findViewById(R.id.totfourcups);

         bcurdprice=(AppCompatTextView)findViewById(R.id.bcurdprice);
         totbcurdprice=(AppCompatTextView)findViewById(R.id.totbcurdprice);

         bmilkprice=(AppCompatTextView)findViewById(R.id.bmilkprice);
         totbmilkprice=(AppCompatTextView)findViewById(R.id.totbmilkprice);

         lassiprice=(AppCompatTextView)findViewById(R.id.lassiprice);
         totlassiprice=(AppCompatTextView)findViewById(R.id.totlassiprice);

        idlyprice=(AppCompatTextView)findViewById(R.id.idlyprice);
        totalidlyprice=(AppCompatTextView)findViewById(R.id.totalidlyprice);

        dosaprice=(AppCompatTextView)findViewById(R.id.dosaprice);
        totaldosaprice=(AppCompatTextView)findViewById(R.id.totaldosaprice);

        tvtotal=(AppCompatTextView)findViewById(R.id.tvtotal);

        appCompatButtoncancel=(AppCompatButton)findViewById(R.id.appCompatButtoncancel);

        appCompatButtoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        branchlist=databaseHelper.getAllCustomers();

        autocustomernames.setThreshold(1);
        autocustomernames.setTag(-1);

        if (Mstd!=null){
            mstdprice.setFocusable(false);
            mstdprice.setText(Mstd);
        }
        if (Vstd!=null){
            vstdprice.setFocusable(false);
            vstdprice.setText(Vstd);
        }

        if (Totstd!=null){
            totsstdprice.setText(Totstd);
        }

        if (Mtm!=null){
            mtmprice.setFocusable(false);
            mtmprice.setText(Mtm);
        }
        if (Vtm!=null){
            vtmprice.setFocusable(false);
            vtmprice.setText(Vtm);
        }
        if (Tottm!=null){
            tottmprice.setText(Tottm);
        }

        if (Mgold!=null){
            mgoldprice.setFocusable(false);
            mgoldprice.setText(Mgold);
        }
        if (Vgold!=null){
            vgoldprice.setFocusable(false);
            vgoldprice.setText(Vgold);
        }
        if (Totgold!=null){
            totgoldprice.setText(Totgold);
        }

        if (Mdtm!=null){
            mdtmprice.setFocusable(false);
            mdtmprice.setText(Mdtm);
        }
        if (Vdtm!=null){
            vdtmprice.setFocusable(false);
            vdtmprice.setText(Vdtm);
        }
        if (Totdtm!=null){
            totdtmprice.setText(Totdtm);
        }
        if (Mvij!=null){
            mvijsmprice.setFocusable(false);
            mvijsmprice.setText(Mvij);
        }
        if (Vvij!=null){
            vvijsmprice.setFocusable(false);
            vvijsmprice.setText(Vvij);
        }
        if (TotVij!=null){
            totvijsmprice.setText(TotVij);
        }

        if (Mmm!=null){
            mmmprice.setFocusable(false);
            mmmprice.setText(Mmm);
        }

        if (Vmm!=null){
            vmmprice.setFocusable(false);
            vmmprice.setText(Vmm);
        }
        if (Totvm!=null){
            totmmprice.setText(Totvm);
        }

        if (Mmodel!=null){
            mmodsmprice.setFocusable(false);
            mmodsmprice.setText(Mmodel);
        }
        if (Vmodel!=null){
            vmodsmprice.setFocusable(false);
            vmodsmprice.setText(Vmodel);
        }
        if (Totmodel!=null){
            totmodsmprice.setText(Totmodel);
        }

        if (Mcow!=null){
            mcmprice.setFocusable(false);
            mcmprice.setText(Mcow);
        }
        if (Vcow!=null){
            vcmprice.setFocusable(false);
            vcmprice.setText(Vcow);
        }
        if (TotCow!=null){
            totcmprice.setText(TotCow);
        }

        if (Mhun!=null){
            mhunmlcups.setFocusable(false);
            mhunmlcups.setText(Mhun);
        }

        if (Vhun!=null){
            vhunmlcups.setFocusable(false);
            vhunmlcups.setText(Vhun);
        }
        if (Tothun!=null){
            tothuncups.setText(Tothun);
        }
        if (Mcurd!=null){
            mscurdprice.setFocusable(false);
            mscurdprice.setText(Mcurd);
        }
        if (Vcurd!=null){
            vscurdprice.setFocusable(false);
            vscurdprice.setText(Vcurd);
        }

        if (Totcurd!=null){
            totvscurdprice.setText(Totcurd);
        }
        if (Mfour!=null){
            mfourcups.setFocusable(false);
            mfourcups.setText(Mfour);
        }
        if (Vfour!=null){
            vfourcups.setFocusable(false);
            vfourcups.setText(Vfour);
        }
        if (Totfour!=null){
            totfourcups.setText(Totfour);
        }
        if (Mbcurd!=null){
            mbcurdprice.setFocusable(false);
            mbcurdprice.setText(Mbcurd);
        }

        if (Vbcurd!=null){
            vbcurdprice.setFocusable(false);
            vbcurdprice.setText(Vbcurd);
        }
        if (Totbcurd!=null){
            totbcurdprice.setText(Totbcurd);
        }
        if (Mbmilk!=null){
            mbmilkprice.setFocusable(false);
            mbmilkprice.setText(Mbmilk);
        }
        if (Vbmilk!=null){
            vbmilkprice.setFocusable(false);
            vbmilkprice.setText(Vbmilk);
        }
        if (Totbmilk!=null){
            totbmilkprice.setText(Totbmilk);
        }

        if (Mlassi!=null){
            mlassiprice.setFocusable(false);
            mlassiprice.setText(Mlassi);
        }
        if (Vlassi!=null){
            vlassiprice.setFocusable(false);
            vlassiprice.setText(Vlassi);
        }
        if (TotLassi!=null){
            totlassiprice.setText(TotLassi);
        }
        if (Midly!=null){
            midlyprice.setFocusable(false);
            midlyprice.setText(Midly);
        }
        if (Vidly!=null){
            vidlyprice.setFocusable(false);
            vidlyprice.setText(Vidly);
        }
        if (TotIdly!=null){
            totalidlyprice.setText(TotIdly);
        }

        if (Mdosa!=null){
            mdosaprice.setFocusable(false);
            mdosaprice.setText(Mdosa);
        }
        if (Vdosa!=null){
            vdosaprice.setFocusable(false);
            vdosaprice.setText(Vdosa);
        }
        if (TotDosa!=null){
            totaldosaprice.setText(TotDosa);
        }
        if (TotalAmount!=null){
            if (TotalAmount.contains("Total Cost:")){
                tvtotal.setText(""+TotalAmount);
            }else {
                tvtotal.setText("Total Cost:"+TotalAmount);
            }

        }

        if (CustomerName!=null){
//            ArrayList<String> customlist=new ArrayList<>();
//            customlist.add(CustomerName);
//            customlist.add(Customerid);
            autocustomernames.setText(CustomerName);
            final ArrayAdapter<String> adapters = new ArrayAdapter<>(
                    this,android.R.layout.simple_list_item_1 ,fundbranchlist);
            // Setting Adapter to the Spinner
            autocustomernames.setAdapter(adapters);
//            autocustomernames.setEnabled(false);
//            autocustomernames.setClickable(false);
        }else {
            ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
                customerslist=databaseHelper.getAllCustomers();
                if (customerslist.size()==0){
                    new DownloadTask().execute();
                    // notify user you are online
                }else {
                    customersnameslist=databaseHelper.getAllCustomersNamesList();
                    for (int i = 0; i < customersnameslist.size(); i++) {
                        try
                        {
                            Customers vlead = customersnameslist.get(i);
                            String name=vlead.getCustomerName();
                            CustomerNameList.add(name);
                        }catch(IndexOutOfBoundsException in)
                        {
                            in.printStackTrace();
                        }
                    }
                    final ArrayAdapter<String> adapters = new ArrayAdapter<>(
                            this,android.R.layout.simple_list_item_1 ,CustomerNameList);
                    // Setting Adapter to the Spinner
                    autocustomernames.setAdapter(adapters);
                }
            }
            else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                // notify user you are not online
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
            }
        }






        appCompatButtonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fundbranchlist.size() == 0 ||fundbranchlist.indexOf(autocustomernames.getText().toString()) == -1) {
                    autocustomernames.setText("");
                    autocustomernames.setError("Invalid Name.");
                    Toast.makeText(getApplicationContext(),"Enter Customer name",Toast.LENGTH_SHORT).show();
                }else {
                    String Mstd=mstdprice.getText().toString();
                    String Vstd=vstdprice.getText().toString();
                    String Totstd=totsstdprice.getText().toString();
                    String Mtm=mtmprice.getText().toString();
                    String Vtm=vtmprice.getText().toString();
                    String Tottm=tottmprice.getText().toString();
                    String Mgold=mgoldprice.getText().toString();
                    String Vgold=vgoldprice.getText().toString();
                    String Totgold=totgoldprice.getText().toString();
                    String Mdtm=mdtmprice.getText().toString();
                    String Vdtm=vdtmprice.getText().toString();
                    String Totdtm=totdtmprice.getText().toString();
                    String Mvij=mvijsmprice.getText().toString();
                    String Vvij=vvijsmprice.getText().toString();
                    String TotVij=totvijsmprice.getText().toString();
                    String Mmm=mmmprice.getText().toString();
                    String Vmm=vmmprice.getText().toString();
                    String Totvm=totmmprice.getText().toString();
                    String Mmodel=mmodsmprice.getText().toString();
                    String Vmodel=vmodsmprice.getText().toString();
                    String Totmodel=totmodsmprice.getText().toString();
                    String Mcow=mcmprice.getText().toString();
                    String Vcow=vcmprice.getText().toString();
                    String TotCow=totcmprice.getText().toString();
                    String Mhun=mhunmlcups.getText().toString();
                    String Vhun=vhunmlcups.getText().toString();
                    String Tothun=tothuncups.getText().toString();
                    String Mcurd=mscurdprice.getText().toString();
                    String Vcurd=vscurdprice.getText().toString();
                    String Totcurd=totvscurdprice.getText().toString();
                    String Mfour=mfourcups.getText().toString();
                    String Vfour=vfourcups.getText().toString();
                    String Totfour=totfourcups.getText().toString();
                    String Mbcurd=mbcurdprice.getText().toString();
                    String Vbcurd=vbcurdprice.getText().toString();
                    String Totbcurd=totbcurdprice.getText().toString();
                    String Mbmilk=mbmilkprice.getText().toString();
                    String Vbmilk=vbmilkprice.getText().toString();
                    String Totbmilk=totbmilkprice.getText().toString();
                    String Mlassi=mlassiprice.getText().toString();
                    String Vlassi=vlassiprice.getText().toString();
                    String TotLassi=totlassiprice.getText().toString();
                    String Midly=midlyprice.getText().toString();
                    String Vidly=vidlyprice.getText().toString();
                    String TotIdly=totalidlyprice.getText().toString();
                    String Mdosa=mdosaprice.getText().toString();
                    String Vdosa=vdosaprice.getText().toString();
                    String TotDosa=totaldosaprice.getText().toString();
                    String TotalAmount=tvtotal.getText().toString();

                    Intent intent=new Intent(getApplicationContext(),InvoiceActivity.class);
                    intent.putExtra("Mstd", Mstd);
                    intent.putExtra("Vstd", Vstd);
                    intent.putExtra("Totstd", Totstd);
                    intent.putExtra("Mtm", Mtm);
                    intent.putExtra("Vtm", Vtm);
                    intent.putExtra("Tottm", Tottm);
                    intent.putExtra("Mgold", Mgold);
                    intent.putExtra("Vgold", Vgold);
                    intent.putExtra("Totgold", Totgold);
                    intent.putExtra("Mdtm", Mdtm);
                    intent.putExtra("Vdtm", Vdtm);
                    intent.putExtra("Totdtm", Totdtm);
                    intent.putExtra("Mvij", Mvij);
                    intent.putExtra("Vvij", Vvij);
                    intent.putExtra("TotVij", TotVij);
                    intent.putExtra("Mmm", Mmm);
                    intent.putExtra("Vmm", Vmm);
                    intent.putExtra("Totvm", Totvm);
                    intent.putExtra("Mmodel", Mmodel);
                    intent.putExtra("Vmodel", Vmodel);
                    intent.putExtra("Totmodel", Totmodel);
                    intent.putExtra("Mcow", Mcow);
                    intent.putExtra("Vcow", Vcow);
                    intent.putExtra("TotCow", TotCow);
                    intent.putExtra("Mhun", Mhun);
                    intent.putExtra("Vhun", Vhun);
                    intent.putExtra("Tothun", Tothun);
                    intent.putExtra("Mcurd", Mcurd);
                    intent.putExtra("Vcurd", Vcurd);
                    intent.putExtra("Totcurd", Totcurd);
                    intent.putExtra("Mfour", Mfour);
                    intent.putExtra("Vfour", Vfour);
                    intent.putExtra("Totfour", Totfour);
                    intent.putExtra("Mbcurd", Mbcurd);
                    intent.putExtra("Vbcurd", Vbcurd);
                    intent.putExtra("Totbcurd", Totbcurd);
                    intent.putExtra("Mbmilk", Mbmilk);
                    intent.putExtra("Vbmilk", Vbmilk);
                    intent.putExtra("Totbmilk", Totbmilk);
                    intent.putExtra("Mlassi", Mlassi);
                    intent.putExtra("Vlassi", Vlassi);
                    intent.putExtra("TotLassi", TotLassi);
                    intent.putExtra("Midly", Midly);
                    intent.putExtra("Vidly", Vidly);
                    intent.putExtra("TotIdly", TotIdly);
                    intent.putExtra("Mdosa", Mdosa);
                    intent.putExtra("Vdosa", Vdosa);
                    intent.putExtra("TotDosa", TotDosa);
                    intent.putExtra("TotalAmount", TotalAmount);
                    intent.putExtra("Customerid",Customerid);
                    intent.putExtra("CustomerName",CustomerName);
                    intent.putExtra("Commission",Commission);
                    intent.putExtra("TMCommission",TMCommission);
                    Log.v("","CustomerName----->"+CustomerName);
                    Log.v("","Customerid----->"+Customerid);
                    startActivity(intent);
                }
            }
        });
        // Declaring an Adapter and initializing it to the data pump
//        autocustomernames.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (autocustomernames.isEnabled()){
//                    String data=autocustomernames.getText().toString(); // this is select particular item from list;
//                    Log.v("","data"+data);
//                    int position1=autocustomernames.get(); //
//                    Log.v("","position"+position1);
//                    customeridlist=databaseHelper.getCustomerid(position1+1);
//                    for (int k = 0; k < customeridlist.size(); k++) {
//                        Customers options = customeridlist.get(k);
//                        Customerid=options.getCustomerId();
//                        CustomerName=options.getCustomerName();
//                        Commission=options.getCommission();
//                        TMCommission=options.getTMCommission();
//                        Log.v("","Customerid222----->"+Customerid);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        autocustomernames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v("checkclickitem", "checkclickitem" + position);
                if(branchadapter!=null && !branchadapter.isEmpty())

                {
                    fbranch=	hmFundingBranch.get(branchadapter.getItem(position));

                    Customerid=fbranch.getCustomerId();

                    CustomerName=fbranch.getCustomerName();

                    Commission=fbranch.getCommission();
                    Log.v("","Commission"+Commission);

                    TMCommission=fbranch.getTMCommission();

                    Log.v("","TMCommission"+TMCommission);

                    String CustomerName=fbranch.getCustomerName();


                    Log.v("", " on Customerid"+Customerid);

                    Log.v("", " on CustomerName"+CustomerName);
                    autocustomernames.setText(CustomerName);
                }
            }
        });

        autocustomernames.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                    if (fundbranchlist.size() == 0 ||fundbranchlist.indexOf(autocustomernames.getText().toString()) == -1) {


                        autocustomernames.setText("");
                        autocustomernames.setError("Invalid Name.");
                    }
                }
            }
        });

        if(branchlist!=null && branchlist.size()>0)
        {
            String[] vistleadvalues = new String[branchlist.size()];

            hmFundingBranch = new HashMap<String, Customers>();

            for (int i = 0; i < branchlist.size(); i++) {
                Customers vlead = branchlist.get(i);
                vistleadvalues[i] = vlead.getCustomerName();

                fundbranchlist.add(vlead.getCustomerName());
                String FundingBranchId = vlead.getCustomerId();




                Log.v("Milk", "CustomerId"+FundingBranchId);
                Log.v("Milk", "getCustomerName"+vlead.getCustomerName());

                fundingbranchlist.add(FundingBranchId);
                hmFundingBranch.put(vlead.getCustomerId(), vlead);
                hmFundingBranch.put(vlead.getCustomerName(), vlead);

            }

            for (int i = 0; i < vistleadvalues.length; i++) {
                Log.v("village[i]", "village array" + vistleadvalues[i]);
            }

            if (vistleadvalues.length > 0) {

                branchadapter = new ArrayAdapter<String>(this, R.layout.mylistdata, vistleadvalues);
                autocustomernames.setAdapter(branchadapter);


            }

        }


        //S.T.D

        mstdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mstdpriceRequest();
            }
        });

        mstdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //do something
                    vstdprice.setText("0");
                }

            }
        });
        mstdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mstdprice.setFocusableInTouchMode(true);
                mstdprice.setFocusable(true);
            }
        });


        vstdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vstdpriceRequest();
            }
        });
        vstdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //do something
                    mstdprice.setText("0");
                }
            }
        });
        vstdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vstdprice.setFocusableInTouchMode(true);
                vstdprice.setFocusable(true);
            }
        });



        //T.M

        mtmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mtmpriceRequest();
            }
        });

        mtmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vtmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }
                }

            }
        });


        mtmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtmprice.setFocusableInTouchMode(true);
                mtmprice.setFocusable(true);
            }
        });

        vtmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vtmpriceRequest();
            }
        });
        vtmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mtmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }
                }

            }
        });

        vtmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vtmprice.setFocusableInTouchMode(true);
                vtmprice.setFocusable(true);
            }
        });

        //GOLD

        mgoldprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mgoldpriceRequest();
            }
        });

        mgoldprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vgoldprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }
                }
            }
        });

        mgoldprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mgoldprice.setFocusableInTouchMode(true);
                mgoldprice.setFocusable(true);
            }
        });

        vgoldprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vgoldpriceRequest();
            }
        });
        vgoldprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mgoldprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }
                }
            }
        });
        vgoldprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vgoldprice.setFocusableInTouchMode(true);
                vgoldprice.setFocusable(true);
            }
        });

        //D.T.M

        mdtmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mdtmpriceRequest();
            }
        });

        mdtmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vdtmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }
                }
            }
        });

        mdtmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdtmprice.setFocusableInTouchMode(true);
                mdtmprice.setFocusable(true);
            }
        });

        vdtmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vdtmpriceRequest();
            }
        });
        vdtmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mdtmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }
                }
            }
        });

        vdtmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vdtmprice.setFocusableInTouchMode(true);
                vdtmprice.setFocusable(true);
            }
        });

        //Vij.Small

        mvijsmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mvijsmpriceRequest();
            }
        });

        mvijsmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vvijsmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                }
            }
        });
        mvijsmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mvijsmprice.setFocusableInTouchMode(true);
                mvijsmprice.setFocusable(true);
            }
        });

        vvijsmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vvijsmpriceRequest();
            }
        });
        vvijsmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mvijsmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();
                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                }
            }
        });

        vvijsmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vvijsmprice.setFocusableInTouchMode(true);
                vvijsmprice.setFocusable(true);
            }
        });

        //M.M

        mmmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmmpriceRequest();
            }
        });

        mmmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vmmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }
                }
            }
        });

        mmmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmmprice.setFocusableInTouchMode(true);
                mmmprice.setFocusable(true);
            }
        });

        vmmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vmmpriceRequest();
            }
        });
        vmmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mmmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }
                }
            }
        });

        vmmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vmmprice.setFocusableInTouchMode(true);
                vmmprice.setFocusable(true);
            }
        });

        //Model Small

        mmodsmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mmodsmpriceRequest();
            }
        });

        mmodsmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vmodsmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }
                }
            }
        });

        mmodsmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mmodsmprice.setFocusableInTouchMode(true);
                mmodsmprice.setFocusable(true);
            }
        });

        vmodsmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vmodsmpriceRequest();
            }
        });
        vmodsmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mmodsmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }
                }
            }
        });

        vmodsmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vmodsmprice.setFocusableInTouchMode(true);
                vmodsmprice.setFocusable(true);
            }
        });

        //Cow Milk

        mcmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mcmpriceRequest();
            }
        });

        mcmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vcmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }
                }
            }
        });

        mcmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcmprice.setFocusableInTouchMode(true);
                mcmprice.setFocusable(true);
            }
        });

        vcmprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vcmpriceRequest();
            }
        });
        vcmprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mcmprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }
                }
            }
        });

        vcmprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vcmprice.setFocusableInTouchMode(true);
                vcmprice.setFocusable(true);
            }
        });

        //100Ml Cups

        mhunmlcups.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mhunmlcupsRequest();
            }
        });

        mhunmlcups.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vhunmlcups.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                }
            }
        });

        mhunmlcups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mhunmlcups.setFocusableInTouchMode(true);
                mhunmlcups.setFocusable(true);
            }
        });

        vhunmlcups.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vhunmlcupsRequest();
            }
        });
        vhunmlcups.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mhunmlcups.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                }
            }
        });

        vhunmlcups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vhunmlcups.setFocusableInTouchMode(true);
                vhunmlcups.setFocusable(true);
            }
        });

        //Curd 175Ml

        mscurdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mscurdpriceRequest();
            }
        });

        mscurdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vscurdprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }
                }
            }
        });

        mscurdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mscurdprice.setFocusableInTouchMode(true);
                mscurdprice.setFocusable(true);
            }
        });

        vscurdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vscurdpriceRequest();
            }
        });
        vscurdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mscurdprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }
                }
            }
        });

        vscurdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vscurdprice.setFocusableInTouchMode(true);
                vscurdprice.setFocusable(true);
            }
        });

        //400Ml Cups

        mfourcups.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mfourcupsRequest();
            }
        });

        mfourcups.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vfourcups.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }
                }
            }
        });

        mfourcups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mfourcups.setFocusableInTouchMode(true);
                mfourcups.setFocusable(true);
            }
        });

        vfourcups.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vfourcupsRequest();
            }
        });
        vfourcups.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mfourcups.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }
                }
            }
        });
        vfourcups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vfourcups.setFocusableInTouchMode(true);
                vfourcups.setFocusable(true);
            }
        });

        //Curd 450Ml

        mbcurdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mbcurdpriceRequest();
            }
        });

        mbcurdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vbcurdprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }
                }
            }
        });

        mbcurdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbcurdprice.setFocusableInTouchMode(true);
                mbcurdprice.setFocusable(true);
            }
        });

        vbcurdprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vbcurdpriceRequest();
            }
        });
        vbcurdprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mbcurdprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }
                }
            }
        });

        vbcurdprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vbcurdprice.setFocusableInTouchMode(true);
                vbcurdprice.setFocusable(true);
            }
        });

        //B Milk

        mbmilkprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mbmilkpriceRequest();
            }
        });

        mbmilkprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vbmilkprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }
                }
            }
        });

        mbmilkprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mbmilkprice.setFocusableInTouchMode(true);
                mbmilkprice.setFocusable(true);
            }
        });

        vbmilkprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vbmilkpriceRequest();
            }
        });
        vbmilkprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mbmilkprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }
                }
            }
        });

        vbmilkprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vbmilkprice.setFocusableInTouchMode(true);
                vbmilkprice.setFocusable(true);
            }
        });

        //Lassi

        mlassiprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mlassipriceRequest();
            }
        });

        mlassiprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vlassiprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }
                }
            }
        });

        mlassiprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlassiprice.setFocusableInTouchMode(true);
                mlassiprice.setFocusable(true);
            }
        });

        vlassiprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vlassipriceRequest();
            }
        });
        vlassiprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mlassiprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }
                }
            }
        });
        vlassiprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vlassiprice.setFocusableInTouchMode(true);
                vlassiprice.setFocusable(true);
            }
        });

        //Idly Butter

        midlyprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                midlypriceRequest();
            }
        });

        midlyprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vidlyprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();

                    String Mlassiprice=mlassiprice.getText().toString();
                    String Vlassiprice=vlassiprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }

                    if (Mlassiprice.isEmpty()){
                        mlassiprice.setText("0");
                    }
                    if (Vlassiprice.isEmpty()){
                        vlassiprice.setText("0");
                    }
                }
            }
        });

        midlyprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                midlyprice.setFocusableInTouchMode(true);
                midlyprice.setFocusable(true);
            }
        });

        vidlyprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vidlypriceRequest();
            }
        });
        vidlyprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    midlyprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();

                    String Mlassiprice=mlassiprice.getText().toString();
                    String Vlassiprice=vlassiprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }

                    if (Mlassiprice.isEmpty()){
                        mlassiprice.setText("0");
                    }
                    if (Vlassiprice.isEmpty()){
                        vlassiprice.setText("0");
                    }
                }
            }
        });

        vidlyprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vidlyprice.setFocusableInTouchMode(true);
                vidlyprice.setFocusable(true);
            }
        });

        //Dosa Butter

        mdosaprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mdosapriceRequest();
            }
        });

        mdosaprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vdosaprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();

                    String Mlassiprice=mlassiprice.getText().toString();
                    String Vlassiprice=vlassiprice.getText().toString();

                    String Midlyprice=midlyprice.getText().toString();
                    String Vidlyprice=vidlyprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }

                    if (Mlassiprice.isEmpty()){
                        mlassiprice.setText("0");
                    }
                    if (Vlassiprice.isEmpty()){
                        vlassiprice.setText("0");
                    }

                    if (Midlyprice.isEmpty()){
                        midlyprice.setText("0");
                    }
                    if (Vidlyprice.isEmpty()){
                        vidlyprice.setText("0");
                    }
                }
            }
        });

        mdosaprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mdosaprice.setFocusableInTouchMode(true);
                mdosaprice.setFocusable(true);
            }
        });

        vdosaprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                vdosapriceRequest();
            }
        });
        vdosaprice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    mdosaprice.setText("0");
                    String Mstdprice=mstdprice.getText().toString();
                    String Vstdprice=vstdprice.getText().toString();
                    String Mtmprice=mtmprice.getText().toString();
                    String Vtmprice=vtmprice.getText().toString();
                    String Mgoldprice=mgoldprice.getText().toString();
                    String Vgoldprice=vgoldprice.getText().toString();
                    String Mdtmprice=mdtmprice.getText().toString();
                    String Vdtmprice=vdtmprice.getText().toString();

                    String Mvijsmprice=mvijsmprice.getText().toString();
                    String Vvijsmprice=vvijsmprice.getText().toString();

                    String Mmmprice=mmmprice.getText().toString();
                    String Vmmprice=vmmprice.getText().toString();

                    String Mmodsmprice=mmodsmprice.getText().toString();
                    String Vmodsmprice=vmodsmprice.getText().toString();

                    String Mcmprice=mcmprice.getText().toString();
                    String Vcmprice=vcmprice.getText().toString();

                    String Mhunmlcups=mhunmlcups.getText().toString();
                    String Vhunmlcups=vhunmlcups.getText().toString();

                    String Mscurdprice=mscurdprice.getText().toString();
                    String Vscurdprice=vscurdprice.getText().toString();

                    String Mfourcups=mfourcups.getText().toString();
                    String Vfourcups=vfourcups.getText().toString();

                    String Mbcurdprice=mbcurdprice.getText().toString();
                    String Vbcurdprice=vbcurdprice.getText().toString();

                    String Mbmilkprice=mbmilkprice.getText().toString();
                    String Vbmilkprice=vbmilkprice.getText().toString();

                    String Mlassiprice=mlassiprice.getText().toString();
                    String Vlassiprice=vlassiprice.getText().toString();

                    String Midlyprice=midlyprice.getText().toString();
                    String Vidlyprice=vidlyprice.getText().toString();


                    if (Mstdprice.isEmpty()){
                        mstdprice.setText("0");
                    }
                    if (Vstdprice.isEmpty()){
                        vstdprice.setText("0");
                    }

                    if (Mtmprice.isEmpty()){
                        mtmprice.setText("0");
                    }
                    if (Vtmprice.isEmpty()){
                        vtmprice.setText("0");
                    }

                    if (Mgoldprice.isEmpty()){
                        mgoldprice.setText("0");
                    }
                    if (Vgoldprice.isEmpty()){
                        vgoldprice.setText("0");
                    }

                    if (Mdtmprice.isEmpty()){
                        mdtmprice.setText("0");
                    }
                    if (Vdtmprice.isEmpty()){
                        vdtmprice.setText("0");
                    }
                    if (Mvijsmprice.isEmpty()){
                        mvijsmprice.setText("0");
                    }
                    if (Vvijsmprice.isEmpty()){
                        vvijsmprice.setText("0");
                    }

                    if (Mmmprice.isEmpty()){
                        mmmprice.setText("0");
                    }
                    if (Vmmprice.isEmpty()){
                        vmmprice.setText("0");
                    }

                    if (Mmodsmprice.isEmpty()){
                        mmodsmprice.setText("0");
                    }
                    if (Vmodsmprice.isEmpty()){
                        vmodsmprice.setText("0");
                    }

                    if (Mcmprice.isEmpty()){
                        mcmprice.setText("0");
                    }
                    if (Vcmprice.isEmpty()){
                        vcmprice.setText("0");
                    }
                    if (Mhunmlcups.isEmpty()){
                        mhunmlcups.setText("0");
                    }
                    if (Vhunmlcups.isEmpty()){
                        vhunmlcups.setText("0");
                    }

                    if (Mscurdprice.isEmpty()){
                        mscurdprice.setText("0");
                    }
                    if (Vscurdprice.isEmpty()){
                        vscurdprice.setText("0");
                    }

                    if (Mfourcups.isEmpty()){
                        mfourcups.setText("0");
                    }
                    if (Vfourcups.isEmpty()){
                        vfourcups.setText("0");
                    }

                    if (Mbcurdprice.isEmpty()){
                        mbcurdprice.setText("0");
                    }
                    if (Vbcurdprice.isEmpty()){
                        vbcurdprice.setText("0");
                    }

                    if (Mbmilkprice.isEmpty()){
                        mbmilkprice.setText("0");
                    }
                    if (Vbmilkprice.isEmpty()){
                        vbmilkprice.setText("0");
                    }

                    if (Mlassiprice.isEmpty()){
                        mlassiprice.setText("0");
                    }
                    if (Vlassiprice.isEmpty()){
                        vlassiprice.setText("0");
                    }

                    if (Midlyprice.isEmpty()){
                        midlyprice.setText("0");
                    }
                    if (Vidlyprice.isEmpty()){
                        vidlyprice.setText("0");
                    }
                }
            }
        });

        vdosaprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vdosaprice.setFocusableInTouchMode(true);
                vdosaprice.setFocusable(true);
            }
        });


    }



    //Dosa Butter
    @SuppressLint("SetTextI18n")
    private void vdosapriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=dosaprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vdosaprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vdosaprice.toString().length() == 0) {
                totalAmount();
            }else {
                totaldosaprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void mdosapriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=dosaprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mdosaprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mdosaprice.toString().length() == 0) {
                totalAmount();
            }else {
                totaldosaprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Idly Butter
    @SuppressLint("SetTextI18n")
    private void vidlypriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=idlyprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vidlyprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vidlyprice.toString().length() == 0) {
                totalAmount();
            }else {
                totalidlyprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    private void midlypriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=idlyprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=midlyprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (midlyprice.toString().length() == 0) {
                totalAmount();
            }else {
                totalidlyprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Lassi
    @SuppressLint("SetTextI18n")
    private void vlassipriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=lassiprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vlassiprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vlassiprice.toString().length() == 0) {
                totalAmount();
            }else {
                totlassiprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mlassipriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=lassiprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mlassiprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mlassiprice.toString().length() == 0) {
                totalAmount();
            }else {
                totlassiprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //B Milk
    @SuppressLint("SetTextI18n")
    private void vbmilkpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=bmilkprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vbmilkprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vbmilkprice.toString().length() == 0) {
                totalAmount();
            }else {
                totbmilkprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mbmilkpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=bmilkprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mbmilkprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mbmilkprice.toString().length() == 0) {
                totalAmount();
            }else {
                totbmilkprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Curd 450Ml
    @SuppressLint("SetTextI18n")
    private void vbcurdpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=bcurdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vbcurdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vbcurdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totbcurdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mbcurdpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=bcurdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mbcurdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mbcurdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totbcurdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    //400Ml Cups

    @SuppressLint("SetTextI18n")
    private void vfourcupsRequest() {
        double value;
        double valuetwo;


        try {
            String price=fourcups.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vfourcups.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vfourcups.toString().length() == 0) {
                totalAmount();
            }else {
                totfourcups.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mfourcupsRequest() {
        double value;
        double valuetwo;
        try {
            String price=fourcups.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mfourcups.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mfourcups.toString().length() == 0) {
                totalAmount();
            }else {
                totfourcups.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Curd 175Ml

    @SuppressLint("SetTextI18n")
    private void vscurdpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=scurdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vscurdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vscurdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totvscurdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mscurdpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=scurdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mscurdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mscurdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totvscurdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();

            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //100Ml Cups
    @SuppressLint("SetTextI18n")
    private void vhunmlcupsRequest() {
        double value;
        double valuetwo;
        try {
            String price=hunmlcups.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vhunmlcups.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vhunmlcups.toString().length() == 0) {
                totalAmount();
            }else {
                tothuncups.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void totalAmount() {
        double one;
        double two;
        double three;
        double four;
        double five;
        double six;
        double seven;
        double eight;
        double nine;
        double ten;
        double elevn;
        double twelve;
        double thirteen;
        double fourteen;
        double fifteen;
        double sixteen;
        try {
            String wone=totsstdprice.getText().toString();
            String wtwo=tottmprice.getText().toString();
            String wthree=totgoldprice.getText().toString();
            String wfour=totdtmprice.getText().toString();
            String wfive=totvijsmprice.getText().toString();
            String wsix =totmmprice.getText().toString();
            String wseven=totmodsmprice.getText().toString();
            String weight=totcmprice.getText().toString();
            String wnine=tothuncups.getText().toString();
            String wten=totvscurdprice.getText().toString();
            String welevn=totfourcups.getText().toString();
            String wtwelve=totbcurdprice.getText().toString();
            String wthirteen=totbmilkprice.getText().toString();
            String wfourteen=totlassiprice.getText().toString();
            String wfifteen=totalidlyprice.getText().toString();
            String wsixteen=totaldosaprice.getText().toString();

            if (!wone.equalsIgnoreCase("")){
                one=Double.parseDouble(wone);
            }else {
                one=0.00;
            }

            if (!wtwo.equalsIgnoreCase("")){
                two=Double.parseDouble(wtwo);
            }else {
                two=0.00;
            }

            if (!wthree.equalsIgnoreCase("")){
                three=Double.parseDouble(wthree);
            }else {
                three=0.00;
            }

            if (!wfour.equalsIgnoreCase("")){
                four=Double.parseDouble(wfour);
            }else {
                four=0.00;
            }

            if (!wfive.equalsIgnoreCase("")){
                five=Double.parseDouble(wfive);
            }else {
                five=0.00;
            }


            if (!wsix.equalsIgnoreCase("")){
                six=Double.parseDouble(wsix);
            }else {
                six=0.00;
            }

            if (!wseven.equalsIgnoreCase("")){
                seven=Double.parseDouble(wseven);
            }else {
                seven=0.00;
            }

            if (!weight.equalsIgnoreCase("")){
                eight=Double.parseDouble(weight);
            }else {
                eight=0.00;
            }

            if (!wnine.equalsIgnoreCase("")){
                nine=Double.parseDouble(wnine);
            }else {
                nine=0.00;
            }

            if (!wten.equalsIgnoreCase("")){
                ten=Double.parseDouble(wten);
            }else {
                ten=0.00;
            }


            if (!welevn.equalsIgnoreCase("")){
                elevn=Double.parseDouble(welevn);
            }else {
                elevn=0.00;
            }

            if (!wtwelve.equalsIgnoreCase("")){
                twelve=Double.parseDouble(wtwelve);
            }else {
                twelve=0.00;
            }

            if (!wthirteen.equalsIgnoreCase("")){
                thirteen=Double.parseDouble(wthirteen);
            }else {
                thirteen=0.00;
            }

            if (!wfourteen.equalsIgnoreCase("")){
                fourteen=Double.parseDouble(wfourteen);
            }else {
                fourteen=0.00;
            }

            if (!wfifteen.equalsIgnoreCase("")){
                fifteen=Double.parseDouble(wfifteen);
            }else {
                fifteen=0.00;
            }

            if (!wsixteen.equalsIgnoreCase("")){
                sixteen=Double.parseDouble(wsixteen);
            }else {
                sixteen=0.00;
            }



            Double finalAmount=one + two + three + four +five + six + seven + eight +nine + ten + elevn + twelve + thirteen + fourteen + fifteen + sixteen;
            Log.v("","finalAmount"+finalAmount);

            tvtotal.setText("Total Cost :" + new DecimalFormat("##.##").format(finalAmount));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void mhunmlcupsRequest() {
        double value;
        double valuetwo;
        try {
            String price=hunmlcups.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mhunmlcups.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mhunmlcups.toString().length() == 0) {
                totalAmount();
            }else {
                tothuncups.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Cow Milk
    @SuppressLint("SetTextI18n")
    private void vcmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=cmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vcmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vcmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totcmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }


    @SuppressLint("SetTextI18n")
    private void mcmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=cmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mcmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mcmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totcmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Model Small

    @SuppressLint("SetTextI18n")
    private void vmodsmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=modsmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vmodsmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vmodsmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totmodsmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void mmodsmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=modsmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mmodsmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mmodsmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totmodsmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //M.M

    @SuppressLint("SetTextI18n")
    private void vmmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=mmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vmmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vmmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totmmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mmmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=mmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mmmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mmmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totmmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //Vij.Small

    @SuppressLint("SetTextI18n")
    private void vvijsmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=vijsmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vvijsmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vvijsmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totvijsmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mvijsmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=vijsmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mvijsmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mvijsmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totvijsmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }


    //D.T.M
    @SuppressLint("SetTextI18n")
    private void vdtmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=dtmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vdtmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vdtmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totdtmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mdtmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=dtmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mdtmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mdtmprice.toString().length() == 0) {
                totalAmount();
            }else {
                totdtmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }


    //GOLD
    @SuppressLint("SetTextI18n")
    private void vgoldpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=goldprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vgoldprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vgoldprice.toString().length() == 0) {
                totalAmount();
            }else {
                totgoldprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void mgoldpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=goldprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }

            String entry=mgoldprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;

            if (mgoldprice.toString().length() == 0) {
                totalAmount();
            }else {
                totgoldprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //T.M

    @SuppressLint("SetTextI18n")
    private void vtmpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=tmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
            }else {
                value=0.00;
            }
            String entry=vtmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;

            if (vtmprice.toString().length() == 0) {
                totalAmount();
            }else {
                tottmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void mtmpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=tmprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
                Log.v("","value"+value);
            }else {
                value=0.00;
            }

            String entry=mtmprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
                Log.v("","valuetwo"+valuetwo);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;
            Log.v("","finalamount"+finalamount);

            if (mtmprice.toString().length() == 0) {
                totalAmount();
            }else {
                tottmprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    //S.T.D

    @SuppressLint("SetTextI18n")
    private void vstdpriceRequest() {
        double value;
        double valuetwo;

        try {
            String price=stdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
                Log.v("","value"+value);
            }else {
                value=0.00;
            }
            String entry=vstdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
                Log.v("","valuetwo"+valuetwo);
            }else {
                valuetwo=0.00;
            }
            double finalamount = value * valuetwo;
            Log.v("","finalamount"+finalamount);

            if (vstdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totsstdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }


        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    private void mstdpriceRequest() {
        double value;
        double valuetwo;
        try {
            String price=stdprice.getText().toString();
            if (!price.equalsIgnoreCase("")){
                value=Double.parseDouble(price);
                Log.v("","value"+value);
            }else {
                value=0.00;
            }

            String entry=mstdprice.getText().toString();
            if (!entry.equalsIgnoreCase("")){
                valuetwo=Double.parseDouble(entry);
                Log.v("","valuetwo"+valuetwo);
            }else {
                valuetwo=0.00;
            }

            double finalamount = value * valuetwo;
            Log.v("","finalamount"+finalamount);

            if (mstdprice.toString().length() == 0) {
                totalAmount();
            }else {
                totsstdprice.setText(new DecimalFormat("##.##").format(finalamount));
                totalAmount();
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, String, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                SellProductsActivity.this);

        protected void onPreExecute() {

            this.dialog.setMessage("Downloading Customer data...");
            this.dialog.show();

        }

        @Override
        protected String doInBackground(String... unused) {
            String fdfd = "";
            try {
                fdfd = sendSoapRequest(getApplicationContext());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fdfd;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("transactionresponse", result);
            databaseHelper.deleteCustomers();
//            anyType{Customers=anyType{Customer=anyType{Commission=0.00; CustomerId=18; CustomerName=SRINU; TMCommission=0.00; }; Customer=anyType{Commission=0.00; CustomerId=20; CustomerName=MAHALAXMI TRADERS; TMCommission=0.00; };
            String data=result.replace("Customers=anyType","").replace("{","").replace("}","");
            String data1=data.replace("anyType","").replace(" ","").replace("Customer=","").replace("Message=SUCCESS;Status=1;","");
            Log.v("","data1"+data1);

            String[] parts = data1.split(";;");
            for (String part : parts) {
                //do something interesting here
                System.out.print(part);
                Log.v("","part"+part);
//                Commission=0.00;CustomerId=18;CustomerName=SRINU;TMCommission=0.00
                try{
                    String[] temp=part.split(";");
                    CommissionList.add(temp[0].replace("Commission=",""));
                    CustomerIdList.add(temp[1].replace("CustomerId=",""));
                    CustomerNameList.add(temp[2].replace("CustomerName=",""));
                    TMCommissionList.add(temp[3].replace("TMCommission=",""));

                    user.setCommission(temp[0].replace("Commission=",""));
                    user.setCustomerId(temp[1].replace("CustomerId=",""));
                    user.setCustomerName(temp[2].replace("CustomerName=",""));
                    user.setTMCommission(temp[3].replace("TMCommission=",""));
                    databaseHelper.addUser(user);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            Log.v("","CommissionList.size()"+CommissionList.size());
            Log.v("","CustomerIdList.size()"+CustomerIdList.size());
            Log.v("","CustomerNameList.size()"+CustomerNameList.size());
            Log.v("","TMCommissionList.size()"+TMCommissionList.size());

            creatingViews();
            this.dialog.dismiss();
        }

    }

    public void creatingViews(){
        branchlist=databaseHelper.getAllCustomers();

        if(branchlist!=null && branchlist.size()>0)
        {
            String[] vistleadvalues = new String[branchlist.size()];

            hmFundingBranch = new HashMap<String, Customers>();

            for (int i = 0; i < branchlist.size(); i++) {
                Customers vlead = branchlist.get(i);
                vistleadvalues[i] = vlead.getCustomerName();

                fundbranchlist.add(vlead.getCustomerName());
                String FundingBranchId = vlead.getCustomerId();




                Log.v("Milk", "CustomerId"+FundingBranchId);
                Log.v("Milk", "getCustomerName"+vlead.getCustomerName());

                fundingbranchlist.add(FundingBranchId);
                hmFundingBranch.put(vlead.getCustomerId(), vlead);
                hmFundingBranch.put(vlead.getCustomerName(), vlead);

            }

            for (int i = 0; i < vistleadvalues.length; i++) {
                Log.v("village[i]", "village array" + vistleadvalues[i]);
            }

            if (vistleadvalues.length > 0) {

                branchadapter = new ArrayAdapter<String>(this, R.layout.mylistdata, vistleadvalues);
                autocustomernames.setAdapter(branchadapter);


            }

        }

//        customersnameslist=databaseHelper.getAllCustomersNamesList();
//        for (int i = 0; i < customersnameslist.size(); i++) {
//            try
//            {
//                Customers vlead = customersnameslist.get(i);
//                String name=vlead.getCustomerName();
//                CustomerNameList.add(name);
//            }catch(IndexOutOfBoundsException in)
//            {
//                in.printStackTrace();
//            }
//        }
//        final ArrayAdapter<String> adapters = new ArrayAdapter<>(
//                this,android.R.layout.simple_list_item_1 ,CustomerNameList);
//        // Setting Adapter to the Spinner
//        autocustomernames.setAdapter(adapters);
    }


    public String sendSoapRequest(Context context) throws Exception {


        String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "  <SOAP-ENV:Body>\n" +
                "    <ns1:GetCustomers/>\n" +
                "  </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
        Log.i("TAG", "*********************** FinalString Before "+ finalString);



        // send SOAP request
        InputStream resInputStream = sendRequest(finalString);

        // create the response SOAP envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);

        // process SOAP response
        parseResponse(resInputStream, envelope);

        Object bodyIn = envelope.bodyIn;

        SoapObject RequestSOAP = (SoapObject) envelope.bodyIn;
        String response = RequestSOAP.getProperty(0).toString();
        if (bodyIn instanceof SoapFault) {
            throw (SoapFault) bodyIn;
        }
        return response;
    }
    /**
     * Parses the input stream from the response into SoapEnvelope object.
     */
    private void parseResponse(InputStream is, SoapEnvelope envelope)
            throws Exception {

        try {
            System.out.print(is);
            XmlPullParser xp = Xml.newPullParser();
            xp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            xp.setInput(is, "UTF-8");
            envelope.parse(xp);
        } catch (Throwable e) {
            Log.e("LOG_TAG", "Error reading/parsing SOAP response", e);

        }

    }


    private InputStream sendRequest(String requestContent) throws Exception {

        // initialize HTTP post
        HttpPost httpPost = null;

        try {
            httpPost = new HttpPost("http://inventapi.medikonda.net/DairyUnifiedAPI.svc");

            httpPost.addHeader("Content-Type", "text/xml;charset=UTF-8");
            httpPost.addHeader("SOAPAction", "http://tempuri.org/IDairyUnifiedAPI/GetCustomers");
        } catch (Throwable e) {
            Log.e("LOG_TAG", "Error initializing HTTP post for SOAP request", e);
            // throw e;
        }

        // load content to be sent
        try {
            HttpEntity postEntity = new StringEntity(requestContent);
            httpPost.setEntity(postEntity);
        } catch (UnsupportedEncodingException e) {
            Log.e("LOG_TAG",
                    "Unsupported ensoding of content for SOAP request", e);
            throw e;
        }

        // send request
        HttpResponse httpResponse = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            httpResponse = httpClient.execute(httpPost);
        } catch (Throwable e) {
            Log.e("LOG_TAG", "Error sending SOAP request", e);
            // throw e;
        }

        // get SOAP response
        try {
            // get response code
            int responseStatusCode = httpResponse.getStatusLine()
                    .getStatusCode();

            // if the response code is not 200 - OK, or 500 - Internal error,
            // then communication error occurred
            if (responseStatusCode != 200 && responseStatusCode != 500) {
                String errorMsg = "Got SOAP response code "
                        + responseStatusCode + " "
                        + httpResponse.getStatusLine().getReasonPhrase();
                // ...
            }

            // get the response content
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream is = httpEntity.getContent();
            return is;
        } catch (Throwable e) {
            Log.e("LOG_TAG", "Error getting SOAP response", e);
            // throw e;
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
        startActivity(intent);
    }
}
