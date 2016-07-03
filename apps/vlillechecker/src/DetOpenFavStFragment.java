import android.content.Context;
import android.os.Bundle;
import android.view.View;
import checker.Checker;
import com.vlille.checker.Application;
import com.vlille.checker.ui.fragment.StarsListFragment;

public class DetOpenFavStFragment {

    public static void main(String[] args) {
        Context.initApplication(Application.class);

        // Load all stations Fragment
        StarsListFragment favStations = new StarsListFragment();

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();

        favStations.onCreate(null);
        favStations.onViewCreated(new View(), new Bundle());
        favStations.onResume();

        Checker.afterEvent();
    }
}
