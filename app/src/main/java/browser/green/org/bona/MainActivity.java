package browser.green.org.bona;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

import browser.green.org.bona.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {
    private Button btnSea,btnRain,btnThunder,btnDesert;
//    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.getSupportActionBar().hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
//        mMediaPlayer=MediaPlayer.create(this,R.raw.rain);
//        try {
//            mMediaPlayer.prepare();
//        } catch (IllegalStateException e) {
//
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        mMediaPlayer.start();
    }
//    int flag=1;
//    public void isPause(View view) {
//        btnSea=findViewById(R.id.btnPause);
//        if(flag%2==0){
//            btnSea.setBackgroundResource(R.drawable.pause1);
//            mMediaPlayer.start();
//            flag++;
//
//        }
//        else{
//            btnSea.setBackgroundResource(R.drawable.pause);
//            mMediaPlayer.pause();
//            flag++;
//        }
//
//    }

}
