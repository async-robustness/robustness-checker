import android.content.Context;
import android.widget.EditText;
import checker.Checker;
import com.totsp.bookworm.BookForm;
import com.totsp.bookworm.BookWormApplication;
import com.totsp.bookworm.R;


public class SerSaveBack {

    public static void main(String[] args) {

        // Create BookWorm Application
        BookWormApplication app = (BookWormApplication) Context.initApplication(BookWormApplication.class);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // Create BookFrom Activity
        BookForm bf = new BookForm();
        bf.onCreate(null);
        Checker.afterEvent();

        Checker.beforeEvent();
        // find the EditText for book title and write a title
        EditText bookTitleFormTab = (EditText) bf.findViewById(R.id.booktitleform, EditText.class);
        bookTitleFormTab.setText("book title");
        Checker.afterEvent();

        Checker.beforeEvent();
        // find the EditText for book author and write an author name
        EditText bookAuthors = (EditText) bf.findViewById(R.id.bookauthors, EditText.class);
        bookAuthors.setText("book author");
        Checker.afterEvent();

        Checker.beforeEvent();
        bf.saveButton.callOnClick();
        Checker.afterEvent();

        Checker.beforeEvent();
        // Pause the activity
        bf.onBackPressed();
        Checker.afterEvent();
    }
}
