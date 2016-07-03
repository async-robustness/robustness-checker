import android.content.Context;
import checker.Checker;
import com.totsp.bookworm.BookForm;
import com.totsp.bookworm.BookWormApplication;
import com.totsp.bookworm.model.Book;


public class DetRetrieveCoverButton {

    public static void main(String[] args) {

        // Create BookWorm Application
        BookWormApplication app = (BookWormApplication) Context.initApplication(BookWormApplication.class);

        // Create BookForm Activity
        BookForm bf = new BookForm();
        bf.onCreate(null);

        // Select a book with id > 0 (to go into the background task)
        Book selectedBook = new Book();
        selectedBook.id = 1;
        app.selectedBook = selectedBook;

        Checker.setCheckerMode(args[0]);

        // Check whether deterministic:
        Checker.beforeEvent();
        bf.retrieveCoverButton.callOnClick();
        Checker.afterEvent();
    }
}
