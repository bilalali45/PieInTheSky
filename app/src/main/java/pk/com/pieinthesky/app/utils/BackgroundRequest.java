package pk.com.pieinthesky.app.utils;

import android.os.AsyncTask;

public abstract class BackgroundRequest<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public Result executeAndWait(Params... params) {
        Result result = null;
        try {
            result = this.execute(params).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}