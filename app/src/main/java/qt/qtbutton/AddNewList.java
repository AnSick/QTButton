package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class AddNewList extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_list);
    }

    public void createNewList(View view) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/AddList";
        final String SOAP_METHOD_NAME = "AddList";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
Thread createNewListThread =
        new Thread(new Runnable() {

            @Override
            public void run() {
                EditText et_listName = (EditText) findViewById(R.id.et_listName);
                String listName = String.valueOf(et_listName.getText());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("name", listName);
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
                    SoapPrimitive responseObject = (SoapPrimitive) soapEnvelope.getResponse();
                    if (responseObject == null) {
                        System.out.println("Null response");
                    } else {
                        String str = responseObject.toString();
                        result = Integer.valueOf(str);
                        System.out.println(result);
                    }
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                }
                if ((result != null) && (result != 0)) {
                    Intent intent = new Intent(AddNewList.this, AddListLines.class);
                    intent.putExtra("listId", result);
                    startActivity(intent);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Incorrect number or password!",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                }
            }

        });
createNewListThread.start();
        try {
            createNewListThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
