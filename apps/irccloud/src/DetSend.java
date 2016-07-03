import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import checker.*;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.MessageActivity;
import com.irccloud.android.data.BuffersDataSource;
import com.irccloud.android.data.ServersDataSource;

import java.util.ArrayList;

public class DetSend {

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

        // Write some text
        ma.messageTxt.setText("Hello");

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // click on "send"
        ma.messageTxt.callOnEditorActionListener(new TextView(), EditorInfo.IME_ACTION_SEND, new KeyEvent(EditorInfo.IME_ACTION_SEND));
        Checker.afterEvent();

    }
}
