package player.drive.google.googledriveplayer;

import android.app.Activity;
import android.content.IntentSender;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;

import static android.content.ContentValues.TAG;

public abstract class AuthInGoogle extends Activity
        implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    GoogleApiClient googleApiClient;

    final int REQUEST_CODE_AUTH = 100;

    @Override
    protected void onResume() {
        super.onResume();

        if(googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_AUTH);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Account selection activity error");
            }
        }
    }
}