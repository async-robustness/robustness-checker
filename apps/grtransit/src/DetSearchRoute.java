import android.content.Context;
import android.view.View;
import checker.Checker;
import net.kw.shrdlu.grtgtfs.R;
import net.kw.shrdlu.grtgtfs.SearchActivity;
import net.kw.shrdlu.grtgtfs.GRTApplication;


public class DetSearchRoute {

    public static void main(String[] args) {

        // Create GRTApplication
        Context.initApplication(GRTApplication.class);

        // Create Search Activity
        SearchActivity sa = new SearchActivity();
        sa.onCreate(null); // creates
        //sa.onStart(); // not implemented in the app
        //sa.onResume(); // not implemented in the app

        // write some text to the input field (starts a stop search)
        sa.mSearchText.callOnTextChangedListener(0, "text", 0, 0, 15); // calls search stop AsyncTask

        View v = new View();
        v.setId(R.id.button_searchroutes);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        sa.onButtonClick(v); // click on the button for route search that creates an AsyncTask
        Checker.afterEvent();

    }
}
