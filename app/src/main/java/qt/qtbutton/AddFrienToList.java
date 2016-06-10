package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;
import qt.qtbutton.model.Friend;

import qt.qtbutton.model.ListItem;

public class AddFrienToList extends AppCompatActivity {
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/GetNotActiveFriends";
    private static final String SOAP_METHOD_NAME = "GetNotActiveFriends";
    private static final String URL = Global.URL;
    private static final String NAMESPACE = "http://tempuri.org/";
    ArrayList<String> lists = new ArrayList<String>();
    List<Friend> listsResult = new ArrayList<>();
    ArrayList<String> result = new ArrayList<String>();
    int listId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_frien_to_list);
        listId = getIntent().getExtras().getInt("listId");
        ListView lvMain = (ListView) findViewById(R.id.friendInList);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.friend_in_list, R.id.friendInListInfo, lists);
        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMain.invalidateViews();
        lvMain.refreshDrawableState();
        listsResult = getListFriends();
        for (Friend localResult : listsResult) {
            lists.add(localResult.getName());
        }
    }
    public void onFriendItemClick(View view) {

        if (view instanceof AppCompatTextView) {
            AppCompatTextView textView = (AppCompatTextView) view;
            String name = textView.getText().toString();
            String telephone=null;
            for (Friend friend : listsResult)
            {
                if ((friend.getName()).equals(name))
                {
                    telephone = friend.getTelephone();
                }
            }
            sendrequest(listId, telephone);

            Toast.makeText(getApplicationContext(), "Sent", Toast.LENGTH_SHORT).show();
        }
    }
    public List<Friend> getListFriends() {

        Thread getListThread =
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
                            count = ((SoapObject) soapEnvelope.getResponse()).getPropertyCount();
                            for (int i = 0; i < count; i++) {
                                String str = ((SoapObject) soapEnvelope.getResponse()).getPropertyAsString(i);
                                result.add(str);
                            }
                        } catch (Exception e) {
                            Log.i("Check_Soap_Service", "Exception : " + e.toString());
                        }

                        for (int i = 0; i < count; i++) {
                            String[] strResult = result.get(i).split("\\+");
                            Friend friend = new Friend(strResult[1], strResult[0]);
                            listsResult.add( friend );
                        }
                    }
                });
        getListThread.start();
        try {
            getListThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listsResult;
    }
    public void sendrequest(int list, String tele){
        final int localId = list;
        final String localTele = tele;
        Thread sendThread =
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        SoapObject Request = new SoapObject(NAMESPACE, "AddListOwner");
                        Request.addProperty("tel", Global.tel);
                        Request.addProperty("pass", Global.pass);
                        Request.addProperty("listId", listId);
                        Request.addProperty("friendTel", localTele);
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
                            aht.call("http://tempuri.org/IService1/AddListOwner", soapEnvelope);
                        } catch (Exception e) {
                            Log.i("Check_Soap_Service", "Exception : " + e.toString());
                        }
                    }});
        sendThread.start();
        try {
            sendThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    public void goBackToLists(View view){
        Intent intent = new Intent(AddFrienToList.this, ListsPage.class);

        startActivity(intent);
    }
}
