package pk.com.Taj.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import pk.com.Taj.app.connectivity.HttpWeb;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;

import org.json.JSONObject;

import java.util.Hashtable;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();

    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmailAddress;
    private EditText etPassword;
    private EditText etConfirmPassword;

    private TextView tvFirstName;
    private TextView tvEmailAddress;
    private TextView tvPassword;

    private LinearLayout container_full_name;
    private LinearLayout container_email;
    private LinearLayout container_password;

    private CircleImageView btn_next;
    private boolean hasFullName = false;
    private boolean hasEmail = false;
    private int activeIndex = 0;
    private Hashtable<Integer, LinearLayout> hashtable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("SIGN UP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmailAddress = findViewById(R.id.etEmailAddress);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        tvFirstName = findViewById(R.id.tvFirstName);
        tvEmailAddress = findViewById(R.id.tvEmailAddress);
        tvPassword = findViewById(R.id.tvPassword);

        container_full_name = findViewById(R.id.container_full_name);
        container_email = findViewById(R.id.container_email);
        container_password = findViewById(R.id.container_password);

        hashtable = new Hashtable<>();
        hashtable.put(0, container_full_name);
        hashtable.put(1, container_email);
        hashtable.put(2, container_password);

        btn_next = findViewById(R.id.btn_next);
        hashtable.get(activeIndex).setVisibility(View.VISIBLE);
    }

    public void btnSignUp_onClick(View view) {

        if (etFirstName.getText().toString().trim().length() == 0) {
            UIHelper.showErrorDialog(this, "", "First Name is required");
            etFirstName.requestFocus();
            return;
        }
        if (etLastName.getText().toString().trim().length() == 0) {
            UIHelper.showErrorDialog(this, "", "Last Name is required");
            etLastName.requestFocus();
            return;
        }
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
        if (etPassword.getText().toString().trim().length() == 0) {
            UIHelper.showErrorDialog(this, "", "Password is required");
            etPassword.requestFocus();
            return;
        }

        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            UIHelper.showErrorDialog(this, "", "Password and confirm password must be same");
            etPassword.requestFocus();
            return;
        }
        if (HttpWeb.isConnectingToInternet(this)) {

            RegisterTask(etFirstName.getText().toString(), etLastName.getText().toString(), etEmailAddress.getText().toString(), etPassword.getText().toString());
        } else {
            UIHelper.showErrorDialog(this, "", "No network connection.");
        }


    }


    private void RegisterTask(final String FirstName, final String LastName, final String Email, final String Password) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setTitle("Please wait");
                dialog.setMessage("Details Checking...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.Register(FirstName, LastName, Email, Password);
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                dialog.dismiss();
                try {
                    final boolean status = jsonObject.getBoolean("Status");

                    UIHelper.showLongToast(SignUpActivity.this, jsonObject.getString("Message"));
                    if (status) {

                        Intent intent = new Intent();
                        intent.putExtra("uid", Email);
                        intent.putExtra("pwd", Password);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
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

    public void setActiveLayout(int index){
        if(index < hashtable.size() ) {
            hashtable.get(activeIndex).setVisibility(View.GONE);
            activeIndex=index;
            hashtable.get(activeIndex).setVisibility(View.VISIBLE);
        }
    }
    public void btnNext_onClick(View view) {
            if (activeIndex == 0) {
                if (etFirstName.getText().toString().trim().length() == 0) {
                    UIHelper.showErrorDialog(this, "", "First Name is required");
                    etFirstName.requestFocus();
                }else if (etLastName.getText().toString().trim().length() == 0) {
                    UIHelper.showErrorDialog(this, "", "Last Name is required");
                    etLastName.requestFocus();
                } else {
                  setActiveLayout(activeIndex+1);
                }
            } else if (activeIndex == 1) {
                if (etEmailAddress.getText().toString().trim().length() == 0) {
                    UIHelper.showErrorDialog(this, "", "Email address is required");
                    etEmailAddress.requestFocus();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmailAddress.getText().toString()).matches()) {
                    UIHelper.showErrorDialog(this, "", "Invalid Email address");
                    etEmailAddress.requestFocus();
                } else {
                   setActiveLayout(activeIndex+1);
                }
            } else if (activeIndex == 2) {
                if (etPassword.getText().toString().trim().length() == 0) {
                    UIHelper.showErrorDialog(this, "", "Password is required");
                    etPassword.requestFocus();
                } else {
                    if (HttpWeb.isConnectingToInternet(this)) {
                        RegisterTask(etFirstName.getText().toString(), etLastName.getText().toString(), etEmailAddress.getText().toString(), etPassword.getText().toString());
                        hasEmail = false;
                    } else {
                        UIHelper.showErrorDialog(this, "", "No network connection.");
                    }
                }
            }
        }

    @Override
    public void onBackPressed() {
        if(activeIndex > 0){
            setActiveLayout(activeIndex-1);
        }else{
            super.onBackPressed();
        }
    }
}
