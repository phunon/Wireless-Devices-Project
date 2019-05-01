package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Student{

    public String studentName;

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    String classname;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String score;


    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;

    }

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(Student.class)
    }



    public Student(String studentName,String score,String classname) {
        this.studentName = studentName;
        this.score = score;
        this.classname = classname;

    }

}