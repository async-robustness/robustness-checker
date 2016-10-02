/* This file is part of Aard Dictionary for Android <http://aarddict.org>.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License <http://www.gnu.org/licenses/gpl-3.0.txt>
 * for more details.
 *
 * Copyright (C) 2010 Igor Tkach
 */

package aarddict.android;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import aarddict.Article;
import aarddict.ArticleNotFound;
import aarddict.Entry;
import aarddict.LookupWord;
import aarddict.RedirectTooManyLevels;
import aarddict.Volume;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import checker.Checker;
import checker.ProcMode;
import checker.SkipException;

public class ArticleViewActivity extends BaseDictionaryActivity {

    private static final String BASE_URL = "aard://A/";

    private final static String    TAG                 = ArticleViewActivity.class
                                                               .getName();

    public static final int        NOOK_KEY_PREV_LEFT  = 92;
    public static final int        NOOK_KEY_NEXT_LEFT  = 93;

    public static final int        NOOK_KEY_PREV_RIGHT = 94;
    public static final int        NOOK_KEY_NEXT_RIGHT = 95;

    private String                 sharedCSS;
    private String                 mediawikiSharedCSS;
    private String                 mediawikiMonobookCSS;
    private String                 js;

    public List<HistoryItem>      backItems;  // made public to be accessible from the driver class
    //private Timer                  timer;
    private TimerTask              currentTask;
    private TimerTask              currentHideNextButtonTask;
    private boolean                useAnimation        = false;

    private Map<Article, ScrollXY> scrollPositionsH;
    private Map<Article, ScrollXY> scrollPositionsV;
    private boolean                saveScrollPos       = true;

