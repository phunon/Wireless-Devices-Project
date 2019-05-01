package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Courses {

    public String courseNamee;


    public Courses() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getCourseNamee() {
        return courseNamee;
    }

    public void setCourseNamee(String courseNamee) {
        this.courseNamee = courseNamee;
    }
    public Courses(String courseNamee) {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        this.courseNamee = courseNamee;
    }
}