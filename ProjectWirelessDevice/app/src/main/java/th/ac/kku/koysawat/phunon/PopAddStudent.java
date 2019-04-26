package th.ac.kku.koysawat.phunon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class PopAddStudent extends Activity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    FirebaseDatabase database;
    ArrayList<Student> studentArrayList;
    Boolean check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popaddst_window);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        Button btn = (Button) findViewById(R.id.bt_addst);
        database = FirebaseDatabase.getInstance();
        Bundle extras = getIntent().getExtras();
        String courses_id = extras.getString("course_id");
        DatabaseReference myRef = database.getReference().child("Courses").child(courses_id).child("student");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                        GenericTypeIndicator<ArrayList<Student>> t = new GenericTypeIndicator<ArrayList<Student>>() {};
                        studentArrayList = dataSnapshot.getValue(t);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value\
                Log.i("Firebase databsae",error.toString());
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check = false;
                final EditText name = (EditText) findViewById(R.id.addName);
                final EditText email = (EditText) findViewById(R.id.addEmail);
                check = true;
                Bundle extras = getIntent().getExtras();
                String courses_id = extras.getString("course_id");
                DatabaseReference reference = database.getReference().child("Courses").child(courses_id).child("student");
                Student student = new Student("RandomRandomRandom",name.getText().toString(),email.getText().toString());
                studentArrayList.add(student);
                reference.setValue(studentArrayList);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (check) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(), ClassActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value\
                        Log.i("Firebase databsae",error.toString());
                    }
                });
            }
        });
    }
}
