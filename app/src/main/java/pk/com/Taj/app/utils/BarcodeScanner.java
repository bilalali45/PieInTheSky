package pk.com.Taj.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;


import pk.com.Taj.app.helper.UIHelper;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;


public class BarcodeScanner extends ZBarScannerView implements ZBarScannerView.ResultHandler {
    public static final String ACTION = "pk.com.pieinthesky.app.BarcodeScanner";
    public static final int RESULT_SCANNED = 101;
    public static final int RESULT_UNKNOWN = 102;

    private Context context;

    private ScanDataType resultDataType = ScanDataType.TEXT;


    public BarcodeScanner(Context context) {
        super(context);
        this.context = context;
        setResultHandler(this);
        setLaserEnabled(true);
        setLaserColor(Color.argb(150, 0, 255, 0));
        setMaskColor(Color.argb(100, 150, 150, 150));
        setBorderColor(Color.argb(150, 200, 200, 200));
    }

    @Override
    protected IViewFinder createViewFinderView(Context context) {
        return new CustomViewFinderView(context);
    }

    @Override
    public void handleResult(Result result) {
        String data = result.getContents();
        ScanDataType scanDataType = getScanDataType(data);

        if (scanDataType == resultDataType || resultDataType == ScanDataType.TEXT) {
            Intent in = new Intent(ACTION);
            in.putExtra("resultCode", RESULT_SCANNED);
            in.putExtra("data", data);
            LocalBroadcastManager.getInstance(context).sendBroadcast(in);
        } else {
            UIHelper.showShortToast(context, "Invalid barcode.");
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                resumeCameraPreview(BarcodeScanner.this);
            }
        }, 1000);
    }


    public ScanDataType getResultDataType() {
        return resultDataType;
    }

    public void setResultDataType(ScanDataType resultDataType) {
        this.resultDataType = resultDataType;
    }

    private ScanDataType getScanDataType(String data) {
        if (data.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            return ScanDataType.NUMERIC;
        } else {
            return ScanDataType.TEXT;
        }
    }

    private static class CustomViewFinderView extends ViewFinderView {
        int position = 0;
        int direction = 1;

        public CustomViewFinderView(Context context) {
            super(context);
        }

        @Override
        public void drawLaser(Canvas canvas) {
            Rect framingRect = this.getFramingRect();
            if (position == 0)
                position = framingRect.top;
            else if (position >= framingRect.bottom - 5)
                direction = -1;
            else if (position <= framingRect.top)
                direction = 1;

            position += (5 * direction);
            canvas.drawRect(framingRect.left + 2, position - 1, framingRect.right - 1, position + 5, this.mLaserPaint);
            this.postInvalidateDelayed(15L, framingRect.left - 10, framingRect.top - 10, framingRect.right + 10, framingRect.bottom + 10);
        }
    }


    public enum ScanDataType {
        TEXT, NUMERIC;
    }

}

