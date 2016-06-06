package qt.qtbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ItemInList extends AppCompatActivity {
    public static ArrayList<Integer> ids = ListsPage.ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_in_list);
    }
}
