import android.content.Context;
import checker.Checker;
import net.kw.shrdlu.grtgtfs.FavstopsActivity;
import net.kw.shrdlu.grtgtfs.GRTApplication;


public class DetFavStops {

    public static void main(String[] args) {

        // Create GRTApplication
        Context.initApplication(GRTApplication.class);

        Checker.setCheckerMode(args[0]);
        Checker.beforeEvent();

        // Create Favorite Stops Activity
        FavstopsActivity fa = new FavstopsActivity();
        fa.onCreate(null);
        //fa.onStart(); // not implemented in the app
        fa.onResume(); // creates AsynTask

        Checker.afterEvent();
    }
}
