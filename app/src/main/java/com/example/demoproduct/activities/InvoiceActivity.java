package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.model.Customers;
import com.example.demoproduct.printer.DeviceListActivity;
import com.hoin.btsdk.BluetoothService;
import com.hoin.btsdk.PrintPic;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Set;

import static com.example.demoproduct.printer.DeviceListActivity.EXTRA_DEVICE_ADDRESS;

public class InvoiceActivity extends AppCompatActivity {
    private AppCompatTextView mstdprice,mtmprice,mgoldprice,mdtmprice,mvijsmprice,mmmprice,mmodsmprice,mcmprice,
            mhunmlcups,mscurdprice,mfourcups,mbcurdprice,mbmilkprice,mlassiprice,midlyprice,mdosaprice;
    private AppCompatTextView vstdprice,vtmprice,vgoldprice,vdtmprice,vvijsmprice,vmmprice,vmodsmprice,vcmprice,
            vhunmlcups,vscurdprice,vfourcups,vbcurdprice,vbmilkprice,vlassiprice,vidlyprice,vdosaprice;
    private AppCompatTextView totsstdprice,tottmprice,totgoldprice,totdtmprice,totvijsmprice,totmmprice,totmodsmprice,totcmprice,
            tothuncups,totvscurdprice,totfourcups,totbcurdprice,totbmilkprice,totlassiprice,totalidlyprice,totaldosaprice;
    private AppCompatTextView tvtotal,customername;
    private AppCompatTextView currentDate;
    String Customerid;
    String CustomerName;
    String date;
    String Commission="";
    String TMCommission="";
    double finalcommission;
    double aftercommissiontotal;

    String Mstd;
    String Vstd;
    String Totstd;
    String Mtm;
    String Vtm;
    String Tottm;
    String Mgold;
    String Totgold;
    String Mdtm;
    String Totdtm;
    String Mvij;
    String TotVij;
    String Mmm;
    String Totvm;
    String Mmodel;
    String Totmodel;
    String Mcow;
    String TotCow;
    String Mhun;
    String Tothun;
    String Mcurd;
    String Totcurd;
    String Mfour;
    String Totfour;
    String Mbcurd;
    String Totbcurd;
    String Mbmilk;
    String Totbmilk;
    String Mlassi;
    String TotLassi;
    String Midly;
    String TotIdly;
    String Mdosa;
    String TotDosa;
    String TotalAmount;
    String Total;
    String InvoiceNumber="";
    int TotalQnty;
    String FinalTotal;
    AppCompatButton appCompatButtonPrint,btnSearch,appCompatButtonExit;
    AppCompatButton appCompatButtonUpdate,appCompatButtonGenerate;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    String AgentName="";
    String currenttime="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

         Mstd = getIntent().getStringExtra("Mstd");
         Vstd = getIntent().getStringExtra("Vstd");
         Totstd = getIntent().getStringExtra("Totstd");
         Mtm = getIntent().getStringExtra("Mtm");
         Vtm = getIntent().getStringExtra("Vtm");
         Tottm = getIntent().getStringExtra("Tottm");
         Mgold = getIntent().getStringExtra("Mgold");
        String Vgold = getIntent().getStringExtra("Vgold");
         Totgold = getIntent().getStringExtra("Totgold");
         Mdtm = getIntent().getStringExtra("Mdtm");
        String Vdtm = getIntent().getStringExtra("Vdtm");
         Totdtm = getIntent().getStringExtra("Totdtm");
         Mvij = getIntent().getStringExtra("Mvij");
        String Vvij = getIntent().getStringExtra("Vvij");
         TotVij = getIntent().getStringExtra("TotVij");
         Mmm = getIntent().getStringExtra("Mmm");
        String Vmm = getIntent().getStringExtra("Vmm");
         Totvm = getIntent().getStringExtra("Totvm");
         Mmodel = getIntent().getStringExtra("Mmodel");
        String Vmodel = getIntent().getStringExtra("Vmodel");
         Totmodel = getIntent().getStringExtra("Totmodel");
         Mcow = getIntent().getStringExtra("Mcow");
        String Vcow = getIntent().getStringExtra("Vcow");
         TotCow = getIntent().getStringExtra("TotCow");
         Mhun = getIntent().getStringExtra("Mhun");
        String Vhun = getIntent().getStringExtra("Vhun");
         Tothun = getIntent().getStringExtra("Tothun");
         Mcurd = getIntent().getStringExtra("Mcurd");
        String Vcurd = getIntent().getStringExtra("Vcurd");
         Totcurd = getIntent().getStringExtra("Totcurd");
         Mfour = getIntent().getStringExtra("Mfour");
        String Vfour = getIntent().getStringExtra("Vfour");
         Totfour = getIntent().getStringExtra("Totfour");
         Mbcurd = getIntent().getStringExtra("Mbcurd");
        String Vbcurd = getIntent().getStringExtra("Vbcurd");
         Totbcurd = getIntent().getStringExtra("Totbcurd");
         Mbmilk = getIntent().getStringExtra("Mbmilk");
        String Vbmilk = getIntent().getStringExtra("Vbmilk");
         Totbmilk = getIntent().getStringExtra("Totbmilk");
         Mlassi = getIntent().getStringExtra("Mlassi");
        String Vlassi = getIntent().getStringExtra("Vlassi");
         TotLassi = getIntent().getStringExtra("TotLassi");
         Midly = getIntent().getStringExtra("Midly");
        String Vidly = getIntent().getStringExtra("Vidly");
         TotIdly = getIntent().getStringExtra("TotIdly");
         Mdosa = getIntent().getStringExtra("Mdosa");
        String Vdosa = getIntent().getStringExtra("Vdosa");
         TotDosa = getIntent().getStringExtra("TotDosa");
         TotalAmount = getIntent().getStringExtra("TotalAmount");
         Total=TotalAmount.replace("Total Cost :","");
        FinalTotal=Total.replace("Total Cost:","");
        Customerid=getIntent().getStringExtra("Customerid");
        CustomerName=getIntent().getStringExtra("CustomerName");
        Commission=getIntent().getStringExtra("Commission");
        TMCommission=getIntent().getStringExtra("TMCommission");

