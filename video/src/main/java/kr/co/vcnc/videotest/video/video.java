package kr.co.vcnc.videotest.video;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.RelativeLayout;
import org.w3c.dom.Text;

import java.io.IOException;


public class video extends Activity implements TextureView.SurfaceTextureListener {

    private static final String FILE_URL = "http://www.w3schools.com/html/mov_bbb.mp4";
    private MediaPlayer mMediaPlayer;

    private float mVideoWidth;
    private float mVideoHeight;
    private TextureView mTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        calculateSize();
        initView();
    }

    public void calculateSize(){
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();

        String mHeight = "360";
        String mWidth = "240";
//        metadataRetriever.setDataSource(getApplicationContext(), Uri.parse(FILE_URL));
//        String mHeight = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
//        String mWidth = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        mVideoHeight = Float.parseFloat(mHeight);
        mVideoWidth = Float.parseFloat(mWidth);
    }

    private void initView() {
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mTextureView.setSurfaceTextureListener(this);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rootview);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        updateTextureViewSize((int) event.getX(), (int)event.getY());
                        break;
                }
                return true;
            }
        });
    }

    private void updateTextureViewSize(int viewWidth, int viewHeight) {

        int pivotX = viewWidth / 2;
        int pivotY = viewHeight / 2;

        Matrix matrix = new Matrix();
        matrix.setScale(2.0f, 2.0f, pivotX, pivotY);

        mTextureView.setTransform(matrix);
        mTextureView.setLayoutParams(new RelativeLayout.LayoutParams(viewWidth, viewHeight));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_video, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Surface surface = new Surface(surfaceTexture);

        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(FILE_URL));

            mMediaPlayer.setSurface(surface);
            mMediaPlayer.setLooping(true);

            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    mp.seekTo(5000);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        if (mMediaPlayer.getCurrentPosition() >= 6000) {
            mMediaPlayer.pause();
        }
    }
}
