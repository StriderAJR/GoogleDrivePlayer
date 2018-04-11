package player.drive.google.googledriveplayer;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.OpenFileActivityBuilder;

import java.io.File;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class HomePage extends AuthInGoogle {

    DriveId idSelectedFile;

    Metadata metadata;
    final  int REQUEST_CODE_OPENER = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button SelectFileButton = (Button) findViewById(R.id.SelectFileButton);
        SelectFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleApiClient.connect();
            }
        });

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.INTERNET},
                1);
    }

    private ResultCallback<DriveApi.DriveContentsResult> driveContentsSelectedFileCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {
                    if (!result.getStatus().isSuccess()) {
                        return;
                    }
                    String lnk = metadata.getWebContentLink();
                    SystemFiles.SaveFile(HomePage.this, FileName, lnk);
                    StartPlayer();
                }
            };

    private  ResultCallback<DriveResource.MetadataResult> metadataSelectedFileCallback = new
            ResultCallback<DriveResource.MetadataResult>() {
                @Override
                public void onResult(DriveResource.MetadataResult result) {
                    if(result.getStatus().isSuccess())
                        metadata = result.getMetadata();
                }
            };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void StartPlayer() {
        Intent intent = new Intent(this, PlayerPage.class);
        intent.putExtra("FileName", FileName);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_OPENER:
                if(resultCode == RESULT_OK) {
                    idSelectedFile = (DriveId) data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
                    SaveLink();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    String FileName = "link.rec";

    private void SaveLink() {
        DriveFile file = idSelectedFile.asDriveFile();
        file.getMetadata(googleApiClient).setResultCallback(metadataSelectedFileCallback);
        file.open(googleApiClient, DriveFile.MODE_READ_ONLY, null)
                .setResultCallback(driveContentsSelectedFileCallback);
    }

    private  void SelectFile() {
        if(idSelectedFile == null) {
            IntentSender intentSender = Drive.DriveApi
                    .newOpenFileActivityBuilder()
                    .build(googleApiClient);
            try {
                startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Failed to activity file selection");
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        SelectFile();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @TargetApi(23)
    private boolean maybeRequestPermission() {
        if (requiresPermission()) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(23)
    private boolean requiresPermission() {
        return Util.SDK_INT >= 23
                && checkSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED;
    }
}
