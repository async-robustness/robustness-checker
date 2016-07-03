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

package com.irccloud.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

import java.net.Proxy;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

import checker.SkipException;
import com.irccloud.android.data.BuffersDataSource;
import com.irccloud.android.data.ChannelsDataSource;
import com.irccloud.android.data.EventsDataSource;
import com.irccloud.android.data.ServersDataSource;
import com.irccloud.android.data.UsersDataSource;

public class NetworkConnection {
	private static final String TAG = "IRCCloud";
	private static NetworkConnection instance = null;

	public static final int WEBSOCKET_TAG = 0x50C37;
	public static final int BACKLOG_TAG = 0xB4C106;
	
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	public static final int STATE_DISCONNECTING = 3;
	private int state = STATE_DISCONNECTED;

	private UserInfo userInfo = null;
	private ArrayList<Handler> handlers = null;
	private String session = null;
	private volatile int last_reqid = 0;
	private Timer shutdownTimer = null;
	private Timer idleTimer = null;
	public long idle_interval = 1000;
    private volatile int failCount = 0;
	private long reconnect_timestamp = 0;
	private String useragent = null;
    private String streamId = null;
    private int accrued = 0;
    private boolean backlog = false;
    int currentBid = -1;
    long firstEid = -1;

	public static final int BACKLOG_BUFFER_MAX = 100;
	
	public static final int EVENT_CONNECTIVITY = 0;
	public static final int EVENT_USERINFO = 1;
	public static final int EVENT_MAKESERVER = 2;
	public static final int EVENT_MAKEBUFFER = 3;
	public static final int EVENT_DELETEBUFFER = 4;
	public static final int EVENT_BUFFERMSG = 5;
	public static final int EVENT_HEARTBEATECHO = 6;
	public static final int EVENT_CHANNELINIT = 7;
	public static final int EVENT_CHANNELTOPIC = 8;
	public static final int EVENT_JOIN = 9;
	public static final int EVENT_PART = 10;
	public static final int EVENT_NICKCHANGE = 11;
	public static final int EVENT_QUIT = 12;
	public static final int EVENT_MEMBERUPDATES = 13;
	public static final int EVENT_USERCHANNELMODE = 14;
	public static final int EVENT_BUFFERARCHIVED = 15;
	public static final int EVENT_BUFFERUNARCHIVED = 16;
	public static final int EVENT_RENAMECONVERSATION = 17;
	public static final int EVENT_STATUSCHANGED = 18;
	public static final int EVENT_CONNECTIONDELETED = 19;
	public static final int EVENT_AWAY = 20;
	public static final int EVENT_SELFBACK = 21;
	public static final int EVENT_KICK = 22;
	public static final int EVENT_CHANNELMODE = 23;
	public static final int EVENT_CHANNELTIMESTAMP = 24;
	public static final int EVENT_SELFDETAILS = 25;
	public static final int EVENT_USERMODE = 26;
	public static final int EVENT_SETIGNORES = 27;
	public static final int EVENT_BADCHANNELKEY = 28;
	public static final int EVENT_OPENBUFFER = 29;
	public static final int EVENT_INVALIDNICK = 30;
	public static final int EVENT_BANLIST = 31;
	public static final int EVENT_WHOLIST = 32;
	public static final int EVENT_WHOIS = 33;
	public static final int EVENT_LINKCHANNEL = 34;
	public static final int EVENT_LISTRESPONSEFETCHING = 35;
	public static final int EVENT_LISTRESPONSE = 36;
	public static final int EVENT_LISTRESPONSETOOMANY = 37;
	public static final int EVENT_CONNECTIONLAG = 38;
	public static final int EVENT_GLOBALMSG = 39;
	public static final int EVENT_ACCEPTLIST = 40;
    public static final int EVENT_NAMESLIST = 41;
    public static final int EVENT_REORDERCONNECTIONS = 42;
    public static final int EVENT_CHANNELTOPICIS = 43;

	public static final int EVENT_BACKLOG_START = 100;
	public static final int EVENT_BACKLOG_END = 101;
    public static final int EVENT_BACKLOG_FAILED = 102;
	public static final int EVENT_FAILURE_MSG = 103;
	public static final int EVENT_SUCCESS = 104;
	public static final int EVENT_PROGRESS = 105;
	public static final int EVENT_ALERT = 106;

    public static final int EVENT_DEBUG = 999;

	private static final String IRCCLOUD_HOST = "";

	private final Object parserLock = new Object();
	private WifiManager.WifiLock wifiLock = null;
	
	public long clockOffset = 0;

	private float numbuffers = 0;
	private float totalbuffers = 0;
    private int currentcount = 0;

	public boolean ready = false;
	public String globalMsg = null;

	private HashMap<Integer, OOBIncludeTask> oobTasks = new HashMap<Integer, OOBIncludeTask>();

    TrustManager tms[];

