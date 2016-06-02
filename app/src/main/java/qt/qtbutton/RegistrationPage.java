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

public class RegistrationPage extends AppCompatActivity {
    public EditText et_numberRegistration;
    public EditText et_passwordRegistration;
    public EditText et_nameRegistration;
    public String numberRegistration, passwordRegistration, nameRegistration;
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/Reg";
    private static final String SOAP_METHOD_NAME = "Reg";
    private static final String URL = "http://91.122.171.34:25565/Design_Time_Addresses/WcfServiceLibrary1/Service1";
    private static final String NAMESPACE = "http://tempuri.org/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);
    }

    public void registration(View view) {

        et_numberRegistration = (EditText) findViewById(R.id.et_numberRegistration);
        et_passwordRegistration = (EditText) findViewById(R.id.et_passwordRegistration);
        et_nameRegistration = (EditText) findViewById(R.id.et_nameRegistration);
        numberRegistration = String.valueOf(et_numberRegistration.getText());
        passwordRegistration = String.valueOf(et_passwordRegistration.getText());
        nameRegistration = String.valueOf(et_nameRegistration.getText());
        if ((numberRegistration.isEmpty()) || (numberRegistration.equals(null)) || (numberRegistration.equals("")) || (passwordRegistration.isEmpty()) || (passwordRegistration.equals(null)) || (passwordRegistration.equals(""))) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You haven't filled in information!",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                    Request.addProperty("tel", numberRegistration);
                    Request.addProperty("pass", passwordRegistration);
                    Request.addProperty("name", nameRegistration);
                    SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                    soapEnvelope.dotNet = true;

                    soapEnvelope.setAddAdornments(false);
                    soapEnvelope.encodingStyle = SoapSerializationEnvelope.ENC;
                    soapEnvelope.env = SoapSerializationEnvelope.ENV;
                    soapEnvelope.implicitTypes = true;
                    soapEnvelope.setOutputSoapObject(Request);

//                    String result = "false";
                    HttpTransportSE aht = new HttpTransportSE(URL);
                    aht.debug = true;

                    try {
                        aht.call(SOAP_ACTION, soapEnvelope);
                        SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                        // SoapObject result =(SoapObject) soapEnvelope.bodyIn;
                        //TODO: appropriate parsing and processing routine for resultString
                        Log.i("Check_Soap_Service", "resultString -  " + resultString);
                        // result = Boolean.getBoolean((((SoapPrimitive) soapEnvelope.getResponse()).toString()));
                        //                      result = resultString.toString();
                        //   System.out.println(result);
                    } catch (Exception e) {
                        Log.i("Check_Soap_Service", "Exception : " + e.toString());
                        //result = "false";
                    }

/*
                    Stubber stub = new Stubber();
                    result = stub.loginStub(numberField, passwordField);
*/
                  /*  if (result.equals("true")) {
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
*/
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Success!",
                                    Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    });
                    Intent intent = new Intent(RegistrationPage.this, MainPage.class);
                    startActivity(intent);
                }
            }).start();

        }
    }
}
