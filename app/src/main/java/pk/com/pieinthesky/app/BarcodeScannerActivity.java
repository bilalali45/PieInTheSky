package pk.com.pieinthesky.app;


import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.utils.BarcodeScanner;

public class BarcodeScannerActivity extends AppCompatActivity {

    private ImageButton btnFlash;
    private BarcodeScanner barcodeScanner;
    private boolean flashState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scan Barcode");


        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        btnFlash = (ImageButton) findViewById(R.id.btnFlash);

        barcodeScanner = new BarcodeScanner(this);
        contentFrame.addView(barcodeScanner);

        checkPermission();
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.CAMERA}, ActivityRequest.REQUEST_CAMERA_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == ActivityRequest.REQUEST_CAMERA_PERMISSION) {
                barcodeScanner.startCamera();
            }
        }
    }

    public void btnFlash_onClick(View view) {
        if (flashState) {
            flashState = false;
            barcodeScanner.setFlash(flashState);
            btnFlash.setImageResource(R.drawable.ic_flash_on);
        } else {
            flashState = true;
            barcodeScanner.setFlash(flashState);
            btnFlash.setImageResource(R.drawable.ic_flash_off);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeScanner.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeScanner.stopCamera();
    }

}

