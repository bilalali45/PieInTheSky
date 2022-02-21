package pk.com.Taj.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import pk.com.Taj.app.helper.ActivityRequest;

public class SendOTPActivity extends AppCompatActivity {

    private String Username;
    private EditText etMobileNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        getSupportActionBar().setTitle("Send OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Username = getIntent().getStringExtra("Username");

        etMobileNo = (EditText) findViewById(R.id.etMobileNo);

    }

    public void btnSendOTP_onClick(View view) {
        Intent intent = new Intent(SendOTPActivity.this, VerifyOTPActivity.class);
        intent.putExtra("Username",Username);
        intent.putExtra("MobileNo",etMobileNo.getText().toString());
        startActivityForResult(intent, ActivityRequest.REQUEST_VERIFY_OTP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_VERIFY_OTP && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
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

}
