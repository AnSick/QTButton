package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class AddFriend extends AppCompatActivity {
    public static ArrayList<String> lists = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

    }
    public void CreateFriend(View view) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/AddFriend";
        final String SOAP_METHOD_NAME = "AddFriend";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
Thread createFriendThread =
        new Thread(new Runnable() {

            @Override
            public void run() {
                EditText et_telephone = (EditText) findViewById(R.id.et_friendTel);
                String telephone = String.valueOf(et_telephone.getText());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Log.i("AddFriend", "tel:  " + telephone);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("friendTel", telephone);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                soapEnvelope.dotNet = true;

                soapEnvelope.setAddAdornments(false);
                soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                soapEnvelope.env = SoapSerializationEnvelope.ENV;
                soapEnvelope.implicitTypes = true;
                soapEnvelope.setOutputSoapObject(Request);
                HttpTransportSE aht = new HttpTransportSE(URL);
                aht.debug = true;
                boolean done;
                try {
                    aht.call(SOAP_ACTION, soapEnvelope);
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                }
                    Intent intent = new Intent(AddFriend.this, FriendList.class);
                    startActivity(intent);
            }
        });
        createFriendThread.start();
        try {
            createFriendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddFriend.this, ListsPage.class);
        startActivity(intent);
    }
}
