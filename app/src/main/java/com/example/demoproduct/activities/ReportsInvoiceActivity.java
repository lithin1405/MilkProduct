package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.adapters.EnglishAdapter;
import com.example.demoproduct.model.CustomerReports;
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

public class ReportsInvoiceActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    AppCompatButton appCompatButtonPrint,btnSearch;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    String strDate;
    private AppCompatTextView mstdprice,mtmprice,mgoldprice,mdtmprice,mvijsmprice,mmmprice,mmodsmprice,mcmprice,
            mhunmlcups,mscurdprice,mfourcups,mbcurdprice,mbmilkprice,mlassiprice,midlyprice,mdosaprice;
    private AppCompatTextView vstdprice,vtmprice,vgoldprice,vdtmprice,vvijsmprice,vmmprice,vmodsmprice,vcmprice,
            vhunmlcups,vscurdprice,vfourcups,vbcurdprice,vbmilkprice,vlassiprice,vidlyprice,vdosaprice;
    private AppCompatTextView totsstdprice,tottmprice,totgoldprice,totdtmprice,totvijsmprice,totmmprice,totmodsmprice,totcmprice,
            tothuncups,totvscurdprice,totfourcups,totbcurdprice,totbmilkprice,totlassiprice,totalidlyprice,totaldosaprice;
    private AppCompatTextView tvtotal,numtowords,agentname,comission,totalqnty,invoice;
    AppCompatTextView date;
    AppCompatTextView netamount;
    String numbertowords;
    ProgressBar loadingProgressBar;
    String FinalTotal;
    String Customerid="";
    String CustomerName="";
    String Description="";
    String Discount="";
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
    String Total;
    String AgentName;
    int TotalQnty;
    String currenttime;


    
    String InvoiceNo="";
    LinearLayoutCompat llmilklayout;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_invoice);
        InvoiceNo = getIntent().getStringExtra("InvoiceNo");
        SharedPreferences prefs = getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
        AgentName = prefs.getString("AgentName", null);
