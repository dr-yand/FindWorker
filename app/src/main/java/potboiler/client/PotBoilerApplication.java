package potboiler.client;

import android.app.Application;

import com.vk.sdk.VKSdk;

public class PotBoilerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        VKSdk.initialize(getApplicationContext());
    }
}
