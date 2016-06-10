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

public class AddListLines extends AppCompatActivity {
    public static ArrayList<String> lists = new ArrayList<String>();
    //   public static ArrayList<String> result = new ArrayList<String>();
    public static int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list_lines);
        listId = getIntent().getExtras().getInt("listId");
        ListView lvMain = (ListView) findViewById(R.id.productListView);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.product_line, R.id.productInfo, lists);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMain.invalidateViews();
        lvMain.refreshDrawableState();
        lists = getLines();

    }

    public void createNewLine(View view) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/AddListLine";
        final String SOAP_METHOD_NAME = "AddListLine";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        Thread threadCreateNewLine =
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
                Integer lineId = 0;
                try {
                    aht.call(SOAP_ACTION, soapEnvelope);
                    SoapPrimitive responseObject = (SoapPrimitive) soapEnvelope.getResponse();
                    String str = responseObject.toString();
                    System.out.println(str);
                    lineId = Integer.valueOf(str);
                    System.out.println(lineId);
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                    // result = "";
                }
                if (lineId != 0) {
                    addInfoToLine(lineId, productName);
                }
            }

        });
        threadCreateNewLine.start();
        try {
            threadCreateNewLine.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void addInfoToLine(Integer lineId, String productForLine) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/UpdateListLines";
        final String SOAP_METHOD_NAME = "UpdateListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        final Integer line = lineId;
        final String productLine = productForLine;
        Thread threadAddInfoToLine =
        new Thread(new Runnable() {

            @Override
            public void run() {
                //EditText et_productName = (EditText) findViewById(R.id.et_newProduct);
                //String productName = String.valueOf(et_productName.getText());
                System.out.println("I AM UPDATING OUR FUCKING LINE");
                EditText et_numberOfProduct = (EditText) findViewById(R.id.et_numberOfProduct);
                String numberOfProduct = String.valueOf(et_numberOfProduct.getText());
                Spinner spinner = (Spinner) findViewById(R.id.spinner);
                String category = String.valueOf(spinner.getSelectedItem());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("listLineId", line);
                Request.addProperty("productName", productLine);
                Request.addProperty("count", numberOfProduct);
                Request.addProperty("measureTypeName", category);
                Request.addProperty("comment", "hello");
                // Request.addProperty("isBought", 0);
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

        });
        threadAddInfoToLine.start();
        try {
            threadAddInfoToLine.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lists = getLines();
        Intent intent = new Intent(AddListLines.this, AddListLines.class);
        intent.putExtra("listId", listId);
        startActivity(intent);

    }

    public ArrayList<String> getLines() {
        final String SOAP_ACTION = "http://tempuri.org/IService1/GetNotBoughtListLines";
        final String SOAP_METHOD_NAME = "GetNotBoughtListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        final ArrayList<String> localLines = new ArrayList<String>();
        final ArrayList<String> result = new ArrayList<>();
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
        }).start();

        return localLines;
    }

    public void createListFinal(View view) {
        Intent intent = new Intent(AddListLines.this, ListsPage.class);
        startActivity(intent);
    }

    public void refresh(View view) {          //refresh is onClick name given to the button
        createNewLine(view);
        onRestart();
    }

    @Override
    protected void onRestart() {

        // TODO Auto-generated method stub
        super.onRestart();
        Intent i = new Intent(AddListLines.this, AddListLines.class);  //your class
        startActivity(i);
        finish();

    }
}
