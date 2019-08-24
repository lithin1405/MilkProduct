package com.example.demoproduct.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproduct.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.StringTokenizer;

public class LoginActivity extends AppCompatActivity {

    private AppCompatEditText textInputEditTextEmail,textInputEditTextPassword;
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://inventapi.medikonda.net/DairyUnifiedAPI.svc";
    String user_id;
    String password;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Start Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView title = toolbar.findViewById(R.id.toolbar_title);
        ImageView backArrow = toolbar.findViewById(R.id.toolbar_back_arrow);
        title.setText("Login");
        title.setPadding(0, 0, 60, 0);
        ImageView logout=findViewById(R.id.logout);
        logout.setVisibility(View.GONE);


        AppCompatButton appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textInputEditTextEmail=(AppCompatEditText)findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword=(AppCompatEditText)findViewById(R.id.textInputEditTextPassword);

//        lt_loading_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Your code
//                lt_loading_view.pauseAnimation();
//            }
//        });
        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = textInputEditTextEmail.getText().toString();
                password = textInputEditTextPassword.getText().toString();
                if (user_id.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter Username",Toast.LENGTH_SHORT).show();
                }else if (password.equalsIgnoreCase("")){
                    Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_SHORT).show();
                }else {
                    ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

                    if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
                        new LoginTask().execute();
                        // notify user you are online
                    }
                    else if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

                        // notify user you are not online
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String sendSoapRequest(Context context) throws Exception {


        String finalString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns1=\"http://schemas.datacontract.org/2004/07/InventAPI\" xmlns:ns2=\"http://tempuri.org/\">\n" +
                "  <SOAP-ENV:Body>\n" +
                "    <ns2:GetLoginInfo>\n" +
                "      <ns2:userInfo>\n" +
                "        <ns1:HashValue>?</ns1:HashValue>\n" +
                "        <ns1:Password>"+password+"</ns1:Password>\n" +
                "        <ns1:Username>"+user_id+"</ns1:Username>\n" +
                "      </ns2:userInfo>\n" +
                "    </ns2:GetLoginInfo>\n" +
                "  </SOAP-ENV:Body>\n" +
                "</SOAP-ENV:Envelope>";
        Log.i("TAG", "*********************** FinalString Before "
                + finalString);



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
            httpPost.addHeader("SOAPAction", "http://tempuri.org/IDairyUnifiedAPI/GetLoginInfo");
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
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<String, String, String> {

        private final ProgressDialog dialog = new ProgressDialog(
                LoginActivity.this,R.style.MyAlertDialogStyle);


        protected void onPreExecute() {

            this.dialog.setMessage("Logging in...");
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
//            anyType{AgentId=4; AgentName=Phani; Message=SUCCESS; OrganizationId=1; Status=1; }
            if (result != null) {
                String data = result.replace("anyType", "").replace("{", "").replace("}", "");
                String[] dateSplit = data.split(";");
                String AgentId = dateSplit[0];
                String AgentName = dateSplit[1];
                String Message = dateSplit[2];
                String OrganizationId = dateSplit[3];
                String Status = dateSplit[4];
                String[] AgtId = AgentId.split("=");
                String AId = AgtId[1];

                String[] AgtName = AgentName.split("=");
                String AName = AgtName[1];

                String[] OrgzationId = OrganizationId.split("=");
                String OrgId = OrgzationId[1];

                String[] Stus = Status.split("=");
                String Stuss = Stus[1];
                String[] message = Message.split("=");
                String finalmessage = message[1];
                if (finalmessage.equalsIgnoreCase("SUCCESS")) {
                    SharedPreferences sp = getApplicationContext().getSharedPreferences("milkproduct_pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("AgentId", Integer.parseInt(AId));
                    editor.putString("AgentName", AName);
                    editor.putString("OrganizationId", OrgId);
                    editor.putString("Status", Stuss);
                    editor.apply();
                    this.dialog.dismiss();
                    if (AName.equalsIgnoreCase("Phani")){
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(getApplicationContext(), SellProductsActivity.class);
                        startActivity(intent);
                    }

                } else {
                this.dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }

        }


}