    private SSLSocketFactory IRCCloudSocketFactory = new SSLSocketFactory() {
        final String CIPHERS[] = {
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA",
                "TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA",
                "TLS_DHE_RSA_WITH_AES_128_CBC_SHA",
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA",
                "TLS_DHE_DSS_WITH_AES_128_CBC_SHA",
                "TLS_ECDHE_RSA_WITH_RC4_128_SHA",
                "TLS_ECDHE_ECDSA_WITH_RC4_128_SHA",
                "TLS_RSA_WITH_AES_128_CBC_SHA",
                "TLS_RSA_WITH_AES_256_CBC_SHA",
                "SSL_RSA_WITH_3DES_EDE_CBC_SHA",
                "SSL_RSA_WITH_RC4_128_SHA",
                "SSL_RSA_WITH_RC4_128_MD5",
        };
        final String PROTOCOLS[] = {
                "TLSv1.2", "TLSv1.1", "TLSv1"
        };
        SSLSocketFactory internalSocketFactory;

        private void init() {
            try {
                SSLContext c = SSLContext.getInstance("TLS");
                c.init(null, tms, null);
                internalSocketFactory = c.getSocketFactory();
            } catch (SkipException e) {
                throw e; // not to block SkipExceptions
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return CIPHERS;
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return CIPHERS;
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            if(internalSocketFactory == null)
                init();
            SSLSocket socket = (SSLSocket)internalSocketFactory.createSocket(s, host, port, autoClose);
            try {
                socket.setEnabledProtocols(PROTOCOLS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }

            try {
                socket.setEnabledCipherSuites(CIPHERS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }
            return socket;
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            if(internalSocketFactory == null)
                init();
            SSLSocket socket = (SSLSocket)internalSocketFactory.createSocket(host, port);
            try {
                socket.setEnabledProtocols(PROTOCOLS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }

            try {
                socket.setEnabledCipherSuites(CIPHERS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }
            return socket;
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
            if(internalSocketFactory == null)
                init();
            SSLSocket socket = (SSLSocket)internalSocketFactory.createSocket(host, port, localHost, localPort);
            try {
                socket.setEnabledProtocols(PROTOCOLS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }

            try {
                socket.setEnabledCipherSuites(CIPHERS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }
            return socket;
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            if(internalSocketFactory == null)
                init();
            SSLSocket socket = (SSLSocket)internalSocketFactory.createSocket(host, port);
            try {
                socket.setEnabledProtocols(PROTOCOLS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }

            try {
                socket.setEnabledCipherSuites(CIPHERS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }
            return socket;
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            if(internalSocketFactory == null)
                init();
            SSLSocket socket = (SSLSocket)internalSocketFactory.createSocket(address, port, localAddress, localPort);
            try {
                socket.setEnabledProtocols(PROTOCOLS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }

            try {
                socket.setEnabledCipherSuites(CIPHERS);
            } catch (IllegalArgumentException e) {
                //Not supported on older Android versions
            }
            return socket;
        }
    };

	public static NetworkConnection getInstance() {
		if(instance == null) {
			instance = new NetworkConnection();
		}
		return instance;
	}

	BroadcastReceiver connectivityListener = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if(ni != null && ni.isConnected() && state == STATE_DISCONNECTED && session != null && handlers.size() > 0) {
				if(idleTimer != null)
					idleTimer.cancel();
				idleTimer = null;
				connect(session);
			} else if(ni == null || !ni.isConnected()) {
                cancel_idle_timer();
                reconnect_timestamp = 0;

                state = STATE_DISCONNECTED;
                notifyHandlers(EVENT_CONNECTIVITY, null);
			}
		}
	};
	
	@SuppressWarnings("deprecation")
	public NetworkConnection() {
		String version;
		String network_type = null;
		try {
			version = "/" + IRCCloudApplication.getInstance().getPackageManager().getPackageInfo(IRCCloudApplication.getInstance().getApplicationContext().getPackageName(), 0).versionName;
        } catch (SkipException e) {
            throw e; // not to block SkipExceptions
        } catch (Exception e) {
			version = "";
		}

		try {
			ConnectivityManager cm = (ConnectivityManager)IRCCloudApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			if(ni != null)
				network_type = ni.getTypeName();
        } catch (SkipException e) {
            throw e; // not to block SkipExceptions
        } catch (Exception e) {
		}
		
		useragent = "IRCCloud" + version + " (" + android.os.Build.MODEL + "; " + Locale.getDefault().getCountry().toLowerCase() + "; "
				+ "Android " + android.os.Build.VERSION.RELEASE;
		
		if(network_type != null)
			useragent += "; " + network_type;
		
		useragent += ")";
		
		WifiManager wfm = (WifiManager) IRCCloudApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		wifiLock = wfm.createWifiLock(TAG);
		
		tms = new TrustManager[1];
		tms[0] = new X509TrustManager() {
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				throw new CertificateException("Not implemented");
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				try {
					TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
					trustManagerFactory.init((KeyStore)null);

					for (TrustManager trustManager: trustManagerFactory.getTrustManagers()) {  
					    if (trustManager instanceof X509TrustManager) {  
					        X509TrustManager x509TrustManager = (X509TrustManager)trustManager;  
					        x509TrustManager.checkServerTrusted(chain, authType);
					    }  
					}
				} catch (KeyStoreException e) {
					throw new CertificateException(e);
				} catch (NoSuchAlgorithmException e) {
					throw new CertificateException(e);
				}
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
	}
	
	public int getState() {
		return state;
	}
	
	public void disconnect() {

		if(idleTimer != null) {
            try {
    			idleTimer.cancel();
            } catch (NullPointerException e) {
                //The timer expired already
            }
			idleTimer = null;
		}
		if(wifiLock.isHeld())
			wifiLock.release();
		reconnect_timestamp = 0;
		for(Integer bid : oobTasks.keySet()) {
			try {
				oobTasks.get(bid).cancel(true);
            } catch (SkipException e) {
                throw e; // not to block SkipExceptions
            } catch (Exception e) {
			}
		}
		oobTasks.clear();
		session = null;
        for(BuffersDataSource.Buffer b : BuffersDataSource.getInstance().getBuffers()) {
            if(!b.scrolledUp && EventsDataSource.getInstance().getHighlightStateForBuffer(b.bid, b.last_seen_eid, b.type) == 0)
                EventsDataSource.getInstance().pruneEvents(b.bid);
        }
		try {
			IRCCloudApplication.getInstance().getApplicationContext().unregisterReceiver(connectivityListener);
		} catch (IllegalArgumentException e) {
			//The broadcast receiver hasn't been registered yet
		}
	}
	
	public Object login(String email, String password) {
		/*try {
            String postdata = "email="+URLEncoder.encode(email, "UTF-8")+"&password="+URLEncoder.encode(password, "UTF-8");
            String response = doFetch(new URL("https://" + IRCCLOUD_HOST + "/chat/login"), postdata, null);
			return new JSONObject(response);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
		} catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                JSONObject o = new JSONObject();
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                o.put("exception", sw.toString());
                return o;
            } catch (JSONException e1) {
                e.printStackTrace();
            }
		}*/
		return null;
	}

    public Object fetchJSON(String url) throws IOException {
       /* try {
            String response = doFetch(new URL(url), null, null);
            JSONObject o = new JSONObject(response);
            return o;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        return null;
    }

    public Object registerGCM(String regId, String sk) /*throws IOException*/ {
		/*String postdata = "device_id="+regId+"&session="+sk;
		try {
            String response = doFetch(new URL("https://" + IRCCLOUD_HOST + "/gcm-register"), postdata, sk);
			JSONObject o = new JSONObject(response);
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}
	
	public Object unregisterGCM(String regId, String sk) /*throws IOException*/ {
		/*String postdata = "device_id="+regId+"&session="+sk;
		try {
            String response = doFetch(new URL("https://" + IRCCLOUD_HOST + "/gcm-unregister"), postdata, sk);
			JSONObject o = new JSONObject(response);
			return o;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return null;
	}

    public ArrayList<Object> networkPresets() /*throws IOException*/ {
        /*try {
            String response = doFetch(new URL("https://" + IRCCLOUD_HOST + "/static/networks.json"), null, null);
            JSONObject o = new JSONObject(response);
            return o.getJSONArray("networks");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        return null;
    }

    public synchronized void connect(String sk) {
        Context ctx = IRCCloudApplication.getInstance().getApplicationContext();
		session = sk;
        String host = null;
        int port = -1;

        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            if(ctx != null)
                ctx.registerReceiver(connectivityListener, intentFilter);
        } catch (SkipException e) {
            throw e; // not to block SkipExceptions
        } catch (Exception e) {
        }

        if(ctx != null) {
            ConnectivityManager cm = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();

            if(ni == null || !ni.isConnected()) {
                cancel_idle_timer();
                state = STATE_DISCONNECTED;
                reconnect_timestamp = 0;
                notifyHandlers(EVENT_CONNECTIVITY, null);
                return;
            }
        }

        if(state == STATE_CONNECTING || state == STATE_CONNECTED) {
            Log.w(TAG, "Ignoring duplicate connect request");
            return;
        }
        state = STATE_CONNECTING;

        if(oobTasks.size() > 0) {
            Log.d("IRCCloud", "Clearing OOB tasks before connecting");
        }
        for(Integer bid : oobTasks.keySet()) {
            try {
                oobTasks.get(bid).cancel(true);
            } catch (SkipException e) {
                throw e; // not to block SkipExceptions
            } catch (Exception e) {
            }
        }
        oobTasks.clear();

        if(Build.VERSION.SDK_INT <11) {
            if(ctx != null) {
                host = android.net.Proxy.getHost(ctx);
                port = android.net.Proxy.getPort(ctx);
            }
        } else {
            host = System.getProperty("http.proxyHost", null);
            try {
                port = Integer.parseInt(System.getProperty("http.proxyPort", "8080"));
            } catch (NumberFormatException e) {
                port = -1;
            }
        }

		if(!wifiLock.isHeld())
			wifiLock.acquire();
		
		/*List<BasicNameValuePair> extraHeaders = Arrays.asList(
		    new BasicNameValuePair("Cookie", "session="+session),
		    new BasicNameValuePair("User-Agent", useragent)
		);*/

		String url = "wss://" + IRCCLOUD_HOST;
		if(EventsDataSource.getInstance().highest_eid > 0) {
			url += "/?since_id=" + EventsDataSource.getInstance().highest_eid;
            if(streamId != null && streamId.length() > 0)
                url += "&stream_id=" + streamId;
        }

        if(host != null && host.length() > 0 && !host.equalsIgnoreCase("localhost") && !host.equalsIgnoreCase("127.0.0.1") && port > 0) {
            Log.d(TAG, "Connecting: " + url + " via proxy: " + host);
        } else {
            Log.d(TAG, "Connecting: " + url);
        }

        Log.d(TAG, "Attempt: " + failCount);


        Log.d("IRCCloud", "Creating websocket");
		reconnect_timestamp = 0;
        idle_interval = 0;
        accrued = 0;
		notifyHandlers(EVENT_CONNECTIVITY, null);

	}

    public void logout() {
        disconnect();
        ready = false;
        streamId = null;
        accrued = 0;
        SharedPreferences.Editor editor = IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).edit();
        try {
            String regId = GCMIntentService.getRegistrationId(IRCCloudApplication.getInstance().getApplicationContext());
            if(regId.length() > 0) {
                //Store the old session key so GCM can unregister later
                editor.putString(regId, IRCCloudApplication.getInstance().getApplicationContext().getSharedPreferences("prefs", 0).getString("session_key", ""));
                GCMIntentService.scheduleUnregisterTimer(1000, regId);
            }
        } catch (SkipException e) {
            throw e; // not to block SkipExceptions
        } catch (Exception e) {
            //GCM might not be available on the device
        }
        editor.remove("session_key");
        editor.remove("gcm_registered");
        editor.remove("mentionTip");
        editor.remove("userSwipeTip");
        editor.remove("bufferSwipeTip");
        editor.remove("longPressTip");
        editor.remove("email");
        editor.remove("name");
        editor.remove("highlights");
        editor.remove("autoaway");
        editor.commit();
        ServersDataSource.getInstance().clear();
        BuffersDataSource.getInstance().clear();
        ChannelsDataSource.getInstance().clear();
        UsersDataSource.getInstance().clear();
        EventsDataSource.getInstance().clear();
        Notifications.getInstance().clear();
    }

	private synchronized int send(String method, Object params) {
		return 0;
	}
	
	public int heartbeat(int cid, int bid, long last_seen_eid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("selectedBuffer", bid);
			JSONObject eids = new JSONObject();
			eids.put(String.valueOf(bid), last_seen_eid);
			JSONObject cids = new JSONObject();
			cids.put(String.valueOf(cid), eids);
			o.put("seenEids", cids.toString());
			return send("heartbeat", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int disconnect(int cid, String message) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("msg", message);
			return send("disconnect", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int reconnect(int cid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			return send("reconnect", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int say(int cid, String to, String message) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			if(to != null)
				o.put("to", to);
			o.put("msg", message);
			return send("say", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int join(int cid, String channel, String key) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("channel", channel);
			o.put("key", key);
			return send("join", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int part(int cid, String channel, String message) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("channel", channel);
			o.put("msg", message);
			return send("part", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int kick(int cid, String channel, String nick, String message) {
		return say(cid, channel, "/kick " + nick + " " + message);
	}
	
	public int mode(int cid, String channel, String mode) {
		return say(cid, channel, "/mode " + channel + " " + mode);
	}
	
	public int invite(int cid, String channel, String nick) {
		return say(cid, channel, "/invite " + nick + " " + channel);
	}
	
	public int archiveBuffer(int cid, long bid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("id", bid);
			return send("archive-buffer", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int unarchiveBuffer(int cid, long bid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("id", bid);
			return send("unarchive-buffer", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int deleteBuffer(int cid, long bid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("id", bid);
			return send("delete-buffer", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int deleteServer(int cid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			return send("delete-connection", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int addServer(String hostname, int port, int ssl, String netname, String nickname, String realname, String server_pass, String nickserv_pass, String joincommands, String channels) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("hostname", hostname);
			o.put("port", port);
			o.put("ssl", String.valueOf(ssl));
			o.put("netname", netname);
			o.put("nickname", nickname);
			o.put("realname", realname);
			o.put("server_pass", server_pass);
			o.put("nspass", nickserv_pass);
			o.put("joincommands", joincommands);
			o.put("channels", channels);
			return send("add-server", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int editServer(int cid, String hostname, int port, int ssl, String netname, String nickname, String realname, String server_pass, String nickserv_pass, String joincommands) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("hostname", hostname);
			o.put("port", port);
			o.put("ssl", String.valueOf(ssl));
			o.put("netname", netname);
			o.put("nickname", nickname);
			o.put("realname", realname);
			o.put("server_pass", server_pass);
			o.put("nspass", nickserv_pass);
			o.put("joincommands", joincommands);
			o.put("cid", cid);
			return send("edit-server", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int ignore(int cid, String mask) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("mask", mask);
			return send("ignore", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int unignore(int cid, String mask) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("mask", mask);
			return send("unignore", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
        return -1;
	}
	
	public int set_prefs(String prefs) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("prefs", prefs);
			return send("set-prefs", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int set_user_settings(String email, String realname, String hwords, boolean autoaway) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("email", email);
			o.put("realname", realname);
			o.put("hwords", hwords);
			o.put("autoaway", autoaway?"1":"0");
			return send("user-settings", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int ns_help_register(int cid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			return send("ns-help-register", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int set_nspass(int cid, String nspass) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("nspass", nspass);
			return send("set-nspass", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int whois(int cid, String nick, String server) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("nick", nick);
			if(server != null)
				o.put("server", server);
			return send("whois", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int topic(int cid, String channel, String topic) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			o.put("channel", channel);
			o.put("topic", topic);
			return send("topic", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}
	
	public int back(int cid) {
		/*try {
			JSONObject o = new JSONObject();
			o.put("cid", cid);
			return send("back", o);
		} catch (JSONException e) {
			e.printStackTrace();
			return -1;
		}*/
		return -1;
	}

    public int reorder_connections(String cids) {
        /*try {
            JSONObject o = new JSONObject();
            o.put("cids", cids);
            return send("reorder-connections", o);
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }*/
        return -1;
    }

    public void request_backlog(int cid, int bid, long beforeId) {
		try {
			if(oobTasks.containsKey(bid)) {
                Log.w("IRCCloud", "Ignoring duplicate backlog request for BID: " + bid);
				return;
			}
			if(Looper.myLooper() == null)
				Looper.prepare();
			
			OOBIncludeTask task = new OOBIncludeTask(bid);
			oobTasks.put(bid, task);
			
			if(beforeId > 0)
				task.execute(new URL("https://" + IRCCLOUD_HOST + "/chat/backlog?cid="+cid+"&bid="+bid+"&beforeid="+beforeId));
			else
				task.execute(new URL("https://" + IRCCLOUD_HOST + "/chat/backlog?cid="+cid+"&bid="+bid));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cancel_idle_timer() {
		if(idleTimer != null) {
			idleTimer.cancel();
			idleTimer = null;
		}
	}
	
	public void schedule_idle_timer() {
		if(idleTimer != null) {
            try {
    			idleTimer.cancel();
            } catch (SkipException e) {
                throw e; // not to block SkipExceptions
            } catch (Exception e) {

            }
			idleTimer = null;
		}
		if(idle_interval <= 0)
			return;
		
		try {
			/*idleTimer = new Timer();
			idleTimer.schedule(new TimerTask() {
                public void run() {
                    if (handlers.size() > 0) {
                        Log.i(TAG, "Websocket idle time exceeded, reconnecting...");
                        state = STATE_DISCONNECTING;
                        notifyHandlers(EVENT_CONNECTIVITY, null);
                        connect(session);
                    }
                    idleTimer = null;
                    reconnect_timestamp = 0;
                }
            }, idle_interval);*/
			reconnect_timestamp = System.currentTimeMillis() + idle_interval;
		} catch (IllegalStateException e) {
			//It's possible for timer to get canceled by another thread before before it gets scheduled
			//so catch the exception
		}
	}
	
	public long getReconnectTimestamp() {
		return reconnect_timestamp;
	}

    public interface Parser {
        public void parse(IRCCloudJSONObject object) ;
    }

    private class BroadcastParser implements Parser {
        int type;

        BroadcastParser(int t) {
            type = t;
        }

        public void parse(IRCCloudJSONObject object) {
            if(!backlog)
                notifyHandlers(type, object);
        }
    };

    HashMap<String,Parser> parserMap = new HashMap<String, Parser>() {{
        //Ignored events
        put("idle", null);
        put("end_of_backlog", null);
        put("oob_skipped", null);

        put("header", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object)  {
                idle_interval = object.getLong("idle_interval") + 10000;
                clockOffset = object.getLong("time") - (System.currentTimeMillis()/1000);
                currentcount = 0;
                currentBid = -1;
                firstEid = -1;
                streamId = object.getString("streamid");
                if(object.has("accrued"))
                    accrued = object.getInt("accrued");
                if(!(object.has("resumed") && object.getBoolean("resumed"))) {
                    Log.d("IRCCloud", "Socket was not resumed");
                }
            }
        });

        put("global_system_message", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                String msgType = object.getString("system_message_type");
                if(msgType == null || (!msgType.equalsIgnoreCase("eval") && !msgType.equalsIgnoreCase("refresh"))) {
                    globalMsg = object.getString("msg");
                    notifyHandlers(EVENT_GLOBALMSG, object);
                }
            }
        });

        put("num_invites", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object)  {
                if(userInfo != null)
                    userInfo.num_invites = object.getInt("num_invites");
            }
        });

        put("stat_user", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                userInfo = new UserInfo(object);
                SharedPreferences.Editor prefs = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext()).edit();
                prefs.putString("name", userInfo.name);
                prefs.putString("email", userInfo.email);
                prefs.putString("highlights", userInfo.highlights);
                prefs.putBoolean("autoaway", userInfo.auto_away);
                if(userInfo.prefs != null) {
                    prefs.putBoolean("time-24hr", false); //userInfo.prefs.has("time-24hr") && userInfo.prefs.getBoolean("time-24hr"));
                    prefs.putBoolean("time-seconds", false); //userInfo.prefs.has("time-seconds") && userInfo.prefs.getBoolean("time-seconds"));
                    prefs.putBoolean("mode-showsymbol", false); //userInfo.prefs.has("mode-showsymbol") && userInfo.prefs.getBoolean("mode-showsymbol"));
                    prefs.putBoolean("nick-colors", false); //userInfo.prefs.has("nick-colors") && userInfo.prefs.getBoolean("nick-colors"));
                } else {
                    prefs.putBoolean("time-24hr", false);
                    prefs.putBoolean("time-seconds", false);
                    prefs.putBoolean("mode-showsymbol", false);
                    prefs.putBoolean("nick-colors", false);
                }
                prefs.commit();
                notifyHandlers(EVENT_USERINFO, userInfo);
            }
        });

        put("set_ignores", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.updateIgnores(object.cid(), object.getJsonArray("masks"));
                if(!backlog)
                    notifyHandlers(EVENT_SETIGNORES, object);
            }
        });
        put("ignore_list", get("set_ignores"));

        put("heartbeat_echo", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                /*JsonObject seenEids = object.getJsonObject("seenEids");
                Iterator<Map.Entry<String, JsonElement>> i = seenEids.entrySet().iterator();
                while(i.hasNext()) {
					Map.Entry<String, JsonElement> entry = i.next();
                    JsonObject eids = entry.getValue().getAsJsonObject();
                    Iterator<Map.Entry<String, JsonElement>> j = eids.entrySet().iterator();
                    while(j.hasNext()) {
						Map.Entry<String, JsonElement> eidentry = j.next();
                        String bid = eidentry.getKey();
                        long eid = eidentry.getValue().getAsLong();
                        BuffersDataSource.getInstance().updateLastSeenEid(Integer.valueOf(bid), eid);
                        Notifications.getInstance().deleteOldNotifications(Integer.valueOf(bid), eid);
                        Notifications.getInstance().updateLastSeenEid(Integer.valueOf(bid), eid);
                    }
                }
                if(!backlog) {
                    notifyHandlers(EVENT_HEARTBEATECHO, object);
                    Notifications.getInstance().showNotifications(null);
                }*/
            }
        });

        //Backlog
        put("oob_include", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                try {
                    if(Looper.myLooper() == null)
                        Looper.prepare();
                    String url = "https://" + IRCCLOUD_HOST + object.getString("url");
                    OOBIncludeTask t = new OOBIncludeTask(-1);
                    oobTasks.put(-1, t);
                    t.execute(new URL(url));
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        put("backlog_starts", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                numbuffers = object.getInt("numbuffers");
                totalbuffers = 0;
                currentBid = -1;
                notifyHandlers(EVENT_BACKLOG_START, null);
                backlog = true;
            }
        });

        put("backlog_complete", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                accrued = 0;
                backlog = false;
                Log.d("IRCCloud", "Cleaning up invalid BIDs");
                BuffersDataSource.getInstance().purgeInvalidBIDs();
                ChannelsDataSource.getInstance().purgeInvalidChannels();
                if(userInfo.connections > 0 && (ServersDataSource.getInstance().count() == 0 || BuffersDataSource.getInstance().count() == 0)) {
                    Log.e("IRCCloud", "Failed to load buffers list, reconnecting");
                    notifyHandlers(EVENT_BACKLOG_FAILED, null);
                    streamId = null;
                } else {
                    failCount = 0;
                    ready = true;
                    notifyHandlers(EVENT_BACKLOG_END, null);
                }
            }
        });

        //Misc. popup alerts
        put("bad_channel_key", new BroadcastParser(EVENT_BADCHANNELKEY));
        put("invalid_nick", new BroadcastParser(EVENT_INVALIDNICK));
        BroadcastParser alert = new BroadcastParser(EVENT_ALERT);
        String[] alerts = {"too_many_channels",
                "no_such_channel",
                "no_such_nick",
                "invalid_nick_change",
                "chan_privs_needed",
                "accept_exists",
                "banned_from_channel",
                "oper_only",
                "no_nick_change",
                "no_messages_from_non_registered",
                "not_registered",
                "already_registered",
                "too_many_targets",
                "no_such_server",
                "unknown_command",
                "help_not_found",
                "accept_full",
                "accept_not",
                "nick_collision",
                "nick_too_fast",
                "save_nick",
                "unknown_mode",
                "user_not_in_channel",
                "need_more_params",
                "users_dont_match",
                "users_disabled",
                "invalid_operator_password",
                "flood_warning",
                "privs_needed",
                "operator_fail",
                "not_on_channel",
                "ban_on_chan",
                "cannot_send_to_chan",
                "user_on_channel",
                "no_nick_given",
                "no_text_to_send",
                "no_origin",
                "only_servers_can_change_mode",
                "silence",
                "no_channel_topic",
                "invite_only_chan",
                "channel_full"};
        for(String event : alerts) {
            put(event, alert);
        }

        //Server events
        put("makeserver", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                ServersDataSource.Server server = s.createServer(object.cid(), object.getString("name"), object.getString("hostname"),
                        object.getInt("port"), object.getString("nick"), object.getString("status"), object.getString("lag").equalsIgnoreCase("undefined")?0:object.getLong("lag"), object.getBoolean("ssl")?1:0,
                        object.getString("realname"), object.getString("server_pass"), object.getString("nickserv_pass"), object.getString("join_commands"),
                        object.getJsonObject("fail_info"), object.getString("away"), object.getJsonArray("ignores"), (object.has("order")&&!object.getString("order").equals("undefined"))?object.getInt("order"):0);
                Notifications.getInstance().deleteNetwork(object.cid());
                if(object.getString("name") != null && object.getString("name").length() > 0)
                    Notifications.getInstance().addNetwork(object.cid(), object.getString("name"));
                else
                    Notifications.getInstance().addNetwork(object.cid(), object.getString("hostname"));

                if(!backlog)
                    notifyHandlers(EVENT_MAKESERVER, server);
            }
        });
        put("server_details_changed", get("makeserver"));
        put("connection_deleted", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.deleteAllDataForServer(object.cid());
                Notifications.getInstance().deleteNetwork(object.cid());
                Notifications.getInstance().showNotifications(null);
                if(!backlog)
                    notifyHandlers(EVENT_CONNECTIONDELETED, object.cid());
            }
        });
        put("status_changed", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.updateStatus(object.cid(), object.getString("new_status"), object.getJsonObject("fail_info"));
                if(!backlog) {
                    if(object.getString("new_status").equals("disconnected")) {
                        ArrayList<BuffersDataSource.Buffer> buffers = BuffersDataSource.getInstance().getBuffersForServer(object.cid());
                        for(BuffersDataSource.Buffer b : buffers) {
                            ChannelsDataSource.getInstance().deleteChannel(b.bid);
                        }
                    }
                    notifyHandlers(EVENT_STATUSCHANGED, object);
                }
            }
        });
        put("connection_lag", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.updateLag(object.cid(), object.getLong("lag"));
                if(!backlog)
                    notifyHandlers(EVENT_CONNECTIONLAG, object);
            }
        });
        put("isupport_params", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.updateUserModes(object.cid(), object.getString("usermodes"));
                s.updateIsupport(object.cid(), object.getJsonObject("params"));
            }
        });
        put("reorder_connections", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                /*JsonArray order = object.getJsonArray("order");
                for(int i = 0; i < order.size(); i++) {
                    ServersDataSource.Server server = s.getServer(order.get(i).getAsInt());
                    server.order = i + 1;
                }*/
                notifyHandlers(EVENT_REORDERCONNECTIONS, object);
            }
        });

        //Buffer events
        put("open_buffer", new BroadcastParser(EVENT_OPENBUFFER));
        put("makebuffer", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                BuffersDataSource.Buffer buffer = b.createBuffer(object.bid(), object.cid(),
                        (object.has("min_eid") && !object.getString("min_eid").equalsIgnoreCase("undefined"))?object.getLong("min_eid"):0,
                        (object.has("last_seen_eid") && !object.getString("last_seen_eid").equalsIgnoreCase("undefined"))?object.getLong("last_seen_eid"):-1, object.getString("name"), object.getString("buffer_type"),
                        (object.has("archived") && object.getBoolean("archived"))?1:0, (object.has("deferred") && object.getBoolean("deferred"))?1:0, (object.has("timeout") && object.getBoolean("timeout"))?1:0);
                Notifications.getInstance().deleteOldNotifications(buffer.bid, buffer.last_seen_eid);
                Notifications.getInstance().updateLastSeenEid(buffer.bid, buffer.last_seen_eid);
                if(!backlog)
                    notifyHandlers(EVENT_MAKEBUFFER, buffer);
                totalbuffers++;
            }
        });
        put("delete_buffer", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                b.deleteAllDataForBuffer(object.bid());
                Notifications.getInstance().deleteNotificationsForBid(object.bid());
                Notifications.getInstance().showNotifications(null);
                if(!backlog)
                    notifyHandlers(EVENT_DELETEBUFFER, object.bid());
            }
        });
        put("buffer_archived", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                b.updateArchived(object.bid(), 1);
                if(!backlog)
                    notifyHandlers(EVENT_BUFFERARCHIVED, object.bid());
            }
        });
        put("buffer_unarchived", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                b.updateArchived(object.bid(), 0);
                if(!backlog)
                    notifyHandlers(EVENT_BUFFERUNARCHIVED, object.bid());
            }
        });
        put("rename_conversation", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                b.updateName(object.bid(), object.getString("new_name"));
                if(!backlog)
                    notifyHandlers(EVENT_RENAMECONVERSATION, object.bid());
            }
        });
        Parser msg = new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                EventsDataSource.Event event = e.addEvent(object);
                BuffersDataSource.Buffer b = BuffersDataSource.getInstance().getBuffer(object.bid());

                if(b != null && event.eid > b.last_seen_eid && event.isImportant(b.type) && ((event.highlight || b.type.equals("conversation")))) {
                    //JSONObject bufferDisabledMap = null;
                    boolean show = true;
                    /*if(userInfo != null && userInfo.prefs != null && userInfo.prefs.has("buffer-disableTrackUnread")) {
                        bufferDisabledMap = userInfo.prefs.getJSONObject("buffer-disableTrackUnread");
                        if(bufferDisabledMap != null && bufferDisabledMap.has(String.valueOf(b.bid)) && bufferDisabledMap.getBoolean(String.valueOf(b.bid)))
                            show = false;
                    }*/
                    if(show && Notifications.getInstance().getNotification(event.eid) == null) {
                        String message = ColorFormatter.irc_to_html(event.msg);
                        message = ColorFormatter.html_to_spanned(message).toString();
                        Notifications.getInstance().addNotification(event.cid, event.bid, event.eid, (event.nick != null)?event.nick:event.from, message, b.name, b.type, event.type);
                        if(!backlog) {
                            if(b.type.equals("conversation"))
                                Notifications.getInstance().showNotifications(b.name + ": " + message);
                            else if(b.type.equals("console")) {
                                if(event.from == null || event.from.length() == 0) {
                                    ServersDataSource.Server s = ServersDataSource.getInstance().getServer(event.cid);
                                    if(s.name != null && s.name.length() > 0)
                                        Notifications.getInstance().showNotifications(s.name + ": " + message);
                                    else
                                        Notifications.getInstance().showNotifications(s.hostname + ": " + message);
                                } else {
                                    Notifications.getInstance().showNotifications(event.from + ": " + message);
                                }
                            } else
                                Notifications.getInstance().showNotifications(b.name + ": <" + event.from + "> " + message);
                        }
                    }
                } else if(b == null && !oobTasks.containsKey(-1)) {
                    Log.e("IRCCloud", "Got a message for a buffer that doesn't exist, reconnecting!");
                    notifyHandlers(EVENT_BACKLOG_FAILED, null);
                    streamId = null;
                }

                if(!backlog)
                    notifyHandlers(EVENT_BUFFERMSG, event);
            }
        };

        String[] msgs = {
                "buffer_msg",
                "buffer_me_msg",
                "wait",
                "banned",
                "kill",
                "connecting_cancelled",
                "target_callerid",
                "notice",
                "server_motdstart",
                "server_welcome",
                "server_motd",
                "server_endofmotd",
                "server_nomotd",
                "server_luserclient",
                "server_luserop",
                "server_luserconns",
                "server_luserme",
                "server_n_local",
                "server_luserchannels",
                "server_n_global",
                "server_yourhost",
                "server_created",
                "server_luserunknown",
                "server_snomask",
                "services_down",
                "your_unique_id",
                "callerid",
                "target_notified",
                "myinfo",
                "hidden_host_set",
                "unhandled_line",
                "unparsed_line",
                "connecting_failed",
                "nickname_in_use",
                "channel_invite",
                "motd_response",
                "socket_closed",
                "channel_mode_list_change",
                "msg_services",
                "stats", "statslinkinfo", "statscommands", "statscline", "statsnline", "statsiline", "statskline", "statsqline", "statsyline", "statsbline", "statsgline", "statstline", "statseline", "statsvline", "statslline", "statsuptime", "statsoline", "statshline", "statssline", "statsuline", "statsdebug", "endofstats",
                "inviting_to_channel",
                "error",
                "too_fast",
                "no_bots",
                "wallops",
                "logged_in_as",
                "sasl_fail",
                "sasl_too_long",
                "sasl_aborted",
                "sasl_already",
                "you_are_operator",
                "btn_metadata_set",
                "sasl_success",
                "cap_ls",
                "cap_req",
                "cap_ack",
                "cap_raw",
                "help_topics_start","help_topics","help_topics_end", "helphdr", "helpop", "helptlr", "helphlp", "helpfwd", "helpign",
                "version",
                "newsflash",
                "invited",
                "codepage",
                "logged_out",
                "nick_locked",
                "info_response",
                "generic_server_info",
                "unknown_umode",
                "bad_ping",
                "rehashed_config",
                "knock",
                "bad_channel_mask",
                "kill_deny",
                "chan_own_priv_needed",
                "not_for_halfops",
                "chan_forbidden",
                "starircd_welcome",
                "zurna_motd",
                "ambiguous_error_message",
                "list_usage",
                "list_syntax",
                "who_syntax",
                "text",
                "admin_info",
                "watch_status",
                "sqline_nick"
        };
        for(String event : msgs) {
            put(event, msg);
        }

        //Channel events
        put("link_channel", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog)
                    notifyHandlers(EVENT_LINKCHANNEL, object);
            }
        });
        put("channel_init", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                /*ChannelsDataSource c = ChannelsDataSource.getInstance();
                ChannelsDataSource.Channel channel = c.createChannel(object.cid(), object.bid(), object.getString("chan"),
                        object.getJsonObject("topic").get("text").isJsonNull()?"":object.getJsonObject("topic").get("text").getAsString(),
                        object.getJsonObject("topic").get("time").getAsLong(),
                        object.getJsonObject("topic").get("nick").getAsString(), object.getString("channel_type"),
                        object.getLong("timestamp"));
                c.updateMode(object.bid(), object.getString("mode"), object.getJsonObject("ops"), true);
                UsersDataSource u = UsersDataSource.getInstance();
                u.deleteUsersForBuffer(object.cid(), object.bid());
                JsonArray users = object.getJsonArray("members");
                for(int i = 0; i < users.size(); i++) {
                    JsonObject user = users.get(i).getAsJsonObject();
                    u.createUser(object.cid(), object.bid(), user.get("nick").getAsString(), user.get("usermask").getAsString(), user.get("mode").getAsString(), user.get("away").getAsBoolean()?1:0, false);
                }
                if(!backlog)
                    notifyHandlers(EVENT_CHANNELINIT, channel);
                    */
            }
        });
        put("channel_topic", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    ChannelsDataSource c = ChannelsDataSource.getInstance();
                    c.updateTopic(object.bid(), object.getString("topic"), object.getLong("eid"), object.getString("author"));
                    notifyHandlers(EVENT_CHANNELTOPIC, object);
                }
            }
        });
        put("channel_url", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ChannelsDataSource c = ChannelsDataSource.getInstance();
                c.updateURL(object.bid(), object.getString("url"));
            }
        });
        put("channel_mode", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                /*if(!backlog) {
                    ChannelsDataSource c = ChannelsDataSource.getInstance();
                    c.updateMode(object.bid(), object.getString("newmode"), object.getJsonObject("ops"), false);
                    notifyHandlers(EVENT_CHANNELMODE, object);
                }*/
            }
        });
        put("channel_mode_is", get("channel_mode"));
        put("channel_timestamp", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                if(!backlog) {
                    ChannelsDataSource c = ChannelsDataSource.getInstance();
                    c.updateTimestamp(object.bid(), object.getLong("timestamp"));
                    notifyHandlers(EVENT_CHANNELTIMESTAMP, object);
                }
            }
        });
        put("joined_channel", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.createUser(object.cid(), object.bid(), object.getString("nick"), object.getString("hostmask"), "", 0);
                    notifyHandlers(EVENT_JOIN, object);
                }
            }
        });
        put("you_joined_channel", get("joined_channel"));
        put("parted_channel", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.deleteUser(object.cid(), object.bid(), object.getString("nick"));
                    if(object.type().equals("you_parted_channel")) {
                        ChannelsDataSource c = ChannelsDataSource.getInstance();
                        c.deleteChannel(object.bid());
                        u.deleteUsersForBuffer(object.cid(), object.bid());
                    }
                    notifyHandlers(EVENT_PART, object);
                }
            }
        });
        put("you_parted_channel", get("parted_channel"));
        put("quit", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.deleteUser(object.cid(), object.bid(), object.getString("nick"));
                    notifyHandlers(EVENT_QUIT, object);
                }
            }
        });
        put("quit_server", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog)
                    notifyHandlers(EVENT_QUIT, object);
            }
        });
        put("kicked_channel", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.deleteUser(object.cid(), object.bid(), object.getString("nick"));
                    if(object.type().equals("you_kicked_channel")) {
                        ChannelsDataSource c = ChannelsDataSource.getInstance();
                        c.deleteChannel(object.bid());
                        u.deleteUsersForBuffer(object.cid(), object.bid());
                    }
                    notifyHandlers(EVENT_KICK, object);
                }
            }
        });
        put("you_kicked_channel", get("kicked_channel"));

        //Member list events
        put("nickchange", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.updateNick(object.cid(), object.bid(), object.getString("oldnick"), object.getString("newnick"));
                    if(object.type().equals("you_nickchange")) {
                        ServersDataSource.getInstance().updateNick(object.cid(), object.getString("newnick"));
                    }
                    notifyHandlers(EVENT_NICKCHANGE, object);
                }
            }
        });
        put("you_nickchange", get("nickchange"));
        put("user_channel_mode", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.updateMode(object.cid(), object.bid(), object.getString("nick"), object.getString("newmode"));
                    notifyHandlers(EVENT_USERCHANNELMODE, object);
                }
            }
        });
        put("member_updates", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                /*JsonObject updates = object.getJsonObject("updates");
                Iterator<Map.Entry<String, JsonElement>> i = updates.entrySet().iterator();
                while(i.hasNext()) {
					Map.Entry<String, JsonElement> entry = i.next();
                    JsonObject user = entry.getValue().getAsJsonObject();
                    UsersDataSource u = UsersDataSource.getInstance();
                    u.updateAway(object.cid(), object.bid(), user.get("nick").getAsString(), user.get("away").getAsBoolean()?1:0);
                    u.updateHostmask(object.cid(), object.bid(), user.get("nick").getAsString(), user.get("usermask").getAsString());
                }*/
                if(!backlog)
                    notifyHandlers(EVENT_MEMBERUPDATES, null);
            }
        });
        put("user_away", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                UsersDataSource u = UsersDataSource.getInstance();
                u.updateAwayMsg(object.cid(), object.bid(), object.getString("nick"), 1, object.getString("msg"));
                b.updateAway(object.bid(), object.getString("msg"));
                if(!backlog)
                    notifyHandlers(EVENT_AWAY, object);
            }
        });
        put("away", get("user_away"));
        put("user_back", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource b = BuffersDataSource.getInstance();
                UsersDataSource u = UsersDataSource.getInstance();
                u.updateAwayMsg(object.cid(), object.bid(), object.getString("nick"), 0, "");
                b.updateAway(object.bid(), "");
                if(!backlog)
                    notifyHandlers(EVENT_AWAY, object);
            }
        });
        put("self_away", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                UsersDataSource u = UsersDataSource.getInstance();
                u.updateAwayMsg(object.cid(), object.bid(), object.getString("nick"), 1, object.getString("away_msg"));
                s.updateAway(object.cid(), object.getString("away_msg"));
                if(!backlog)
                    notifyHandlers(EVENT_AWAY, object);
            }
        });
        put("self_back", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                UsersDataSource u = UsersDataSource.getInstance();
                u.updateAwayMsg(object.cid(), object.bid(), object.getString("nick"), 0, "");
                s.updateAway(object.cid(), "");
                if(!backlog)
                    notifyHandlers(EVENT_SELFBACK, object);
            }
        });
        put("self_details", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                ServersDataSource s = ServersDataSource.getInstance();
                s.updateUsermask(object.cid(), object.getString("usermask"));
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog)
                    notifyHandlers(EVENT_SELFDETAILS, object);
            }
        });
        put("user_mode", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource e = EventsDataSource.getInstance();
                e.addEvent(object);
                if(!backlog) {
                    ServersDataSource s = ServersDataSource.getInstance();
                    s.updateMode(object.cid(), object.getString("newmode"));
                    notifyHandlers(EVENT_USERMODE, object);
                }
            }
        });

        //Various lists
        put("ban_list", new BroadcastParser(EVENT_BANLIST));
        put("accept_list", new BroadcastParser(EVENT_ACCEPTLIST));
        put("names_reply", new BroadcastParser(EVENT_NAMESLIST));
        put("whois_response", new BroadcastParser(EVENT_WHOIS));
        put("list_response_fetching", new BroadcastParser(EVENT_LISTRESPONSEFETCHING));
        put("list_response_toomany", new BroadcastParser(EVENT_LISTRESPONSETOOMANY));
        put("list_response", new BroadcastParser(EVENT_LISTRESPONSE));
        put("who_response", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                if(!backlog) {
                    /*UsersDataSource u = UsersDataSource.getInstance();
                    JsonArray users = object.getJsonArray("users");
                    for(int i = 0; i < users.size(); i++) {
                        JsonObject user = users.get(i).getAsJsonObject();
                        u.updateHostmask(object.cid(), object.bid(), user.get("nick").getAsString(), user.get("usermask").getAsString());
                        u.updateAway(object.cid(), object.bid(), user.get("nick").getAsString(), user.get("away").getAsBoolean()?1:0);
                    }*/
                    notifyHandlers(EVENT_WHOLIST, object);
                }
            }
        });
        put("time", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                EventsDataSource.Event e = EventsDataSource.getInstance().addEvent(object);
                if(!backlog) {
                    notifyHandlers(EVENT_ALERT, object);
                    notifyHandlers(EVENT_BUFFERMSG, e);
                }
            }
        });
        put("channel_topic_is", new Parser() {
            @Override
            public void parse(IRCCloudJSONObject object) {
                BuffersDataSource.Buffer b = BuffersDataSource.getInstance().getBufferByName(object.cid(), object.getString("chan"));
                ChannelsDataSource.Channel c = ChannelsDataSource.getInstance().getChannelForBuffer(b.bid);
                if(c != null) {
                    c.topic_author = object.getString("author");
                    c.topic_time = object.getLong("time");
                    c.topic_text = object.getString("text");
                }
                if(!backlog)
                    notifyHandlers(EVENT_CHANNELTOPICIS, object);
            }
        });
    }};

	private void parse_object(IRCCloudJSONObject object) {
		cancel_idle_timer();
		//Log.d(TAG, "Backlog: " + backlog + " New event: " + object);
		if(!object.has("type")) {
			//Log.d(TAG, "Response: " + object);
			if(object.has("success") && !object.getBoolean("success") && object.has("message")) {
				notifyHandlers(EVENT_FAILURE_MSG, object);
			} else if(object.has("success")) {
				notifyHandlers(EVENT_SUCCESS, object);
			}
			return;
		}
		String type = object.type();
		if(type != null && type.length() > 0) {
            //notifyHandlers(EVENT_DEBUG, "Type: " + type + " BID: " + object.bid() + " EID: " + object.eid());
			//Log.d(TAG, "New event: " + type);
            Parser p = parserMap.get(type);
            if(p != null) {
                p.parse(object);
			} else if(!parserMap.containsKey(type)) {
				//Log.w(TAG, "Unhandled type: " + object);
			}

            if(backlog || type.equals("backlog_complete")) {
                if((object.bid() > -1 || type.equals("backlog_complete")) && !type.equals("makebuffer") && !type.equals("channel_init")) {
                    currentcount++;
                    if(object.bid() != currentBid) {
                        if(currentBid != -1 && currentcount >= BACKLOG_BUFFER_MAX) {
                            EventsDataSource.getInstance().pruneEvents(currentBid, firstEid);
                        }
                        currentBid = object.bid();
                        firstEid = object.eid();
                        currentcount = 0;
                    }
                }
                if(numbuffers > 0 && currentcount < BACKLOG_BUFFER_MAX) {
                    notifyHandlers(EVENT_PROGRESS, ((totalbuffers + ((float)currentcount / (float)BACKLOG_BUFFER_MAX))/ numbuffers) * 1000.0f);
                }
            } else if(accrued > 0) {
                notifyHandlers(EVENT_PROGRESS, ((float)currentcount++ / (float)accrued) * 1000.0f);
            }
        }

        if(!backlog && idle_interval > 0 && accrued < 1)
			schedule_idle_timer();
	}
	
	private String doFetch(URL url, String postdata, String sk) throws Exception {
		HttpURLConnection conn = null;

        Proxy proxy = null;
        String host = null;
        int port = -1;

        if(Build.VERSION.SDK_INT <11) {
            Context ctx = IRCCloudApplication.getInstance().getApplicationContext();
            if(ctx != null) {
                host = android.net.Proxy.getHost(ctx);
                port = android.net.Proxy.getPort(ctx);
            }
        } else {
            host = System.getProperty("http.proxyHost", null);
            try {
                port = Integer.parseInt(System.getProperty("http.proxyPort", "8080"));
            } catch (NumberFormatException e) {
                port = -1;
            }
        }

        if(host != null && host.length() > 0 && !host.equalsIgnoreCase("localhost") && !host.equalsIgnoreCase("127.0.0.1") && port > 0) {
            InetSocketAddress proxyAddr = new InetSocketAddress(host, port);
            proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
        }

        if (url.getProtocol().toLowerCase().equals("https")) {
            HttpsURLConnection https = (HttpsURLConnection)((proxy != null)?url.openConnection(proxy):url.openConnection(Proxy.NO_PROXY));
            if(url.getHost().equals(IRCCLOUD_HOST))
                https.setSSLSocketFactory(IRCCloudSocketFactory);
            conn = https;
        } else {
        	conn = (HttpURLConnection)((proxy != null)?url.openConnection(proxy):url.openConnection(Proxy.NO_PROXY));
        }

		conn.setRequestProperty("Connection", "close");
		conn.setRequestProperty("User-Agent", useragent);
        conn.setRequestProperty("Accept", "application/json");
        if(sk != null)
            conn.setRequestProperty("Cookie", "session="+sk);
        if(postdata != null) {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            OutputStream ostr = null;
            try {
                ostr = conn.getOutputStream();
                ostr.write(postdata.getBytes());
            } finally {
                if (ostr != null)
                    ostr.close();
            }
        }
		BufferedReader reader = null;
		String response = "";
		conn.connect();
		try {
			if(conn.getInputStream() != null) {
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()), 512);
			}
		} catch (IOException e) {
			if(conn.getErrorStream() != null) {
				reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()), 512);
			}
		}

		if(reader != null) {
			response = toString(reader);
			reader.close();
		}
		return response;
	}

	private static String toString(BufferedReader reader) throws IOException {
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append('\n');
		}
		return sb.toString();
	}

	public void addHandler(Handler handler) {
		if(handlers == null)
			handlers = new ArrayList<Handler>();
		if(!handlers.contains(handler))
			handlers.add(handler);
		if(shutdownTimer != null) {
			shutdownTimer.cancel();
			shutdownTimer = null;
		}
	}

	public void removeHandler(Handler handler) {
        if(handlers != null) {
            handlers.remove(handler);
            if(handlers.isEmpty()){
                if(shutdownTimer == null) {
                    //shutdownTimer = new Timer();
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(IRCCloudApplication.getInstance().getApplicationContext());
                    long timeout = Long.valueOf(prefs.getString("timeout", "300000"));
                    /*shutdownTimer.schedule( new TimerTask(){
                         public void run() {
                             if(handlers.isEmpty()) {
                                 disconnect();
                             }
                             shutdownTimer = null;
                          }
                       }, timeout);*/
                }
                if(idleTimer != null && state != STATE_CONNECTED) {
                    idleTimer.cancel();
                    idleTimer = null;
                    failCount = 0;
                    if(wifiLock.isHeld())
                        wifiLock.release();
                    reconnect_timestamp = 0;
                    state = STATE_DISCONNECTED;
                }
            }
        }
	}

	public boolean isVisible() {
		if(handlers != null && handlers.size() > 0)
			return true;
		else
			return false;
	}
	
	public void notifyHandlers(int message, Object object) {
		notifyHandlers(message,object,null);
	}
	
	public synchronized void notifyHandlers(int message, Object object, Handler exclude) {
		if(handlers != null && (message == EVENT_PROGRESS || accrued == 0)) {
			for(int i = 0; i < handlers.size(); i++) {
				Handler handler = handlers.get(i);
				if(handler != exclude) {
					Message msg = handler.obtainMessage(message, object);
					handler.sendMessage(msg);
				}
			}
		}
	}
	
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	public class UserInfo {
        public String name;
        public String email;
        public boolean verified;
        public int last_selected_bid;
        public long connections;
        public long active_connections;
        public long join_date;
        public boolean auto_away;
        public String limits_name;
        public Object limits;
        public int num_invites;
        public Object prefs;
        public String highlights;
		
		public UserInfo(IRCCloudJSONObject object) {
			name = object.getString("name");
			email = object.getString("email");
			verified = object.getBoolean("verified");
			last_selected_bid = object.getInt("last_selected_bid");
			connections = object.getLong("num_connections");
			active_connections = object.getLong("num_active_connections");
			join_date = object.getLong("join_date");
			auto_away = object.getBoolean("autoaway");
			if(object.has("prefs") && !object.getString("prefs").equals("null"))
				prefs = new Object(); // new JSONObject(object.getString("prefs"));
			else
				prefs = null;
			
			limits_name = object.getString("limits_name");
			limits = object.getJsonObject("limits");

			/*if(object.has("highlights")) {
				JsonArray h = object.getJsonArray("highlights");
				highlights = "";
				for(int i = 0; i < h.size(); i++) {
					if(highlights.length() > 0)
						highlights += ", ";
					highlights += h.get(i).getAsString();
				}
			}*/
		}
	}
	
	private class OOBIncludeTask extends AsyncTask<URL, Void, Boolean> {
		private int bid = -1;
		private URL mUrl;
		private long retryDelay = 1000;
		
		public OOBIncludeTask(int bid) {
			this.bid = bid;
		}
		
		@Override
		protected Boolean doInBackground(URL... url) {
			try {
				long totalTime = System.currentTimeMillis();
				long totalParseTime = 0;
				long totalJSONTime = 0;
                long longestEventTime = 0;
                String longestEventType = "";

				Log.d(TAG, "Requesting: " + url[0]);
				mUrl = url[0];
				HttpURLConnection conn = null;
                Proxy proxy = null;
                String host = null;
                int port = -1;

                if(Build.VERSION.SDK_INT <11) {
                    host = android.net.Proxy.getHost(IRCCloudApplication.getInstance().getApplicationContext());
                    port = android.net.Proxy.getPort(IRCCloudApplication.getInstance().getApplicationContext());
                } else {
                    host = System.getProperty("http.proxyHost", null);
                    port = Integer.parseInt(System.getProperty("http.proxyPort", "8080"));
                }

                if(host != null && host.length() > 0 && !host.equalsIgnoreCase("localhost") && !host.equalsIgnoreCase("127.0.0.1")) {
                    InetSocketAddress proxyAddr = new InetSocketAddress(host, port);
                    proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
                }

                if (url[0].getProtocol().toLowerCase().equals("https")) {
		            HttpsURLConnection https = (proxy != null)?(HttpsURLConnection) url[0].openConnection(proxy):(HttpsURLConnection) url[0].openConnection(Proxy.NO_PROXY);
                    https.setSSLSocketFactory(IRCCloudSocketFactory);
		            conn = https;
		        } else {
		        	conn = (HttpURLConnection)((proxy != null)?url[0].openConnection(proxy):url[0].openConnection(Proxy.NO_PROXY));
		        }
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Connection", "close");
				conn.setRequestProperty("Cookie", "session="+session);
				conn.setRequestProperty("Accept", "application/json");
				conn.setRequestProperty("Content-type", "application/json");
				conn.setRequestProperty("Accept-Encoding", "gzip");
				conn.setRequestProperty("User-Agent", useragent);
				conn.connect();
                if(conn.getResponseCode() == 200) {
                    /*JsonReader reader = null;
                    try {
                        if(conn.getInputStream() != null) {
                            if(conn.getContentEncoding() != null && conn.getContentEncoding().equalsIgnoreCase("gzip"))
                                reader = new JsonReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())));
                            else if(conn.getInputStream() != null)
                                reader = new JsonReader(new InputStreamReader(conn.getInputStream()));
                        }
                    } catch (IOException e) {
                        if(conn.getErrorStream() != null) {
                            if(conn.getContentEncoding() != null && conn.getContentEncoding().equalsIgnoreCase("gzip"))
                                reader = new JsonReader(new InputStreamReader(new GZIPInputStream(conn.getErrorStream())));
                            else if(conn.getErrorStream() != null)
                                reader = new JsonReader(new InputStreamReader(conn.getErrorStream()));
                        }
                    }

                    if(reader != null && reader.peek() == JsonToken.BEGIN_ARRAY) {
                        synchronized(parserLock) {
                            cancel_idle_timer();
                            Log.d(TAG, "Connection time: " + (System.currentTimeMillis() - totalTime) + "ms");
                            Log.d(TAG, "Beginning backlog...");
                            if(bid > 0)
                                notifyHandlers(EVENT_BACKLOG_START, null);
                            numbuffers = 0;
                            totalbuffers = 0;
                            currentBid = -1;
                            firstEid = -1;
                            backlog = true;
                            if(bid == -1) {
                                BuffersDataSource.getInstance().invalidate();
                                ChannelsDataSource.getInstance().invalidate();
                            }
                            JsonParser parser = new JsonParser();
                            reader.beginArray();
                            int count = 0;
                            while(reader.hasNext()) {
                                if(isCancelled()) {
                                    Log.d("IRCCloud", "Backlog parsing cancelled");
                                    return false;
                                }
                                long time = System.currentTimeMillis();
                                JsonElement e = parser.parse(reader);
                                totalJSONTime += (System.currentTimeMillis() - time);
                                time = System.currentTimeMillis();
                                IRCCloudJSONObject o = new IRCCloudJSONObject(e.getAsJsonObject());
                                try {
                                    parse_object(o);
                                } catch (Exception ex) {
                                    Log.e(TAG, "Unable to parse message type: " + o.type());
                                    ex.printStackTrace();
                                }
                                long t = (System.currentTimeMillis() - time);
                                if(t > longestEventTime) {
                                    longestEventTime = t;
                                    longestEventType = o.type();
                                }
                                totalParseTime += t;
                                count++;

                            }
                            reader.endArray();
                            backlog = false;
                            //Debug.stopMethodTracing();
                            totalTime = (System.currentTimeMillis() - totalTime);

                            Log.d(TAG, "Backlog complete: " + count + " events");
                            Log.d(TAG, "JSON parsing took: " + totalJSONTime + "ms (" + (totalJSONTime/(float)count) + "ms / object)");
                            Log.d(TAG, "Backlog processing took: " + totalParseTime + "ms (" + (totalParseTime/(float)count) + "ms / object)");
                            Log.d(TAG, "Total OOB load time: " +  totalTime + "ms (" + (totalTime/(float)count) +"ms / object)");
                            Log.d(TAG, "Longest event: " + longestEventType + " (" + longestEventTime + "ms)");
                            totalTime -= totalJSONTime;
                            totalTime -= totalParseTime;
                            Log.d(TAG, "Total non-processing time: " +  totalTime + "ms (" + (totalTime/(float)count) +"ms / object)");

                            ArrayList<BuffersDataSource.Buffer> buffers = BuffersDataSource.getInstance().getBuffers();
                            for(BuffersDataSource.Buffer b : buffers) {
                                Notifications.getInstance().deleteOldNotifications(b.bid, b.last_seen_eid);
                                if(b.timeout > 0 && bid == -1) {
                                    Log.d(TAG, "Requesting backlog for timed-out buffer: " + b.name);
                                    request_backlog(b.cid, b.bid, 0);
                                }
                            }
                            Notifications.getInstance().showNotifications(null);
                            schedule_idle_timer();
                            if(bid > 0) {
                                notifyHandlers(EVENT_BACKLOG_END, null);
                            }
                        }
                    } else {
                        throw new Exception("Unexpected JSON response");
                    }
                    if(reader != null)
                        reader.close();

                    if(bid != -1) {
                        BuffersDataSource.getInstance().updateTimeout(bid, 0);
                    }
                    oobTasks.remove(bid);

                    Log.d(TAG, "OOB fetch complete!");

                    numbuffers = 0;*/
                    return true;
                } else {
                    Log.e(TAG, "Invalid response code: " + conn.getResponseCode());
                    throw new Exception("Invalid response code: " + conn.getResponseCode());
                }
            } catch (SkipException e) {
                throw e; // not to block SkipExceptions
            } catch (Exception e) {
                e.printStackTrace();
				if(bid != -1) {
					if(!isCancelled()) {
                        BuffersDataSource.Buffer b = BuffersDataSource.getInstance().getBuffer(bid);
                        if(b != null && b.timeout == 1) {
                            Log.w(TAG, "Failed to fetch backlog for timed-out buffer, retrying in " + retryDelay + "ms");
                            /*new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     doInBackground(mUrl);
                                 }
                            }, retryDelay);*/
                            retryDelay *= 2;
                        } else {
                            Log.w(TAG, "Failed to fetch backlog");
                            oobTasks.remove(bid);
                        }
					}
                }
			}

            notifyHandlers(EVENT_BACKLOG_FAILED, null);
            if(bid == -1) {
                Log.e(TAG, "Failed to fetch the initial backlog, reconnecting!");
                streamId = null;
            }
			return false;
		}
    }
	
}
