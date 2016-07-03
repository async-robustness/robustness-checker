import android.content.Context;
import android.os.Bundle;
import android.view.View;
import checker.Checker;
import com.vlille.checker.Application;
import com.vlille.checker.ui.fragment.AllStationsFragment;

public class DetOpenAllStFragment {

    public static void main(String[] args) {
        Context.initApplication(Application.class);

        AllStationsFragment allStations = new AllStationsFragment();

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();

        allStations.onCreate(null);
        // allStations.setStations(stations);
        allStations.onViewCreated(new View(), new Bundle());
        allStations.onResume();

        Checker.afterEvent();
    }
}
