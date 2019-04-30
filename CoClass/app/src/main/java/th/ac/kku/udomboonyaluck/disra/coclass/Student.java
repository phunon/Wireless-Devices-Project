package th.ac.kku.udomboonyaluck.disra.coclass;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Student {
    public String username;
    public String id;
    public int score;

    public Student(){

    }

    public Student(String username,String id, int score){
        this.username = username;
        this.id = id;
        this.score = score;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("id",id);
        result.put("score",score);
        return result;
    }
}
