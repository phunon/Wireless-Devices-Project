package th.ac.kku.koysawat.phunon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PopJoin extends Activity {

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
        btn.setText("Join");
        //push join btn
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText tv_course_name = (EditText) findViewById(R.id.addCourse);
                final String name2 = tv_course_name.getText().toString();
                //check class name
                for (int i = 0;i< listclass.size();i++){
                    if (name2.equals(listclass.get(i))){
                        chk = 1;
                        break;
                    }   else {
                        chk = 0;
                    }
                }
                if (chk == 0 ){
                    Toast.makeText(getApplicationContext(),"had not a course name",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    chk = 1;
                    startActivity(intent);
                }else {
                    myRefuser.child(Name+"/"+name2).child("Status").setValue("student");
                    myRefclass.child(name2+"/"+"Student/"+Name).child("Score").setValue("0");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        });


    }
}

