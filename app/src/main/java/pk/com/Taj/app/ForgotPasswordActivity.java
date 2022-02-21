package pk.com.Taj.app;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import pk.com.Taj.app.connectivity.HttpWeb;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;

import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();

    private EditText etEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setTitle("FORGOT PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etEmailAddress = findViewById(R.id.etEmailAddress);

    }


    public void btnForgotPassword_onClick(View view) {
        if (etEmailAddress.getText().toString().trim().length() == 0) {
            UIHelper.showErrorDialog(this, "", "Email address is required");
            etEmailAddress.requestFocus();
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.getText().toString()).matches()) {
            UIHelper.showErrorDialog(this, "", "Invalid Email address");
            etEmailAddress.requestFocus();
            return;
        }
        if (HttpWeb.isConnectingToInternet(this)) {

            ForgotPasswordTask(etEmailAddress.getText().toString());
        } else {
            UIHelper.showErrorDialog(this, "", "No network connection.");
        }
    }


    private void ForgotPasswordTask(final String Email) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(ForgotPasswordActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Details Checking...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ForgotPassword(Email);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if(status)
                    {
                        etEmailAddress.setText("");
                    }
                        UIHelper.showShortToast(ForgotPasswordActivity.this,  jsonObject.getString("Message"));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
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
