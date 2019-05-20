package th.ac.kku.udomboonyaluck.disra.coclass;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Student {
    public String username;
    public String id;
    public int score;
    String url;
    public Student(){

    }

    public Student(String username,String id, int score,String url){
        this.username = username;
        this.id = id;
        this.score = score;
        this.url = url;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username",username);
        result.put("id",id);
        result.put("score",score);
        result.put("proUrl",url);
        return result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
