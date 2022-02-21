package pk.com.pieinthesky.app;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.HttpWeb;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.GPSManager;
import pk.com.pieinthesky.app.utils.LocalDataManager;
import pk.com.pieinthesky.app.utils.SessionManager;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {


    private TextView tvTitle;
    private VideoView videoView;
    private LinearLayout lyEmptyWindow;

    ServiceManager serviceManager = new ServiceManager();

    private GoogleSignInClient googleSignInClient;

    private User user;

    private boolean isGotoNextScreen = true;
    private boolean isSuccessfulLogin = false;
    private boolean isAnimationEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //setContentView(R.layout.activity_splash_video);

        //printHashKey(this);
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //Making notification bar transparent
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            //Change status bar color
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        */

        ServiceManager.setContext(this);
        LocalDataManager.createInstance(this);
        SessionManager.createInstance(this);
        GPSManager.createInstance(this);

        Configuration.setAppLoaded(LocalDataManager.getInstance().getBoolean("AppLoaded"));

        if (Configuration.isAppLoaded() == false) {
            MyFirebaseMessagingService.createNotificationChannel(this);
            FirebaseMessaging.getInstance().subscribeToTopic("pits-all");
            FirebaseMessaging.getInstance().subscribeToTopic("pits-android");
            LocalDataManager.getInstance().putBoolean("AppLoaded", true);
        }

        String APIToken = LocalDataManager.getInstance().getString("APIToken");
        if (APIToken != null) {
            ServiceManager.setToken(APIToken);
        }

        user = new User();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        OnlineLogin();

        tvTitle = findViewById(R.id.tvTitle);

        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(1500);
        fadeIn.setFillAfter(true);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimationEnd = true;
                if (isGotoNextScreen) {
                    gotoNextScreen();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvTitle.startAnimation(fadeIn);


        /*
        try {
            videoView = findViewById(R.id.videoView);
            lyEmptyWindow = findViewById(R.id.lyEmptyWindow);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
            videoView.setVideoURI(video);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            lyEmptyWindow.setVisibility(View.GONE);
                        }
                    }, 300);
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    gotoNextScreen();
                }
            });


        } catch (Exception ex) {
            gotoNextScreen();
        }
        */


        //Intent intent = new Intent(SplashActivity.this, LocationActivity.class);
        //startActivity(intent);
    }


    private void OnlineLogin() {
        if (HttpWeb.isConnectingToInternet(this) && isLoginSession()) {
            isGotoNextScreen = false;
            isGotoNextScreen = !sessionLogin();

        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (videoView != null) {
            videoView.start();
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


    private void login() {
        if (HttpWeb.isConnectingToInternet(this)) {
            UserLoginTask();
        } else {
            isGotoNextScreen = true;
        }
    }


    private void UserLoginTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {

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

                            isSuccessfulLogin = true;
                        }
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }

                isGotoNextScreen = true;
                if (isAnimationEnd) {
                    gotoNextScreen();
                }
            }
        }.execute();
    }

    private void gotoNextScreen() {
        //if (isLoginSession()) {
            Intent intent = new Intent(SplashActivity.this, LoginSelectionActivity.class);
            intent.putExtra("IsSuccessfulLogin", isSuccessfulLogin);
            startActivity(intent);
      //  } else {
       //     Intent intent = new Intent(SplashActivity.this, AppIntroActivity.class);
        //    startActivity(intent);
        //}
        finish();
    }


    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("splah", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("splah", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("splah", "printHashKey()", e);
        }
    }

}