package com.alice.a7blankproject;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CbrDataManager {

    private static final String URL       = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
    private static final String NAMESPACE = "http://web.cbr.ru/";

    private static final String SOAP_METHOD_GET_EXCHANGE_RATES_ON_DATE = "GetCursOnDate";
    private static final String SOAP_METHOD_GET_EXCHANGE_RATE_DYMANICS = "GetCursDynamics";
    private static final String SOAP_METHOD_GET_CURRENCY_INFO          = "EnumValutes";

    private static final String SOAP_PARAM_ON_DATE     = "On_date";
    private static final String SOAP_PARAM_FROM_DATE   = "FromDate";
    private static final String SOAP_PARAM_TO_DATE     = "ToDate";
    private static final String SOAP_PARAM_CURRENCY_ID = "ValutaCode";
    private static final String SOAP_PARAM_SELD        = "Seld";

    private static final String SOAP_RESULT_CURRENCY_NAME      = "Vname";
    private static final String SOAP_RESULT_CURRENCY_ID        = "Vcode";
    private static final String SOAP_RESULT_CURRENCY_CODE      = "VchCode";
    private static final String SOAP_RESULT_ENUM_CURRENCY_CODE = "VcharCode";
    private static final String SOAP_RESULT_EXCHANGE_RATE      = "Vcurs";
    private static final String SOAP_RESULT_DATE               = "CursDate";

    private Map<String, String> currencyIds;

    public static CurrencyInfo[] getExchangeRatesByDate(Date date, Set<String> preferredCurrencies) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(SOAP_PARAM_ON_DATE, TimeUtils.getSOAPDateString(date));
        SoapObject response = soapRequest(SOAP_METHOD_GET_EXCHANGE_RATES_ON_DATE, parameters);

        if (response == null) {
            return null;
        }

        List<CurrencyInfo> currencyInfoList = new ArrayList<>();

        response = (SoapObject) response.getProperty(1);
        SoapObject currencyXmlInfoArray = (SoapObject) response.getProperty("ValuteData");

        for (int i = 0; i < currencyXmlInfoArray.getPropertyCount(); i++) {
            SoapObject currencyXmlInfo = (SoapObject) currencyXmlInfoArray.getProperty(i);
            String currencyCode = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_CURRENCY_CODE);
            if (preferredCurrencies.contains(currencyCode)) {
                String currencyName = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_CURRENCY_NAME);
                Double exchangeRate = Double.parseDouble(currencyXmlInfo.getPropertyAsString(SOAP_RESULT_EXCHANGE_RATE));
                currencyInfoList.add(new CurrencyInfo(currencyCode, currencyName, exchangeRate));
            }
        }

        CurrencyInfo[] currencyInfoArray = new CurrencyInfo[currencyInfoList.size()];
        currencyInfoList.toArray(currencyInfoArray);

        return currencyInfoArray;
    }

    public void getExchangeRatesByDateInterval(String currencyCode, Date endDate, int dateCount) {

    }

    private String getCurrencyId(String currencyCode) {
        return "";
    }

    private static SoapObject soapRequest(String methodName, Map<String, String> parameters) {
        SoapObject request = new SoapObject(NAMESPACE, methodName);

        for (Map.Entry<String, String> param : parameters.entrySet()) {
            request.addProperty(param.getKey(), param.getValue());
        }

        SoapSerializationEnvelope soapEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        soapEnvelop.dotNet = true;
        soapEnvelop.setOutputSoapObject(request);

        HttpTransportSE aht = new HttpTransportSE(URL);
        aht.debug = true;

        try {
            String soapAction = NAMESPACE + methodName;
            aht.call(soapAction, soapEnvelop);
            return (SoapObject) soapEnvelop.getResponse();
        }
        catch (Exception e) {
            Log.i("Check_Soap_Service","Exception : " + e.toString());
        }
        finally {
            Log.i("Check_Soap_Service", "requestDump: " + aht.requestDump);
            Log.i("Check_Soap_Service", "responseDump: " + aht.responseDump);
        }

        return null;
    }
}
