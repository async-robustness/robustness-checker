package com.vlille.checker.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.vlille.checker.Application;
import com.vlille.checker.R;

/**
 * Show informations about vlille checker.
 */
public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        String versionNumber = Application.getVersionNumber();

        TextView tvAboutText = new TextView(R.id.about_text);
        tvAboutText.setText(getString(R.string.about_text, versionNumber));
	}

}
