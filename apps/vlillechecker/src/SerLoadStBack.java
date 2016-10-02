import android.content.Context;
import android.view.View;
import android.os.Bundle;
import checker.Checker;
import com.vlille.checker.Application;
import com.vlille.checker.ui.fragment.AllStationsFragment;


public class SerLoadStBack {

    public static void main(String[] args) {

        Context.initApplication(Application.class);

        Checker.setCheckerMode(args[0]);
        //Checker.setHintPivot("mCancelled");

        Checker.beforeEvent();
        // Event 1 - Load all stations Fragment
        AllStationsFragment allStations = new AllStationsFragment();
        allStations.onCreate(null);
        allStations.onViewCreated(new View(), new Bundle());
        allStations.onResume();

        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 2 - Click on  the first item
        allStations.getListView().callOnItemClickListener(0, 0);
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 3 - Go back from the fragment
        allStations.onPause();
        Checker.afterEvent();

    }
}
