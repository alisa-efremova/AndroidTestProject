package com.alice.a7blankproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.support.v7.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import android.util.Log;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.alice.a7blankproject.Message";

    private static final String SOAP_ACTION      = "http://web.cbr.ru/GetCursOnDate";
    private static final String SOAP_METHOD_NAME = "GetCursOnDate";
    private static final String URL              = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
    private static final String NAMESPACE        = "http://web.cbr.ru/";

    TextView tvInfo;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvInfo = (TextView) findViewById(R.id.tvInfo);
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        //startService(new Intent(this, MyService.class));


        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.currency);
        String[] currencies = getResources().getStringArray(R.array.currencies);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, currencies);
        textView.setAdapter(adapter);

        soapRequest();
    }

    @Override
    protected void onResume() {
        Boolean notif = sp.getBoolean("notif", false);
        String address = sp.getString("address", "");
        String text = "Notifications are "
                + ((notif) ? "enabled, address = " + address : "disabled");
        tvInfo.setText(text);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(this, MyService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            item.setIntent(new Intent(this, PrefActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    //private static Object getSOAPDateString(java.util.Date itemValue) {
    private static String getSOAPDateString() {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    public void soapRequest() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SoapObject request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
        String dt = getSOAPDateString();
        request.addProperty("On_date", dt);

        SoapSerializationEnvelope soapEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        soapEnvelop.dotNet = true;
        soapEnvelop.setOutputSoapObject(request);

        HttpTransportSE aht = new HttpTransportSE(URL);
        aht.debug = true;

        try {
            aht.call(SOAP_ACTION, soapEnvelop);

            SoapObject response = (SoapObject) soapEnvelop.getResponse();
            response = (SoapObject) response.getProperty(1);
            SoapObject currencyInfoArray = (SoapObject) response.getProperty("ValuteData");
            /*SoapObject currencyInfo = (SoapObject) currencyInfoArray.getProperty("ValuteCursOnDate");
            Log.i("Check_Soap_Service","currencyInfoArray : "+currencyInfoArray.toString());
            String currencyName = currencyInfo.getPropertyAsString("Vname");
            Log.i("Check_Soap_Service", "vname : " + currencyName);
            Float exchangeRate = Float.parseFloat(currencyInfo.getPropertyAsString("Vcurs"));
            Log.i("Check_Soap_Service", "vcurs : " + exchangeRate);
*/

            for (int i = 0; i < currencyInfoArray.getPropertyCount(); i++)
            {
                SoapObject currencyInfo = (SoapObject) currencyInfoArray.getProperty(i);
                String[] currencies = {"USD", "EUR"};
                String currencyCode = currencyInfo.getPropertyAsString("VchCode");
                if (simpleSearch(currencies, currencyCode))
                {
                    String currencyName = currencyInfo.getPropertyAsString("Vname");
                    Log.i("Check_Soap_Service", "vname: " + currencyName);
                    Log.i("Check_Soap_Service", "code: " + currencyCode);
                    Float exchangeRate = Float.parseFloat(currencyInfo.getPropertyAsString("Vcurs"));
                    Log.i("Check_Soap_Service", "vcurs : " + exchangeRate);
                }
            }
        }
        catch (Exception e)
        {
            Log.i("Check_Soap_Service","Exception : "+e.toString());
        }
        finally
        {
            Log.i(getClass().getSimpleName(), "requestDump : "+aht.requestDump);
            Log.i(getClass().getSimpleName(), "responseDump : "+aht.responseDump);
        }
    }

    public static boolean simpleSearch(String[] arr, String targetValue) {
        for (String s: arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }
        return false;
    }
}
