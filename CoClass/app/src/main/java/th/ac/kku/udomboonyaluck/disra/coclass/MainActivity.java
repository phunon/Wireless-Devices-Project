package th.ac.kku.udomboonyaluck.disra.coclass;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Button signOutBtn;
    private ImageButton addCourse;
    RecyclerView recyclerView;
    private Dialog dialog;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private CircleImageView profileImage;
    private FirebaseUser firebaseUser;
    private TextView username,numofCourse;
    private FirebaseUser FireUser;
    String name = "",sid = "";
    String cCode;
    MyRecyclerAdapter adapter;
    String[] courses;
    int numCourse = 0, numOfStd = 0;
    Boolean added = false;

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

        recyclerView = findViewById(R.id.recycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //add course

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("");
        FireUser = auth.getCurrentUser();

        getUsernameID();

        // Recycler view
        dbRef = database.getReference("courses");
        dbRef.keepSynced(true);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                updateCourse(dataSnapshot);

                if(numCourse != 0){
                    MyRecyclerAdapter adapter = new MyRecyclerAdapter(MainActivity.this,courses);
                    recyclerView.setAdapter(adapter);
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

                                writeNewCourse(FireUser.getUid(),name,cCode);
                                dialog.dismiss();
                            }
                        });

                        random.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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

                        added = true;

                        final EditText courseCode = dialog.findViewById(R.id.courseCode);
                        Button confirm = dialog.findViewById(R.id.confirm);

                        confirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final String cCode = courseCode.getText().toString();
                                dbRef = database.getReference("");
                                dbRef.child("courses").orderByChild("code").equalTo(cCode).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        getUsernameID();
                                        dbRef.child("courses").child(cCode).orderByChild("username").equalTo(name).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if(dataSnapshot.exists()){
                                                    added = false;
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                        if(dataSnapshot.exists() && added){
                                            dialog.dismiss();
                                            Student student = new Student(name,sid,0);
                                            Map<String, Object> studentValues = student.toMap();
                                            Map<String, Object> childUpdates = new HashMap<>();

                                            numOfStd = 0;
                                            for (DataSnapshot snapshot : dataSnapshot.child(cCode + "/students/").getChildren()){
                                                numOfStd += 1;
                                            }

                                            childUpdates.put("/courses/" + cCode + "/students/student" + numOfStd, studentValues);
                                            dbRef = database.getReference("");
                                            dbRef.updateChildren(childUpdates);
                                            added = false;
                                        } else if(added) {
                                            added = false;
                                            Toast.makeText(MainActivity.this,"Course doesn't exist.",Toast.LENGTH_LONG).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        // add courses

        firebaseUser = auth.getCurrentUser();
        profileImage = findViewById(R.id.profileImage);
        Picasso.get().load(firebaseUser.getPhotoUrl()).into(profileImage);

        numofCourse = findViewById(R.id.numOfCourse);
        numofCourse.setText("Your course : " + numCourse);

    }

    private void updateCourse(DataSnapshot dataSnapshot) {
        int i = 0;

        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
            Course course = snapshot.getValue(Course.class);
            String teacher = (String) course.toMap().get("teacher");
            if(teacher != null){
                if(teacher.equals(name)){
                    numCourse++;
                }
            }
        }

        if(numCourse != 0){
            numofCourse.setText("Your course : " + numCourse);
            courses = new String[numCourse];
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Course course = snapshot.getValue(Course.class);
                String teacher = (String) course.toMap().get("teacher");
                String courseName = (String) course.toMap().get("coursename");
                assert teacher != null;
                if(teacher.equals(name)) {
                    courses[i] = courseName;
                    i++;
                }
            }
        }
    }

    private void writeNewCourse(String uid, String cName, String cCode) {
        final String teacherName = name;
        dbRef = database.getReference("");

        Course course = new Course(cName,cCode,teacherName);
        Map<String, Object> courseValues = course.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/courses/" + cCode, courseValues);
        dbRef.updateChildren(childUpdates);
    }

    public void signOut() {
        auth.signOut();
        this.finish();
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                username = findViewById(R.id.showName);
                username.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
