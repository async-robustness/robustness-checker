import android.content.Context;
import android.view.MenuItem;
import checker.Checker;
import com.vlille.checker.Application;
import com.vlille.checker.R;
import com.vlille.checker.ui.HomeActivity;

public class DetUpdateStations {

    public static void main(String[] args) {
        Context.initApplication(Application.class);

        HomeActivity ha = new HomeActivity();
        ha.onCreate(null); //onStart and onResume not implemented

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();
        ha.onOptionsItemSelected(new MenuItem(R.id.main_menu_refresh));
        Checker.afterEvent();
    }
}