//        Calendar calendar = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
//        currenttime = mdformat.format(calendar.getTime());
        loadingProgressBar = findViewById(R.id.loading_progressbar);
        loadingProgressBar.setVisibility(View.VISIBLE);
        llmilklayout=findViewById(R.id.llmilklayout);
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
            new ReportsInvoiceActivity.DownloadTask().execute();
            // notify user you are online

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }


        mService = new BluetoothService(this, mHandler);

        if(!mService.isAvailable()){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
//        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        appCompatButtonPrint =(AppCompatButton)findViewById(R.id.appCompatButtonPrint);
        btnSearch = (AppCompatButton)findViewById(R.id.btnSearch);




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

        numtowords=findViewById(R.id.numtowords);

        agentname=findViewById(R.id.agentname);

        netamount=findViewById(R.id.netamount);

        comission=findViewById(R.id.comission);

        totalqnty=findViewById(R.id.totalqnty);

        invoice=findViewById(R.id.invoice);

        date=findViewById(R.id.date);
//        Calendar c = Calendar.getInstance();
//        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//        strDate = sdf.format(c.getTime());
//        date.setText("Date:"+strDate);
        appCompatButtonPrint.setOnClickListener(new ReportsInvoiceActivity.ClickEvent());

        btnSearch.setOnClickListener(new ReportsInvoiceActivity.ClickEvent());

//        agentname.setText("Agent Name : "+AgentName);
//        mstdprice.setText(Mstd);
//        vstdprice.setText(Vstd);
//        totsstdprice.setText(Totstd);
//
//        mtmprice.setText(Mtm);
//        vtmprice.setText(Vtm);
//        tottmprice.setText(Tottm);

//        mgoldprice.setText(Mgold);
//        vgoldprice.setText(Vgold);
//        totgoldprice.setText(Totgold);
//
//        mdtmprice.setText(Mdtm);
//        vdtmprice.setText(Vdtm);
//        totdtmprice.setText(Totdtm);
//
//        mvijsmprice.setText(Mvij);
//        vvijsmprice.setText(Vvij);
//        totvijsmprice.setText(TotVij);
//
//        mmmprice.setText(Mmm);
//        vmmprice.setText(Vmm);
//        totmmprice.setText(Totvm);
//
//
//
//        mmodsmprice.setText(Mmodel);
//        vmodsmprice.setText(Vmodel);
//        totmodsmprice.setText(Totmodel);
//
//        mcmprice.setText(Mcow);
//        vcmprice.setText(Vcow);
//        totcmprice.setText(TotCow);
//
//
//
//        mhunmlcups.setText(Mhun);
//        vhunmlcups.setText(Vhun);
//        tothuncups.setText(Tothun);
//
//        mscurdprice.setText(Mcurd);
//        vscurdprice.setText(Vcurd);
//        totvscurdprice.setText(Totcurd);
//
//
//
//        mfourcups.setText(Mfour);
//        vfourcups.setText(Vfour);
//        totfourcups.setText(Totfour);
//
//        mbcurdprice.setText(Mbcurd);
//        vbcurdprice.setText(Vbcurd);
//        totbcurdprice.setText(Totbcurd);
//
//
//
//        mbmilkprice.setText(Mbmilk);
//        vbmilkprice.setText(Vbmilk);
//        totbmilkprice.setText(Totbmilk);
//
//        mlassiprice.setText(Mlassi);
//        vlassiprice.setText(Vlassi);
//        totlassiprice.setText(TotLassi);
//
//
//
//        midlyprice.setText(Midly);
//        vidlyprice.setText(Vidly);
//        totalidlyprice.setText(TotIdly);
//
//        mdosaprice.setText(Mdosa);
//        vdosaprice.setText(Vdosa);
//        totaldosaprice.setText(TotDosa);

//        invoice.setText("Invoice No:"+InvoiceNumber);

//        int a=Integer.parseInt(Mstd);
//        int b=Integer.parseInt(Mtm);
//        int cc=Integer.parseInt(Mgold);
//        int d=Integer.parseInt(Mdtm);
//        int e=Integer.parseInt(Mvij);
//        int f=Integer.parseInt(Mmm);
//        int g=Integer.parseInt(Mmodel);
//        int h=Integer.parseInt(Mcow);
//        int i=Integer.parseInt(Mhun);
//        int j=Integer.parseInt(Mcurd);
//        int k=Integer.parseInt(Mfour);
//        int l=Integer.parseInt(Mbcurd);
//        int m=Integer.parseInt(Mbmilk);
//        int n=Integer.parseInt(Mlassi);
//        int o=Integer.parseInt(Midly);
//        int p=Integer.parseInt(Mdosa);
//
//        TotalQnty= a + b + cc + d + e + f + g + h + i + j + k + l + m + n + o + p;
//
//        totalqnty.setText(""+TotalQnty);
//
//        creatingViews();
    }

    @SuppressLint("SetTextI18n")
    private void creatingViews() {
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

       try {
            a=Integer.parseInt(Mstd);
            b=Integer.parseInt(Mtm);
            cc=Integer.parseInt(Mgold);
            d=Integer.parseInt(Mdtm);
            e=Integer.parseInt(Mvij);
            f=Integer.parseInt(Mmm);
            g=Integer.parseInt(Mmodel);
            h=Integer.parseInt(Mcow);
            i=Integer.parseInt(Mhun);
            j=Integer.parseInt(Mcurd);
            k=Integer.parseInt(Mfour);
            l=Integer.parseInt(Mbcurd);
            m=Integer.parseInt(Mbmilk);
            n=Integer.parseInt(Mlassi);
            o=Integer.parseInt(Midly);
            p=Integer.parseInt(Mdosa);

           TotalQnty= a + b + cc + d + e + f + g + h + i + j + k + l + m + n + o + p;

           totalqnty.setText(""+TotalQnty);
       }catch (NumberFormatException eh){
           eh.printStackTrace();
       }
        finalcommission=Double.parseDouble(Commission);

        Log.v("","finalcommission"+finalcommission);

        double qq=Double.parseDouble(FinalTotal);
        Log.v("","afterdeduction"+qq);

        aftercommissiontotal= qq - finalcommission;
        Log.v("","aftercommissiontotal"+aftercommissiontotal);

        llmilklayout.setVisibility(View.VISIBLE);

//        tvtotal.setText(String.valueOf(qq));
//        netamount.setText("Net Amount:"+aftercommissiontotal);
//        comission.setText(String.valueOf("Commission : "+finalcommission));
        try {
            numbertowords =   Currency.convertToIndianCurrency(String.valueOf(aftercommissiontotal));
            numtowords.setText("( "+numbertowords+"/-)");

        }catch (NumberFormatException hj){
            hj.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if( mService.isBTopen() == false){
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
                            con_dev = mService.getDevByMac( device.getAddress());

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
//                case R.id.btnSend:
//                    msg = edtContext.getText().toString();
//                    if( msg.length() > 0 ){
//                        mService.sendMessage(msg, "GBK");
//                    }
//                    break;
//                case R.id.qr_code_Send:
//                    cmd = new byte[7];
//                    cmd[0] = 0x1B;
//                    cmd[1] = 0x5A;
//                    cmd[2] = 0x00;
//                    cmd[3] = 0x02;
//                    cmd[4] = 0x07;
//                    cmd[5] = 0x17;
//                    cmd[6] = 0x00;
//                    msg = getResources().getString(R.string.bluetooth_qr_code_Send_string);
//                    if( msg.length() > 0){
//                        mService.write(cmd);
//                        mService.sendMessage(msg, "GBK");
//                    }
//                    break;
//                case R.id.btnClose:
//                    mService.stop();
//                    break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private  final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String reciept = "";
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
//                            btnClose.setEnabled(true);
//                            btnSend.setEnabled(true);
//                            qrCodeBtnSend.setEnabled(true);
//                            btnSendDraw.setEnabled(true);

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
                                        "Invoice No: "+InvoiceNo+"\n"+
                                        "Date: "+strDate + " "+currenttime+"\n"+
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

        ProgressDialog progressDialog;

        protected void onPreExecute() {

            loadingProgressBar.setVisibility(View.VISIBLE);

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


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("transactionresponse", result);
//            anyType{ReportDetails=anyType{ReportRec=anyType{Comm=2; CustName=SRINU; InvDate=7/19/2019; InvNo=5051041657; NetAmount=0; Qty=2; }; ReportRec=anyType{Comm=2; CustName=SRINU; InvDate=7/19/2019; InvNo=1395734412; NetAmount=0; Qty=2; }; }; TotalAmount=0; TotalComm=4; TotalQty=4; }
                String data = result.replace("ReportDetails=anyType", "").replace("{","").replace("}","").replace("ReportRec=anyType", "").replace("; ; ; Status=1;","");
                String data1=data.replace("anyType","").replace("Customer=","").replace(" Message=SUCCESS;","");
                Log.v("", "data" + data1);
//            Comm=0;CustName=MAHALAXMITRADERS;InvDate=7/2/20199:17:21AM;InvNo=2749495311;NetAmount=100;Qty=0
                String[] parts = data1.split(" OrderDetails=OrderDetailInfo=");
                String name1 = parts[0];
                String name2 = parts[1];
                Log.v("", "name1" + name1);
                Log.v("", "name2" + name2);

//            name1=    AgentName=Phani; Amount=0.00; Commission=2.00; CustomerName=SRINU; Description= N/A ; Discount=0.00; OrderDate=2019-07-19T11:47:43;
//           name2 =  ProductId=2; ProductTypeId=3; Quantity=2; TotalCost=96.00; ; OrderDetailInfo=ProductId=3; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=4; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=5; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=6; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=7; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=8; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=9; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=10; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=11; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=12; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=13; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=14; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=15; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=16; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=18; ProductTypeId=3; Quantity=0; TotalCost=0.00

                String[] aaa=name1.split("; ");
                invoice.setText("Invoice No :"+InvoiceNo);
                agentname.setText("Agent Name : "+aaa[0].replace("AgentName=",""));
                tvtotal.setText(aaa[1].replace("Amount=",""));
                FinalTotal=aaa[1].replace("Amount=","");

                comission.setText("Commission : "+aaa[2].replace("Commission=",""));
                Commission=aaa[2].replace("Commission=","");
                String Comm=aaa[2].replace("Commission=","");
                double means;
                double commii;
                double finale;
                try {
                    means=Double.parseDouble(FinalTotal);
                    Log.v("","means"+means);
                    commii=Double.parseDouble(Comm);
                    Log.v("","commii"+commii);
                    finale=means - commii;
                    Log.v("","finale"+finale);
                    String FinalTotal=String.valueOf(finale);
                    netamount.setText("Net Amount :"+FinalTotal);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
                CustomerName=aaa[3].replace("CustomerName=","");
                Description=aaa[4].replace("Description= ","");
                Discount=aaa[5].replace("Discount=","");
                String currentdate=aaa[6].replace("OrderDate=","");
                String [] ccc=currentdate.split("T");
                date.setText("Date:"+ccc[0]);
                strDate=ccc[0];
                currenttime=ccc[1];


//            name2 =  ProductId=2; ProductTypeId=3; Quantity=2; TotalCost=96.00; ; OrderDetailInfo=ProductId=3; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=4; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=5; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=6; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=7; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=8; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=9; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=10; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=11; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=12; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=13; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=14; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=15; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=16; ProductTypeId=3; Quantity=0; TotalCost=0.00; ; OrderDetailInfo=ProductId=18; ProductTypeId=3; Quantity=0; TotalCost=0.00
            String[] bbb = name2.split("; ; ");
            String aaaa=bbb[0];
            Log.v("","Product 0"+aaaa);
            String bbbb=bbb[1];
            Log.v("","Product 1"+bbbb);
            String cccc=bbb[2];
            Log.v("","Product 2"+cccc);
            String dddd=bbb[3];
            Log.v("","Product 3"+dddd);
            String eeee=bbb[4];
            Log.v("","Product 4"+eeee);
            String ffff=bbb[5];
            Log.v("","Product 5"+ffff);
            String gggg=bbb[6];
            Log.v("","Product 6"+gggg);
            String hhhh=bbb[7];
            Log.v("","Product 7"+hhhh);
            String iiii=bbb[8];
            Log.v("","Product 8"+iiii);
            String jjjj=bbb[9];
            Log.v("","Product 9"+jjjj);
            String kkkk=bbb[10];
            Log.v("","Product 10"+kkkk);

            String llll=bbb[11];
            Log.v("","Product 11"+llll);
            String mmmm=bbb[12];
            Log.v("","Product 12"+mmmm);
            String nnnn=bbb[13];
            Log.v("","Product 13"+nnnn);
            String oooo=bbb[14];
            Log.v("","Product 14"+oooo);
            String pppp=bbb[15];
            Log.v("","Product 15"+pppp);

            String[] ppp=aaaa.split("; ");
            mstdprice.setText(ppp[2].replace("Quantity=",""));
            totsstdprice.setText(ppp[3].replace("TotalCost=",""));
            Mstd=ppp[2].replace("Quantity=","");
            Totstd=ppp[3].replace("TotalCost=","");

            String[] mmm=bbbb.split("; ");
            mtmprice.setText(mmm[2].replace("Quantity=",""));
            tottmprice.setText(mmm[3].replace("TotalCost=",""));
            Mtm=mmm[2].replace("Quantity=","");
            Tottm=mmm[3].replace("TotalCost=","");

            String[] nnn=cccc.split("; ");
            mgoldprice.setText(nnn[2].replace("Quantity=",""));
            totgoldprice.setText(nnn[3].replace("TotalCost=",""));
            Mgold=nnn[2].replace("Quantity=","");
            Totgold=nnn[3].replace("TotalCost=","");


            String[] qqq=dddd.split("; ");
            mdtmprice.setText(qqq[2].replace("Quantity=",""));
            totdtmprice.setText(qqq[3].replace("TotalCost=",""));
            Mdtm=qqq[2].replace("Quantity=","");
            Totdtm=qqq[3].replace("TotalCost=","");

            String[] rrr=eeee.split("; ");
            mvijsmprice.setText(rrr[2].replace("Quantity=",""));
            totvijsmprice.setText(rrr[3].replace("TotalCost=",""));
            Mvij=rrr[2].replace("Quantity=","");
            TotVij=rrr[3].replace("TotalCost=","");

            String[] sss=ffff.split("; ");
            mmmprice.setText(sss[2].replace("Quantity=",""));
            totmmprice.setText(sss[3].replace("TotalCost=",""));
            Mmm=sss[2].replace("Quantity=","");
            Totvm=sss[3].replace("TotalCost=","");

            String[] ttt=gggg.split("; ");
            mmodsmprice.setText(ttt[2].replace("Quantity=",""));
            totmodsmprice.setText(ttt[3].replace("TotalCost=",""));
            Mmodel=ttt[2].replace("Quantity=","");
            Totmodel=ttt[3].replace("TotalCost=","");

            String[] uuu=hhhh.split("; ");
            mcmprice.setText(uuu[2].replace("Quantity=",""));
            totcmprice.setText(uuu[3].replace("TotalCost=",""));
            Mcow=uuu[2].replace("Quantity=","");
            TotCow=uuu[3].replace("TotalCost=","");

            String[] vvv=iiii.split("; ");
            mhunmlcups.setText(vvv[2].replace("Quantity=",""));
            tothuncups.setText(vvv[3].replace("TotalCost=",""));
            Mhun=vvv[2].replace("Quantity=","");
            Tothun=vvv[3].replace("TotalCost=","");

            String[] www=jjjj.split("; ");
            mscurdprice.setText(www[2].replace("Quantity=",""));
            totvscurdprice.setText(www[3].replace("TotalCost=",""));
            Mcurd=www[2].replace("Quantity=","");
            Totcurd=www[3].replace("TotalCost=","");

            String[] xxx=kkkk.split("; ");
            mfourcups.setText(xxx[2].replace("Quantity=",""));
            totfourcups.setText(xxx[3].replace("TotalCost=",""));
            Mfour=xxx[2].replace("Quantity=","");
            Totfour=xxx[3].replace("TotalCost=","");

            String[] yyy=llll.split("; ");
            mbcurdprice.setText(yyy[2].replace("Quantity=",""));
            totbcurdprice.setText(yyy[3].replace("TotalCost=",""));
            Mbcurd=yyy[2].replace("Quantity=","");
            Totbcurd=yyy[3].replace("TotalCost=","");

            String[] zzz=mmmm.split("; ");
            mbmilkprice.setText(zzz[2].replace("Quantity=",""));
            totbmilkprice.setText(zzz[3].replace("TotalCost=",""));
            Mbmilk=zzz[2].replace("Quantity=","");
            Totbmilk=zzz[3].replace("TotalCost=","");

            String[] aaaaa=nnnn.split("; ");
            mlassiprice.setText(aaaaa[2].replace("Quantity=",""));
            totlassiprice.setText(aaaaa[3].replace("TotalCost=",""));
            Mlassi=aaaaa[2].replace("Quantity=","");
            TotLassi=aaaaa[3].replace("TotalCost=","");

            String[] bbbbb=oooo.split("; ");
            midlyprice.setText(bbbbb[2].replace("Quantity=",""));
            totalidlyprice.setText(bbbbb[3].replace("TotalCost=",""));
            Midly=bbbbb[2].replace("Quantity=","");
            TotIdly=bbbbb[3].replace("TotalCost=","");

            String[] ccccc=pppp.split("; ");
            mdosaprice.setText(ccccc[2].replace("Quantity=",""));
            totaldosaprice.setText(ccccc[3].replace("TotalCost=",""));
            Mdosa=ccccc[2].replace("Quantity=","");
            TotDosa=ccccc[3].replace("TotalCost=","");

            creatingViews();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                loadingProgressBar.setVisibility(View.GONE);
            }
        }



    public String sendSoapRequest(Context context) throws Exception {


        String finalString = "<?xml version =\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://tempuri.org/\">\n" +
                "  <SOAP-ENV:Body>\n" +
                "    <ns1:GetInvoiceInfo>\n" +
                "      <ns1:invoiceNo>"+InvoiceNo+"</ns1:invoiceNo>\n" +
                "    </ns1:GetInvoiceInfo>\n" +
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
            httpPost.addHeader("SOAPAction", "http://tempuri.org/IDairyUnifiedAPI/GetInvoiceInfo");
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

