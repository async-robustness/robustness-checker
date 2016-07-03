import android.content.Context;
import checker.Checker;
import com.totsp.bookworm.BookSearch;
import com.totsp.bookworm.BookWormApplication;


public class SerSearchBack {

    public static void main(String[] args) {

        // Create BookWorm Application
        Context.initApplication(BookWormApplication.class);

        Checker.setCheckerMode(args[0]);

        // Initiaize the activity
        Checker.beforeEvent();
        // Create BookSearch Activity
        BookSearch bs = new BookSearch();
        bs.onCreate(null);
        Checker.afterEvent();

        Checker.beforeEvent();
        // Write text "Book"
        bs.searchInput.setText("Book");
        Checker.afterEvent();

        Checker.beforeEvent();
        // Click to search
        bs.searchButton.callOnClick();
        Checker.afterEvent();

        Checker.beforeEvent();
        // Press on the back button (pause and stop the activity)
        bs.onBackPressed();
        Checker.afterEvent();
    }
}
