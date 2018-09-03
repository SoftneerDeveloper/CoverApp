package ke.co.coverapp.coverapp.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.VideoView;

import ke.co.coverapp.coverapp.R;
import ke.co.coverapp.coverapp.application.MyApplication;

import static ke.co.coverapp.coverapp.pojo.Keys.keys;

public class SplashActivity extends AppCompatActivity {
    boolean skip_corousel = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.SKIP_GETTING_STARTED, false);
    boolean skip_login = MyApplication.readFromPreferences(MyApplication.getAppContext(), keys.SKIP_LOGIN, false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_splash);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        VideoView videoViewSplash = (VideoView )findViewById(R.id.videoViewSplash);
        //VideoView videoViewSplash = new VideoView(this);
        //setContentView(videoViewSplash);
        Uri path =  Uri.parse("android.resource://"+ getPackageName()+"/"+R.raw.anime1);
        videoViewSplash.setVideoURI(path);
        videoViewSplash.layout(0, 0, 0, 0);
        videoViewSplash.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {

            }
        });
        //MediaPlayer music;
        //music=MediaPlayer.create(SplashActivity.this,R.raw.startupsound);
        //music.start();
        videoViewSplash.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVolume(0f, 0f);
               // mp.setLooping(true);
            }
        });
        videoViewSplash.start();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (skip_corousel && skip_login) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else if (!skip_corousel){
                    startActivity(new Intent(SplashActivity.this, IntroActivity.class));
                    finish();

                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();

                }
                finish();

            }
        }, keys.SPLASH_DISPLAY_TIMER);
    }



}
