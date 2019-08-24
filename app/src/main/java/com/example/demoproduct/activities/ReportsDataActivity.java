package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproduct.R;
import com.example.demoproduct.adapters.EnglishAdapter;
import com.example.demoproduct.model.CustomerReports;
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
import java.util.Arrays;
import java.util.Objects;

public class ReportsDataActivity extends AppCompatActivity {
    ProgressBar loadingProgressBar;
    String Fromdate="";
    String Todate="";
    ArrayList<CustomerReports> totaldatalist;
    ArrayList<CustomerReports> CustNameList;
    ArrayList<CustomerReports> InvDateList ;
    ArrayList<CustomerReports> InvNoList;
    ArrayList<CustomerReports> NetAmountList;
    ArrayList<CustomerReports> QtyList;
    String TotalAmount="";
    String TotalComm="";
    String TotalQty="";
    EnglishAdapter adapter;
    RecyclerView recyclerView;
    TextView totalamount,totalcommission,tottalqty;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_data);
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ReportsDataActivity.this);
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
        loadingProgressBar = findViewById(R.id.loading_progressbar);
        loadingProgressBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        Fromdate= Objects.requireNonNull(intent.getExtras()).getString("Fromdate");
        Todate = Objects.requireNonNull(intent.getExtras()).getString("Todate");
        totaldatalist=new ArrayList<>();
        CustNameList=new ArrayList<>();
        InvDateList=new ArrayList<>();
        InvNoList=new ArrayList<>();
        NetAmountList=new ArrayList<>();
        QtyList=new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.installed_app_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        totalamount=(TextView)findViewById(R.id.totalamount);
        totalcommission=(TextView)findViewById(R.id.totalcommission);
        tottalqty=(TextView)findViewById(R.id.tottalqty);
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
                new ReportsDataActivity.DownloadTask().execute();
                // notify user you are online

        }
        else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
        }

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


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.i("transactionresponse", result);
//            transactionresponse: anyType{ReportDetails=anyType{}; TotalAmount=0; TotalComm=0; TotalQty=0; }

//            anyType{ReportDetails=anyType{ReportRec=anyType{Comm=0; CustName=MAHALAXMI TRADERS; InvDate=7/2/2019 9:17:21 AM; InvNo=2749495311; NetAmount=100; Qty=0; }; ReportRec=anyType{Comm=0; CustName=MAHALAXMI TRADERS; InvDate=7/2/2019 9:17:21 AM; InvNo=2677666407; NetAmount=100; Qty=4; }; ReportRec=anyType{Comm=0; CustName=MAHALAXMI TRADERS; InvDate=7/2/2019 9:17:21 AM; InvNo=1743176164; NetAmount=100; Qty=4; }; ReportRec=anyType{Comm=0; CustName=MAHALAXMI TRADERS; InvDate=7/2/2019 9:17:21 AM; InvNo=2527444288; NetAmount=100; Qty=4; }; }; TotalAmount=400; TotalComm=0; TotalQty=12; }

//            anyType{Customers=anyType{Customer=anyType{Commission=0.00; CustomerId=18; CustomerName=SRINU; TMCommission=0.00; }; Customer=anyType{Commission=0.00; CustomerId=20; CustomerName=MAHALAXMI TRADERS; TMCommission=0.00; };
            if (result.trim().equalsIgnoreCase("anyType{ReportDetails=anyType{}; TotalAmount=0; TotalComm=0; TotalQty=0; }")){
                loadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Data not Found",Toast.LENGTH_LONG).show();
                finish();

             }else {
                String data = result.replace("ReportDetails=anyType", "").replace("ReportRec=anyType", "").replace("{", "").replace("}", "");
//            String data1=data.replace("anyType","").replace(" ","").replace("Customer=","").replace("Message=SUCCESS;Status=1;","");
                Log.v("", "data" + data);
//            Comm=0;CustName=MAHALAXMITRADERS;InvDate=7/2/20199:17:21AM;InvNo=2749495311;NetAmount=100;Qty=0
                String[] parts = data.split("; ; ;");
                String name1 = parts[0];
                String name2 = parts[1];
                Log.v("", "name1" + name1);
                Log.v("", "name2" + name2);

                String[] aaa = name1.replace("anyType", "").split("; ; ");
                for (String part : aaa) {
                    //do something interesting here
                    System.out.print(part);
                    Log.v("", "part" + part);
//                Commission=0.00;CustomerId=18;CustomerName=SRINU;TMCommission=0.00

                    try {
                        String[] temp = part.split("; ");
                        CustomerReports customerReports = new CustomerReports();
                        String commission = temp[0].replace("Comm=", "");

                        customerReports.setComm(commission);

                        String customername = temp[1].replace("CustName=", "");

                        customerReports.setCustName(customername);

                        String invoicedate = temp[2].replace("InvDate=", "");

                        customerReports.setInvDate(invoicedate);

                        String invoiceno = temp[3].replace("InvNo=", "");


                        customerReports.setInvNo(invoiceno);
                        String netamount = temp[4].replace("NetAmount=", "");

                        customerReports.setNetAmount(netamount);

                        String qnty = temp[5].replace("Qty=", "");

                        customerReports.setQty(qnty);
                        totaldatalist.add(customerReports);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                String[] bbb = name2.split("; ");
                TotalAmount = bbb[0].replace("TotalAmount=", "");
                TotalComm = bbb[1].replace("TotalComm=", "");
                TotalQty = bbb[2].replace("TotalQty=", "");

                Log.v("", "TotalAmount" + TotalAmount);
                Log.v("", "TotalComm" + TotalComm);
                Log.v("", "TotalQty" + TotalQty);
                totalamount.setText(new DecimalFormat("##.##").format(Double.parseDouble(TotalAmount)));
                totalcommission.setText(new DecimalFormat("##.##").format(Double.parseDouble(TotalComm)));
                tottalqty.setText(new DecimalFormat("##.##").format(Double.parseDouble(TotalQty)));

                Log.v("", "CommList.size()" + totaldatalist.size());

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                loadingProgressBar.setVisibility(View.GONE);
                creatingViews();
            }
        }

    }

    private void creatingViews() {
        Log.v("","CommList.size()"+totaldatalist.size());
        adapter = new EnglishAdapter(this, totaldatalist);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(ReportsDataActivity.this,
                DividerItemDecoration.VERTICAL));
    }


    public String sendSoapRequest(Context context) throws Exception {


        String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://schemas.datacontract.org/2004/07/InventAPI\" xmlns:ns2=\"http://tempuri.org/\">\n" +
                "  <SOAP-ENV:Body>\n" +
                "    <ns2:GetReportInfo>\n" +
                "      <ns2:reportReq>\n" +
                "        <ns1:FromDate>"+Fromdate+"</ns1:FromDate>\n" +
                "        <ns1:HashValue>?</ns1:HashValue>\n" +
                "        <ns1:ToDate>"+Todate+"</ns1:ToDate>\n" +
                "      </ns2:reportReq>\n" +
                "    </ns2:GetReportInfo>\n" +
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
            httpPost.addHeader("SOAPAction", "http://tempuri.org/IDairyUnifiedAPI/GetReportInfo");
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
