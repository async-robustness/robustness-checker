import android.content.Context;
import checker.Checker;
import net.kw.shrdlu.grtgtfs.SearchActivity;
import net.kw.shrdlu.grtgtfs.GRTApplication;


public class SerSearchStopx2Txt {
    // invoke search route x 2 by changing text
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
        sa.mSearchText.callOnTextChangedListener(0, "text", 0, 0, 15);
        Checker.afterEvent();

        Checker.beforeEvent();
        sa.mSearchText.callOnTextChangedListener(0, "text2", 0, 0, 15);
        Checker.afterEvent();

    }
}

// A violation between 1st search doInBack and 2nd search synchronous part (writes mQuery in onTextChangedListener)
// the event handler invoked by text change writes mQuery and causes conflict-ser violation
