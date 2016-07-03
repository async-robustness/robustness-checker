import android.content.Context;
import android.view.View;
import checker.Checker;
import net.kw.shrdlu.grtgtfs.R;
import net.kw.shrdlu.grtgtfs.SearchActivity;
import net.kw.shrdlu.grtgtfs.GRTApplication;


public class SerSearchStopTxtRouteButton {

    // invoke search stop (by text) and route (by button)
    public static void main(String[] args) {

        // Create GRTApplication
        Context.initApplication(GRTApplication.class);

        Checker.setCheckerMode(args[0]);

        // set a hint that the root of the conflict cycle is in sync code
        Checker.setHintRootInSync(true);

        Checker.beforeEvent();
        // Create Search Activity
        SearchActivity sa = new SearchActivity();
        sa.onCreate(null);
        Checker.afterEvent();

        Checker.beforeEvent();
        sa.mSearchText.callOnTextChangedListener(0, "text", 0, 0, 15); // searches when text changes
        Checker.afterEvent();

        Checker.beforeEvent();
        View v = new View();
        v.setId(R.id.button_searchroutes);
        sa.onButtonClick(v); // search by clicking a button for routes
        Checker.afterEvent();

    }
}
