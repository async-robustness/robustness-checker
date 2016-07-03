/*
 * Copyright (c) 2013 IRCCloud, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.irccloud.android.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.irccloud.android.R;
import com.irccloud.android.ShareActionProviderHax;

import java.util.Timer;
import java.util.TimerTask;

public class ImageViewerActivity extends BaseActivity implements ShareActionProviderHax.OnShareActionProviderSubVisibilityChangedListener{

    private class OEmbedTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            /*try {
                JSONObject o = NetworkConnection.getInstance().fetchJSON(params[0]);
                if(o.getString("type").equalsIgnoreCase("photo"))
                    return o.getString("url");
            } catch (Exception e) {
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if(url != null) {
                loadImage(url);
            } else {
                fail();
            }
        }
    }

    private class ClLyTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            /*try {
                JSONObject o = NetworkConnection.getInstance().fetchJSON(params[0]);
                if(o.getString("item_type").equalsIgnoreCase("image"))
                    return o.getString("content_url");
            } catch (Exception e) {
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(String url) {
            if(url != null) {
                loadImage(url);
            } else {
                fail();
            }
        }
    }

    ProgressBar mProgress;
    Timer mHideTimer;

    public class JSInterface {
        public void imageFailed() {
            fail();
        }

        public void imageClicked() {
            ImageViewerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(getSupportActionBar().isShowing()) {
                        mHideTimer.cancel();
                        mHideTimer = null;
                        getSupportActionBar().hide();
                    } else {
                        getSupportActionBar().show();
                        hide_actionbar();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_imageviewer);
        if(Integer.parseInt(Build.VERSION.SDK) >= 14 && Integer.parseInt(Build.VERSION.SDK) < 19)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        getSupportActionBar().setTitle("Image Viewer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_translucent));

        mProgress = (ProgressBar)findViewById(R.id.progress, ProgressBar.class);

        if(getIntent() != null && getIntent().getDataString() != null) {
            String url = getIntent().getDataString().replace(getResources().getString(R.string.IMAGE_SCHEME), "http");
            String lower = url.toLowerCase().replace("https://", "").replace("http://", "");
            if(lower.startsWith("www.dropbox.com/")) {
                if(lower.startsWith("www.dropbox.com/s/")) {
                    url = url.replace("://www.dropbox.com/s/", "://dl.dropboxusercontent.com/s/");
                } else {
                    url = url + "?dl=1";
                }
            } else if(lower.startsWith("imgur.com/")) {
                new OEmbedTask().execute("http://api.imgur.com/oembed.json?url=" + url);
                return;
            } else if(lower.startsWith("flickr.com/") || lower.startsWith("www.flickr.com/")) {
                new OEmbedTask().execute("https://www.flickr.com/services/oembed/?format=json&url=" + url);
                return;
            } else if(lower.startsWith("instagram.com/") || lower.startsWith("www.instagram.com/") || lower.startsWith("instagr.am/")) {
                new OEmbedTask().execute("http://api.instagram.com/oembed?url=" + url);
                return;
            } else if(lower.startsWith("cl.ly")) {
                new ClLyTask().execute(url);
                return;
            }
            loadImage(url);
        } else {
            finish();
        }
    }

    private void loadImage(String urlStr) {
        // load WebWiew removed
    }

    private void fail() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getDataString().replace(getResources().getString(R.string.IMAGE_SCHEME), "http")));
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mProgress.getVisibility() == View.GONE)
            hide_actionbar();
    }

    private void hide_actionbar() {
        if(mHideTimer != null)
            mHideTimer.cancel();
        //mHideTimer = new Timer(); // remove calls to Timer
        /*mHideTimer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        ImageViewerActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getSupportActionBar().hide();
                            }
                        });
                        mHideTimer = null;
                    }
                    //}, 3000);*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_imageviewer, menu);

        if(getIntent() != null && getIntent().getDataString() != null) {
            Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse(getIntent().getDataString().replace(getResources().getString(R.string.IMAGE_SCHEME), "http")));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getIntent().getDataString().replace(getResources().getString(R.string.IMAGE_SCHEME), "http"));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if(item.getItemId() == R.id.action_browser) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getIntent().getDataString().replace(getResources().getString(R.string.IMAGE_SCHEME), "http")));
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShareActionProviderSubVisibilityChanged(boolean visible) {
        if(visible) {
            if(mHideTimer != null)
                mHideTimer.cancel();
        } else {
            hide_actionbar();
        }
    }
}
