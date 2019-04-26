package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class CoClass {

    public Courses coursse_id;
    public Student student_id;
    public Teacher teacher_id;

    public CoClass() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CoClass(Courses coursse_id, Student student_id, Teacher teacher_id) {
        this.coursse_id = coursse_id;
        this.student_id = student_id;
        this.teacher_id = teacher_id;
    }

}