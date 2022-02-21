package pk.com.pieinthesky.app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.utils.LocalDataManager;

import org.json.JSONObject;

public class VerifyOTPActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();

    private EditText etPin1;
    private EditText etPin2;
    private EditText etPin3;
    private EditText etPin4;
    private EditText etPin5;
    private EditText etPin6;

    private EditText etPrevious;
    private EditText etNext;

    private String Username;
    private String MobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        getSupportActionBar().setTitle("Verify OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Username = getIntent().getStringExtra("Username");
        MobileNo = getIntent().getStringExtra("MobileNo");

        etPin1 = (EditText) findViewById(R.id.etPin1);
        etPin2 = (EditText) findViewById(R.id.etPin2);
        etPin3 = (EditText) findViewById(R.id.etPin3);
        etPin4 = (EditText) findViewById(R.id.etPin4);
        etPin5 = (EditText) findViewById(R.id.etPin5);
        etPin6 = (EditText) findViewById(R.id.etPin6);

        etPrevious = etPin1;
        etNext = etPin1;

        etPin1.setOnKeyListener(onKeyListener);
        etPin2.setOnKeyListener(onKeyListener);
        etPin3.setOnKeyListener(onKeyListener);
        etPin4.setOnKeyListener(onKeyListener);
        etPin5.setOnKeyListener(onKeyListener);
        etPin6.setOnKeyListener(onKeyListener);

        etPin1.setOnFocusChangeListener(onFocusChangeListener);
        etPin2.setOnFocusChangeListener(onFocusChangeListener);
        etPin3.setOnFocusChangeListener(onFocusChangeListener);
        etPin4.setOnFocusChangeListener(onFocusChangeListener);
        etPin5.setOnFocusChangeListener(onFocusChangeListener);
        etPin6.setOnFocusChangeListener(onFocusChangeListener);

        etPin1.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(VerifyOTPActivity.this.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(etPin1, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);

    }


    View.OnKeyListener onKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                etNext.requestFocus();
            } else if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_DEL && ((EditText) view).getText().length() == 0 && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                etPrevious.setText("");
                etPrevious.requestFocus();
            } else if (view.equals(etPin6) && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                //verifyOTP();
            }
            return false;
        }
    };

    View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {

                if (view.getId() == R.id.etPin1) {
                    etPrevious = etPin1;
                    etNext = etPin2;
                } else if (view.getId() == R.id.etPin2) {
                    etPrevious = etPin1;
                    etNext = etPin3;
                } else if (view.getId() == R.id.etPin3) {
                    etPrevious = etPin2;
                    etNext = etPin4;
                } else if (view.getId() == R.id.etPin4) {
                    etPrevious = etPin3;
                    etNext = etPin5;
                } else if (view.getId() == R.id.etPin5) {
                    etPrevious = etPin4;
                    etNext = etPin6;
                } else if (view.getId() == R.id.etPin6) {
                    etPrevious = etPin5;
                    etNext = etPin6;
                }
            }
        }
    };

    private void verifyOTP() {
        String OTPCode = etPin1.getText().toString() + etPin2.getText().toString() + etPin3.getText().toString() + etPin4.getText().toString() + etPin5.getText().toString() + etPin6.getText().toString();
        if (OTPCode.length() == 6) {

            VerifyOTPTask(Username,OTPCode);
        }
    }



    private void VerifyOTPTask(final String Username,final String OTPCode) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(VerifyOTPActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Details Checking...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.VerifyOTP(Username,OTPCode);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        String token = jsonObject.getString("Token");

                        ServiceManager.setToken(token);
                        LocalDataManager.getInstance().putString("APIToken", token);
                        Configuration.setLogin(false);
                        Configuration.setOfflineLogin(false);

                        setResult(RESULT_OK);
                        finish();
                    } else {
                        UIHelper.showErrorDialog(VerifyOTPActivity.this, "", jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    private void ResendOTPTask(final String Username) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(VerifyOTPActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Details Checking...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ResendOTP(Username);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");

                    UIHelper.showShortToast(VerifyOTPActivity.this, jsonObject.getString("Message"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }




    public void btnDone_onClick(View view) {
        verifyOTP();
    }

    public void btnSendOTP_onClick(View view) {
        ResendOTPTask(Username);
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
}