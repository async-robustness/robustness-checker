import android.content.Context;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import checker.Checker;
import com.irccloud.android.IRCCloudApplication;
import com.irccloud.android.activity.MessageActivity;
import com.irccloud.android.data.BuffersDataSource;
import com.irccloud.android.data.ServersDataSource;

import java.util.ArrayList;

public class SerSendClickUser {

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
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 2 - Write some text
        ma.messageTxt.setText("Hello");
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 3 - Click on "send"
        ma.messageTxt.callOnEditorActionListener(new TextView(), EditorInfo.IME_ACTION_SEND, new KeyEvent(EditorInfo.IME_ACTION_SEND));
        Checker.afterEvent();

        Checker.beforeEvent();
        // Event 4 - Double click on the user name
        ma.onUserDoubleClicked("Username");
        Checker.afterEvent();

    }
}
