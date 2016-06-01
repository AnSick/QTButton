package qt.qtbutton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Надюша on 29.05.2016.
 */
public class LoginPage extends AppCompatActivity {
    public EditText et_numberField;
    public EditText et_passwordField;
    public String numberField;
    public String passwordField;
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/AuthIn";
    private static final String SOAP_METHOD_NAME = "AuthIn";
    private static final String URL = "http://91.122.172.236:25565/Design_Time_Addresses/WcfServiceLibrary1/Service1";
    private static final String NAMESPACE = "http://tempuri.org/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
    }

    public void confirmLogin(View view) {

        et_numberField = (EditText) findViewById(R.id.et_numberField);
        et_passwordField = (EditText) findViewById(R.id.et_passwordField);
        numberField = String.valueOf(et_numberField.getText());
        passwordField = String.valueOf(et_passwordField.getText());
        new Thread(new Runnable() {

            @Override
            public void run() {
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", numberField);
                Request.addProperty("pass", passwordField);
                SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
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
                    SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                    //TODO: appropriate parsing and processing routine for resultString
                    Log.i("Check_Soap_Service", "resultString -  " + resultString);
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                }
            }
        }).start();

    }
}
