package th.ac.kku.koysawat.phunon;

<<<<<<< HEAD
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
=======
import android.app.ProgressDialog;
import android.content.Intent;
>>>>>>> refs/remotes/origin/master
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
<<<<<<< HEAD
import android.util.Base64;
=======
>>>>>>> refs/remotes/origin/master
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

<<<<<<< HEAD
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
=======
import java.util.ArrayList;
import java.util.UUID;
>>>>>>> refs/remotes/origin/master

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private ProgressDialog progressDialog;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    TextView nav_us,nav_em;
    //Button trigger;
    //Spinner spinner;
    NavigationView navigationView;
    View headerLayout;
    CircleImageView imgProfile;
    RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    RecyclerAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Courses> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Navigation View
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

        // Card View
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        //String uniqueID = UUID.randomUUID().toString();
        //Courses course = new Courses("gkgkj","COURSE TEST","test test test test");
        DatabaseReference myRef = database.getReference().child("Courses");//.child(uniqueID);
        //myRef.setValue(course);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Courses course = data.getValue(Courses.class);
                    Log.i("Courses", course.toString());
                    courses.add(course);
                }
                adapter = new RecyclerAdapter(MainActivity.this,courses);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value\
            }
        });

        //trigger.setOnClickListener(this);
        nav_us.setText(FireUser.getDisplayName());
        nav_em.setText(FireUser.getEmail());

        // Image Profile Update
        for(UserInfo profile : FireUser.getProviderData()) {
            String facebookUserId = null;
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                facebookUserId = profile.getUid();
                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?type=large";
                Picasso.with(this).load(photoUrl).into(imgProfile);

            } else {
                Picasso.with(this).load(FireUser.getPhotoUrl()).into(imgProfile);
            }
        }

        /*spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter =
                ArrayAdapter.createFromResource(this ,
                        R.array.intents , android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
<<<<<<< HEAD
        spinner.setAdapter(adapter);
=======
        spinner.setAdapter(adapter);*/
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference myRef = database.getReference("Courses");

>>>>>>> refs/remotes/origin/master
    }

    public void init(){
        //trigger = findViewById(R.id.trigger);
        headerLayout = navigationView.getHeaderView(0);
        nav_us = headerLayout.findViewById(R.id.nav_username);
        nav_em = headerLayout.findViewById(R.id.nav_email);
        imgProfile = headerLayout.findViewById(R.id.imageView);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //layoutManager = new LinearLayoutManager(this);
        //adapter = new RecyclerAdapter();
        courses = new ArrayList<Courses>();
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
        if (i == R.id.fab) {

            Snackbar.make(v, "Added your class", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        /*if (i == R.id.trigger) {
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
        }*/
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
        if (id == R.id.signout) {
            signOut();
            Toast.makeText(MainActivity.this, "Sign out.", Toast.LENGTH_SHORT).show();
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