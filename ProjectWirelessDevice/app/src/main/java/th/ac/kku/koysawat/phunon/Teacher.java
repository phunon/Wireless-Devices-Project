package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
class Teacher {

    public String uid;
    public String name;
    public String email;
    public String start_at;

    public Teacher() {
        // Default constructor required for calls to DataSnapshot.getValue(Teacher.class)
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }

    public String getStart_at() {
        return start_at;
    }

    public Teacher(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.start_at = new Date().toString();
    }
}
