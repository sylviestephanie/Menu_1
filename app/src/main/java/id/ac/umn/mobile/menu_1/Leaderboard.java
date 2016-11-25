package id.ac.umn.mobile.menu_1;

/**
 * Created by Vannia Ferdina on 11/25/2016.
 */

public class Leaderboard {
    private String username;
    private String score;
    private String current_level;

    public Leaderboard(){}

    public Leaderboard(String username, String score, String current_level) {
        this.username = username;
        this.score = score;
        this.current_level = current_level;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(String current_level) {
        this.current_level = current_level;
    }
}
