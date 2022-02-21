package pk.com.pieinthesky.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.Cuisine;
import pk.com.pieinthesky.app.beans.FilterDetail;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.HttpWeb;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.LocalDataManager;
import pk.com.pieinthesky.app.utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LoginSelectionActivity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton facebookLoginButton;

    private GoogleSignInClient googleSignInClient;

    private LinearLayout lyConnection;
    private LinearLayout lyLoading;
    private ConstraintLayout clMainWindow;
    private FloatingActionButton fabRefresh;

    ServiceManager serviceManager = new ServiceManager();
    private boolean isSuccessfulLogin = false;
    private boolean isInvalidToken = false;
    private boolean isUserLoginRequest = false;

    private User user;
    int RC_SIGN_IN = 501;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_selection);

        lyConnection = findViewById(R.id.lyConnection);
        lyLoading = findViewById(R.id.lyLoading);
        clMainWindow = findViewById(R.id.clMainWindow);
        fabRefresh = findViewById(R.id.fabRefresh);

        facebookLoginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        facebookLoginButton.setPermissions(Arrays.asList("email"));

        user = new User();

        callbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                useFacebookLoginInformation(accessToken);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnlineLogin();
            }
        });

        isSuccessfulLogin = getIntent().getBooleanExtra("IsSuccessfulLogin", false);
        isInvalidToken = getIntent().getBooleanExtra("IsInvalidToken", false);
        isUserLoginRequest = getIntent().getBooleanExtra("IsUserLoginRequest", false);

        if (isInvalidToken == false && isUserLoginRequest == false) {
            gotoNextScreen();
        }
    }

    private void OnlineLogin() {
        if (HttpWeb.isConnectingToInternet(this)) {
            lyConnection.setVisibility(View.GONE);
            clMainWindow.setVisibility(View.VISIBLE);
            if (isLoginSession()) {
                clMainWindow.setVisibility(View.GONE);
                lyLoading.setVisibility(View.VISIBLE);
                sessionLogin();
            }
        } else {
            clMainWindow.setVisibility(View.GONE);
            lyLoading.setVisibility(View.GONE);
            lyConnection.setVisibility(View.VISIBLE);
        }
    }


    private boolean isLoginSession() {
        boolean isLogin = false;
        try {
            isLogin = SessionManager.getInstance().isLoggedIn();

            if (isLogin == false) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                isLogin = accessToken != null && !accessToken.isExpired();
            }
            if (isLogin == false) {
                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
                isLogin = googleSignInAccount != null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLogin;
    }

    private boolean sessionLogin() {
        boolean isLogin = false;
        try {
            if (SessionManager.getInstance().isLoggedIn()) {
                Configuration.setAppLoginWith(Configuration.AppLoginWith.User);
                user.setLoginWith(Configuration.AppLoginWith.User.value);
                user.setUsername(SessionManager.getInstance().getUsername());
                user.setPassword(SessionManager.getInstance().getPassword());

                login();
                isLogin = true;
            }

            if (isLogin == false) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                if (accessToken != null && !accessToken.isExpired()) {
                    useFacebookLoginInformation(accessToken);
                    isLogin = true;
                }
            }

            if (isLogin == false) {
                GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
                if (googleSignInAccount != null) {
                    useGoogleSignInformation(googleSignInAccount);
                    isLogin = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isLogin;
    }

    public void btnLogin_onClick(View view) {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_LOGIN);
    }

    public void btnFacebookLogin_onClick(View view) {

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            useFacebookLoginInformation(accessToken);
        } else {
            facebookLoginButton.performClick();
        }
    }


    public void btnGooglePlusLogin_onClick(View view) {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (googleSignInAccount != null) {
            useGoogleSignInformation(googleSignInAccount);
        } else {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clMainWindow.setVisibility(View.VISIBLE);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            googleSignInResult(task);
        } else if (requestCode == ActivityRequest.REQUEST_LOGIN && resultCode == RESULT_OK) {
            gotoNextScreen();
        } else if (requestCode == ActivityRequest.REQUEST_LOGIN_SELECTION && resultCode == RESULT_OK) {
            finish();
        } else if (requestCode == ActivityRequest.REQUEST_VERIFY_OTP && resultCode == RESULT_OK) {
            login();
        } else if (requestCode == ActivityRequest.REQUEST_SIGN_UP && resultCode == RESULT_OK) {
            Configuration.setAppLoginWith(Configuration.AppLoginWith.User);
            user.setLoginWith(Configuration.AppLoginWith.User.value);
            user.setUsername(data.getStringExtra("uid"));
            user.setPassword(data.getStringExtra("pwd"));

            login();
        }
    }

    private void useFacebookLoginInformation(AccessToken accessToken) {
        try {
            if (accessToken.getDeclinedPermissions().size() == 0) {
                GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Configuration.setAppLoginWith(Configuration.AppLoginWith.Facebook);
                            user.setLoginWith(Configuration.AppLoginWith.Facebook.value);
                            user.setUniqueId(object.getString("id"));
                            user.setFirstName(object.getString("first_name"));
                            user.setLastName(object.getString("last_name"));
                            user.setEmail(object.getString("email"));
                            user.setPhotoURL(object.getJSONObject("picture").getJSONObject("data").getString("url"));

                            login();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email,picture.width(200)");
                request.setParameters(parameters);
                request.executeAsync();
            } else {
                LoginManager.getInstance().logOut();
                UIHelper.showShortToast(this, "We are required to read the email filed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void googleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
            useGoogleSignInformation(googleSignInAccount);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void useGoogleSignInformation(GoogleSignInAccount account) {
        try {
            Configuration.setAppLoginWith(Configuration.AppLoginWith.GoogleSignIn);
            user.setLoginWith(Configuration.AppLoginWith.GoogleSignIn.value);
            user.setUniqueId(account.getId());
            user.setFirstName(account.getGivenName());
            user.setLastName(account.getFamilyName());
            user.setEmail(account.getEmail());
            if (account.getPhotoUrl() != null) {
                user.setPhotoURL(account.getPhotoUrl().toString());
            }

            login();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static Bitmap getImageFromURL(final String url) {
        Bitmap bitmap = null;
        bitmap = new BackgroundRequest<String, Void, Bitmap>() {

            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    URL imageURL = new URL(url);
                    bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return bitmap;
            }
        }.executeAndWait();

        return bitmap;
    }

    private void login() {
        if (HttpWeb.isConnectingToInternet(this)) {
            UserLoginTask();
        }
    }

    private void UserLoginTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                clMainWindow.setVisibility(View.GONE);
                lyLoading.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                if (user.getLoginWith() == Configuration.AppLoginWith.User.value) {
                    jobject = serviceManager.Login(user.getLoginWith(), user.getUsername(), user.getPassword());
                } else {
                    jobject = serviceManager.Login(user.getLoginWith(), user.getUniqueId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhotoURL());
                }

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        if (statusCode == 101) {
                            JSONObject joUser = jsonObject.getJSONObject("User");

                            if (user.getLoginWith() != Configuration.AppLoginWith.User.value) {
                                String token = joUser.getString("Token");

                                ServiceManager.setToken(token);
                                LocalDataManager.getInstance().putString("APIToken", token);
                            }

                            user.setUserId(joUser.getString("Id"));
                            user.setFirstName(joUser.getString("FirstName"));
                            user.setLastName(joUser.getString("LastName"));
                            user.setEmail(joUser.getString("UserEmail"));
                            user.setMobileNo(joUser.getString("MobileNumber"));
                            user.setPhotoURL(joUser.getString("ProfileImageURL"));
                            user.setCoverPhotoURL(joUser.getString("CoverImageURL"));

                            Configuration.setUser(user);
                            Configuration.setLogin(true);
                            Configuration.setOfflineLogin(false);


                            if (user.getLoginWith() == Configuration.AppLoginWith.User.value && !SessionManager.getInstance().isLoggedIn()) {
                                SessionManager.getInstance().login(user.getUsername(), user.getPassword());
                            }

                            FirebaseIDService.updateToken();

                            gotoNextScreen();
                        } else if (statusCode == 102) {

                            Intent intent = new Intent(LoginSelectionActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("Username", user.getUsername());
                            intent.putExtra("MobileNo", user.getUsername());
                            startActivityForResult(intent, ActivityRequest.REQUEST_VERIFY_OTP);
                        }
                    } else {
                        clMainWindow.setVisibility(View.VISIBLE);
                        UIHelper.showErrorDialog(LoginSelectionActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    clMainWindow.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

                lyLoading.setVisibility(View.GONE);

            }
        }.execute();
    }


    public void btnSignUp_onClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_SIGN_UP);
    }

    private void gotoNextScreen() {
        if (isUserLoginRequest == false) {
            Intent intent = new Intent(LoginSelectionActivity.this, MainActivity.class);
            startActivityForResult(intent, ActivityRequest.REQUEST_LOGIN_SELECTION);
        } else {
            Intent in = new Intent(ActivityRequest.REQUEST_LOGIN_ACTION);
            in.putExtra("resultCode", ActivityRequest.REQUEST_LOGIN);
            LocalBroadcastManager.getInstance(this).sendBroadcast(in);
            finish();
        }
    }
}
