import android.content.Context;
import android.view.LayoutInflater;
import checker.*;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.MessageActivity;
import com.irccloud.android.data.BuffersDataSource;
import com.irccloud.android.data.ServersDataSource;
import com.irccloud.android.fragment.BuffersListFragment;

import java.util.ArrayList;

public class DetSelectBuffer {

    public static void main(String[] args) {

        // Init Application
        Context.initApplication(IRCCloudApplication.class);

        // Initialize message activity
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

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // select buffer
        ma.onBufferSelected(-1);
        Checker.afterEvent();

    }
}