    @Override
    void initUI() {
        this.scrollPositionsH = Collections
                .synchronizedMap(new HashMap<Article, ScrollXY>());
        this.scrollPositionsV = Collections
                .synchronizedMap(new HashMap<Article, ScrollXY>());
        loadAssets();

        if (DeviceInfo.EINK_SCREEN) {
            useAnimation = false;
            setContentView(R.layout.eink_article_view);
            N2EpdController.n2MainActivity = this;
        }
        // Setup animations only on non-eink screens
        else {
            useAnimation = true;
            getWindow().requestFeature(Window.FEATURE_PROGRESS);
            setContentView(R.layout.article_view);
        }

        //timer = new Timer();

        backItems = Collections.synchronizedList(new LinkedList<HistoryItem>());

        final Button nextButton = (Button) findViewById(R.id.NextButton, Button.class);
        nextButton.getBackground().setAlpha(180);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (nextButton.getVisibility() == View.VISIBLE) {
                    updateNextButtonVisibility();
                    nextArticle();
                    updateNextButtonVisibility();
                }
            }
        });

        setProgressBarVisibility(true);
    }


    private void scrollTo(ScrollXY s) {
        scrollTo(s.x, s.y);
    }

    private void scrollTo(int x, int y) {
        saveScrollPos = false;
        Log.d(TAG, "Scroll to " + x + ", " + y);
        saveScrollPos = true;
    }

    private void goToSection(String section) {
        Log.d(TAG, "Go to section " + section);
        if (section == null || section.trim().equals("")) {
            scrollTo(0, 0);
        } else {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            goBack();
            break;
        case NOOK_KEY_PREV_LEFT:
        case NOOK_KEY_PREV_RIGHT:
        case KeyEvent.KEYCODE_VOLUME_UP:
            break;
        case KeyEvent.KEYCODE_VOLUME_DOWN:
        case NOOK_KEY_NEXT_LEFT:
        case NOOK_KEY_NEXT_RIGHT:
            break;
        default:
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // eat key ups corresponding to key downs so that volume keys don't beep
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
        case KeyEvent.KEYCODE_VOLUME_UP:
        case KeyEvent.KEYCODE_VOLUME_DOWN:
            break;
        default:
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    private boolean zoomIn() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            return textZoomIn();
        }
        else {
            // removed WebView code
            return false;
        }
    }

    private boolean textZoomIn() {
        // removed WebView code
        return false;
    }

    private boolean zoomOut() {
        // removed WebView code
        return false;
    }

    private boolean textZoomOut() {
        // removed WebView code
        return false;
    }

    private void goBack() {
        if (backItems.size() == 1) {
            finish();
        }
        if (currentTask != null) {
            return;
        }
        if (backItems.size() > 1) {
            HistoryItem current = backItems.remove(backItems.size() - 1);
            HistoryItem prev = backItems.get(backItems.size() - 1);

            Article prevArticle = prev.article;
            if (prevArticle.equalsIgnoreSection(current.article)) {
                resetTitleToCurrent();
                if (!prevArticle.sectionEquals(current.article)
                        && !restoreScrollPos()) {
                    goToSection(prevArticle.section);
                }
            } else {
                showCurrentArticle();
            }
        }
    }

    private void nextArticle() {
        HistoryItem current = backItems.get(backItems.size() - 1);
        if (current.hasNext()) {
            showNext(current);
        }
    }

    @Override
    public boolean onSearchRequested() {
        Intent intent = getIntent();
        String action = intent == null ? null : intent.getAction();
        if (action != null) {
            String word = null;
            if (action.equals(Intent.ACTION_SEARCH)) {
                word = intent.getStringExtra("query");
            } else if (action.equals(Intent.ACTION_SEND)) {
                word = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
            if (word != null) {
                Intent next = new Intent();
                next.setClass(this, LookupActivity.class);
                next.setAction(Intent.ACTION_SEARCH);
                next.putExtra(SearchManager.QUERY, word);
                startActivity(next);
            }
        }
        finish();
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
            finish();
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }

    // made public to be accessible from the driver class
    public final static int MENU_VIEW_ONLINE  = 1;
    public final static int MENU_NEW_LOOKUP   = 2;
    public final static int MENU_ZOOM_IN      = 3;
    public final static int MENU_ZOOM_OUT     = 4;
    public final static int MENU_NEXT         = 5;
    public final static int MENU_FIND_IN_PAGE = 6;

    private MenuItem miViewOnline;

    private MenuItem miNextArticle;

    private Method   showFindDialogMethod;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showFindDialogMethod != null) {
            MenuItem miFindInPage = menu.add(0, MENU_FIND_IN_PAGE, 0,
                    R.string.mnFindInPage).setIcon(
                    android.R.drawable.ic_menu_search);
        }
        miViewOnline = menu.add(0, MENU_VIEW_ONLINE, 0, R.string.mnViewOnline)
                .setIcon(android.R.drawable.ic_menu_view);
        menu.add(0, MENU_NEW_LOOKUP, 0, R.string.mnNewLookup).setIcon(
                android.R.drawable.ic_menu_search);
        menu.add(0, MENU_ZOOM_OUT, 0, R.string.mnZoomOut).setIcon(
                R.drawable.ic_menu_zoom_out);
        menu.add(0, MENU_ZOOM_IN, 0, R.string.mnZoomIn).setIcon(
                R.drawable.ic_menu_zoom_in);
        miNextArticle = menu.add(0, MENU_NEXT, 0, R.string.mnNext).setIcon(
                android.R.drawable.ic_media_next);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean enableViewOnline = false;
        boolean hasNextArticle = false;
        if (this.backItems.size() > 0) {
            HistoryItem historyItem = backItems.get(backItems.size() - 1);
            Article current = historyItem.article;
            Volume d = dictionaryService.getVolume(current.volumeId);
            enableViewOnline = d != null && d.getArticleURLTemplate() != null;
            hasNextArticle = historyItem.hasNext();
        }
        miViewOnline.setEnabled(enableViewOnline);
        miNextArticle.setVisible(hasNextArticle);
        return true;
    }

    private void showFindDialog() {
        // removed Webview code
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_VIEW_ONLINE:
            viewOnline();
            break;
        case android.R.id.home:
        case MENU_NEW_LOOKUP:
            onSearchRequested();
            break;
        case MENU_ZOOM_IN:
            zoomIn();
            break;
        case MENU_ZOOM_OUT:
            zoomOut();
            break;
        case MENU_NEXT:
            nextArticle();
            break;
        case MENU_FIND_IN_PAGE:
            showFindDialog();
            break;
        default:
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void viewOnline() {
        if (this.backItems.size() > 0) {
            Article current = this.backItems.get(this.backItems.size() - 1).article;
            Volume d = dictionaryService.getVolume(current.volumeId);
            String url = d == null ? null : d.getArticleURL(current.title);
            if (url != null) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }

    private void showArticle(String volumeId, long articlePointer, String word,
            String section) {
        Log.d(TAG, "word: " + word);
        Log.d(TAG, "dictionaryId: " + volumeId);
        Log.d(TAG, "articlePointer: " + articlePointer);
        Log.d(TAG, "section: " + section);
        Volume d = dictionaryService.getVolume(volumeId);
        Entry entry = new Entry(d.getId(), word, articlePointer);
        entry.section = section;
        this.showArticle(entry);
    }

    private void showArticle(Entry entry) {
        List<Entry> result = new ArrayList<Entry>();
        result.add(entry);

        try {
            Iterator<Entry> currentIterator = dictionaryService.followLink(
                    entry.title, entry.volumeId);
            while (currentIterator.hasNext() && result.size() < 20) {
                Entry next = currentIterator.next();
                if (!next.equals(entry)) {
                    result.add(next);
                }
            }
        } catch (ArticleNotFound e) {
            Log.d(TAG, String.format("Article \"%s\" not found - unexpected",
                    e.word));
        }
        showNext(new HistoryItem(result));
    }

    private Map<Article, ScrollXY> getScrollPositions() {
        int orientation = getWindowManager().getDefaultDisplay()
                .getOrientation();
        switch (orientation) {
        //case Surface.ROTATION_0:
        //case Surface.ROTATION_180:
        //    return scrollPositionsV;
        default:
            return scrollPositionsH;
        }
    }

    private void saveScrollPos(int x, int y) {
        if (!saveScrollPos) {
            // Log.d(TAG, "Not saving scroll position (disabled)");
            return;
        }
        if (backItems.size() > 0) {
            Article a = backItems.get(backItems.size() - 1).article;
            Map<Article, ScrollXY> positions = getScrollPositions();
            ScrollXY s = positions.get(a);
            if (s == null) {
                s = new ScrollXY(x, y);
                positions.put(a, s);
            } else {
                s.x = x;
                s.y = y;
            }
            // Log.d(TAG, String.format("Saving scroll position %s for %s", s,
            // a.title));
            getScrollPositions().put(a, s);
        }
    }

    private boolean restoreScrollPos() {
        if (backItems.size() > 0) {
            Article a = backItems.get(backItems.size() - 1).article;
            ScrollXY s = getScrollPositions().get(a);
            if (s == null) {
                return false;
            }
            scrollTo(s);
            return true;
        }
        return false;
    }

    private void showNext(HistoryItem item_) {
        final HistoryItem item = new HistoryItem(item_);
        final Entry entry = item.next();

        //UI only
        runOnUiThread(new Runnable() {
            public void run() {
                setTitle(item);
                setProgress(1000);
            }
        });

        Runnable curTask = new Runnable() {
        //currentTask = new TimerTask() {
            public void run() {
                try {
                    Article a = dictionaryService.getArticle(entry);
                    try {
                        a = dictionaryService.redirect(a);
                        item.article = new Article(a);
                    } catch (ArticleNotFound e) {
                        showMessage(getString(R.string.msgRedirectNotFound,
                                e.word.toString()));
                        return;
                    } catch (RedirectTooManyLevels e) {
                        showMessage(getString(R.string.msgTooManyRedirects,
                                a.getRedirect()));
                        return;
                    } catch (SkipException e) {
                            throw e;  // not to block SkipException
                    } catch (Exception e) {
                        Log.e(TAG, "Redirect failed", e);
                        showError(getString(R.string.msgErrorLoadingArticle,
                                a.title));
                        return;
                    }

                    HistoryItem oldCurrent = null;
                    if (!backItems.isEmpty())
                        oldCurrent = backItems.get(backItems.size() - 1);

                    backItems.add(item);

                    if (oldCurrent != null) {
                        HistoryItem newCurrent = item;
                        if (newCurrent.article
                                .equalsIgnoreSection(oldCurrent.article)) {

                            final String section = oldCurrent.article
                                    .sectionEquals(newCurrent.article) ? null
                                    : newCurrent.article.section;

                            // UI only
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    resetTitleToCurrent();
                                    if (section != null) {
                                        goToSection(section);
                                    }
                                    setProgress(10000);
                                    currentTask = null;
                                }
                            });
                        } else {
                            showCurrentArticle();
                        }
                    } else {
                        showCurrentArticle();
                    }
                } catch (SkipException e) {
                    throw e;  // not to block SkipException
                } catch (Exception e) {
                    String msg = getString(R.string.msgErrorLoadingArticle,
                            entry.title);
                    Log.e(TAG, msg, e);
                    showError(msg);
                }
            }
        };

        /*try {
            //timer.schedule(currentTask, 0);
        } catch (SkipException e) {
            throw e;  // not to block SkipException
        } catch (Exception e) {
            Log.d(TAG, "Failed to schedule task", e);
        }*/

        try {
            // runs async on a background thread
            //Checker.setProcMode(ProcMode.ASYNCBack);
            Checker.beforeAsyncProc(ProcMode.ASYNCBack); // might skip the whole async proc // must be inside try
            curTask.run();
        } catch (SkipException e) {
            // Do not block the caller
        } finally {
            Checker.afterAsyncProc();
            //Checker.setProcMode(ProcMode.SYNCMain);
        }
    }

    private void showCurrentArticle() {
        runOnUiThread(new Runnable() {
            public void run() {
                setProgress(5000);
                resetTitleToCurrent();
                Article a = backItems.get(backItems.size() - 1).article;
                Log.d(TAG, "Show article: " + a.text);
            }
        });
    }

    private void updateNextButtonVisibility() {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            return;
        }
        if (currentHideNextButtonTask != null) {
            currentHideNextButtonTask.cancel();
            currentHideNextButtonTask = null;
        }
        boolean hasNextArticle = false;
        if (backItems.size() > 0) {
            HistoryItem historyItem = backItems.get(backItems.size() - 1);
            hasNextArticle = historyItem.hasNext();
        }
        final Button nextButton = (Button) findViewById(R.id.NextButton, Button.class);
        if (hasNextArticle) {
            if (nextButton.getVisibility() == View.GONE) {
                nextButton.setVisibility(View.VISIBLE);
            }

            Runnable curTask = new Runnable() {
            //currentHideNextButtonTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (useAnimation) {
                                // removed anomation code
                            } else {
                                nextButton.setVisibility(View.GONE);
                            }
                            currentHideNextButtonTask = null;
                        }
                    });
                }
            };
            /*try {
                //timer.schedule(currentHideNextButtonTask, 1800);
                curTask.run();
            } catch (IllegalStateException e) {
                // this may happen if orientation changes while users touches
                // screen
                Log.d(TAG, "Failed to schedule \"Next\" button hide", e);
            }*/
        } else {
            nextButton.setVisibility(View.GONE);
        }
    }

    private void showMessage(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                currentTask = null;
                setProgress(10000);
                resetTitleToCurrent();
                Toast.makeText(ArticleViewActivity.this, message,
                        Toast.LENGTH_LONG).show();
                if (backItems.isEmpty()) {
                    finish();
                }
            }
        });
    }

    private void showError(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                currentTask = null;
                setProgress(10000);
                resetTitleToCurrent();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                        ArticleViewActivity.this);
                dialogBuilder
                        .setTitle(R.string.titleError)
                        .setMessage(message)
                        .setNeutralButton(R.string.btnDismiss,
                                new OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                        if (backItems.isEmpty()) {
                                            finish();
                                        }
                                    }
                                });
                dialogBuilder.show();
            }
        });
    }

    private void setTitle(CharSequence articleTitle, CharSequence dictTitle) {
        setTitle(getString(R.string.titleArticleViewActivity, articleTitle,
                dictTitle));
    }

    private void resetTitleToCurrent() {
        if (!backItems.isEmpty()) {
            HistoryItem current = backItems.get(backItems.size() - 1);
            setTitle(current);
        }
    }

    private void setTitle(HistoryItem item) {
        StringBuilder title = new StringBuilder();
        boolean hasNextArticle = false;
        if (item.entries.size() > 1) {
            title.append(item.entryIndex + 1).append("/")
                    .append(item.entries.size()).append(" ");
            hasNextArticle = item.hasNext();
        }
        if (miNextArticle != null) {
            miNextArticle.setVisible(hasNextArticle);
        }
        Entry entry = item.current();
        title.append(entry.title);
        // commented out set title - id sent from the driver is a stub value
        //setTitle(title, dictionaryService.getDisplayTitle(entry.volumeId));
    }

    private String wrap(String articleText) {
        StringBuilder sb = new StringBuilder("<html>").append("<head>");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            sb.append("<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=0'/>");
        }
        return sb.append(this.sharedCSS).append(this.mediawikiSharedCSS)
                .append(this.mediawikiMonobookCSS).append(this.js)
                .append("</head>").append("<body>")
                .append("<div id=\"globalWrapper\">").append(articleText)
                .append("</div>").append("</body>").append("</html>")
                .toString();
    }

    private String wrapCSS(String css) {
        return String.format("<style type=\"text/css\">%s</style>", css);
    }

    private String wrapJS(String js) {
        return String
                .format("<script type=\"text/javascript\">%s</script>", js);
    }

    private void loadAssets() {
        try {
            this.sharedCSS = wrapCSS(readFile("shared.css"));
            this.mediawikiSharedCSS = wrapCSS(readFile("mediawiki_shared.css"));
            this.mediawikiMonobookCSS = wrapCSS(readFile("mediawiki_monobook.css"));
            this.js = wrapJS(readFile("aar.js"));
        } catch (IOException e) {
            Log.e(TAG, "Failed to load assets", e);
        }
    }

    private String readFile(String name) throws IOException {
        final char[] buffer = new char[0x1000];
        StringBuilder out = new StringBuilder();
        // commented out reading file content
        /*InputStream is = getResources().getAssets().open(name);
        Reader in = new InputStreamReader(is, "UTF-8");
        int read;
        do {
            read = in.read(buffer, 0, buffer.length);
            if (read > 0) {
                out.append(buffer, 0, read);
            }
        } while (read >= 0);*/
        return out.toString();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            saveTextZoomPref();
        }
        else {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            Editor e = prefs.edit();
            boolean success = e.commit();
            if (!success) {
                Log.w(TAG, "Failed to save article view scale pref");
            }
        }
    }

    // extends public onCreate in BaseDictionaryActivity
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            applyTextZoomPref();
        }
        else {
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            float scale = prefs.getFloat("articleView.scale", 1.0f);
            int initialScale = Math.round(scale * 100);
            Log.d(TAG, "Setting initial article view scale to " + initialScale);
        }
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            try {
                Method getActionBar = getClass().getMethod("getActionBar");
                Object actionBar = getActionBar.invoke(this);
                Method setDisplayHomeAsUpEnabled = actionBar.getClass()
                        .getMethod("setDisplayHomeAsUpEnabled", boolean.class);
                setDisplayHomeAsUpEnabled.invoke(actionBar, true);
            } catch (SkipException e) {
                throw e;  // not to block SkipException
            } catch (Exception e) {
            }
        }
    }

    private void applyTextZoomPref() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int textZoom = prefs.getInt("articleView.textZoom", 100);
    }

    private void saveTextZoomPref() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Editor e = prefs.edit();
        e.putInt("articleView.textZoom", 0);
        boolean success = e.commit();
        if (!success) {
            Log.w(TAG, "Failed to save article view text zoom pref");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //timer.cancel();
        scrollPositionsH.clear();
        scrollPositionsV.clear();
        backItems.clear();
    }

    @Override
    void onDictionaryServiceReady() {
        if (this.backItems.isEmpty()) {
            final Intent intent = getIntent();
            if (intent != null && intent.getAction() != null) {
                String action = intent.getAction();
                String _word = null;
                if (action.equals(Intent.ACTION_SEARCH)) {
                    _word = intent.getStringExtra("query");
                } else if (action.equals(Intent.ACTION_SEND)) {
                    _word = intent.getStringExtra(Intent.EXTRA_TEXT);
                }

                final String word = _word;

                if (word != null) {

                    if (currentTask != null) {
                        currentTask.cancel();
                    }

                    Runnable curTask = new Runnable() {
                    //currentTask = new TimerTask() {
                        @Override
                        public void run() {
                            setProgress(500);
                            String currentWord = word;
                            Log.d(TAG,
                                    "intent.getDataString(): "
                                            + intent.getDataString());
                            while (currentWord.length() > 0) {
                                Iterator<Entry> results = dictionaryService
                                        .lookup(currentWord);
                                Log.d(TAG, "Looked up " + word);
                                if (results.hasNext()) {
                                    currentTask = null;
                                    Entry entry = results.next();
                                    showArticle(entry);
                                    break;
                                } else {
                                    currentWord = currentWord.substring(0,
                                            currentWord.length() - 1);
                                }
                            }
                            if (currentWord.length() == 0) {
                                onSearchRequested();
                            }
                        }
                    };

                    try {
                        //timer.schedule(currentTask, 0);
                        curTask.run();
                    } catch (SkipException e) {
                        throw e;  // not to block SkipException
                    } catch (Exception e) {
                        Log.d(TAG, "Failed to schedule task", e);
                        showError(getString(R.string.msgErrorLoadingArticle,
                                word));
                    }
                }
            } else {
                String word = intent.getStringExtra("word");
                String section = intent.getStringExtra("section");
                String volumeId = intent.getStringExtra("volumeId");
                long articlePointer = intent.getLongExtra("articlePointer", -1);
                dictionaryService.setPreferred(volumeId);
                showArticle(volumeId, articlePointer, word, section);
            }
        } else {
            showCurrentArticle();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("backItems", new LinkedList(backItems));
        outState.putSerializable("scrollPositionsH", new HashMap(
                scrollPositionsH));
        outState.putSerializable("scrollPositionsV", new HashMap(
                scrollPositionsV));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        backItems = Collections.synchronizedList((List) savedInstanceState
                .getSerializable("backItems"));
        scrollPositionsH = Collections.synchronizedMap((Map) savedInstanceState
                .getSerializable("scrollPositionsH"));
        scrollPositionsV = Collections.synchronizedMap((Map) savedInstanceState
                .getSerializable("scrollPositionsV"));
    }
}
