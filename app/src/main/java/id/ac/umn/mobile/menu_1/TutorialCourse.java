package id.ac.umn.mobile.menu_1;

/**
 * Created by sylviestephanie on 11/24/16.
 */
public class TutorialCourse {
    String title;
    String desc;
    int image;
    int flag;

    TutorialCourse(){}

    TutorialCourse(String title, String desc, int image, int flag) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.flag = flag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

}
