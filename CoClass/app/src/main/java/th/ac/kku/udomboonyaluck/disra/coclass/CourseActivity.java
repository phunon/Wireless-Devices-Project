package th.ac.kku.udomboonyaluck.disra.coclass;

import android.app.Dialog;
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
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    ArrayList<String> lstUrl;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstUrl = new ArrayList<>();
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        course_act_name = findViewById(R.id.course_act_name);
        course_act_teacher = findViewById(R.id.course_act_teacher);
        student_list = findViewById(R.id.student_list);
        more = findViewById(R.id.more);

        dialog = new Dialog(this);

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
                lstUrl.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Student student = snapshot.getValue(Student.class);
                    assert student != null;
                    lstStudent.add(student);
                    assert lstStudent != null;
                    lstUrl.add(snapshot.child("proUrl").getValue().toString());
                    StudentRecyclerAdapter recyclerViewAdapter = new StudentRecyclerAdapter(CourseActivity.this,lstStudent,code,lstUrl);
                    student_list.setLayoutManager(new LinearLayoutManager(CourseActivity.this));
                    student_list.setAdapter(recyclerViewAdapter);
                }
                assert lstStudent != null;
                StudentRecyclerAdapter recyclerViewAdapter = new StudentRecyclerAdapter(CourseActivity.this,lstStudent,code,lstUrl);
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

                            Button confirm;
                            final EditText filenameEdt;

                            dialog.setContentView(R.layout.export_json);
                            dialog.show();

                            confirm = dialog.findViewById(R.id.confirm);
                            filenameEdt = dialog.findViewById(R.id.filename);
                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    final String filename = filenameEdt.getText().toString();

                                    dbRef =  database.getReference("/courses/" + code + "/students/");
                                    dbRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            int count = 0;
                                            String jsonStd = "{\"students\":\n[\n";
                                            JSONObject jsonObj = new JSONObject();
                                            for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                                                Student student = snapshot.getValue(Student.class);
                                                assert student != null;
                                                String jsonString = "{\"student" + count + "\":{\n\"id\": \"" + student.getId() + "\"," +
                                                        "\n\"username\":\"" + student.getUsername() + "\"," +
                                                        "\n\"score\": " + student.getScore() + "\n}\n}";

                                                if(count == dataSnapshot.getChildrenCount()-1){
                                                    jsonStd += jsonString + "\n]";
                                                } else {
                                                    jsonStd += jsonString + ",\n";
                                                }
                                                count++;
                                            }
                                            jsonStd += "}";
                                            try {
                                                jsonObj = new JSONObject(jsonStd);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            String fileContents = jsonObj.toString();
                                            FileOutputStream outputStream;

                                            if(!filename.equals("")){
                                                try {
                                                    outputStream = openFileOutput(filename + ".json", Context.MODE_PRIVATE);
                                                    outputStream.write(fileContents.getBytes());
                                                    outputStream.close();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                Toast.makeText(getBaseContext(),"Your json file at \"/data/data/th.ac.kku.udomboonyaluck.disra.coclass/files/" + filename + ".json\"",Toast.LENGTH_LONG).show();
                                                Log.d("jsonStudent",jsonObj.toString());
                                                dialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            return true;
                        } else if (item.getItemId() == R.id.open) {
                            Button confirm;
                            final EditText filenameEdt;
                            dialog.setContentView(R.layout.open_json);
                            dialog.show();

                            confirm = dialog.findViewById(R.id.confirm);
                            filenameEdt = dialog.findViewById(R.id.filename);

                            confirm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String filename = filenameEdt.getText().toString();

                                    if(!filename.equals("")){
                                        String collected = null;
                                        FileInputStream fis = null;
                                        try {
                                            fis = openFileInput(filename + ".json");
                                            byte[] dataArray = new byte[fis.available()];
                                            while(fis.read(dataArray) != -1){
                                                collected = new String(dataArray);
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } finally {
                                            try {
                                                if(fis != null){
                                                    fis.close();

                                                    TableLayout tableLayout;
                                                    dialog.setContentView(R.layout.table);
                                                    tableLayout = dialog.findViewById(R.id.table);

                                                    JSONObject allScore = new JSONObject(collected);
                                                    JSONArray stdScore = (JSONArray) allScore.get("students");

                                                    for(int i = 0 ; i < stdScore.length() ; i++){
                                                        TableRow tableRow = new TableRow(getBaseContext());
                                                        TextView id = new TextView(getBaseContext());
                                                        TextView username = new TextView(getBaseContext());
                                                        TextView score = new TextView(getBaseContext());

                                                        id.setPadding(20,20,20,20);
                                                        username.setPadding(20,20,20,20);
                                                        score.setPadding(20,20,20,20);

                                                        id.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                        username.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                                        score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


                                                        JSONObject object = stdScore.getJSONObject(i);
                                                        JSONObject student = object.getJSONObject("student" + i);
                                                        id.setText(student.getString("id"));
                                                        username.setText(student.getString("username"));
                                                        score.setText(student.getString("score"));

                                                        tableRow.addView(id);
                                                        tableRow.addView(username);
                                                        tableRow.addView(score);
                                                        tableLayout.addView(tableRow);
                                                    }

                                                    dialog.show();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
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
