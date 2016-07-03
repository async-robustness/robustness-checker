import android.content.Context;
import android.preference.Preference;
import checker.Checker;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.PreferencesActivity;

public class SerSaveSettingsSavePref {

    public static void main(String[] args) {

        Context.initApplication(IRCCloudApplication.class);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // Event 1 - Initialize the activity
        PreferencesActivity pa = new PreferencesActivity();
        pa.onCreate(null);
        //ma.onStart(); not implemented in Message Activity
        pa.onResume();
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 2 - save settings
        pa.settingstoggle.onPreferenceChange(new Preference(), new Object());
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 3 - save preferences
        pa.prefstoggle.onPreferenceChange(new Preference(), new Object());
        Checker.afterEvent();

    }
}
