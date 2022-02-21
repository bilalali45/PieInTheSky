package pk.com.pieinthesky.app;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import pk.com.pieinthesky.app.connectivity.ServiceManager;

import org.json.JSONObject;

public class FirebaseIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        updateToken();
    }

    public static String getToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static boolean updateToken() {
        return updateToken(getToken());
    }

    public static boolean updateToken(String token) {
        boolean result = false;
        try {
            class task extends AsyncTask<String, Void, JSONObject> {
                protected JSONObject doInBackground(String... params) {
                    JSONObject jobject;
                    ServiceManager serviceManager = new ServiceManager();
                    jobject = serviceManager.RefreshFirebaseToken(Configuration.getUserId(), params[0]);
                    return jobject;
                }
            }
            if(Configuration.isLogin()) {
                new task().execute(token);
            }
            return true;
        } catch (Exception e) {

        }
        return result;
    }
}
