package th.ac.kku.koysawat.phunon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    TextView nav_us,nav_em;
    NavigationView navigationView;
    View headerLayout;
    CircleImageView imgProfile;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Courses> courses;
    boolean ftoggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...please wait");
        progressDialog.show();
        ftoggle = true;
        // Card View
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        database = FirebaseDatabase.getInstance();
        //String uniqueID = UUID.randomUUID().toString();
        //Student student = new Student(FireUser.getUid(),FireUser.getDisplayName(),FireUser.getEmail());
        //Teacher teacher = new Teacher(FireUser.getUid(),FireUser.getDisplayName(),FireUser.getEmail());
        //ArrayList<Student> studentArrayList = new ArrayList<Student>();
        //ArrayList<Teacher> teacherArrayList = new ArrayList<Teacher>();
        //studentArrayList.add(student);
        //studentArrayList.add(new Student("klfjdlskjflksd","Mister A",null));
        //teacherArrayList.add(teacher);
        //Courses course = new Courses(uniqueID,"COURSE TEST","test test test test",studentArrayList,teacherArrayList);
        DatabaseReference myRef = database.getReference().child("Courses");
        //myRef.child(uniqueID).setValue(course);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Courses course = data.getValue(Courses.class);
                    courses.add(course);
                }
                adapter = new RecyclerAdapter(MainActivity.this,courses);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value\
                Log.i("Firebase databsae",error.toString());
                progressDialog.dismiss();
            }
        });
        // Test Add Database
        /*DatabaseReference ref = database.getReference().child("Users").child(FireUser.getUid());
        Users user = new Users(FireUser.getUid(), FireUser.getDisplayName(),FireUser.getEmail());
        ref.setValue(user);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value\
                Log.i("Firebase databsae",error.toString());
            }
        });*/


        // Float action bar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

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

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference myRef = database.getReference("Courses");

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
        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab_3);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
        if (i == R.id.fab) {
            if(ftoggle){
                // Animations
                Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
                Animation show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
                Animation show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
                // show
                fabAnim(fab1,show_fab_1,1.7,0.5,true);
                fabAnim(fab2,show_fab_2,0,1.7,true);
                fabAnim(fab3,show_fab_3,-1.7,0.5,true);
                ftoggle = false;
            } else {
                // Animations
                Animation hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
                Animation hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
                Animation hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);
                // hide
                fabAnim(fab1,hide_fab_1,-1.7,-0.5,false);
                fabAnim(fab2,hide_fab_2,0,-1.7,false);
                fabAnim(fab3,hide_fab_3,1.7,-0.5,false);
                ftoggle = true;
            }
            //Snackbar.make(v, "Added your class", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        if (i == R.id.fab_1) {
            startActivity(new Intent(MainActivity.this, PopAdd.class));
        }
        if (i == R.id.fab_2) {

        }
        if (i == R.id.fab_3) {

        }
    }

    public void fabAnim(FloatingActionButton fab,Animation show_fab,double x,double y,boolean b) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab.getLayoutParams();
        layoutParams.rightMargin += (int) (fab.getWidth() * x);
        layoutParams.bottomMargin += (int) (fab.getHeight() * y);
        fab.setLayoutParams(layoutParams);
        fab.startAnimation(show_fab);
        fab.setClickable(b);
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