package th.ac.kku.koysawat.phunon;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    TextView userTxt;
    Button signout;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signout = findViewById(R.id.signout);
        signout.setOnClickListener(this);
        userTxt = findViewById(R.id.userShow);
        userTxt.setText("Username: " + FireUser.getDisplayName() + "\nEmail: " + FireUser.getEmail() + "\nUID: " + FireUser.getUid());

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(this ,
                        R.array.intents , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
<<<<<<< HEAD
=======


>>>>>>> refs/remotes/origin/master
    }

    public void signOut() {
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut(); // Facebook Log out
        this.finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signout) {
            signOut();
            Toast.makeText(MainActivity.this, "Sign out.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickIm(View view) {
        int pos = spinner.getSelectedItemPosition();
        Intent intent = null;
        switch (pos) {
            case 0:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.kku.ac.th"));
                break;
            case 1:
                intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:(+43)009700"));
                break;
            case 2:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("geo:0.0?q=Khon Kaen University"));
                break;
            case 3:
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("content://contacts/people"));
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
