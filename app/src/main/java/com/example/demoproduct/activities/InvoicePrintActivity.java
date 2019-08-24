package com.example.demoproduct.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.printer.DeviceListActivity;
import com.hoin.btsdk.BluetoothService;
import com.hoin.btsdk.PrintPic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.Set;

import static com.example.demoproduct.printer.DeviceListActivity.*;



public class InvoicePrintActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;
    AppCompatButton appCompatButtonPrint,btnSearch,appCompatButtonExit;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    String strDate;
    private AppCompatTextView mstdprice,mtmprice,mgoldprice,mdtmprice,mvijsmprice,mmmprice,mmodsmprice,mcmprice,
            mhunmlcups,mscurdprice,mfourcups,mbcurdprice,mbmilkprice,mlassiprice,midlyprice,mdosaprice;
    private AppCompatTextView vstdprice,vtmprice,vgoldprice,vdtmprice,vvijsmprice,vmmprice,vmodsmprice,vcmprice,
            vhunmlcups,vscurdprice,vfourcups,vbcurdprice,vbmilkprice,vlassiprice,vidlyprice,vdosaprice;
    private AppCompatTextView totsstdprice,tottmprice,totgoldprice,totdtmprice,totvijsmprice,totmmprice,totmodsmprice,totcmprice,
            tothuncups,totvscurdprice,totfourcups,totbcurdprice,totbmilkprice,totlassiprice,totalidlyprice,totaldosaprice;
    private AppCompatTextView tvtotal,numtowords,agentname,comission,totalqnty,invoice;
    AppCompatTextView netamount;
    String numbertowords;

    String FinalTotal;
    String Customerid="";
    String CustomerName="";
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
    String InvoiceNumber="";
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print);
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
        String TotalAmount = getIntent().getStringExtra("TotalAmount");
        Total=TotalAmount.replace("Total Cost:","");
        FinalTotal=Total.replace("Total Cost:","");
        Customerid=getIntent().getStringExtra("Customerid");
        CustomerName=getIntent().getStringExtra("CustomerName");
        Commission=getIntent().getStringExtra("Commission");
        TMCommission=getIntent().getStringExtra("TMCommission");
        InvoiceNumber=getIntent().getStringExtra("InvoiceNumber");
        Log.v("","Customerid"+Customerid);
        Log.v("","CustomerName"+CustomerName);
        Log.v("","Commission"+Commission);
        Log.v("","TMCommission"+TMCommission);


        mService = new BluetoothService(this, mHandler);

        if(!mService.isAvailable()){
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
//        final View rootView = getWindow().getDecorView().findViewById(android.R.id.content);

        appCompatButtonPrint =findViewById(R.id.appCompatButtonPrint);
        appCompatButtonExit=findViewById(R.id.appCompatButtonExit);
        btnSearch = findViewById(R.id.btnSearch);

        appCompatButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),SellProductsActivity.class);
                startActivity(intent);
            }
        });



         mstdprice=(AppCompatTextView)findViewById(R.id.mstdprice);
         vstdprice=(AppCompatTextView)findViewById(R.id.vstdprice);

         mtmprice=(AppCompatTextView)findViewById(R.id.mtmprice);
         vtmprice=(AppCompatTextView)findViewById(R.id.vtmprice);

         mgoldprice=(AppCompatTextView)findViewById(R.id.mgoldprice);
         vgoldprice=(AppCompatTextView)findViewById(R.id.vgoldprice);

         mdtmprice=(AppCompatTextView)findViewById(R.id.mdtmprice);
         vdtmprice=(AppCompatTextView)findViewById(R.id.vdtmprice);

         mvijsmprice=(AppCompatTextView)findViewById(R.id.mvijsmprice);
         vvijsmprice=(AppCompatTextView)findViewById(R.id.vvijsmprice);

         mmmprice=(AppCompatTextView)findViewById(R.id.mmmprice);
         vmmprice=(AppCompatTextView)findViewById(R.id.vmmprice);

         mmodsmprice=(AppCompatTextView)findViewById(R.id.mmodsmprice);
         vmodsmprice=(AppCompatTextView)findViewById(R.id.vmodsmprice);

         mcmprice=(AppCompatTextView)findViewById(R.id.mcmprice);
         vcmprice=(AppCompatTextView)findViewById(R.id.vcmprice);

         mhunmlcups=(AppCompatTextView)findViewById(R.id.mhunmlcups);
         vhunmlcups=(AppCompatTextView)findViewById(R.id.vhunmlcups);

         mscurdprice=(AppCompatTextView)findViewById(R.id.mscurdprice);
         vscurdprice=(AppCompatTextView)findViewById(R.id.vscurdprice);

         mfourcups=(AppCompatTextView)findViewById(R.id.mfourcups);
         vfourcups=(AppCompatTextView)findViewById(R.id.vfourcups);

         mbcurdprice=(AppCompatTextView)findViewById(R.id.mbcurdprice);
         vbcurdprice=(AppCompatTextView)findViewById(R.id.vbcurdprice);

         mbmilkprice=(AppCompatTextView)findViewById(R.id.mbmilkprice);
         vbmilkprice=(AppCompatTextView)findViewById(R.id.vbmilkprice);

         mlassiprice=(AppCompatTextView)findViewById(R.id.mlassiprice);
         vlassiprice=(AppCompatTextView)findViewById(R.id.vlassiprice);

         midlyprice=(AppCompatTextView)findViewById(R.id.midlyprice);
         vidlyprice=(AppCompatTextView)findViewById(R.id.vidlyprice);

         mdosaprice=(AppCompatTextView)findViewById(R.id.mdosaprice);
         vdosaprice=(AppCompatTextView)findViewById(R.id.vdosaprice);

         totsstdprice=(AppCompatTextView)findViewById(R.id.totsstdprice);

         tottmprice=(AppCompatTextView)findViewById(R.id.tottmprice);

         totgoldprice=(AppCompatTextView)findViewById(R.id.totgoldprice);

         totdtmprice=(AppCompatTextView)findViewById(R.id.totdtmprice);

         totvijsmprice=(AppCompatTextView)findViewById(R.id.totvijsmprice);

         totmmprice=(AppCompatTextView)findViewById(R.id.totmmprice);

         totmodsmprice=(AppCompatTextView)findViewById(R.id.totmodsmprice);

         totcmprice=(AppCompatTextView)findViewById(R.id.totcmprice);

         tothuncups=(AppCompatTextView)findViewById(R.id.tothuncups);

         totvscurdprice=(AppCompatTextView)findViewById(R.id.totvscurdprice);

         totfourcups=(AppCompatTextView)findViewById(R.id.totfourcups);

         totbcurdprice=(AppCompatTextView)findViewById(R.id.totbcurdprice);

         totbmilkprice=(AppCompatTextView)findViewById(R.id.totbmilkprice);

         totlassiprice=(AppCompatTextView)findViewById(R.id.totlassiprice);

         totalidlyprice=(AppCompatTextView)findViewById(R.id.totalidlyprice);

         totaldosaprice=(AppCompatTextView)findViewById(R.id.totaldosaprice);

         tvtotal=(AppCompatTextView)findViewById(R.id.tvtotal);

         numtowords=(AppCompatTextView)findViewById(R.id.numtowords);

        agentname=(AppCompatTextView)findViewById(R.id.agentname);

        netamount=(AppCompatTextView)findViewById(R.id.netamount);

        comission=(AppCompatTextView)findViewById(R.id.comission);

        totalqnty=(AppCompatTextView)findViewById(R.id.totalqnty);

        invoice=(AppCompatTextView)findViewById(R.id.invoice);

        AppCompatTextView date=(AppCompatTextView)findViewById(R.id.date);
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        strDate = sdf.format(c.getTime());
        date.setText("Date:"+strDate);
        SharedPreferences prefs = getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
        AgentName = prefs.getString("AgentName", null);
        Calendar calendar = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
         currenttime = mdformat.format(calendar.getTime());

        appCompatButtonPrint.setOnClickListener(new ClickEvent());

        btnSearch.setOnClickListener(new ClickEvent());

        agentname.setText("Agent Name : "+AgentName);
        mstdprice.setText(Mstd);
        vstdprice.setText(Vstd);
        totsstdprice.setText(Totstd);

        mtmprice.setText(Mtm);
        vtmprice.setText(Vtm);
        tottmprice.setText(Tottm);

        mgoldprice.setText(Mgold);
        vgoldprice.setText(Vgold);
        totgoldprice.setText(Totgold);

        mdtmprice.setText(Mdtm);
        vdtmprice.setText(Vdtm);
        totdtmprice.setText(Totdtm);

        mvijsmprice.setText(Mvij);
        vvijsmprice.setText(Vvij);
        totvijsmprice.setText(TotVij);

        mmmprice.setText(Mmm);
        vmmprice.setText(Vmm);
        totmmprice.setText(Totvm);



        mmodsmprice.setText(Mmodel);
        vmodsmprice.setText(Vmodel);
        totmodsmprice.setText(Totmodel);

        mcmprice.setText(Mcow);
        vcmprice.setText(Vcow);
        totcmprice.setText(TotCow);



        mhunmlcups.setText(Mhun);
        vhunmlcups.setText(Vhun);
        tothuncups.setText(Tothun);

        mscurdprice.setText(Mcurd);
        vscurdprice.setText(Vcurd);
        totvscurdprice.setText(Totcurd);



        mfourcups.setText(Mfour);
        vfourcups.setText(Vfour);
        totfourcups.setText(Totfour);

        mbcurdprice.setText(Mbcurd);
        vbcurdprice.setText(Vbcurd);
        totbcurdprice.setText(Totbcurd);



        mbmilkprice.setText(Mbmilk);
        vbmilkprice.setText(Vbmilk);
        totbmilkprice.setText(Totbmilk);

        mlassiprice.setText(Mlassi);
        vlassiprice.setText(Vlassi);
        totlassiprice.setText(TotLassi);



        midlyprice.setText(Midly);
        vidlyprice.setText(Vidly);
        totalidlyprice.setText(TotIdly);

        mdosaprice.setText(Mdosa);
        vdosaprice.setText(Vdosa);
        totaldosaprice.setText(TotDosa);

        invoice.setText("Invoice No:"+InvoiceNumber);

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

        totalqnty.setText(""+TotalQnty);

        creatingViews();
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

        tvtotal.setText(String.valueOf(qq));
        netamount.setText("Net Amount:"+aftercommissiontotal);
        comission.setText(String.valueOf("Commission : "+finalcommission));
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
                    con_dev = mService.getDevByMac("DC:0D:30:75:EF:41");

                    mService.connect(con_dev);
                    Set<BluetoothDevice> pairedDevices = mService.getPairedDev();

                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            device.getName();
                            Toast.makeText(getApplicationContext(),"Device Name:"+device.getName(),Toast.LENGTH_SHORT).show();
                            device.getAddress();
                            Toast.makeText(getApplicationContext(),"Device Address:"+device.getAddress(),Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String noDevices = getResources().getText(R.string.bluetooth_none_paired).toString();
                        Toast.makeText(getApplicationContext(),""+noDevices,Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.btnSearch:
                    Intent serverIntent = new Intent(getApplicationContext(),DeviceListActivity.class);
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




    /**

     */
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
                                        "Invoice No: "+InvoiceNumber+"\n"+
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


}
