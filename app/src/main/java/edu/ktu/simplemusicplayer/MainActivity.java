package edu.ktu.simplemusicplayer;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button playButton;
    SeekBar positionBar,soundBar;
    TextView elapsedTime,remainingTime;
    MediaPlayer mp;
    int totalTime;

    ListView myPlayList;
    //String items;
    private File root;
    private ArrayList<File> fileList = new ArrayList<File>();

    private playListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playButton = (Button) findViewById(R.id.playButton);
        elapsedTime = (TextView) findViewById(R.id.elapsedTime);
        remainingTime=(TextView) findViewById(R.id.timeLeft);

        myPlayList=(ListView) findViewById(R.id.playListLv);

        final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());

        adapter = new playListAdapter(this,mySongs);
        myPlayList.setAdapter(adapter);

        myPlayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),mySongs.get(position).getName()+" is playing now.",Toast.LENGTH_LONG).show();
                playMusic(mySongs.get(position).getName());
            }
        });

        //mp=MediaPlayer.create(this,R.raw.example_music);
        mp=MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/music/"+mySongs.get(0).getName()));
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime=mp.getDuration();
        mp.stop();

        positionBar = (SeekBar) findViewById(R.id.positionBar);
        positionBar.setMax(totalTime);
        positionBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mp.seekTo(i);
                    positionBar.setProgress(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        soundBar=(SeekBar) findViewById(R.id.volumeBar);
        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                float soundLevel=i/100f;
                mp.setVolume(soundLevel,soundLevel);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mp !=null) {
                    try{
                        Message message=new Message();
                        message.what = mp.getCurrentPosition();
                        handler.sendMessage(message);
                        Thread.sleep(1000);
                    }catch (InterruptedException e){}
                }
            }
        }).start();

    }

    private void playMusic(String fileName){
        mp.stop();
        //mp = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/Music/intro.mp3"));
        mp = MediaPlayer.create(this, Uri.parse(Environment.getExternalStorageDirectory().getPath()+ "/music/"+fileName));
        mp.setLooping(true);
        mp.setLooping(true);
        mp.seekTo(0);
        mp.setVolume(0.5f,0.5f);
        totalTime=mp.getDuration();
        positionBar.setMax(totalTime);
        playButton.setBackgroundResource(R.drawable.stop);
        mp.start();
    }

    private ArrayList<File> findSongs(File root) {
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile: files){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message){
            int currentPosition = message.what;
            positionBar.setProgress(currentPosition);

            String passedTime = createTimeLabel(currentPosition);
            elapsedTime.setText(passedTime);

            String timeLeft = createTimeLabel(totalTime-currentPosition);
            remainingTime.setText("- "+timeLeft);
        }
    };

    public String createTimeLabel(int time){
        String timeLabel="";
        int min=time/1000/60;
        int sec=time/1000%60;

        timeLabel=min+":";
        if(sec<10){
            timeLabel +="0";
        }
        timeLabel +=sec;
        return timeLabel;
    }

    public void playButtonClick(View view){
        if(!mp.isPlaying()){
            mp.start();
            playButton.setBackgroundResource(R.drawable.stop);
        }else{
            mp.pause();
            playButton.setBackgroundResource(R.drawable.play);
        }
    }
}
