package aarddict.android;

import android.content.Context;
import android.view.ViewGroup;

public class EinkArticleView extends ViewGroup { //removed extends ArticleView that extends WebView

    public EinkArticleView(Context context) {
        super(context);
    }

    private boolean partial;
    public static int HSCROLL_SIZE;
}
