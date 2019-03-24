package th.ac.kku.koysawat.phunon;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    TextView userTxt,nav_us,nav_em;
    Button signout,trigger;
    Spinner spinner;
    NavigationView navigationView;
    View headerLayout;
    ImageView imgProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
        signout.setOnClickListener(this);
        trigger.setOnClickListener(this);
        userTxt.setText("Username: " + FireUser.getDisplayName() + "\nEmail: " + FireUser.getEmail() + "\nUID: " + FireUser.getUid());
        nav_us.setText(FireUser.getDisplayName());
        nav_em.setText(FireUser.getEmail());
        //imgProfile.getBackground();
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(this ,
                        R.array.intents , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }

    public void init(){
        signout = findViewById(R.id.signout);
        userTxt = findViewById(R.id.userShow);
        trigger = findViewById(R.id.trigger);
        headerLayout = navigationView.getHeaderView(0);
        nav_us = headerLayout.findViewById(R.id.nav_username);
        nav_em = headerLayout.findViewById(R.id.nav_email);
        imgProfile = headerLayout.findViewById(R.id.imageView);
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
        } if (i == R.id.fab) {
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } if (i == R.id.trigger) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
