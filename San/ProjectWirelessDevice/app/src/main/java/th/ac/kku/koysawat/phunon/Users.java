package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;
import java.util.Date;

@IgnoreExtraProperties
public class Users {

    public String uid;
    public String name;
    public String email;
    public String create_at;

    public Users() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Users(String uid,String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.create_at = new Date().toString();
    }

}