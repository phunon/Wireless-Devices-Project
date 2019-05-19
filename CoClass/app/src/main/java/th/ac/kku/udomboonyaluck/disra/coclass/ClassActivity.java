package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ClassActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String code;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseUser FireUser;
    String name = "";
    private Boolean addQ = false, canAdd = true;

    TextView className,teacherName,currentScore;
    ImageButton callTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        database = FirebaseDatabase.getInstance();
        FireUser = auth.getCurrentUser();

        className = findViewById(R.id.class_act_name);
        teacherName = findViewById(R.id.class_act_teacher);
        currentScore = findViewById(R.id.current_score);
        callTeacher = findViewById(R.id.callTeacher);

        Intent intent = getIntent();
        className.setText(intent.getStringExtra("courseName"));
        teacherName.setText(intent.getStringExtra("teacher"));
        code = intent.getStringExtra("code");

        dbRef = database.getReference("/users/");
        dbRef.child(FireUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                assert user != null;
                name = (String) user.toMap().get("username");

                dbRef = database.getReference("/courses/" + code + "/students/");
                dbRef.orderByChild("username").equalTo(name).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Student students = snapshot.getValue(Student.class);
                            assert students != null;
                            String score = String.valueOf(students.getScore());
                            currentScore.setText(score);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        callTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQ = true;
                canAdd = true;
                dbRef = database.getReference("/courses/" + code + "/queues/");
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int queueSize = 0,count = 0;

                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            String myName = dataSnapshot.child(name).getKey();

                            assert myName != null;
                            if(myName.equals(name)){
                                canAdd = false;
                                count++;
                            }
                        }

                        if(count == 0 && addQ && canAdd){
                            dbRef = database.getReference("/courses/" + code + "/queues/" + name + "/");
                            dbRef.setValue(queueSize);
                            addQ = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
