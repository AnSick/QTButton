package qt.qtbutton;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

public class FriendList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/GetActiveFriends";
    private static final String SOAP_METHOD_NAME = "GetActiveFriends";
    private static final String URL = Global.URL;
    private static final String NAMESPACE = "http://tempuri.org/";
    ArrayList<String> lists = new ArrayList<String>();
    List<List<String>> listsResult = new ArrayList<>();
    ArrayList<String> result = new ArrayList<String>();
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        if (drawerLayout != null) {
            drawerLayout.addDrawerListener(mDrawerToggle);
        }
        mDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        ListView lvMain = (ListView) findViewById(R.id.friendListView);

        // создаем адаптер
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.friend_line, R.id.friendInfo, lists);
        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMain.invalidateViews();
        lvMain.refreshDrawableState();
        listsResult.clear();
        lists.clear();
        listsResult = getListFriends();
        for (List<String> result : listsResult) {
            lists.add(result.get(0));
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
    public void goToAddFriend(View view) {
        Intent intent = new Intent(FriendList.this, AddFriend.class);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if ((item.getTitle().toString()).equals("Друзья")){
            Intent intent = new Intent(FriendList.this, FriendList.class);
            startActivity(intent);
        }
        if ((item.getTitle().toString()).equals("Списки")) {
            Intent intent = new Intent(FriendList.this, ListsPage.class);
            startActivity(intent);
        }
        if ((item.getTitle().toString()).equals("Выход")) {
            Intent intent = new Intent(FriendList.this, MainPage.class);
            startActivity(intent);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }
    public List<List<String>> getListFriends() {

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
                            List<String> temp = new ArrayList<>();
                            temp.add(strResult[0]);
                            temp.add(strResult[1]);
                            listsResult.add( temp );
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
}
