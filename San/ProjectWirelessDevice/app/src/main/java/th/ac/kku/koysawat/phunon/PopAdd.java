package th.ac.kku.koysawat.phunon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    ArrayList<String> listclass;
    int chk = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popadd_window);
        listclass = new ArrayList<>();
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

        final String Name = auth.getCurrentUser().getDisplayName();
        database = FirebaseDatabase.getInstance();
        final DatabaseReference myRefclass = database.getReference("Class");
        final DatabaseReference myRefuser = database.getReference("User");
        //check course name
        myRefclass.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String  courseName = data.getKey();
                    listclass.add(courseName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Button btn = (Button) findViewById(R.id.bt_add);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText tv_course_name = (EditText) findViewById(R.id.addCourse);
                final String name2 = tv_course_name.getText().toString();
                //check all class name
                for (int i = 0;i< listclass.size();i++){
                    if (name2.equals(listclass.get(i))){
                        Toast.makeText(getApplicationContext(),"had a course name",Toast.LENGTH_SHORT).show();
                        chk = 1;
                        break;
                    }   else {
                        chk = 0;
                    }
                }
                if (chk == 1 ){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    chk = 0;
                    startActivity(intent);
                }else {
                    myRefuser.child(Name+"/"+name2).child("Status").setValue("teacher");
                    myRefclass.child(name2+"/"+"Teacher").child("Name").setValue(Name);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

                //final EditText detail = (EditText) findViewById(R.id.addDetail);
                /*database = FirebaseDatabase.getInstance();
                String uniqueID = UUID.randomUUID().toString();
                Teacher teacher = new Teacher(FireUser.getUid(),FireUser.getDisplayName(),FireUser.getEmail());
                ArrayList<Teacher> teacherArrayList = new ArrayList<Teacher>();
                teacherArrayList.add(teacher);*/


                //myRef.child(uniqueID).setValue(course);

                /*myRef.addValueEventListener(new ValueEventListener() {
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
                });*/
            }
        });
    }
}
