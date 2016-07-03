import android.content.Context;
import checker.Checker;
import net.kw.shrdlu.grtgtfs.SearchActivity;
import net.kw.shrdlu.grtgtfs.GRTApplication;


public class DetSearchStop {

    public static void main(String[] args) {

        // Create GRTApplication
        Context.initApplication(GRTApplication.class);

        // Create Search Activity
        SearchActivity sa = new SearchActivity();
        sa.onCreate(null); // creates
        //sa.onStart(); // not implemented in the app
        //sa.onResume(); // not implemented in the app

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        sa.mSearchText.callOnTextChangedListener(0, "text", 0, 0, 15); // calls search stop AsyncTask
        Checker.afterEvent();

    }
}
