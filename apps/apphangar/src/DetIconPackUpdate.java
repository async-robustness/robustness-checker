import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.mimic.apphangar.IconPackHelper;
import ca.mimic.apphangar.Settings;
import checker.Checker;


public class DetIconPackUpdate {

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

        Settings.PrefsFragment pf = new Settings.PrefsFragment();
        // sets icon_pack_preference.setOnPreferenceClickListener to call IconPackHelper.PickIconPack with isPicker=false, moreAppIcon=false
        pf.onCreate(new Bundle());
        //Fragment can be added inside layout file and inflated
        pf.onCreateView(new LayoutInflater(), new ViewGroup(), new Bundle());
        pf.onViewCreated(new View(), new Bundle());
        pf.onActivityCreated(new Bundle());
        pf.onResume();
        pf.icon_pack_preference.callOnPreferenceClickListener(new Preference());  // sets AlertDialog.onClick

        Checker.setCheckerMode(args[0]);
        //Checker.setHintPivot("mAppRowAdapter");
        // Check whether deterministic:
        Checker.beforeEvent();
        IconPackHelper.alertDialog.callOnClickListener(IconPackHelper.alertDialog, 0);  // nondet
        Checker.afterEvent();

    }
}
