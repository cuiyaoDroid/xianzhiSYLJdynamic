package com.xianzhi.tool.view;

import java.util.Timer;
import java.util.TimerTask;

import com.xianzhisylj.dynamic.R;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class VideoPlayerDialog extends AlertDialog implements OnPreparedListener,OnCompletionListener{
	private String path;
	private ImageButton cancal_btn;
	private SeekBar seekBar;
	private ImageButton playBtn;
	public VideoPlayerDialog(Context context, int theme, String path) {
		super(context, theme);
		this.path = path;
	}

	public VideoPlayerDialog(Context context) {
		super(context);
	}

	MediaController controller;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_videoplayer);
		surface = (SurfaceView) findViewById(R.id.surface);
		cancal_btn = (ImageButton) findViewById(R.id.cancal_btn);
		cancal_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		surfaceHolder = surface.getHolder();// SurfaceHolder是SurfaceView的控制接口
		surfaceHolder.setFixedSize(200, 200);
		surfaceHolder.addCallback(new Callback() {
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if (player == null) {
					return;
				}
				if (player.isPlaying()) {
					player.stop();
				}
				player.release();
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				player = new MediaPlayer();
				player.setAudioStreamType(AudioManager.STREAM_MUSIC);
				player.setOnPreparedListener(VideoPlayerDialog.this);
				player.setOnCompletionListener(VideoPlayerDialog.this);
				player.setDisplay(surfaceHolder);
				try {
					player.setDataSource(path);
					player.prepare();
					player.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 设置显示视频显示在SurfaceView上
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				// TODO Auto-generated method stub
			}
		});
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		seekBar = (SeekBar) findViewById(R.id.seekbar);  
		playBtn=(ImageButton)findViewById(R.id.play_btn);
		playBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(player==null){
					return;
				}
				if(player.isPlaying()){
					player.pause();
					playBtn.setImageResource(android.R.drawable.ic_media_play);
				}else{
					player.start();
					playBtn.setImageResource(android.R.drawable.ic_media_pause);
				}
			}
		});
		playBtn.setImageResource(android.R.drawable.ic_media_pause);
	}//进度条处理  
	private boolean isChanging=false;
    class MySeekbar implements OnSeekBarChangeListener {  
        public void onProgressChanged(SeekBar seekBar, int progress,  
                boolean fromUser) {  
        }  
  
        public void onStartTrackingTouch(SeekBar seekBar) {  
        	player.pause();
            isChanging=true;    
        }  
  
        public void onStopTrackingTouch(SeekBar seekBar) {  
        	if(player!=null){
        		player.start();
        		player.seekTo(seekBar.getProgress());
        		playBtn.setImageResource(android.R.drawable.ic_media_pause);
        	}
            isChanging=false;    
        }  
  
    } 

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		playVideo();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (player != null) {
			if (!player.isPlaying())
				player.stop();
		}
		if(mTimerTask!=null){
			mTimerTask.cancel();
		}
		if(mTimer!=null){
			mTimer.cancel();
		}
	}

	private MediaPlayer player = null;
	private SurfaceView surface;
	private SurfaceHolder surfaceHolder;

	private void playVideo() {
		if (player != null) {
			if (!player.isPlaying()) {
				player.start();
				controller.show();
			}
		}
	}
	TimerTask mTimerTask;
	Timer mTimer;
	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		seekBar.setMax(player.getDuration());
		seekBar.setOnSeekBarChangeListener(new MySeekbar());  
		mTimer = new Timer();    
        mTimerTask = new TimerTask() {    
            @Override    
            public void run() {     
                if(isChanging==true) {   
                    return;    
                }  
                seekBar.setProgress(player.getCurrentPosition());  
            }    
        };   
        mTimer.schedule(mTimerTask, 0, 100);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		playBtn.setImageResource(android.R.drawable.ic_media_play);
	}

}