package th.ac.kku.udomboonyaluck.disra.coclass;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Course {
    public String coursename;
    public String code;
    public String teacher;

    public Course(String name, String code, String teacher){
        this.coursename = name;
        this.code = code;
        this.teacher = teacher;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public Course() {

    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("coursename",coursename);
        result.put("code",code);
        result.put("teacher",teacher);
        return result;
    }

    public String getCoursename() {
        return coursename;
    }

    public String getCode() {
        return code;
    }

    public String getTeacher() {
        return teacher;
    }
}
