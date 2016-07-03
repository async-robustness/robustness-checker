import android.content.Context;
import checker.Checker;
import com.totsp.bookworm.BookForm;
import com.totsp.bookworm.BookWormApplication;
import com.totsp.bookworm.model.Book;


public class SerGenerateBack {

    public static void main(String[] args) {
        // Create BookWorm Application
        BookWormApplication app = (BookWormApplication) Context.initApplication(BookWormApplication.class);

        Checker.setCheckerMode(args[0]);

        Checker.beforeEvent();
        // Create BookForm Activity
        BookForm bf = new BookForm();
        bf.onCreate(null);

        //set selected book (to go into the background task)
        Book selectedBook = new Book();
        // id -1 makes to stay in the same activity and enables onbackpressed of the current activity
        selectedBook.id = -1;
        app.selectedBook = selectedBook;
        Checker.afterEvent();

        Checker.beforeEvent();
        bf.generateCoverButton.callOnClick();
        Checker.afterEvent();

        Checker.beforeEvent();
        // Pause the activity
        bf.onBackPressed();
        Checker.afterEvent();
    }
}
