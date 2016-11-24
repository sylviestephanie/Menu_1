package id.ac.umn.mobile.menu_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
<<<<<<< HEAD
=======
import android.widget.TextView;

import org.w3c.dom.Text;
>>>>>>> origin/master

public class CourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
<<<<<<< HEAD
=======
        String title = getIntent().getStringExtra("TITLE");
        TextView tvTitle=(TextView)findViewById(R.id.title);
        tvTitle.setText(title);

>>>>>>> origin/master
    }
}
