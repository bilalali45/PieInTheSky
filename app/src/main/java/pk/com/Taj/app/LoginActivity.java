package pk.com.Taj.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import pk.com.Taj.app.beans.User;
import pk.com.Taj.app.connectivity.HttpWeb;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.utils.SessionManager;

import org.json.JSONObject;

import java.net.URL;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;

    private LinearLayout lyLoading;
    private ConstraintLayout clMainWindow;

    ServiceManager serviceManager = new ServiceManager();
    private User user;

    CircularProgressButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("LOGIN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lyLoading =findViewById(R.id.lyLoading);
        clMainWindow=findViewById(R.id.clMainWindow);

        user = new User();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btn = (CircularProgressButton) findViewById(R.id.btn_id);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.startAnimation();
            }
        });
    }

    public void btnLogin1_onClick(View view) {
/*        btn.revertAnimation(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_bg_red_round));

            }
        });*/

        btn.revertAnimation();
        btn.resetProgress();
    }

    public void btnLogin_onClick(View view) {
        //btn.startAnimation();
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

        //Intent intent = new Intent(LoginActivity.this, SendOTPActivity.class);
        //startActivityForResult(intent, ActivityRequest.SEND_OTP);
        login();
    }

    private void login() {
        if (etUsername.getText().length() != 0 && etPassword.getText().length() != 0) {
            if (HttpWeb.isConnectingToInternet(this)) {
                Configuration.setAppLoginWith(Configuration.AppLoginWith.User);
                user.setLoginWith(Configuration.AppLoginWith.User.value);
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());

                UserLoginTask();
            } else {
                Snackbar
                        .make(etUsername, "No network connection.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login();
                            }
                        }).show();
            }
        } else {
            UIHelper.showErrorDialog(this, "", "please enter username and password");
        }
    }

    public void btnSignUp_onClick(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_SIGN_UP);
    }

    public void btnForgotPassword_onClick(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_FORGOT_PASSWORD);
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
                jobject = serviceManager.Login(user.getLoginWith(), user.getUsername(), user.getPassword());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                try {
                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {
                        if (statusCode == 101) {
                            JSONObject joUser = jsonObject.getJSONObject("User");

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


                            if (user.getLoginWith() == Configuration.AppLoginWith.User.value) {
                                SessionManager.getInstance().login(user.getUsername(), user.getPassword());
                            }

                            FirebaseIDService.updateToken();

                            setResult(RESULT_OK);
                            finish();
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivityForResult(intent, ActivityRequest.REQUEST_LOGIN);
                        } else if (statusCode == 102) {
                            //Intent intent = new Intent(LoginActivity.this, SendOTPActivity.class);
                            //intent.putExtra("Username", user.getUsername());

                            Intent intent = new Intent(LoginActivity.this, VerifyOTPActivity.class);
                            intent.putExtra("Username", user.getUsername());
                            intent.putExtra("MobileNo", user.getUsername());
                            startActivityForResult(intent, ActivityRequest.REQUEST_VERIFY_OTP);
                        }
                    } else {
                        clMainWindow.setVisibility(View.VISIBLE);
                        UIHelper.showErrorDialog(LoginActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    clMainWindow.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }

                lyLoading.setVisibility(View.GONE);
            }
        }.execute();
    }


    private Bitmap getImageFromURL(final String url) {
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        clMainWindow.setVisibility(View.VISIBLE);

        if (requestCode == ActivityRequest.REQUEST_LOGIN && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        } else if (requestCode == ActivityRequest.REQUEST_VERIFY_OTP && resultCode == RESULT_OK) {
            login();
        } else if (requestCode == ActivityRequest.REQUEST_SIGN_UP && resultCode == RESULT_OK) {
            etUsername.setText(data.getStringExtra("uid"));
            etPassword.setText(data.getStringExtra("pwd"));
            login();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        btn.dispose();
    }
}