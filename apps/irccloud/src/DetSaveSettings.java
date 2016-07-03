import android.content.Context;
import android.preference.Preference;
import checker.Checker;
import checker.CheckerMode;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.PreferencesActivity;
import checker.SkipException;

public class DetSaveSettings {

    public static void main(String[] args) {

        // Init Application
        Context.initApplication(IRCCloudApplication.class);

        // Initialize preferences activity
        PreferencesActivity pa = new PreferencesActivity();
        pa.onCreate(null);
        //ma.onStart();
        pa.onResume();

        Checker.setCheckerMode(args[0]);
        Checker.beforeEvent();
        // save settings
        pa.settingstoggle.onPreferenceChange(new Preference(), new Object());
        Checker.afterEvent();

    }
}
