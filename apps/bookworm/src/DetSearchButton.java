import android.content.Context;
import checker.Checker;
import com.totsp.bookworm.BookSearch;
import com.totsp.bookworm.BookWormApplication;


public class DetSearchButton {

    public static void main(String[] args) {

        // Create BookWorm Application
        Context.initApplication(BookWormApplication.class);

        // Create BookSearch Activity
        BookSearch bs = new BookSearch();
        bs.onCreate(null);

        // enter text into the searchInput text field
        bs.searchInput.setText("Book");

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();
        bs.searchButton.callOnClick();
        Checker.afterEvent();

    }
}
