package pk.com.Taj.app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pk.com.Taj.app.connectivity.HttpWeb;

public class NoConnectionActivity extends AppCompatActivity {

    private FloatingActionButton fabRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_connection);

        fabRefresh = findViewById(R.id.fabRefresh);

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpWeb.isConnectingToInternet(NoConnectionActivity.this)) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (HttpWeb.isConnectingToInternet(NoConnectionActivity.this)) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
}
