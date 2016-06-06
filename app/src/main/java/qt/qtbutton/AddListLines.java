package qt.qtbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class AddListLines extends AppCompatActivity {
    public static ArrayList<String> lists = new ArrayList<String>();
    public static ArrayList<String> result = new ArrayList<String>();

    public static int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_lines);
        listId = Integer.valueOf(getIntent().getExtras().getString("listId"));
        ListView lvMain = (ListView) findViewById(R.id.productListView);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.product_line, R.id.productInfo, lists);
        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

    }

    public void createNewLine() {
        final String SOAP_ACTION = "http://tempuri.org/IService1/AddListLine";
        final String SOAP_METHOD_NAME = "AddListLine";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        new Thread(new Runnable() {

            @Override
            public void run() {
                EditText et_productName = (EditText) findViewById(R.id.et_newProduct);
                String productName = String.valueOf(et_productName.getText());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("listId", listId);
                Request.addProperty("productName", productName);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;

                soapEnvelope.setAddAdornments(false);
                soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                soapEnvelope.env = SoapSerializationEnvelope.ENV;
                soapEnvelope.implicitTypes = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE aht = new HttpTransportSE(URL);
                aht.debug = true;
                Integer result = 0;
                try {
                    aht.call(SOAP_ACTION, soapEnvelope);
                    result = Integer.getInteger((((SoapPrimitive) soapEnvelope.getResponse()).toString()));

                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                    // result = "";
                }
                if (result != 0) {
                    addInfoToLine();
                }
            }

        }).start();

    }

    public void addInfoToLine() {
        final String SOAP_ACTION = "http://tempuri.org/IService1/AddDataListLines";
        final String SOAP_METHOD_NAME = "AddDataListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        new Thread(new Runnable() {

            @Override
            public void run() {
                EditText et_productName = (EditText) findViewById(R.id.et_newProduct);
                String productName = String.valueOf(et_productName.getText());
                EditText et_numberOfProduct = (EditText) findViewById(R.id.et_numberOfProduct);
                String numberOfProduct = String.valueOf(et_numberOfProduct.getText());
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String category = String.valueOf(spinner.getSelectedItem());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("list", listId);
                Request.addProperty("product", productName);
                Request.addProperty("count", numberOfProduct);
                Request.addProperty("measureType", category);
                Request.addProperty("comment", "");
                Request.addProperty("isBought", 0);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;

                soapEnvelope.setAddAdornments(false);
                soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                soapEnvelope.env = SoapSerializationEnvelope.ENV;
                soapEnvelope.implicitTypes = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE aht = new HttpTransportSE(URL);
                aht.debug = true;
                try {
                    aht.call(SOAP_ACTION, soapEnvelope);

                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                    // result = "";
                }
            }

        }).start();

        setContentView(R.layout.activity_add_list_lines);
    }

    public ArrayList<String> getLines() {
        final String SOAP_ACTION = "http://tempuri.org/IService1/GetListLines";
        final String SOAP_METHOD_NAME = "GetListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("listId", listId);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;

                soapEnvelope.setAddAdornments(false);
                soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                soapEnvelope.env = SoapSerializationEnvelope.ENV;
                soapEnvelope.implicitTypes = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE aht = new HttpTransportSE(URL);
                aht.debug = true;
                int count = 0;
                try {
                    aht.call(SOAP_ACTION, soapEnvelope);
                    //  SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                    // SoapObject result =(SoapObject) soapEnvelope.bodyIn;
                    //TODO: appropriate parsing and processing routine for resultString
                    //Log.i("Check_Soap_Service", "resultString -  " + resultString);
                    // result = Boolean.getBoolean((((SoapPrimitive) soapEnvelope.getResponse()).toString()));
                    //   SoapObject resultString = (SoapObject) soapEnvelope.getResponse();
                    count = ((SoapObject) soapEnvelope.getResponse()).getPropertyCount();
                    for (int i = 0; i < count; i++) {
                        String str = ((SoapObject) soapEnvelope.getResponse()).getPropertyAsString(i);
                        result.add(str);
                    }
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                    // result = "";
                }
                for (int i = 0; i < count; i++) {
                    StringTokenizer str = new StringTokenizer(result.get(i), "+");
                    str.nextToken();
                    String wholeLine = str.nextToken() + "  " + str.nextToken() + "  " + str.nextToken();


                    lists.add(wholeLine);
                }

            }
        }).start();

        return lists;
    }

}
