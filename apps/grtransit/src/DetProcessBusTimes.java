import android.content.Context;
import checker.Checker;
import checker.CheckerMode;
import net.kw.shrdlu.grtgtfs.*;


public class DetProcessBusTimes {

    public static void main(String[] args) {

        // Create GRTApplication
        Context.initApplication(GRTApplication.class);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        TimesActivity ta = new TimesActivity();
        ta.onCreate(null);
        //fa.onStart(); // not implemented in the app
        ta.onResume(); // creates AsyncTask to ProcessBusTimes
        Checker.afterEvent();

    }
}
