import android.content.Context;
import android.view.LayoutInflater;
import checker.Checker;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.MessageActivity;
import com.irccloud.android.data.BuffersDataSource;
import com.irccloud.android.data.ServersDataSource;
import com.irccloud.android.fragment.BuffersListFragment;

import java.util.ArrayList;

public class SerSelectBufferBack {

    public static void main(String[] args) {

        Context.initApplication(IRCCloudApplication.class);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // Event 1 - Initialize message activity
        MessageActivity ma = new MessageActivity();
        ma.onCreate(null);
        //ma.onStart(); not implemented in Message Activity
        ma.onResume();
        // create some server and buffer data to enable sending a message
        ma.server = ServersDataSource.getInstance().createServer(0, "", "", 0, "", "", 0, 0, "", "", "", "", null, "", new ArrayList(), 0);
        ma.buffer = BuffersDataSource.getInstance().createBuffer(0, 0, 0, 0, "", "", 0, 0, 0);

        BuffersListFragment bf = new BuffersListFragment();
        bf.onCreate(null);
        bf.onAttach(ma);
        bf.onCreateView(new LayoutInflater(), null, null); //convertview is null and will be initiated
        bf.onResume();
        Checker.afterEvent();


        Checker.beforeEvent();
        // Event 2 - Write some more text
        ma.onBufferSelected(-1);  // creates showNotificationsTask
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 3 - press back button
        ma.onBackPressed();
        Checker.afterEvent();

    }
}
