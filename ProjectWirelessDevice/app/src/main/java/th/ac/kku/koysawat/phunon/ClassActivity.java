package th.ac.kku.koysawat.phunon;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClassActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    FirebaseDatabase database;
    TextView nav_us,nav_em;
    NavigationView navigationView;
    View headerLayout;
    CircleImageView imgProfile;
    TextView txt;
    RecyclerStudentAdapter adapter;
    RecyclerView recyclerView;
    ArrayList<Student> students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        // Navigation View
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        String courses_id = extras.getString("course_id");
        DatabaseReference myRef = database.getReference().child("Courses").child(courses_id).child("student");//.child(uniqueID);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    GenericTypeIndicator<ArrayList<Student>> t = new GenericTypeIndicator<ArrayList<Student>>() {};
                    students = dataSnapshot.getValue(t);
                }
                try {
                    if (students.size() != 0) {
                        adapter = new RecyclerStudentAdapter(ClassActivity.this, students);
                        recyclerView.setAdapter(adapter);
                    } else {
                        //txt.setText("No student");
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
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
    }

    public void init(){
        //trigger = findViewById(R.id.trigger);
        headerLayout = navigationView.getHeaderView(0);
        nav_us = headerLayout.findViewById(R.id.nav_username);
        nav_em = headerLayout.findViewById(R.id.nav_email);
        imgProfile = headerLayout.findViewById(R.id.imageView);
        txt = findViewById(R.id.noneStudent);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }

    public void signOut() {
        auth.getInstance().signOut();
        LoginManager.getInstance().logOut(); // Facebook Log out
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
            Toast.makeText(ClassActivity.this, "Sign out.", Toast.LENGTH_SHORT).show();
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
