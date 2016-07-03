import aarddict.android.DictionariesActivity;
import aarddict.android.LookupActivity;
import aarddict.android.R;
import android.os.Bundle;
import android.widget.Button;
import checker.Checker;

public class SerCreateScanSD {

    public static void main(String[] args) {

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        DictionariesActivity da = new DictionariesActivity();
        // inits UI, starts service, binds service to connection
        // (that async calls onServiceConnected and loads dictionary in the backg)
        // writes dictionaryService in onServiceConnected method
        da.onCreate(new Bundle());
        Checker.afterEvent();


        Checker.beforeEvent();
        Button scanSDButton = (Button) da.findViewById(R.id.scanSDButton, Button.class);
        scanSDButton.callOnClick();
        Checker.afterEvent();

    }
}
