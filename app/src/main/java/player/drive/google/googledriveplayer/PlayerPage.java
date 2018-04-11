package player.drive.google.googledriveplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.devbrackets.android.exomedia.ui.widget.VideoView;

import java.io.File;

public class PlayerPage extends Activity{

    VideoView player;
    String FileName;
    String ContentLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_page);
        player = (VideoView) findViewById(R.id.video_view);

        FileName = GetFileName();
        ContentLink = SystemFiles.ReadFile(this, FileName);
        Play(ContentLink);
    }

    private String GetFileName() {
        Intent intent = getIntent();
        return intent.getStringExtra("FileName");
    }

    private void Play(String contentLink) {
        player.setVideoURI(Uri.parse(contentLink));
    }
}
