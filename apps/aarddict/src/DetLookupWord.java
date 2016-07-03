import aarddict.android.LookupActivity;
import android.os.Bundle;
import checker.Checker;

public class DetLookupWord {

    public static void main(String[] args) {

        LookupActivity la = new LookupActivity();
        // inits UI, starts service, binds service to connection
        // (that async calls onServiceConnected and loads dictionary in the backg)
        // writes dictionaryService in onServiceConnected method
        la.onCreate(new Bundle());
        la.editText.setText("searchword");

        Checker.setCheckerMode(args[0]);
        // check determinism:
        Checker.beforeEvent();
        // calls doLookup and reads dictionaryService
        la.textWatcher.afterTextChanged(la.editText.getText());
        Checker.afterEvent();

    }
}
