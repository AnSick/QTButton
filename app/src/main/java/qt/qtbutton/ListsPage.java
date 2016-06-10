package qt.qtbutton;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import qt.qtbutton.model.ListItem;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;

/**
 * Created by Anna on 01.06.2016.
 * Edited by Nadya on 02.06.2016
 */
public class ListsPage extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    ArrayList<String> lists = new ArrayList<String>();
    ArrayList<String> result = new ArrayList<String>();
    List<ListItem> listsResult = new ArrayList<>();
    public static ArrayList<Integer> ids = new ArrayList<Integer>();
    private static final String SOAP_ACTION = "http://tempuri.org/IService1/GetActiveLists";
    private static final String SOAP_METHOD_NAME = "GetActiveLists";
    private static final String URL = Global.URL;
    private static final String NAMESPACE = "http://tempuri.org/";
    public ArrayAdapter<String> adapter;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists);
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
        // находим список
        ListView lvMain = (ListView) findViewById(R.id.listView);

        // создаем адаптер
        adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, R.id.ItemText, lists);

        // присваиваем адаптер списку
        lvMain.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMain.invalidateViews();
        lvMain.refreshDrawableState();
        listsResult = getListNames();
        Log.i("ListsPage", "Got results:  " + listsResult.size());
        for (ListItem result : listsResult) {
            lists.add(result.getName());
        }
        lvMain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long id) {
                // TODO Auto-generated method stub

                Log.v("long clicked", "pos: " + pos);
                onDeleteClick(view);
                return true;
            }
        });

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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if ((item.getTitle().toString()).equals("Друзья")){
            Intent intent = new Intent(ListsPage.this, FriendList.class);
            startActivity(intent);
        }
        if ((item.getTitle().toString()).equals("Списки")) {
            Intent intent = new Intent(ListsPage.this, ListsPage.class);
            startActivity(intent);
        }
        if ((item.getTitle().toString()).equals("Выход")) {
            Intent intent = new Intent(ListsPage.this, MainPage.class);
            startActivity(intent);
        }
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    public List<ListItem> getListNames() {

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
                    listsResult.add(
                            new ListItem(Integer.valueOf(strResult[0]), strResult[1], strResult[2].startsWith("[Tt]"))
                    );
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

    public void goToListCreation(View view) {
        Intent intent = new Intent(ListsPage.this, AddNewList.class);
        startActivity(intent);
    }

    public void onItemClick(View view) {

        if (view instanceof AppCompatTextView) {
            AppCompatTextView textView = (AppCompatTextView) view;
            String name = textView.getText().toString();
            int localId = -1;
            Log.i("", "clickedOn:    " + name);
            for (ListItem result : listsResult) {
                if (result.getName().equals(name)) {
                    localId = result.getId();
                    break;
                }
            }
            if (localId != -1) {
                Intent intent = new Intent(ListsPage.this, ListPage.class);
                intent.putExtra("listId", localId);
                startActivity(intent);
            }
        }


    }

    public void onDeleteClick(View view) {
        if (view instanceof AppCompatTextView) {

            AppCompatTextView textView = (AppCompatTextView) view;
            String name = textView.getText().toString();

            int localId = -1;
            Log.i("ListsPage", "deleting:    " + name);
            for (ListItem result : listsResult) {
                if (result.getName().equals(name)) {
                    localId = result.getId();
                    break;
                }
            }
            if (localId != -1) {
                deleteList(localId);
            }
        }


    }

    public void deleteList(int id) {
        final String SOAP_ACTION = "http://tempuri.org/IService1/DeleteList";
        final String SOAP_METHOD_NAME = "DeleteList";
        final String NAMESPACE = "http://tempuri.org/";
        final String URL = Global.URL;
        final int ourId = id;
        Thread threadDeleteList =
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        SoapObject Request = new SoapObject(NAMESPACE, SOAP_METHOD_NAME);
                        Request.addProperty("tel", Global.tel);
                        Request.addProperty("pass", Global.pass);
                        Request.addProperty("listId", ourId);
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
                            SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
                        } catch (Exception e) {
                            Log.i("Check_Soap_Service", "Exception : " + e.toString());
                        }
                    }
                }
                );
        threadDeleteList.start();
        try {
            threadDeleteList.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(ListsPage.this, ListsPage.class);
        startActivity(intent);

    }

}
