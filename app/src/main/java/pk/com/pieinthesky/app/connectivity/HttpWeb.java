package pk.com.pieinthesky.app.connectivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpWeb {

    public static boolean isConnectingToInternet(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception ex) {
        }
        return false;
    }

    public String HttResponse(String url, String method, String postdata, String token) {
        String resp = "";
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(0, TimeUnit.SECONDS)
                    .readTimeout(0, TimeUnit.SECONDS)
                    .build();
            Request.Builder builder = new Request.Builder();
            builder.url(url);
            if (!token.equals("")) {
                builder.addHeader("Token", token);
            }
            if (method == "POST") {
                RequestBody sBody = RequestBody.create(MediaType.parse("application/json"), postdata);
                //RequestBody sBody = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postdata);
                builder.post(sBody);
            }
            Request request = builder.build();
            Response response = client.newCall(request).execute();
            resp = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}
