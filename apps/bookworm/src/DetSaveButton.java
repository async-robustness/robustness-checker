import android.content.Context;
import android.widget.EditText;
import checker.Checker;
import com.totsp.bookworm.BookForm;
import com.totsp.bookworm.BookWormApplication;
import com.totsp.bookworm.R;


public class DetSaveButton {

    public static void main(String[] args) {

        // Create BookWorm Application
        Context.initApplication(BookWormApplication.class);

        // Create BookForm Activity
        BookForm bf = new BookForm();
        bf.onCreate(null);

        // find the EditText for book title and write a title
        EditText bookTitleFormTab = (EditText) bf.findViewById(R.id.booktitleform, EditText.class);
        bookTitleFormTab.setText("book title");

        // find the EditText for book author and write an author name
        EditText bookAuthors = (EditText) bf.findViewById(R.id.bookauthors, EditText.class);
        bookAuthors.setText("book author");

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();
        bf.saveButton.callOnClick();
        Checker.afterEvent();
    }
}
