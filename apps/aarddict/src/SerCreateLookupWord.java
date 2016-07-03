import aarddict.android.LookupActivity;
import android.os.Bundle;
import checker.Checker;

public class SerCreateLookupWord {

    public static void main(String[] args) {

        Checker.setCheckerMode(args[0]);
        // Hint the field name of the pivot to the checker
        //Checker.setHintPivot("dictionaryService");

        Checker.beforeEvent();
        LookupActivity la = new LookupActivity();
        // inits UI, starts service, binds service to connection
        // (that async calls onServiceConnected and loads dictionary in the backg)
        // writes dictionaryService in onServiceConnected method
        la.onCreate(new Bundle());
        Checker.afterEvent();

        Checker.beforeEvent();
        la.editText.setText("searchword");
        Checker.afterEvent();

        Checker.beforeEvent();
        // calls doLookup and reads dictionaryService
        la.textWatcher.afterTextChanged(la.editText.getText());
        Checker.afterEvent();

    }
}
