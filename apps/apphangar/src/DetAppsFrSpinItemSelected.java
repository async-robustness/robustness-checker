import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.mimic.apphangar.Settings;
import checker.Checker;


public class DetAppsFrSpinItemSelected {

    public static void main(String[] args) {
        // create application and call onCreate
        Context.initApplication(Application.class);

        // create and call onCreate, onStart and onResume of the Activity
        Settings settingsActivity = new Settings();
        settingsActivity.onCreate(new Bundle());
        //settingsActivity.onStart();
        settingsActivity.onResume();

        // We need appFragment as well to set up some variables (e.g. ListView)
        Settings.AppsFragment ap = new Settings.AppsFragment();
        ap.onCreate(new Bundle());
        //Fragment can be added inside layout file and inflated
        ap.onCreateView(new LayoutInflater(), new ViewGroup(), new Bundle());
        ap.onViewCreated(new View(), new Bundle());
        ap.onActivityCreated(new Bundle());
        ap.onResume();

        Checker.setCheckerMode(args[0]);
        // Check whether deterministic:
        Checker.beforeEvent();
        ap.topSpin.callOnItemSelectedListener(0, 0);
        Checker.afterEvent();
    }
}
