package qt.qtbutton;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
//import org.ksoap2.transport.HttpTransportSE;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    private static final String URL = Global.URL;
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
        if ((numberField.isEmpty()) || (numberField.equals(null)) || (numberField.equals("")) || (passwordField.isEmpty()) || (passwordField.equals(null)) || (passwordField.equals(""))) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You haven't filled in information!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Thread loginThread =
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                    Request.addProperty("tel", numberField);
                    Request.addProperty("pass", passwordField);
                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;

                    soapEnvelope.setAddAdornments(false);
                    soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                    soapEnvelope.env = SoapSerializationEnvelope.ENV;
                    soapEnvelope.implicitTypes = true;
                    soapEnvelope.setOutputSoapObject(Request);

                    String result = "false";
                    HttpTransportSE aht = new HttpTransportSE(URL);
                    aht.debug = true;

                try {
                    aht.call(SOAP_ACTION, soapEnvelope);
                    SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                    result = resultString.toString();
                    System.out.println(result);
                } catch (Exception e) {
                    Log.i("Check_Soap_Service", "Exception : " + e.toString());
                    result = "false";
                }

                    if (result.equals("true")) {
                        Global.tel = numberField;
                        Global.pass = passwordField;
                        Intent intent = new Intent(LoginPage.this, ListsPage.class);
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
            loginThread.start();
            try {
                loginThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}