        //Start Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title);
        ImageView backArrow =(ImageView) toolbar.findViewById(R.id.toolbar_back_arrow);
        title.setText("Invoice");
        title.setPadding(0, 0, 60, 0);
        ImageView logout=(ImageView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(InvoiceActivity.this);
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
        appCompatButtonUpdate=findViewById(R.id.appCompatButtonUpdate);
        appCompatButtonGenerate=findViewById(R.id.appCompatButtonGenerate);
        appCompatButtonPrint=findViewById(R.id.appCompatButtonPrint);
        btnSearch=findViewById(R.id.btnSearch);
        currentDate=findViewById(R.id.currentdate);
        appCompatButtonExit=findViewById(R.id.appCompatButtonExit);

        appCompatButtonPrint.setOnClickListener(new ClickEvent());

        btnSearch.setOnClickListener(new ClickEvent());

        mService = new BluetoothService(this, mHandler);

        if(!mService.isAvailable()){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

        appCompatButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SellProductsActivity.class);
                startActivity(intent);
            }
        });
         mstdprice=findViewById(R.id.mstdprice);
         vstdprice=findViewById(R.id.vstdprice);

         mtmprice=findViewById(R.id.mtmprice);
         vtmprice=findViewById(R.id.vtmprice);

         mgoldprice=findViewById(R.id.mgoldprice);
         vgoldprice=findViewById(R.id.vgoldprice);

         mdtmprice=findViewById(R.id.mdtmprice);
         vdtmprice=findViewById(R.id.vdtmprice);

         mvijsmprice=findViewById(R.id.mvijsmprice);
         vvijsmprice=findViewById(R.id.vvijsmprice);

         mmmprice=findViewById(R.id.mmmprice);
         vmmprice=findViewById(R.id.vmmprice);

         mmodsmprice=findViewById(R.id.mmodsmprice);
         vmodsmprice=findViewById(R.id.vmodsmprice);

         mcmprice=findViewById(R.id.mcmprice);
         vcmprice=findViewById(R.id.vcmprice);

         mhunmlcups=findViewById(R.id.mhunmlcups);
         vhunmlcups=findViewById(R.id.vhunmlcups);

         mscurdprice=findViewById(R.id.mscurdprice);
         vscurdprice=findViewById(R.id.vscurdprice);

         mfourcups=findViewById(R.id.mfourcups);
         vfourcups=findViewById(R.id.vfourcups);

         mbcurdprice=findViewById(R.id.mbcurdprice);
         vbcurdprice=findViewById(R.id.vbcurdprice);

         mbmilkprice=findViewById(R.id.mbmilkprice);
         vbmilkprice=findViewById(R.id.vbmilkprice);

         mlassiprice=findViewById(R.id.mlassiprice);
         vlassiprice=findViewById(R.id.vlassiprice);

         midlyprice=findViewById(R.id.midlyprice);
         vidlyprice=findViewById(R.id.vidlyprice);

         mdosaprice=findViewById(R.id.mdosaprice);
         vdosaprice=findViewById(R.id.vdosaprice);

         totsstdprice=findViewById(R.id.totsstdprice);

         tottmprice=findViewById(R.id.tottmprice);

         totgoldprice=findViewById(R.id.totgoldprice);

         totdtmprice=findViewById(R.id.totdtmprice);

         totvijsmprice=findViewById(R.id.totvijsmprice);

         totmmprice=findViewById(R.id.totmmprice);

         totmodsmprice=findViewById(R.id.totmodsmprice);

         totcmprice=findViewById(R.id.totcmprice);

         tothuncups=findViewById(R.id.tothuncups);

         totvscurdprice=findViewById(R.id.totvscurdprice);

         totfourcups=findViewById(R.id.totfourcups);

         totbcurdprice=findViewById(R.id.totbcurdprice);

         totbmilkprice=findViewById(R.id.totbmilkprice);

         totlassiprice=findViewById(R.id.totlassiprice);

         totalidlyprice=findViewById(R.id.totalidlyprice);

         totaldosaprice=findViewById(R.id.totaldosaprice);

         tvtotal=findViewById(R.id.tvtotal);

        customername=findViewById(R.id.customername);


            if (Mstd.equalsIgnoreCase("")||Mstd.isEmpty()){
                mstdprice.setText("0");
            }else {
                mstdprice.setText(Mstd);
            }
            if (Vstd.equalsIgnoreCase("")){
                vstdprice.setText("0");
            }else {
                vstdprice.setText(Vstd);
            }

            if (Totstd.equalsIgnoreCase("")){
                totsstdprice.setText("0");
            }else {
                totsstdprice.setText(Totstd);
            }

            if (Mtm.equalsIgnoreCase("")||Mtm.isEmpty()){
                mtmprice.setText("0");
            }else {
                mtmprice.setText(Mtm);
            }
            if (Vtm.equalsIgnoreCase("")){
                vtmprice.setText("0");
            }else {
                vtmprice.setText(Vtm);
            }
            if (Tottm.equalsIgnoreCase("")){
                tottmprice.setText("0");
            }else {
                tottmprice.setText(Tottm);
            }

            if (Mgold.equalsIgnoreCase("")||Mgold.isEmpty()){
                mgoldprice.setText("0");
            }else {
                mgoldprice.setText(Mgold);
            }
            if (Vgold.equalsIgnoreCase("")){
                vgoldprice.setText("0");
            }else {
                vgoldprice.setText(Vgold);
            }
            if (Totgold.equalsIgnoreCase("")){
                totgoldprice.setText("0");
            }else {
                totgoldprice.setText(Totgold);
            }

            if (Mdtm.equalsIgnoreCase("")||Mdtm.isEmpty()){
                mdtmprice.setText("0");
            }else {
                mdtmprice.setText(Mdtm);
            }
            if (Vdtm.equalsIgnoreCase("")){
                vdtmprice.setText("0");
            }else {
                vdtmprice.setText(Vdtm);
            }
            if (Totdtm.equalsIgnoreCase("")){
                totdtmprice.setText("0");
            }else {
                totdtmprice.setText(Totdtm);
            }
            if (Mvij.equalsIgnoreCase("")||Mvij.isEmpty()){
                mvijsmprice.setText("0");
            }else {
                mvijsmprice.setText(Mvij);
            }
            if (Vvij.equalsIgnoreCase("")){
                vvijsmprice.setText("0");
            }else {
                vvijsmprice.setText(Vvij);
            }
            if (TotVij.equalsIgnoreCase("")){
                totvijsmprice.setText("0");
            }else {
                totvijsmprice.setText(TotVij);
            }

            if (Mmm.equalsIgnoreCase("")||Mmm.isEmpty()){
                mmmprice.setText("0");
            }else {
                mmmprice.setText(Mmm);
            }

            if (Vmm.equalsIgnoreCase("")){
                vmmprice.setText("0");
            }else {
                vmmprice.setText(Vmm);
            }
            if (Totvm.equalsIgnoreCase("")){
                totmmprice.setText("0");
            }else {
                totmmprice.setText(Totvm);
            }

            if (Mmodel.equalsIgnoreCase("")||Mmodel.isEmpty()){
                mmodsmprice.setText("0");
            }else {
                mmodsmprice.setText(Mmodel);
            }
            if (Vmodel.equalsIgnoreCase("")){
                vmodsmprice.setText("0");
            }else {
                vmodsmprice.setText(Vmodel);
            }
            if (Totmodel.equalsIgnoreCase("")){
                totmodsmprice.setText("0");
            }else {
                totmodsmprice.setText(Totmodel);
            }

            if (Mcow.equalsIgnoreCase("")||Mcow.isEmpty()){
                mcmprice.setText("0");
            }else {
                mcmprice.setText(Mcow);
            }
            if (Vcow.equalsIgnoreCase("")){
                vcmprice.setText("0");
            }else {
                vcmprice.setText(Vcow);
            }
            if (TotCow.equalsIgnoreCase("")){
                totcmprice.setText("0");
            }else {
                totcmprice.setText(TotCow);
            }

            if (Mhun.equalsIgnoreCase("")||Mhun.isEmpty()){
                mhunmlcups.setText("0");
            }else {
                mhunmlcups.setText(Mhun);
            }

            if (Vhun.equalsIgnoreCase("")){
                vhunmlcups.setText("0");
            }else {
                vhunmlcups.setText(Vhun);
            }
            if (Tothun.equalsIgnoreCase("")){
                tothuncups.setText("0");
            }else {
                tothuncups.setText(Tothun);
            }
            if (Mcurd.equalsIgnoreCase("")||Mcurd.isEmpty()){
                mscurdprice.setText("0");
            }else {
                mscurdprice.setText(Mcurd);
            }
            if (Vcurd.equalsIgnoreCase("")){
                vscurdprice.setText("0");
            }else {
                vscurdprice.setText(Vcurd);
            }

            if (Totcurd.equalsIgnoreCase("")){
                totvscurdprice.setText("0");
            }else {
                totvscurdprice.setText(Totcurd);
            }
            if (Mfour.equalsIgnoreCase("")||Mfour.isEmpty()){
                mfourcups.setText("0");
            }else {
                mfourcups.setText(Mfour);
            }
            if (Vfour.equalsIgnoreCase("")){
                vfourcups.setText("0");
            }else {
                vfourcups.setText(Vfour);
            }
            if (Totfour.equalsIgnoreCase("")){
                totfourcups.setText("0");
            }else {
                totfourcups.setText(Totfour);
            }
            if (Mbcurd.equalsIgnoreCase("")||Mbcurd.isEmpty()){
                mbcurdprice.setText("0");
            }else {
                mbcurdprice.setText(Mbcurd);
            }

            if (Vbcurd.equalsIgnoreCase("")){
                vbcurdprice.setText("0");
            }else {
                vbcurdprice.setText(Vbcurd);
            }
            if (Totbcurd.equalsIgnoreCase("")){
                totbcurdprice.setText("0");
            }else {
                totbcurdprice.setText(Totbcurd);
            }
            if (Mbmilk.equalsIgnoreCase("")||Mbmilk.isEmpty()){
                mbmilkprice.setText("0");
            }else {
                mbmilkprice.setText(Mbmilk);
            }
            if (Vbmilk.equalsIgnoreCase("")){
                vbmilkprice.setText("0");
            }else {
                vbmilkprice.setText(Vbmilk);
            }
            if (Totbmilk.equalsIgnoreCase("")){
                totbmilkprice.setText("0");
            }else {
                totbmilkprice.setText(Totbmilk);
            }

            if (Mlassi.equalsIgnoreCase("")||Mlassi.isEmpty()){
                mlassiprice.setText("0");
            }else {
                mlassiprice.setText(Mlassi);
            }
            if (Vlassi.equalsIgnoreCase("")){
                vlassiprice.setText("0");
            }else {
                vlassiprice.setText(Vlassi);
            }
            if (TotLassi.equalsIgnoreCase("")){
                totlassiprice.setText("0");
            }else {
                totlassiprice.setText(TotLassi);
            }
            if (Midly.equalsIgnoreCase("")||Midly.isEmpty()){
                midlyprice.setText("0");
            }else {
                midlyprice.setText(Midly);
            }
            if (Vidly.equalsIgnoreCase("")){
                vidlyprice.setText("0");
            }else {
                vidlyprice.setText(Vidly);
            }
            if (TotIdly.equalsIgnoreCase("")){
                totalidlyprice.setText("0");
            }else {
                totalidlyprice.setText(TotIdly);
            }

            if (Mdosa.equalsIgnoreCase("")||Mdosa.isEmpty()){
                mdosaprice.setText("0");
            }else {
                mdosaprice.setText(Mdosa);
            }
            if (Vdosa.equalsIgnoreCase("")){
                vdosaprice.setText("0");
            }else {
                vdosaprice.setText(Vdosa);
            }
            if (TotDosa.equalsIgnoreCase("")){
                totaldosaprice.setText("0");
            }else {
                totaldosaprice.setText(TotDosa);
            }
            if (Total.equalsIgnoreCase("")||Total.isEmpty()){
                tvtotal.setText("Total Cost: 0.00");
            }else {
                if (Total.contains("Total Cost:")){
                    tvtotal.setText(""+Total);
                }else {
                    tvtotal.setText("Total Cost:"+Total);
                }
            }

            if (CustomerName.equalsIgnoreCase("")){
                customername.setText("");
            }else {
                customername.setText("Customer Name :"+CustomerName);
            }

            creatingViews();

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(c.getTime());
        currentDate.setText("Date:"+date);
        appCompatButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent intent=new Intent(getApplicationContext(),SellProductsActivity.class);
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
                startActivity(intent);
            }
        });
        appCompatButtonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                    new DownloadTask().execute();
                    // notify user you are online
                } else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                    // notify user you are not online
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!mService.isBTopen()){
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null)
            mService.stop();
        mService = null;
    }

    class ClickEvent implements View.OnClickListener {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.appCompatButtonPrint:

                    Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            con_dev = mService.getDevByMac(device.getAddress());

                            mService.connect(con_dev);
                            Toast.makeText(getApplicationContext(),"Device Name:"+device.getName(),Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"Device Address:"+device.getAddress(),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String noDevices = getResources().getText(R.string.bluetooth_none_paired).toString();
                        Toast.makeText(getApplicationContext(),""+noDevices,Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.btnSearch:
                    Intent serverIntent = new Intent(getApplicationContext(), DeviceListActivity.class);
                    startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
                    break;
            }
        }
    }

    private void creatingViews() {
        double commission=Double.parseDouble(Commission);
        Log.v("","commission"+commission);
        double tmcomission=Double.parseDouble(TMCommission);
        Log.v("","tmcomission"+tmcomission);
        String Mstd=mstdprice.getText().toString();
        double a=Double.parseDouble(Mstd);

        double aa=a*commission;

        String Mtm=mtmprice.getText().toString();
        double b=Double.parseDouble(Mtm);

        double bb=b*commission;

        String Mgold=mgoldprice.getText().toString();

        double c=Double.parseDouble(Mgold);

        double cc=c*commission;

        String Mdtm=mdtmprice.getText().toString();
        double d=Double.parseDouble(Mdtm);

        double dd=d*commission;

        String Mvij=mvijsmprice.getText().toString();
        double e=Double.parseDouble(Mvij);

        double ee=e*commission;

        String Mmm=mmmprice.getText().toString();
        double f=Double.parseDouble(Mmm);

        double ff=f*commission;

        String Mmodel=mmodsmprice.getText().toString();
        double g=Double.parseDouble(Mmodel);

        double gg=g*commission;

        String Mcow=mcmprice.getText().toString();
        double h=Double.parseDouble(Mcow);

        double hh=h*commission;

        String Mhun=mhunmlcups.getText().toString();
        double i=Double.parseDouble(Mhun);

        double ii=i*0;

        String Mcurd=mscurdprice.getText().toString();
        double j=Double.parseDouble(Mcurd);

        double jj=j*0;

        String Mfour=mfourcups.getText().toString();
        double k=Double.parseDouble(Mfour);

        double kk=k*0;

        String Mbcurd=mbcurdprice.getText().toString();
        double l=Double.parseDouble(Mbcurd);

        double ll=l*0;

        String Mbmilk=mbmilkprice.getText().toString();
        double m=Double.parseDouble(Mbmilk);

        double mm=m*0;

        String Mlassi=mlassiprice.getText().toString();

        double n=Double.parseDouble(Mlassi);

        double nn=n*0;

        String Midly=midlyprice.getText().toString();

        double o=Double.parseDouble(Midly);

        double oo=o*0;

        String Mdosa=mdosaprice.getText().toString();

        double p=Double.parseDouble(Mdosa);

        double pp=p*0;

        finalcommission= aa + bb + cc + dd + ee + ff  + gg + hh + ii + jj + kk + ll + mm + nn + oo + pp;

        Log.v("","finalcommission"+finalcommission);

        double qq=Double.parseDouble(FinalTotal);
        Log.v("","afterdeduction"+qq);

        aftercommissiontotal= qq - finalcommission;
        Log.v("","aftercommissiontotal"+aftercommissiontotal);


    }


    /**

     */
    @SuppressLint("HandlerLeak")
    private  final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SharedPreferences prefs = getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
            AgentName = prefs.getString("AgentName", null);
            Calendar calendar = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            currenttime = mdformat.format(calendar.getTime());
            int a=Integer.parseInt(Mstd);
            int b=Integer.parseInt(Mtm);
            int cc=Integer.parseInt(Mgold);
            int d=Integer.parseInt(Mdtm);
            int e=Integer.parseInt(Mvij);
            int f=Integer.parseInt(Mmm);
            int g=Integer.parseInt(Mmodel);
            int h=Integer.parseInt(Mcow);
            int i=Integer.parseInt(Mhun);
            int j=Integer.parseInt(Mcurd);
            int k=Integer.parseInt(Mfour);
            int l=Integer.parseInt(Mbcurd);
            int m=Integer.parseInt(Mbmilk);
            int n=Integer.parseInt(Mlassi);
            int o=Integer.parseInt(Midly);
            int p=Integer.parseInt(Mdosa);

            TotalQnty= a + b + cc + d + e + f + g + h + i + j + k + l + m + n + o + p;

            String reciept = "";
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            String lang = getString(R.string.bluetooth_strLang);
//                    printImage();

                            byte[] cmd = new byte[3];
                            cmd[0] = 0x1b;
                            cmd[1] = 0x21;
                            if((lang.compareTo("en")) == 0){
                                cmd[2] |= 0x10;
                                mService.write(cmd);
                                mService.sendMessage("   Phani Milk Products Supply\n", "GBK");
                                cmd[2] &= 0xEF;
                                mService.write(cmd);
//                        "Cement Road,VidyadharaPuram,Vij-12 \n"+
                                reciept =   "Mobile No:9246497111,8885523456 \n"+
                                        "  ---------------------------  \n"+
                                        "           Estimate \n"+
                                        "Invoice No: "+InvoiceNumber+"\n"+
                                        "Date: "+date + " "+currenttime+"\n"+
                                        "Agent Name: "+AgentName +"\n" +
                                        "Contact No:"+" 9246497111\n" +
                                        "Brands     |"+"Rate  |"+"Qty |"+"Amount"+"\n" +
                                        "------      "+"----   "+"----  "+"------"+"\n" +
                                        "S.T.D      |"+"48.00 |"+Mstd+"   |"+Totstd +""+"\n" +
                                        "T.M        |"+"44.00 |"+Mtm +"   |"+Tottm+""+"\n" +
                                        "GOLD       |"+"54.00 |"+Mgold+"   |"+Totgold +""+"\n" +
                                        "D.T.M      |"+"42.00 |"+Mdtm+"   |"+Totdtm +""+"\n" +
                                        "VIJ.SMALL  |"+"45.00 |"+Mvij+"   |"+TotVij +""+"\n" +
                                        "M M        |"+"52.00 |"+Mmm+"   |"+Totvm +""+"\n" +
                                        "MODEL SMALL|"+"45.00 |"+Mmodel+"   |"+Totmodel +""+"\n" +
                                        "COW MILK   |"+"51.00 |"+Mcow+"   |"+TotCow +""+"\n" +
                                        "100ML CUPS |"+"07.25 |"+Mhun+ "   |"+Tothun +""+"\n" +
                                        "CURD 175ML |"+"42.50 |"+Mcurd+ "   |"+Totcurd +""+"\n" +
                                        "400ML CUPS |"+"28.00 |"+Mfour+ "   |"+Totfour +""+"\n" +
                                        "CURD 450ML |"+"45.00 |"+Mbcurd+"   |"+Totbcurd +""+"\n" +
                                        "B MILK     |"+"06.00 |"+Mbmilk+ "   |"+Totbmilk +""+"\n" +
                                        "LASSI      |"+"09.00 |"+Mlassi+ "   |"+TotLassi +""+"\n" +
                                        "IDLY BATTER|"+"30.00 |"+Midly+ "   |"+TotIdly +""+"\n" +
                                        "DOSA BATTER|"+"30.00 |"+Mdosa+ "   |"+TotDosa +""+"\n" +
                                        "Totals             "+ TotalQnty +"   |"+FinalTotal+"\n" +
                                        "             Comission : "+ finalcommission +"\n" +
                                        "            Net Amount : "+aftercommissiontotal+"\n" +
//                                "("+numbertowords+"/-)"+"\n"+
                                        "            Thank You\n" +
                                        "               * * *\n";
                                mService.sendMessage(reciept,"GBK");
                                Log.v("","Message Print \n"+reciept);
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d("status","Connecting.....");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d("status","not found.....");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Toast.makeText(getApplicationContext(), "Device connection was lost",
                            Toast.LENGTH_SHORT).show();
                    Log.d("status","Device connection lost");
//                    btnClose.setEnabled(false);
//                    btnSend.setEnabled(false);
//                    qrCodeBtnSend.setEnabled(false);
//                    btnSendDraw.setEnabled(false);
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    Log.d("status","Unable to connect device");
                    break;
            }
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth open successful", Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(EXTRA_DEVICE_ADDRESS);
                    con_dev = mService.getDevByMac(address);

                    mService.connect(con_dev);
                }
                break;
        }
    }

    @SuppressLint("SdCardPath")
    private void printImage() {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(576);
        pg.initPaint();
        pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
        //
        sendData = pg.printDraw();
        mService.write(sendData);
        Log.d("sendData.length",""+sendData.length);
    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadTask extends AsyncTask<String, String, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                InvoiceActivity.this,R.style.MyAlertDialogStyle);

        protected void onPreExecute() {

            this.dialog.setMessage("Generating Invoice...");
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
            this.dialog.dismiss();
            Log.v("","result"+result);
//            anyType{InvoiceNo=7693927415; Message=SUCCESS; OrderId=9; Status=1; }

            if (result!=null) {

                String data = result.replace("anyType", "").replace("{", "").replace("}", "");
                String[] dateSplit = data.split(";");
                String Invoice = dateSplit[0];
                String Message = dateSplit[1];

                String[] inv = Invoice.split("=");
                InvoiceNumber = inv[1];

                String[] msg = Message.split("=");
                String finalmessage = msg[1];

                if (finalmessage.equalsIgnoreCase("SUCCESS")) {
                    this.dialog.dismiss();
                    appCompatButtonUpdate.setVisibility(View.GONE);
                    appCompatButtonGenerate.setVisibility(View.GONE);
                    appCompatButtonPrint.setVisibility(View.VISIBLE);
                    btnSearch.setVisibility(View.VISIBLE);
                    appCompatButtonExit.setVisibility(View.VISIBLE);
//                    String Mstd = mstdprice.getText().toString();
//                    String Vstd = vstdprice.getText().toString();
//                    String Totstd = totsstdprice.getText().toString();
//                    String Mtm = mtmprice.getText().toString();
//                    String Vtm = vtmprice.getText().toString();
//                    String Tottm = tottmprice.getText().toString();
//                    String Mgold = mgoldprice.getText().toString();
//                    String Vgold = vgoldprice.getText().toString();
//                    String Totgold = totgoldprice.getText().toString();
//                    String Mdtm = mdtmprice.getText().toString();
//                    String Vdtm = vdtmprice.getText().toString();
//                    String Totdtm = totdtmprice.getText().toString();
//                    String Mvij = mvijsmprice.getText().toString();
//                    String Vvij = vvijsmprice.getText().toString();
//                    String TotVij = totvijsmprice.getText().toString();
//                    String Mmm = mmmprice.getText().toString();
//                    String Vmm = vmmprice.getText().toString();
//                    String Totvm = totmmprice.getText().toString();
//                    String Mmodel = mmodsmprice.getText().toString();
//                    String Vmodel = vmodsmprice.getText().toString();
//                    String Totmodel = totmodsmprice.getText().toString();
//                    String Mcow = mcmprice.getText().toString();
//                    String Vcow = vcmprice.getText().toString();
//                    String TotCow = totcmprice.getText().toString();
//                    String Mhun = mhunmlcups.getText().toString();
//                    String Vhun = vhunmlcups.getText().toString();
//                    String Tothun = tothuncups.getText().toString();
//                    String Mcurd = mscurdprice.getText().toString();
//                    String Vcurd = vscurdprice.getText().toString();
//                    String Totcurd = totvscurdprice.getText().toString();
//                    String Mfour = mfourcups.getText().toString();
//                    String Vfour = vfourcups.getText().toString();
//                    String Totfour = totfourcups.getText().toString();
//                    String Mbcurd = mbcurdprice.getText().toString();
//                    String Vbcurd = vbcurdprice.getText().toString();
//                    String Totbcurd = totbcurdprice.getText().toString();
//                    String Mbmilk = mbmilkprice.getText().toString();
//                    String Vbmilk = vbmilkprice.getText().toString();
//                    String Totbmilk = totbmilkprice.getText().toString();
//                    String Mlassi = mlassiprice.getText().toString();
//                    String Vlassi = vlassiprice.getText().toString();
//                    String TotLassi = totlassiprice.getText().toString();
//                    String Midly = midlyprice.getText().toString();
//                    String Vidly = vidlyprice.getText().toString();
//                    String TotIdly = totalidlyprice.getText().toString();
//                    String Mdosa = mdosaprice.getText().toString();
//                    String Vdosa = vdosaprice.getText().toString();
//                    String TotDosa = totaldosaprice.getText().toString();
//                    String TotalAmount = tvtotal.getText().toString();
//                    Intent intent = new Intent(getApplicationContext(), InvoicePrintActivity.class);
//                    intent.putExtra("Mstd", Mstd);
//                    intent.putExtra("Vstd", Vstd);
//                    intent.putExtra("Totstd", Totstd);
//                    intent.putExtra("Mtm", Mtm);
//                    intent.putExtra("Vtm", Vtm);
//                    intent.putExtra("Tottm", Tottm);
//                    intent.putExtra("Mgold", Mgold);
//                    intent.putExtra("Vgold", Vgold);
//                    intent.putExtra("Totgold", Totgold);
//                    intent.putExtra("Mdtm", Mdtm);
//                    intent.putExtra("Vdtm", Vdtm);
//                    intent.putExtra("Totdtm", Totdtm);
//                    intent.putExtra("Mvij", Mvij);
//                    intent.putExtra("Vvij", Vvij);
//                    intent.putExtra("TotVij", TotVij);
//                    intent.putExtra("Mmm", Mmm);
//                    intent.putExtra("Vmm", Vmm);
//                    intent.putExtra("Totvm", Totvm);
//                    intent.putExtra("Mmodel", Mmodel);
//                    intent.putExtra("Vmodel", Vmodel);
//                    intent.putExtra("Totmodel", Totmodel);
//                    intent.putExtra("Mcow", Mcow);
//                    intent.putExtra("Vcow", Vcow);
//                    intent.putExtra("TotCow", TotCow);
//                    intent.putExtra("Mhun", Mhun);
//                    intent.putExtra("Vhun", Vhun);
//                    intent.putExtra("Tothun", Tothun);
//                    intent.putExtra("Mcurd", Mcurd);
//                    intent.putExtra("Vcurd", Vcurd);
//                    intent.putExtra("Totcurd", Totcurd);
//                    intent.putExtra("Mfour", Mfour);
//                    intent.putExtra("Vfour", Vfour);
//                    intent.putExtra("Totfour", Totfour);
//                    intent.putExtra("Mbcurd", Mbcurd);
//                    intent.putExtra("Vbcurd", Vbcurd);
//                    intent.putExtra("Totbcurd", Totbcurd);
//                    intent.putExtra("Mbmilk", Mbmilk);
//                    intent.putExtra("Vbmilk", Vbmilk);
//                    intent.putExtra("Totbmilk", Totbmilk);
//                    intent.putExtra("Mlassi", Mlassi);
//                    intent.putExtra("Vlassi", Vlassi);
//                    intent.putExtra("TotLassi", TotLassi);
//                    intent.putExtra("Midly", Midly);
//                    intent.putExtra("Vidly", Vidly);
//                    intent.putExtra("TotIdly", TotIdly);
//                    intent.putExtra("Mdosa", Mdosa);
//                    intent.putExtra("Vdosa", Vdosa);
//                    intent.putExtra("TotDosa", TotDosa);
//                    intent.putExtra("TotalAmount", TotalAmount);
//                    intent.putExtra("Customerid", Customerid);
//                    intent.putExtra("CustomerName", CustomerName);
//                    intent.putExtra("Commission", Commission);
//                    intent.putExtra("TMCommission", TMCommission);
//                    intent.putExtra("InvoiceNumber", InvoiceNumber);
//                    startActivity(intent);
                } else {
                    this.dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Failed to Generate Invoice", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    public String sendSoapRequest(Context context) throws Exception {
        SharedPreferences prefs = getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
        String agentName = prefs.getString("AgentName", null);
        int AgentId=prefs.getInt("AgentId",0);
        String response="";
        int a;
        int b;
        int cc;
        int d;
        int e;
        int f;
        int g;
        int h;
        int i;
        int j;
        int k;
        int l;
        int m;
        int n;
        int o;
        int p;

        double tota;
        double totb;
        double totcc;
        double totd;
        double tote;
        double totf;
        double totg;
        double toth;
        double toti;
        double totj;
        double totk;
        double totl;
        double totm;
        double totn;
        double toto;
        double totp;

        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String currenttime = mdformat.format(calendar.getTime());
        Log.v("","currenttime"+currenttime);




       try {
           if (!Mstd.equalsIgnoreCase("")){
               a=Integer.parseInt(Mstd);
           }else {
              a=0;
           }
            if (!Mtm.equalsIgnoreCase("")){
                b=Integer.parseInt(Mtm);
            }else {
                b=0;
            }
            if (!Mgold.equalsIgnoreCase("")){
                cc=Integer.parseInt(Mgold);
            }else {
                cc=0;
            }
            if (!Mdtm.equalsIgnoreCase("")){
                d=Integer.parseInt(Mdtm);
            }else {
                d=0;
            }
            if (!Mvij.equalsIgnoreCase("")){
                e=Integer.parseInt(Mvij);
            }else {
                e=0;
            }
            if (!Mmm.equalsIgnoreCase("")){
                f=Integer.parseInt(Mmm);
            }else {
                f=0;
            }
            if (!Mmodel.equalsIgnoreCase("")){
                g=Integer.parseInt(Mmodel);
            }else {
                g=0;
            }
            if (!Mcow.equalsIgnoreCase("")){
                h=Integer.parseInt(Mcow);
            }else {
                h=0;
            }
            if (!Mhun.equalsIgnoreCase("")){
                i=Integer.parseInt(Mhun);
            }else {
                i=0;
            }
            if (!Mcurd.equalsIgnoreCase("")){
                j=Integer.parseInt(Mcurd);
            }else {
                j=0;
            }
            if (!Mfour.equalsIgnoreCase("")){
                k=Integer.parseInt(Mfour);
            }else {
                k=0;
            }
            if (!Mbcurd.equalsIgnoreCase("")){
                l=Integer.parseInt(Mbcurd);
            }else {
                l=0;
            }
            if (!Mbmilk.equalsIgnoreCase("")){
                m=Integer.parseInt(Mbmilk);
            }else {
                m=0;
            }
            if (!Mlassi.equalsIgnoreCase("")){
                n=Integer.parseInt(Mlassi);
            }else {
                n=0;
            }
            if (!Midly.equalsIgnoreCase("")){
                o=Integer.parseInt(Midly);
            }else {
                o=0;
            }
            if (!Mdosa.equalsIgnoreCase("")){
                p=Integer.parseInt(Mdosa);
            }else {
                p=0;
            }


            if (!Totstd.equalsIgnoreCase("")){
                tota=Double.parseDouble(Totstd);
            }else {
                tota=0.00;
            }

            if (!Tottm.equalsIgnoreCase("")){
                totd=Double.parseDouble(Totdtm);
            }else {
                totd=0.00;
            }

            if (!Totgold.equalsIgnoreCase("")){
                totb=Double.parseDouble(Totgold);
            }else {
                totb=0.00;
            }

           if (!Totdtm.equalsIgnoreCase("")){
               totcc=Double.parseDouble(Totdtm);
           }else {
               totcc=0.00;
           }

           if (!TotVij.equalsIgnoreCase("")){
               totp=Double.parseDouble(TotVij);
           }else {
               totp=0.00;
           }
           if (!Totvm.equalsIgnoreCase("")){
               tote=Double.parseDouble(Totvm);
           }else {
               tote=0.00;
           }

           if (!Totmodel.equalsIgnoreCase("")){
               totf=Double.parseDouble(Totmodel);
           }else {
               totf=0.00;
           }

           if (!TotCow.equalsIgnoreCase("")){
               totg=Double.parseDouble(TotCow);
           }else {
               totg=0.00;
           }

           if (!Tothun.equalsIgnoreCase("")){
               toth=Double.parseDouble(Tothun);
           }else {
               toth=0.00;
           }

           if (!Totcurd.equalsIgnoreCase("")){
               toti=Double.parseDouble(Totcurd);
           }else {
               toti=0.00;
           }

           if (!Totfour.equalsIgnoreCase("")){
               totj=Double.parseDouble(Totfour);
           }else {
               totj=0.00;
           }

           if (!Totbcurd.equalsIgnoreCase("")){
               totk=Double.parseDouble(Totbcurd);
           }else {
               totk=0.00;
           }

           if (!Totbmilk.equalsIgnoreCase("")){
               totl=Double.parseDouble(Totbmilk);
           }else {
               totl=0.00;
           }

           if (!TotLassi.equalsIgnoreCase("")){
               totm=Double.parseDouble(TotLassi);
           }else {
               totm=0.00;
           }

           if (!TotIdly.equalsIgnoreCase("")){
               totn=Double.parseDouble(TotIdly);
           }else {
               totn=0.00;
           }

           if (!TotDosa.equalsIgnoreCase("")){
               toto=Double.parseDouble(TotDosa);
           }else {
               toto=0.00;
           }

           double totalamnt = 0.00;
           try{
               if (!Total.equalsIgnoreCase("")){
                   totalamnt=Double.parseDouble(Total);
               }else {
                   totalamnt=0.00;
               }
           }catch (NumberFormatException ee){
               ee.printStackTrace();
           }



           String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                   "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://schemas.datacontract.org/2004/07/InventAPI\" xmlns:ns2=\"http://tempuri.org/\">\n" +
                   "  <SOAP-ENV:Body>\n" +
                   "    <ns2:UpdateOrder>\n" +
                   "      <ns2:orderInfo>\n" +
                   "        <ns1:AgentId> "+ AgentId +" </ns1:AgentId>\n" +
                   "        <ns1:Amount> "+ totalamnt +"</ns1:Amount>\n" +
                   "        <ns1:Commission> "+ finalcommission +" </ns1:Commission>\n" +
                   "        <ns1:CustomerId> "+Customerid+" </ns1:CustomerId>\n" +
                   "        <ns1:Description> "+"N/A"+" </ns1:Description>\n" +
                   "        <ns1:Discount> "+0.00+" </ns1:Discount>\n" +
                   "        <ns1:OrderDate> " + date +"T"+ currenttime + " </ns1:OrderDate>\n" +
                   "        <ns1:OrderDetails>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 2 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mstd+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totstd+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +


                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 3 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mtm+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Tottm+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 4 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mgold+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totgold+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 5 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mdtm+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totdtm+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 6 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mvij+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+TotVij+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 7 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mmm+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totvm+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 8 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mmodel+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totmodel+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 9 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mcow+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+TotCow+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 10 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mhun+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Tothun+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 11 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mcurd+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totcurd+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 12 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mfour+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totfour+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 13 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mbcurd+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totbcurd+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 14 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mbmilk+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+Totbmilk+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 15 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mlassi+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+TotLassi+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 16 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Midly+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+TotIdly+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "       <ns1:OrderDetailInfo>\n" +
                   "            <ns1:ProductId> 18 </ns1:ProductId>\n" +
                   "            <ns1:ProductTypeId> 3 </ns1:ProductTypeId>\n" +
                   "            <ns1:Quantity> "+Mdosa+" </ns1:Quantity>\n" +
                   "            <ns1:TotalCost> "+TotDosa+" </ns1:TotalCost>\n" +
                   "       </ns1:OrderDetailInfo>\n" +

                   "        </ns1:OrderDetails>\n" +
                   "      </ns2:orderInfo>\n" +
                   "    </ns2:UpdateOrder>\n" +
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
           response = RequestSOAP.getProperty(0).toString();
           if (bodyIn instanceof SoapFault) {
               throw (SoapFault) bodyIn;
           }
       }catch (NumberFormatException eh){
           eh.printStackTrace();
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
            httpPost.addHeader("SOAPAction", "http://tempuri.org/IDairyUnifiedAPI/UpdateOrder");
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
}
