package th.ac.kku.udomboonyaluck.disra.coclass;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import android.view.LayoutInflater;

import android.view.ContextMenu;
import android.view.MenuItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.htextview.base.HTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button signOutBtn;
    private FloatingActionButton addCourse;
    private Dialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference dbRef,dbRef2;
    private CircleImageView profileImage;
    private TextView username,numofCourse;
    private FirebaseUser FireUser = auth.getCurrentUser();;
    String name = "",sid = "";
    String cCode;
    int numCourse = 0, numOfStd = 0;
    Boolean added = false,loadData = true;
    String cname = "";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    ArrayList<String> lstcCode;

    ArrayList<String> courseSize;
    ArrayList<Student> lstStudent;

    private HTextView txt_info;
    int delay = 2000; //milliseconds
    Handler handler;
    ArrayList<String> arrMessages = new ArrayList<>();
    int position=0;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOutBtn = findViewById(R.id.sign_out);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...please wait");
        progressDialog.show();

        username = findViewById(R.id.showName);

        //add course


        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("");

        getUsernameID();
        //read code of course from database
        dbRef = database.getReference("courses/");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstcCode = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String code = snapshot.getKey().toString();
                    lstcCode.add(code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dialog = new Dialog(this);
        addCourse = findViewById(R.id.addCourse);
        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setContentView(R.layout.create_join_course);
                dialog.show();

                Button create,join;

                create = dialog.findViewById(R.id.create);
                join = dialog.findViewById(R.id.join);

                create.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.setContentView(R.layout.create_course);
                        dialog.show();

                        final EditText courseName,code;
                        Button confirm;
                        final ImageButton random;

                        courseName = dialog.findViewById(R.id.courseName);
                        code = dialog.findViewById(R.id.code);
                        confirm = dialog.findViewById(R.id.confirm);
                        random = dialog.findViewById(R.id.randomCode);

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String name = courseName.getText().toString();
                                cCode = code.getText().toString();
                                boolean ckcode = true;
                                for(int i = 0; i< lstcCode.size();i++){
                                    if(lstcCode.get(i).equals(cCode)){
                                        Toast.makeText(MainActivity.this,"Code ซ้ำนะจ๊ะ 555",Toast.LENGTH_LONG).show();
                                        ckcode = false;
                                        break;
                                    }
                                }
                                if (ckcode){
                                    writeNewCourse(FireUser.getUid(),name,cCode);
                                    dialog.dismiss();
                                } else {
                                    ckcode = true;
                                }

                            }
                        });

                        random.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // random code
                                String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                                Random rand = new Random();
                                String codeStr = "";

                                for(int i = 0; i < 6 ; i++){
                                    int n = rand.nextInt(str.length());
                                    codeStr += str.charAt(n);
                                }

                                code.setText(codeStr);

                            }
                        });

                    }
                });

                join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.setContentView(R.layout.add_course);
                        dialog.show();
                        final EditText courseCode = dialog.findViewById(R.id.courseCode);
                        Button confirm = dialog.findViewById(R.id.confirm);
                        //join course
                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                added = true;
                                final String cCode = courseCode.getText().toString();
                                dbRef = database.getReference("");
                                boolean haveCode = false,checkName = true;
                                for (int i = 0; i<lstcCode.size() ; i++){
                                    if (cCode.equals(lstcCode.get(i))){
                                        haveCode = true;
                                        break;
                                    }
                                }
                                if (haveCode) {
                                    getUsernameID();
                                    //ref for check name of user
                                    lstStudent = new ArrayList<>();
                                    dbRef = database.getReference("/courses/");
                                    dbRef.child(cCode).child("students").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            lstStudent.clear();
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                Student student = snapshot.getValue(Student.class);
                                                assert student != null;
                                                //get list of student for use in a future
                                                lstStudent.add(student);
                                                if (name.equals(student.getUsername())){
                                                    added = false;
                                                    break;
                                                }
                                            }
                                            //if added is true add student else don't added student to course
                                            if ( added ){
                                                dialog.dismiss();
                                                Student student = new Student(name,sid,0);
                                                final Map<String, Object> studentValues = student.toMap();
                                                final Map<String, Object> childUpdates = new HashMap<>();

                                                numOfStd = lstStudent.size();
                                                childUpdates.put("/courses/" + cCode + "/students/student" + numOfStd, studentValues);
                                                dbRef = database.getReference("");
                                                dbRef.updateChildren(childUpdates);

                                                //get course name / ref for get course name
                                                dbRef = database.getReference("/courses/");
                                                dbRef.child(cCode).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        cname = dataSnapshot.getValue(Course.class).getCoursename() +" ";
                                                        //add to classname of user / can't get course name
                                                        joinClass(FireUser.getUid(),cname,cCode);
                                                        added = false;
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            } else {
                                                added = false;
                                                Toast.makeText(MainActivity.this,"Mee course แล้ว",Toast.LENGTH_LONG).show();
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(MainActivity.this,"Course doesn't exist.",Toast.LENGTH_LONG).show();
                                    haveCode = false;
                                }

                            }
                        });
                    }
                });
            }
        });

        // add จำนวน courses
        dbRef = database.getReference("users/" + FireUser.getUid()+"/owned");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseSize = new ArrayList<>();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    String code = snapshot.getKey().toString();
                    courseSize.add(code);
                    numCourse = courseSize.size();
                    numofCourse = findViewById(R.id.numOfCourse);
                    numofCourse.setText("Your course : " + numCourse);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profileImage = findViewById(R.id.profileImage);
        Picasso.get().load(FireUser.getPhotoUrl()).into(profileImage);


        numofCourse = findViewById(R.id.numOfCourse);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        // Check dataLoad

        if (loadData) {
            adapter = new ViewPagerAdapter(getSupportFragmentManager());
            dbRef.getRoot().child("users").child(FireUser.getUid()).child("owned").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    adapter.AddFragment(new FragmentCourses(),"Course");
                    adapter.AddFragment(new FragmentClasses(),"Class");

                    viewPager.setAdapter(adapter);
                    tabLayout.setupWithViewPager(viewPager);

                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_course);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_class);

                    numCourse = (int) dataSnapshot.getChildrenCount();
                    numofCourse.setText("Your course : " + numCourse);

                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value\
                    Log.i("Firebase databsae",error.toString());
                    progressDialog.dismiss();
                }
            });
            loadData = false;
        }

        // Text show

        arrMessages.add("Slide down");
        arrMessages.add("See more information");
        txt_info = findViewById(R.id.txt_info);
        txt_info.animateText(arrMessages.get(position));
        handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                handler.postDelayed(this, delay);
                if(position>=arrMessages.size())
                    position=0;
                txt_info.animateText(arrMessages.get(position));
                position++;
            }
        }, delay);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.click_list, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option1:
                Toast.makeText(MainActivity.this, "Option 1 click",Toast.LENGTH_LONG).show();
                return true;
            case R.id.option2:
                Toast.makeText(MainActivity.this, "Option 2 click",Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onContextItemSelected(item);

    }

    private void joinClass(String uid, String cName, String cCode) {
        final String teacherName = name;
        dbRef2 = database.getReference("/users/" + uid);

        Course course = new Course(cName,cCode,teacherName);
        Map<String, Object> courseValues = course.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.clear();
        childUpdates.put("/classes/" + cCode, courseValues);
        dbRef2.updateChildren(childUpdates);
    }

    private void writeNewCourse(String uid, String cName, String cCode) {
        final String teacherName = name;
        dbRef = database.getReference("");

        Course course = new Course(cName,cCode,teacherName);
        Map<String, Object> courseValues = course.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/courses/" + cCode, courseValues);
        dbRef.updateChildren(childUpdates);

        childUpdates.clear();
        dbRef = database.getReference("/users/" + uid);
        childUpdates.put("/owned/" + cName, courseValues);
        dbRef.updateChildren(childUpdates);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void signOut() {
        auth.signOut();
        this.finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void getUsernameID(){
        dbRef = database.getReference("/users/");
        dbRef.child(FireUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                name = (String) user.toMap().get("username");
                sid = (String) user.toMap().get("id");
                username.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
