package com.alice.a7blankproject;

import android.app.ListFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

public class CurrentExchangeRatesFragment extends ListFragment {
    public static final String CURRENCY_CODE = "CurrencyCode";
    private static final String SOAP_ACTION        = "http://web.cbr.ru/GetCursOnDate";
    private static final String SOAP_METHOD_NAME   = "GetCursOnDate";
    private static final String SOAP_PARAM_ON_DATE = "On_date";
    private static final String URL                = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
    private static final String NAMESPACE          = "http://web.cbr.ru/";

    SharedPreferences mPreferences;
    CurrencyInfo[] mCurrencies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CurrencyInfo currencyInfo = mCurrencies[position];
        Intent intent = new Intent(getActivity(), ExchangeRateHistoryActivity.class);
        intent.putExtra(CURRENCY_CODE, currencyInfo.getCode());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadCurrentExchangeRatesTask().execute();
    }

    private CurrencyInfo getModel(int position) {
        return (((CurrencyInfoAdapter) getListAdapter()).getItem(position));
    }

    class CurrencyInfoAdapter extends ArrayAdapter<CurrencyInfo> {

        private LayoutInflater mInflater;

        CurrencyInfoAdapter(CurrencyInfo[] list) {
            super(getActivity(), R.layout.fragment_current_exchange_rates, list);
            mInflater = LayoutInflater.from(getActivity());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View row = convertView;
            if (row == null) {
                row = mInflater.inflate(R.layout.fragment_current_exchange_rates, parent, false);
                holder = new ViewHolder();
                holder.nameView = (TextView) row.findViewById(R.id.currency_name);
                holder.exchangeRateView = (TextView) row.findViewById(R.id.exchange_rate);
                row.setTag(holder);
            }
            else {
                holder = (ViewHolder) row.getTag();
            }

            CurrencyInfo currencyInfo = getModel(position);

            holder.nameView.setText(currencyInfo.getName());
            holder.exchangeRateView.setText(Double.toString(currencyInfo.getExchangeRate()));

            return row;
        }

        class ViewHolder {
            public TextView nameView;
            public TextView exchangeRateView;
        }
    }

    class LoadCurrentExchangeRatesTask extends AsyncTask<String, Void, CurrencyInfo[]> {
        @Override
        protected CurrencyInfo[] doInBackground(String... path) {
            return soapRequest();
        }
        @Override
        protected void onProgressUpdate(Void... items) {
        }
        @Override
        protected void onPostExecute(CurrencyInfo[] currencyInfoArray) {
            Toast.makeText(getActivity(), "Данные обновлены", Toast.LENGTH_SHORT).show();
            mCurrencies = currencyInfoArray;
            setListAdapter(new CurrencyInfoAdapter(currencyInfoArray));
        }
    }

    public CurrencyInfo[] soapRequest() {
        SoapObject request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
        String dt = getSOAPDateString();
        request.addProperty(SOAP_PARAM_ON_DATE, dt);

        SoapSerializationEnvelope soapEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        soapEnvelop.dotNet = true;
        soapEnvelop.setOutputSoapObject(request);

        HttpTransportSE aht = new HttpTransportSE(URL);
        aht.debug = true;

        List<CurrencyInfo> currencyInfoList = new ArrayList<CurrencyInfo>();

        try {
            aht.call(SOAP_ACTION, soapEnvelop);

            SoapObject response = (SoapObject) soapEnvelop.getResponse();
            response = (SoapObject) response.getProperty(1);
            SoapObject currencyXmlInfoArray = (SoapObject) response.getProperty("ValuteData");

            Set<String> preferredCurrencies = mPreferences.getStringSet("currencies", null);

            for (int i = 0; i < currencyXmlInfoArray.getPropertyCount(); i++)
            {
                SoapObject currencyXmlInfo = (SoapObject) currencyXmlInfoArray.getProperty(i);
                String currencyCode = currencyXmlInfo.getPropertyAsString("VchCode");
                if (preferredCurrencies.contains(currencyCode))
                {
                    String currencyName = currencyXmlInfo.getPropertyAsString("Vname");
                    Log.i("Check_Soap_Service", "vname: " + currencyName);
                    Log.i("Check_Soap_Service", "code: " + currencyCode);
                    Double exchangeRate = Double.parseDouble(currencyXmlInfo.getPropertyAsString("Vcurs"));
                    Log.i("Check_Soap_Service", "vcurs : " + exchangeRate);
                    currencyInfoList.add(new CurrencyInfo(currencyCode, currencyName, exchangeRate));
                }
            }
        }
        catch (Exception e)
        {
            Log.i("Check_Soap_Service","Exception : " + e.toString());
        }
        finally
        {
            Log.i(getClass().getSimpleName(), "requestDump : " + aht.requestDump);
            Log.i(getClass().getSimpleName(), "responseDump : " + aht.responseDump);
        }

        CurrencyInfo[] currencyInfoArray = new CurrencyInfo[ currencyInfoList.size() ];
        currencyInfoList.toArray( currencyInfoArray );
        Log.i("Check_Soap_Service", "result : " + currencyInfoArray.toString());

        return currencyInfoArray;
    }

    //private static Object getSOAPDateString(java.util.Date itemValue) {
    private static String getSOAPDateString() {

        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }
}