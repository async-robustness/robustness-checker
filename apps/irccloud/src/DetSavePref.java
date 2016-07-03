import android.content.Context;
import android.preference.Preference;
import checker.*;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.PreferencesActivity;

public class DetSavePref {

    public static void main(String[] args) {

        // Init Application
        Context.initApplication(IRCCloudApplication.class);

        // Initialize message activity
        PreferencesActivity pa = new PreferencesActivity();
        pa.onCreate(null);
        //pa.onStart();
        pa.onResume();

        Checker.setCheckerMode(args[0]);
        Checker.beforeEvent();
        // save settings
        pa.prefstoggle.onPreferenceChange(new Preference(), new Object());
        Checker.afterEvent();

    }
}
