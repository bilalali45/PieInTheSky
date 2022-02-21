package pk.com.pieinthesky.app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.adapter.AddressAdapter;
import pk.com.pieinthesky.app.beans.AddressDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class AddressListActivity extends AppCompatActivity {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();

    private LinearLayout btnAddNewAddress;
    private ShimmerFrameLayout shimmer_Address;
    private ListView list_Address;
    private List<AddressDetail> addressList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        getSupportActionBar().setTitle("My Address");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddNewAddress = findViewById(R.id.btnAddNewAddress);
        shimmer_Address = findViewById(R.id.shimmer_Address);
        list_Address = findViewById(R.id.list_Address);

        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressListActivity.this, LocationActivity.class);
                intent.putExtra("Mode", "AddLocation");
                startActivityForResult(intent, ActivityRequest.REQUEST_LOCATION);
            }
        });

        list_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        AddressListTask();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_LOCATION && resultCode == RESULT_OK) {

            AddressDetail addressDetail = (AddressDetail) data.getSerializableExtra("AddressDetail");
            if (addressDetail != null) {
                AddAddressTask(addressDetail);
            }
        }
    }

    private AddressAdapter.OnItemClickListener onItemClickListener = new AddressAdapter.OnItemClickListener() {
        @Override
        public void onDeleteClick(View view, final int position, long id) {

            UIHelper.showConfirmDialog(AddressListActivity.this, "", "Are you sure to delete address?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DeleteAddressTask(addressList.get(position).getAddressId());
                }
            });
        }
    };

    private void AddressListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                shimmer_Address.startShimmer();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddressList(Configuration.getUserId());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        type = new TypeToken<List<AddressDetail>>() {
                        }.getType();

                        addressList = gson.fromJson(jsonObject.getJSONArray("AddressList").toString(), type);
                        Configuration.setAddressList(addressList);
                        AddressAdapter addressAdapter = new AddressAdapter(AddressListActivity.this, addressList);
                        addressAdapter.setOnItemClickListener(onItemClickListener);
                        list_Address.setAdapter(addressAdapter);

                    } else {
                        UIHelper.showShortToast(AddressListActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                shimmer_Address.stopShimmer();
                shimmer_Address.setVisibility(View.GONE);
            }
        }.execute();
    }

    private void AddAddressTask(final AddressDetail addressDetail) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(AddressListActivity.this);
                dialog.setMessage("Please Wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddAddress(Configuration.getUserId(), addressDetail);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {
                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        AddressListTask();
                    } else {
                        UIHelper.showShortToast(AddressListActivity.this, jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    private void DeleteAddressTask(final String AddressId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(AddressListActivity.this);
                dialog.setMessage("Please Wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.DeleteAddress(Configuration.getUserId(), AddressId);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        AddressListTask();
                    } else {
                        UIHelper.showShortToast(AddressListActivity.this, jsonObject.getString("Message"));
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
}
