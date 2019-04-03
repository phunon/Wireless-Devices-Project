package th.ac.kku.koysawat.phunon;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Users {
    public String username;
    public String name;
    public String email;
    public String password;
    public String UID;

    public Users() {
    }

    public Users(String username, String name, String email,String password, String UID) {
        this.name = username;
        this.name = name;
        this.email = email;
        this.password = password;
        this.UID = UID;
    }
}

