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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.UUID;

public class PopAdd extends Activity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser FireUser = auth.getCurrentUser();
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popadd_window);

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

        Button btn = (Button) findViewById(R.id.bt_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name = (EditText) findViewById(R.id.addCourse);
                final EditText detail = (EditText) findViewById(R.id.addDetail);
                database = FirebaseDatabase.getInstance();
                String uniqueID = UUID.randomUUID().toString();
                Teacher teacher = new Teacher(FireUser.getUid(),FireUser.getDisplayName(),FireUser.getEmail());
                ArrayList<Teacher> teacherArrayList = new ArrayList<Teacher>();
                teacherArrayList.add(teacher);
                final Courses course = new Courses(uniqueID,name.getText().toString(),detail.getText().toString(),null,teacherArrayList);
                DatabaseReference myRef = database.getReference().child("Courses");
                myRef.child(uniqueID).setValue(course);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        //String value = dataSnapshot.getValue(String.class);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
