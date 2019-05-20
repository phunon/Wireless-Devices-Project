package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    TextView course_act_name,course_act_teacher;
    ImageView more;
    private List<Student> lstStudent;
    private RecyclerView student_list;
    private String code;
    FirebaseDatabase database;
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        course_act_name = findViewById(R.id.course_act_name);
        course_act_teacher = findViewById(R.id.course_act_teacher);
        student_list = findViewById(R.id.student_list);
        more = findViewById(R.id.more);

        course_act_name.setText(intent.getStringExtra("courseName"));
        course_act_teacher.setText(intent.getStringExtra("teacher"));
        code = intent.getStringExtra("code");

        database = FirebaseDatabase.getInstance();

        lstStudent = new ArrayList<>();
        dbRef = database.getReference("/courses/");
        dbRef.child(code).child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstStudent.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    assert student != null;
                    lstStudent.add(student);
                    assert lstStudent != null;
                    StudentRecyclerAdapter recyclerViewAdapter = new StudentRecyclerAdapter(CourseActivity.this,lstStudent,code);
                    student_list.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
                    student_list.setAdapter(recyclerViewAdapter);
                }
                assert lstStudent != null;
                StudentRecyclerAdapter recyclerViewAdapter = new StudentRecyclerAdapter(CourseActivity.this,lstStudent,code);
                student_list.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
                student_list.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(),v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.export) {
                            dbRef =  database.getReference("/courses/" + code + "/students/");
                            dbRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    int count = 0;
                                    String jsonStd = "{\"students\":[";
                                    JSONObject jsonObj = new JSONObject();
                                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                        Student student = snapshot.getValue(Student.class);
                                        assert student != null;
                                        String jsonString = "{\"student" + count + "\":{\"id\": \"" + student.getId() + "\"," +
                                                "\"username\":\"" + student.getUsername() + "\"," +
                                                "\"score\": " + student.getScore() + "}}";

                                        if(count == dataSnapshot.getChildrenCount()-1){
                                            jsonStd += jsonString + "]";
                                        } else {
                                            jsonStd += jsonString + ",";
                                        }
                                        count++;
                                    }
                                    jsonStd += "}";
                                    try {
                                        jsonObj = new JSONObject(jsonStd);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String filename = code;
                                    String fileContents = jsonObj.toString();
                                    FileOutputStream outputStream;

                                    try {
                                        outputStream = openFileOutput(filename + ".json", Context.MODE_PRIVATE);
                                        outputStream.write(fileContents.getBytes());
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getBaseContext(),"Your json file at \"/data/data/th.ac.kku.udomboonyaluck.disra.coclass/files/ddd.json\"",Toast.LENGTH_LONG).show();
                                    Log.d("jsonStudent",jsonObj.toString());
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.setting);
                popupMenu.show();
            }
        });
    }

}
