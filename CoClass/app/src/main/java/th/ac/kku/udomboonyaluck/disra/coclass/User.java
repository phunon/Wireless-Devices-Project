package th.ac.kku.udomboonyaluck.disra.coclass;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String username;
    public String email;
    public String id;

    public User(){

    }

    public User(String username, String email, String id){
        this.username = username;
        this.email = email;
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("email",email);
        result.put("id",id);
        return result;
    }
}
