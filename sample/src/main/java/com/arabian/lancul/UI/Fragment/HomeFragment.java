package com.arabian.lancul.UI.Fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.arabian.lancul.MainActivity;
import com.arabian.lancul.R;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import hb.xvideoplayer.MxVideoPlayer;
import hb.xvideoplayer.MxVideoPlayerWidget;


public class HomeFragment extends Fragment {

    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    private static final String VIDEO_URL = "https://firebasestorage.googleapis.com/v0/b/lancul-v1.appspot.com/o/Saudi%20Arabia%20in%208K.mp4?alt=media&token=b9891cdf-77a0-49ec-bea2-d3a890fac9a7";

    VideoView mVideoView;
    MediaController mMediaController;

    View mBottomLayout;
    View mVideoLayout;
    TextView mStart;

    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    View view;
    String TAG = "HomeFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

//        String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
        Uri vidUri = Uri.parse(VIDEO_URL);
        mVideoView = (VideoView) view.findViewById(R.id.myVideo);
//        mMediaController = (MediaController) view.findViewById(R.id.media_controller);
//        mVideoView.setMediaController(mMediaController);
        mVideoView.setVideoURI(vidUri);
        mVideoView.start();
        return view;
    }

}
