package id.ac.umn.mobile.menu_1;

/**
 * Created by abdelhaq on 03-Dec-16.
 */

public class Notes {
    public String id,course,note,date;
    public Notes(String id,String course,String note,String date){
        this.id=id;
        this.course=course;
        this.note=note;
        this.date=date;
    }
    public String getId(){
        return id;
    }
    public String getCourse(){
        return course;
    }
    public String getNote(){
        return note;
    }
    public String getDate(){
        return date;
    }
}
