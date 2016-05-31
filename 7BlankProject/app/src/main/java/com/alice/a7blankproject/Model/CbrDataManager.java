package com.alice.a7blankproject.model;

import android.util.Log;

import com.alice.a7blankproject.util.TimeUtils;

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

    public static final String NEWS_URL   = "http://cbr.ru/";
    private static final String URL       = "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx";
    private static final String NAMESPACE = "http://web.cbr.ru/";

    private static final String SOAP_METHOD_GET_EXCHANGE_RATES_ON_DATE = "GetCursOnDate";
    private static final String SOAP_METHOD_GET_EXCHANGE_RATE_DYMANICS = "GetCursDynamic";
    private static final String SOAP_METHOD_GET_CURRENCY_INFO          = "EnumValutes";
    private static final String SOAP_METHOD_GET_NEWS                   = "NewsInfo";

    private static final String SOAP_PARAM_ON_DATE        = "On_date";
    private static final String SOAP_PARAM_FROM_DATE      = "FromDate";
    private static final String SOAP_PARAM_NEWS_FROM_DATE = "fromDate";
    private static final String SOAP_PARAM_TO_DATE        = "ToDate";
    private static final String SOAP_PARAM_CURRENCY_ID    = "ValutaCode";
    private static final String SOAP_PARAM_SELD           = "Seld";

    private static final String SOAP_TABLE_NAME_VALUTE_DATA    = "ValuteData";
    private static final String SOAP_TABLE_NAME_NEWS           = "NewsInfo";
    private static final String SOAP_RESULT_CURRENCY_NAME      = "Vname";
    private static final String SOAP_RESULT_CURRENCY_ID        = "Vcode";
    private static final String SOAP_RESULT_CURRENCY_CODE      = "VchCode";
    private static final String SOAP_RESULT_ENUM_CURRENCY_CODE = "VcharCode";
    private static final String SOAP_RESULT_EXCHANGE_RATE      = "Vcurs";
    private static final String SOAP_RESULT_DATE               = "CursDate";
    private static final String SOAP_RESULT_NEWS_DATE          = "DocDate";
    private static final String SOAP_RESULT_TITLE              = "Title";
    private static final String SOAP_RESULT_URL                = "Url";

    private Map<String, String> mCurrencyIds = new HashMap<>();

    public CbrDataManager(List<String> currencies)
    {
        getCurrencies(currencies);
    }

    public static List<CurrencyInfo> getExchangeRatesByDate(Date date, Set<String> preferredCurrencies) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(SOAP_PARAM_ON_DATE, TimeUtils.getSoapDateString(date));
        SoapObject currencyXmlInfoArray = soapRequest(SOAP_METHOD_GET_EXCHANGE_RATES_ON_DATE, parameters, SOAP_TABLE_NAME_VALUTE_DATA);

        if (currencyXmlInfoArray == null) {
            return new ArrayList<>();
        }

        List<CurrencyInfo> currencyInfoList = new ArrayList<>();
        for (int i = 0; i < currencyXmlInfoArray.getPropertyCount(); i++) {
            SoapObject currencyXmlInfo = (SoapObject) currencyXmlInfoArray.getProperty(i);
            String currencyCode = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_CURRENCY_CODE);
            if (preferredCurrencies.contains(currencyCode)) {
                String currencyName = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_CURRENCY_NAME);
                Double exchangeRate = Double.parseDouble(currencyXmlInfo.getPropertyAsString(SOAP_RESULT_EXCHANGE_RATE));
                currencyInfoList.add(new CurrencyInfo(currencyCode, currencyName, exchangeRate));
            }
        }

        return currencyInfoList;
    }

    public static List<News> getNews(Date fromDate, Date toDate)
    {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(SOAP_PARAM_NEWS_FROM_DATE, TimeUtils.getSoapDateString(fromDate));
        parameters.put(SOAP_PARAM_TO_DATE, TimeUtils.getSoapDateString(toDate));
        SoapObject xmlArr = soapRequest(SOAP_METHOD_GET_NEWS, parameters, SOAP_TABLE_NAME_NEWS);

        if (xmlArr == null) {
            return new ArrayList<>();
        }

        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < xmlArr.getPropertyCount(); i++) {
            SoapObject newsInfo = (SoapObject) xmlArr.getProperty(i);

            Date date    = TimeUtils.parseSoapDate(newsInfo.getPropertyAsString(SOAP_RESULT_NEWS_DATE));
            String title = newsInfo.getPropertyAsString(SOAP_RESULT_TITLE);
            String url   = newsInfo.getPropertyAsString(SOAP_RESULT_URL);
            newsList.add(new News(date, title, url));
        }

        return newsList;
    }

    public List<ExchangeRateByDate> getExchangeRatesByDateInterval(String currencyCode, Date endDate, int dateCount) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(SOAP_PARAM_CURRENCY_ID, mCurrencyIds.get(currencyCode));
        parameters.put(SOAP_PARAM_FROM_DATE, TimeUtils.getSoapDateString(TimeUtils.addToDate(endDate, -dateCount)));
        parameters.put(SOAP_PARAM_TO_DATE, TimeUtils.getSoapDateString(endDate));

        SoapObject xmlArr = soapRequest(SOAP_METHOD_GET_EXCHANGE_RATE_DYMANICS, parameters, SOAP_TABLE_NAME_VALUTE_DATA);
        if (xmlArr == null) {
            return new ArrayList<>();
        }

        List<ExchangeRateByDate> exchangeRateList = new ArrayList<>();
        for (int i = 0; i < xmlArr.getPropertyCount(); i++) {
            SoapObject exchangeRateInfo = (SoapObject) xmlArr.getProperty(i);

            String date         = exchangeRateInfo.getPropertyAsString(SOAP_RESULT_DATE);
            double exchangeRate = Double.parseDouble(exchangeRateInfo.getPropertyAsString(SOAP_RESULT_EXCHANGE_RATE));
            exchangeRateList.add(new ExchangeRateByDate(TimeUtils.parseSoapDate(date), exchangeRate));
        }

        return exchangeRateList;
    }

    private void getCurrencies(List<String> currencies) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(SOAP_PARAM_SELD, "false");

        SoapObject xmlArr = soapRequest(SOAP_METHOD_GET_CURRENCY_INFO, parameters, SOAP_TABLE_NAME_VALUTE_DATA);
        if (xmlArr == null) {
            return;
        }

        for (int i = 0; i < xmlArr.getPropertyCount(); i++) {
            SoapObject currencyXmlInfo = (SoapObject) xmlArr.getProperty(i);

            try {
                String currencyCode = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_ENUM_CURRENCY_CODE);

                if (currencies.contains(currencyCode)) {
                    String currencyId = currencyXmlInfo.getPropertyAsString(SOAP_RESULT_CURRENCY_ID);
                    mCurrencyIds.put(currencyCode, currencyId);
                }
            }
            catch (Exception e) {
                Log.i("Check_Soap_Service", "Exception : " + e.toString());
            }
        }
    }

    private static SoapObject soapRequest(String methodName, Map<String, String> parameters, String tableName) {
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

            SoapObject response = (SoapObject) soapEnvelop.getResponse();
            if (response == null) {
                Log.i("Check_Soap_Service", "Response is null");
                return null;
            }

            response = (SoapObject) response.getProperty(1);
            return (SoapObject) response.getProperty(tableName);
        }
        catch (Exception e) {
            Log.i("Check_Soap_Service","Exception : " + e.toString());
            e.printStackTrace();
        }
        finally {
            Log.i("Check_Soap_Service", "requestDump: " + aht.requestDump);
            Log.i("Check_Soap_Service", "responseDump: " + aht.responseDump);
        }

        return null;
    }
}
