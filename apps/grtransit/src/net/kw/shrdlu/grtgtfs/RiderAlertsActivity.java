/*
 * Copyright 2011 Giles Malet.
 *
 * This file is part of GRTransit.
 * 
 * GRTransit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * GRTransit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with GRTransit.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.kw.shrdlu.grtgtfs;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class RiderAlertsActivity extends MenuListActivity {
	private static final String TAG = "RiderAlertsActivity";

	// The search string to get recent tweets from GRT Rider Alerts
	private final String TwitterURL = "https://api.twitter.com/1.1/statuses/user_timeline.json";
	private final String TwitterQry = "?screen_name=GRT_ROW&count=20&trim_user=true";

	private final String TwitterOauth = "https://api.twitter.com/oauth2/token";
	private static String AccessToken = null;

	private ListArrayAdapter mAdapter;
	private ArrayList<String[]> mListDetails;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		mContext = this;
		setContentView(R.layout.timeslayout);
		super.onCreate(savedInstanceState);

		mListDetails = new ArrayList<String[]>();

		mTitle.setText(R.string.twitter_querying_feed);

		new ProcessTweets().execute();
	}

	/* Do the processing to load the ArrayAdapter for display. */
	private class ProcessTweets extends AsyncTask<Void, Integer, Void> {
		// static final String TAG = "ProcessBusStops";

		@Override
		protected void onPreExecute() {
			mProgress.setVisibility(View.VISIBLE);
		}

		// Update the progress bar.
		@Override
		protected void onProgressUpdate(Integer... parms) {
			mProgress.setProgress(parms[0]);
		}

		@Override
		protected Void doInBackground(Void... foo) {

			/*if (AccessToken == null) {
				// Get the Twitter OAuth token
				final HttpClient httpclient = new DefaultHttpClient();
				final HttpPost httppost = new HttpPost(TwitterOauth);
				httppost.setHeader("Authorization", "Basic " + "");

				publishProgress(15); // fake it

				try {
					final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
					nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
					final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
					entity.setContentType("application/x-www-form-urlencoded;charset=UTF-8");
					httppost.setEntity(entity);

					// Execute HTTP Post Request
					final HttpResponse response = httpclient.execute(httppost);

					final StatusLine statusLine = response.getStatusLine();
					final int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						final HttpEntity responseEntity = response.getEntity();
						final String s = EntityUtils.toString(responseEntity);
						final JSONObject object = (JSONObject) new JSONTokener(s).nextValue();
						final String type = object.getString("token_type");
						final String token = object.getString("access_token");

						if (type.contentEquals("bearer") && !token.isEmpty()) {
							AccessToken = token;	// stash it
						}
					}
				} catch (final ClientProtocolException e) {
					// TODO Auto-generated catch block
				} catch (final IOException e) {
					// TODO Auto-generated catch block
				} catch (final JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			publishProgress(25); // fake it*/
			if (AccessToken != null) {
				mListDetails = readTwitterFeed();
			}
			publishProgress(90); // fake it

			return null;
		}

		@Override
		protected void onPostExecute(Void foo) {

			mProgress.setVisibility(View.INVISIBLE);

			mTitle.setText(R.string.title_rider_alerts);

			if (mListDetails == null) {
				Toast.makeText(mContext, R.string.twitter_fetch_failed, Toast.LENGTH_LONG).show();
			} else if (mListDetails.isEmpty()) {
				Toast.makeText(mContext, R.string.twitter_fetch_nothing, Toast.LENGTH_LONG).show();
			} else {
				mAdapter = new ListArrayAdapter(mContext, R.layout.tweetlayout, mListDetails);
				mContext.setListAdapter(mAdapter);
			}

			publishProgress(100); // fake it
		}
	}

	/**
	 * Get the latest twitter info. Some of this copied from http://www.vogella.com/articles/AndroidJSON/article.html
	 */
	public ArrayList<String[]> readTwitterFeed() {
		/*final StringBuilder builder = new StringBuilder();
		final HttpClient client = new DefaultHttpClient();
		final HttpGet httpGet = new HttpGet(TwitterURL + TwitterQry);
		httpGet.setHeader("Authorization", "Bearer " + AccessToken);

		try {
			final HttpResponse response = client.execute(httpGet);

			final StatusLine statusLine = response.getStatusLine();
			final int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				final HttpEntity entity = response.getEntity();
				final InputStream content = entity.getContent();
				final BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				// Result is an array of tweets
				final JSONArray arr = (JSONArray) new JSONTokener(builder.toString()).nextValue();

				final ArrayList<String[]> tweets = new ArrayList<String[]>();

				// Need to grok dates of form "created_at": "Thu, 15 Nov 2012 18:27:17 +0000"
				final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
				dateFormat.setLenient(true);

				for (int i = 0; i < arr.length(); i++) {
					final String text = new String(arr.getJSONObject(i).get("text").toString());
					String tweettime = new String(arr.getJSONObject(i).get("created_at").toString());

					// Extract & reformat the date
					Date created = null;
					final GregorianCalendar cal = new GregorianCalendar();
					try {
						created = dateFormat.parse(tweettime);
						cal.setTime(created);
						String day, mon;

						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD ) {
							day = APIReflectionWrapper.API9.getDisplayName(cal, Calendar.DAY_OF_WEEK, Calendar.LONG,
									Locale.getDefault());
							mon = APIReflectionWrapper.API9.getDisplayName(cal, Calendar.MONTH, Calendar.SHORT,
									Locale.getDefault());
						} else { // bah
							SimpleDateFormat sdf = new SimpleDateFormat("EEEEEEEE", Locale.getDefault());
							day = sdf.format(new Date());
							sdf = new SimpleDateFormat("MMM", Locale.getDefault());
							mon = sdf.format(new Date());
						}
						tweettime = String.format("%s %02d:%02d - %s %d", day, cal.get(Calendar.HOUR_OF_DAY),
								cal.get(Calendar.MINUTE), mon, cal.get(Calendar.DAY_OF_MONTH));

					} catch (final Exception e) {
						Log.d(TAG, "Exception: " + e.getMessage() + ", parsing tweet date `" + tweettime + "'");
						tweettime = "--:--";
					}

					tweets.add(new String[] { tweettime, text });
				}

				return tweets;

			} else {
				Log.d(TAG, "Failed to download twitter info");
			}
		} catch (final ClientProtocolException e) {
			Log.d(TAG, "ClientProtocolException: " + e.getMessage() + ", Failed to download twitter info");
		} catch (final IOException e) {
			Log.d(TAG, "IOException: " + e.getMessage() + ", Failed to download twitter info");
		} catch (final Exception e) {
			Log.d(TAG, "Exception: " + e.getMessage() + ", Failed to download twitter info");
		}*/

		return null;
	}
}
