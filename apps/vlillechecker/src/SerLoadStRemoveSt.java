import android.content.Context;
import android.view.View;
import android.os.Bundle;
import android.widget.CheckBox;
import checker.Checker;
import com.vlille.checker.Application;
import com.vlille.checker.R;
import com.vlille.checker.ui.fragment.AllStationsFragment;


public class SerLoadStRemoveSt {

    public static void main(String[] args) {

        Context.initApplication(Application.class);

        Checker.setCheckerMode(args[0]);
        // Hint the field name of the pivot to the checker
        // Hint after setting the mode!
        //Checker.setHintPivot("modCount");

        Checker.beforeEvent();
        // event 1 - Load all stations Fragment
        //System.out.println("All stations fragment.. ");
        AllStationsFragment allStations = new AllStationsFragment();
        allStations.onCreate(null);
        // allStations.setStations(stations);
        allStations.onViewCreated(new View(), new Bundle());
        allStations.onResume();
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 2 - Click on  the star checkbox of the first item
        CheckBox checkBox1 = (CheckBox) allStations.getListView().getChildAt(0).findViewById(R.id.detail_starred, CheckBox.class);
        checkBox1.setChecked(true);
        checkBox1.callOnClick();
        Checker.afterEvent();
    }
}
