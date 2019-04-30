package th.ac.kku.udomboonyaluck.disra.coclass;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course {
    public String coursename;
    public String code;
    public String teacher;

    public Course(){

    }

    public Course(String name, String code, String teacher){
        this.coursename = name;
        this.code = code;
        this.teacher = teacher;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("coursename",coursename);
        result.put("code",code);
        result.put("teacher",teacher);
        return result;
    }
}
