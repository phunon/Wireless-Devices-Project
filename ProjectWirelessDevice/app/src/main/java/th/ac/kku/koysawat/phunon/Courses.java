package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Courses {

    public String course_id;
    public ArrayList<Student> student;
    public ArrayList<Teacher> teacher;
    public String name;
    public String create_at;
    public String description;

    public Courses() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getName() {
        return name;
    }

    public String getCreate_at() {
        return create_at;
    }

    public ArrayList<Student> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<Student> student) {
        this.student = student;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourse_id() {
        return course_id;
    }

    public Courses(String course_id, String name, String description, ArrayList<Student> student,ArrayList<Teacher> teacher) {
        this.course_id = course_id;
        this.name = name;
        this.create_at = new Date().toString();
        this.description = description;
        this.student = student;
        this.teacher = teacher;
    }

}