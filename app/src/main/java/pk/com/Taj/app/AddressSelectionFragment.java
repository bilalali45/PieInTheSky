package pk.com.Taj.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.Taj.app.adapter.AddressAdapter;
import pk.com.Taj.app.beans.AddressDetail;
import pk.com.Taj.app.beans.LocationDetail;
import pk.com.Taj.app.connectivity.ServiceManager;
import pk.com.Taj.app.helper.ActivityRequest;
import pk.com.Taj.app.helper.UIHelper;
import pk.com.Taj.app.utils.BackgroundRequest;
import pk.com.Taj.app.widget.IconTextView;

import org.json.JSONObject;


import java.lang.reflect.Type;
import java.util.List;

public class AddressSelectionFragment extends BottomSheetDialogFragment {

    ProgressDialog dialog;

    ServiceManager serviceManager = new ServiceManager();

    private IconTextView tvClose;
    private LinearLayout btnCurrentLocation;
    private LinearLayout btnAddNewAddress;
    private ListView list_Address;

    private OnOptionItemClickListener onOptionItemClickListener;

    private LocationDetail locationDetail = null;
    private List<AddressDetail> addressList = null;

    public void setOnItemClickListener(OnOptionItemClickListener listener) {
        this.onOptionItemClickListener = listener;
    }

    public AddressSelectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_selection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvClose = getView().findViewById(R.id.tvClose);
        btnCurrentLocation = getView().findViewById(R.id.btnCurrentLocation);
        btnAddNewAddress = getView().findViewById(R.id.btnAddNewAddress);
        list_Address = getView().findViewById(R.id.list_Address);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LocationActivity.class);
                intent.putExtra("Mode", "SelectLocation");
                intent.putExtra("LocationDetail", locationDetail);
                startActivityForResult(intent, ActivityRequest.REQUEST_LOCATION);
            }
        });

        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Configuration.isLogin()) {
                    Intent intent = new Intent(getActivity(), LocationActivity.class);
                    intent.putExtra("Mode", "AddLocation");
                    startActivityForResult(intent, ActivityRequest.REQUEST_ADD_LOCATION);
                } else {
                    Intent intent = new Intent(getActivity(), LoginSelectionActivity.class);
                    intent.putExtra("IsUserLoginRequest", true);
                    startActivity(intent);
                }
            }
        });

        list_Address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onOptionItemClickListener.onSelectAddress(addressList.get(position));
                dismiss();
            }
        });

        addressList = Configuration.getAddressList();

        if (addressList != null) {
            AddressAdapter addressAdapter = new AddressAdapter(getActivity(), addressList);
            list_Address.setAdapter(addressAdapter);
        } else {
            if (Configuration.isLogin()) {
                AddressListTask();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_LOCATION && resultCode == Activity.RESULT_OK) {

            LocationDetail newLocationDetail = (LocationDetail) data.getSerializableExtra("LocationDetail");
            if (newLocationDetail != null) {
                if (newLocationDetail.getLatitude() != 0 && newLocationDetail.getLongitude() != 0 && newLocationDetail.getAddress() != null && !newLocationDetail.getAddress().equals("")) {
                    locationDetail = newLocationDetail;

                    onOptionItemClickListener.onSelectLocation(locationDetail);
                    dismiss();
                }
            }
        } else if (requestCode == ActivityRequest.REQUEST_ADD_LOCATION && resultCode == Activity.RESULT_OK) {

            AddressDetail addressDetail = (AddressDetail) data.getSerializableExtra("AddressDetail");
            if (addressDetail != null) {
                AddAddressTask(addressDetail);
            }
        }
    }

    private void AddAddressTask(final AddressDetail addressDetail) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(getActivity());
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
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void AddressListTask() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please Wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddressList(Configuration.getUserId());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {

                        type = new TypeToken<List<AddressDetail>>() {
                        }.getType();

                        addressList = gson.fromJson(jsonObject.getJSONArray("AddressList").toString(), type);
                        Configuration.setAddressList(addressList);
                        AddressAdapter addressAdapter = new AddressAdapter(getActivity(), addressList);
                        list_Address.setAdapter(addressAdapter);

                    } else {
                        UIHelper.showShortToast(getActivity(), jsonObject.getString("Message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    public interface OnOptionItemClickListener {
        public void onSelectLocation(LocationDetail locationDetail);

        public void onSelectAddress(AddressDetail addressDetail);

    }

}
