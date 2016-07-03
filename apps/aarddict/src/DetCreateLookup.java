import aarddict.android.LookupActivity;
import android.os.Bundle;
import checker.Checker;

public class DetCreateLookup {

    public static void main(String[] args) {

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        LookupActivity la = new LookupActivity();
        // inits UI, starts service, binds service to connection
        // (that async calls onServiceConnected and loads dictionary in the backg)
        // writes dictionaryService in onServiceConnected method
        la.onCreate(new Bundle());
        Checker.afterEvent();

    }
}
