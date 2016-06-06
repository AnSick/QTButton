package qt.qtbutton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
    }

    public void goToLogin(View view) {
        startActivity(new Intent(MainPage.this, LoginPage.class));
    }

    public void goToRegistration(View view) {
        Intent intent = new Intent(MainPage.this, RegistrationPage.class);
        startActivity(intent);
    }
}
