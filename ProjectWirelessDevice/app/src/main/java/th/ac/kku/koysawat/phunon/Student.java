package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Date;

@IgnoreExtraProperties
public class Student extends ArrayList<Student> {

    public String uid;
    public String name;
    public String email;
    public String start_at;

    public Student() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStart_at() {
        return start_at;
    }

    public Student(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.start_at = new Date().toString();
    }

}