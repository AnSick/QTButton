package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class ListPage extends AppCompatActivity {

    public static ArrayList<String> lists = new ArrayList<String>();
    //   public static ArrayList<String> result = new ArrayList<String>();
    public static int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listId = getIntent().getExtras().getInt("listId");
        ListView lvMain = (ListView) findViewById(R.id.mainListView);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.product_line, R.id.productInfo, lists);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        //   adapter.notifyDataSetChanged();
        // lvMain.invalidateViews();
        // lvMain.refreshDrawableState();
        lists = getLines();

    }

    public ArrayList<String> getLines() {
        final String SOAP_ACTION = "http://tempuri.org/IService1/GetNotBoughtListLines";
        final String SOAP_METHOD_NAME = "GetNotBoughtListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        final ArrayList<String> localLines = new ArrayList<String>();
        final ArrayList<String> result = new ArrayList<>();
        Thread threadGetLines =
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


                            // lists.add(wholeLine);
                            localLines.add(wholeLine);
                        }

                    }
                });
        threadGetLines.start();
        try {
            threadGetLines.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return localLines;
    }

}
