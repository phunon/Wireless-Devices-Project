package th.ac.kku.udomboonyaluck.disra.coclass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CourseActivity extends AppCompatActivity {

    TextView course_act_name,course_act_teacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        Intent intent = getIntent();
        course_act_name = findViewById(R.id.course_act_name);
        course_act_teacher = findViewById(R.id.course_act_teacher);

        course_act_name.setText(intent.getStringExtra("courseName"));
        course_act_teacher.setText(intent.getStringExtra("teacher"));

    }
}
