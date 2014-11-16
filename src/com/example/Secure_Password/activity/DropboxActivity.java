package com.example.Secure_Password.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.example.Secure_Password.LoaderFileWithDropbox;
import com.example.Secure_Password.R;

public class DropboxActivity extends DataBasedActivity implements OnClickListener {

    private DropboxAPI<AndroidAuthSession> dropbox;

    private final static String FILE_DIR = "/Secure Password/";
    private final static String DB_PATH = database.getPath();

    private final static String APP_KEY = "edcty9nwol5h593";
    private final static String APP_SECRET = "ejabelkntsr68zi";

    private final static String ACCOUNT_PREFS_NAME = "prefs";
    private final static String ACCESS_KEY_NAME = "ACCESS_KEY";
    private final static String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;

    private boolean isLoggedIn;

    private TextView authText;
    private Button logIn;
    private Button uploadFile;
    private Button downloadFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dropbox);

        authText = (TextView) findViewById(R.id.authText);
        logIn = (Button) findViewById(R.id.dropbox_login);
        logIn.setOnClickListener(this);
        uploadFile = (Button) findViewById(R.id.upload_file);
        uploadFile.setOnClickListener(this);
        downloadFile = (Button) findViewById(R.id.download_file);
        downloadFile.setOnClickListener(this);

        AndroidAuthSession session = buildSession();

        dropbox = new DropboxAPI<AndroidAuthSession>(session);
        loggedIn(dropbox.getSession().isLinked());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = dropbox.getSession();

        if (session.authenticationSuccessful()) {
            try {
                session.finishAuthentication();

                storeAuth(session);
                loggedIn(true);
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Ошибка авторизации Dropbox!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loggedIn(boolean isLogged) {
        isLoggedIn = isLogged;
        uploadFile.setEnabled(isLogged);
        downloadFile.setEnabled(isLogged);
        logIn.setText(isLogged ? "Выйти" : "Войти");
        authText.setText(isLogged ? "Вы авторизованы!" : "Пожалуйста, авторизуйтесь!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dropbox_login:
                if (isLoggedIn) {
                    logOut();
                } else {
                    if(USE_OAUTH1) {
                        dropbox.getSession().startAuthentication(DropboxActivity.this);
                    } else {
                        dropbox.getSession().startOAuth2Authentication(DropboxActivity.this);
                    }
                }
                break;

            case R.id.upload_file:
                LoaderFileWithDropbox upload = new LoaderFileWithDropbox(this, dropbox, DB_PATH, FILE_DIR, false);
                upload.execute();
                break;

            case R.id.download_file:
                LoaderFileWithDropbox download = new LoaderFileWithDropbox(this, dropbox, DB_PATH, FILE_DIR, true);
                download.execute();
                break;

            default:
                break;
        }
    }

    private void logOut() {
        // Remove credentials from the session
        dropbox.getSession().unlink();

        // Clear our stored keys
        clearKeys();
        // Change UI state to display logged out version
        loggedIn(false);
    }

    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    private void storeAuth(AndroidAuthSession session) {
        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    private AndroidAuthSession buildSession() {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);

        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        loadAuth(session);
        return session;
    }
}