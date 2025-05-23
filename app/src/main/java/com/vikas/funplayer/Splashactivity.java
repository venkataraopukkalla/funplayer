package com.vikas.funplayer;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.vikas.funplayer.util.RemoteConfigManager;

public class Splashactivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean isRemoteConfigApplied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashactivity);

        setupRemoteConfig();

        // Fallback in case fetch takes too long or no internet
        new Handler().postDelayed(() -> {
            if (!isRemoteConfigApplied) {
                Log.d("Splash", "RemoteConfig fallback triggered");
                goToMainActivity();
            }
        }, 3000); // wait max 3 seconds
    }

    private void setupRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(BuildConfig.DEBUG ? 0 : 3600)
                .build();

        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("RemoteConfig", "Fetch success");
                    } else {
                        Log.w("RemoteConfig", "Fetch failed, using defaults");
                    }

                    RemoteConfigManager.saveRemoteValues(this, mFirebaseRemoteConfig);
                    isRemoteConfigApplied = true;
                    goToMainActivity();
                });
    }

    private void goToMainActivity() {
        startActivity(new Intent(Splashactivity.this, MainActivity.class));
        finish();
    }
}
