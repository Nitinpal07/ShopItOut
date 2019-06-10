package nitin.luckyproject.shopitout;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;


public class HomeActivity extends AppCompatActivity {

private Toolbar mtoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeactivity);

       mtoolbar =findViewById(R.id.home_toolbar);
       setSupportActionBar(mtoolbar);
       getSupportActionBar().setTitle("Daily Shopping List");
    }
}
