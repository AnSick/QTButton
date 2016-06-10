package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class EditLinePage extends AppCompatActivity {
    public static int lineId;
    public static int listId;
    public String product;
    public String number;
    public String category;
    public String[] selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_line_page);
        listId = getIntent().getExtras().getInt("listId");
        lineId = getIntent().getExtras().getInt("lineId");
        product = getIntent().getExtras().getString("productName");
        number = getIntent().getExtras().getString("numberOfProduct");
        category = getIntent().getExtras().getString("category");
        EditText et_product = (EditText) findViewById(R.id.et_editProduct);
        EditText et_numberOfProduct = (EditText) findViewById(R.id.et_editNumberOfProduct);
        Spinner spinner = (Spinner) findViewById(R.id.spinner_edit);
        et_product.setText(product);
        et_numberOfProduct.setText(number);
        selection = getResources().getStringArray(R.array.measure);
        int spinnerSelection = -1;
        for (int i = 0; i < selection.length; i++) {
            if (selection[i].equals(category)) {
                spinnerSelection = i;
                break;
            }
        }
        if (spinnerSelection != -1) {
            spinner.setSelection(spinnerSelection);
        }
    }

    public void onEditLine(View view) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/UpdateListLines";
        final String SOAP_METHOD_NAME = "UpdateListLines";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        final Integer line = lineId;
        Thread threadEditLine =
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("I AM UPDATING OUR FUCKING LINE");
                EditText et_product = (EditText) findViewById(R.id.et_editProduct);
                String localProduct = String.valueOf(et_product.getText());
                EditText et_numberOfProduct = (EditText) findViewById(R.id.et_editNumberOfProduct);
                String localNumberOfProduct = String.valueOf(et_numberOfProduct.getText());
                Spinner spinner = (Spinner) findViewById(R.id.spinner_edit);
                String localCategory = String.valueOf(spinner.getSelectedItem());
                SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                Request.addProperty("tel", Global.tel);
                Request.addProperty("pass", Global.pass);
                Request.addProperty("listLineId", line);
                Request.addProperty("productName", localProduct);
                Request.addProperty("count", localNumberOfProduct);
                Request.addProperty("measureTypeName", localCategory);
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
        threadEditLine.start();
        try {
            threadEditLine.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(EditLinePage.this, ListPage.class);
        intent.putExtra("listId", listId);
        startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EditLinePage.this, ListPage.class);
        intent.putExtra("listId", listId);
        startActivity(intent);
    }
}
