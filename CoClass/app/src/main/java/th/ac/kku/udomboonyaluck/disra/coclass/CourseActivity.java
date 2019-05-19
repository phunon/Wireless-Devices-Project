package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CourseActivity extends AppCompatActivity {

    TextView course_act_name,course_act_teacher;
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


    }
}
