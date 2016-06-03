package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Created by Anna on 01.06.2016.
 * Edited by Nadya on 02.06.2016
 */
public class ListsPage extends AppCompatActivity {
    ArrayList<String> lists = new ArrayList<String>();
    ArrayList<String> result = new ArrayList<String>();
    ArrayList<String> listsResult = new ArrayList<String>();
    ArrayList<Integer> ids = new ArrayList<Integer>();
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/GetActiveLists";
    private static final String SOAP_METHOD_NAME = "GetActiveLists";
    private static final String URL = "http://91.122.171.34:25565/Design_Time_Addresses/WcfServiceLibrary1/Service1";
    private static final String NAMESPACE = "http://tempuri.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lists = getListNames();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);

        // находим список
        ListView lvMain = (ListView) findViewById(R.id.listView);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, R.id.ItemText, lists);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);

    }

    public ArrayList<String> getListNames() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
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
                    Integer id;
                    id = Integer.valueOf(str.nextToken());

                    String name = str.nextToken();

                    listsResult.add(name);
                }

/*
                    Stubber stub = new Stubber();
                    result = stub.loginStub(numberField, passwordField);
*/

            }
        }).start();

        return listsResult;
    }

    public void goToListCreation(View view) {
        Intent intent = new Intent(ListsPage.this, AddNewList.class);
        startActivity(intent);
    }
}
