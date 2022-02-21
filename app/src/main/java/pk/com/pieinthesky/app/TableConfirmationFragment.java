package pk.com.pieinthesky.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.IconTextView;

import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableConfirmationFragment extends Fragment implements View.OnClickListener {

    LinearLayout container_back_button;
    private RelativeLayout rlReservationCompleted;
    private RelativeLayout rlReservationConfirmation;
    private EditText etFullName;
    private EditText etEmailAddress;
    private EditText etMobileNo;
    private TextView tvReservationNo;
    private LinearLayout btnViewReservation;
    private Button btnBack;

    private IconTextView cut_full_name;
    private IconTextView cut_email_id;
    private IconTextView cut_mob_num;

    private ProgressDialog dialog;
    private Button btnConfirm;
    private ServiceManager serviceManager = new ServiceManager();
    private ProgressBar more_progress;

    public TableConfirmationFragment() {

    }

    private String placeId;
    private String reservationDate;
    private String timeSlotId;
    private int noOfGuest;
    private String additionalRequests;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_table_confirmation, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        container_back_button = getView().findViewById(R.id.container_back_button);
        rlReservationCompleted = getView().findViewById(R.id.rlReservationCompleted);
        rlReservationConfirmation = getView().findViewById(R.id.rlReservationConfirmation);
        more_progress = getView().findViewById(R.id.more_progress);
        btnConfirm = getView().findViewById(R.id.btnConfirm);
        etFullName = getView().findViewById(R.id.etFullName);
        etEmailAddress = getView().findViewById(R.id.etEmailAddress);
        etMobileNo = getView().findViewById(R.id.etMobileNo);
        cut_email_id = getView().findViewById(R.id.cut_email_id);
        cut_full_name = getView().findViewById(R.id.cut_full_name);
        cut_mob_num = getView().findViewById(R.id.cut_mob_num);
        tvReservationNo = getView().findViewById(R.id.tvReservationNo);
        btnViewReservation = getView().findViewById(R.id.btnViewReservation);
        btnBack = getView().findViewById(R.id.btnBack);

        cut_email_id.setOnClickListener(this);
        cut_full_name.setOnClickListener(this);
        cut_mob_num.setOnClickListener(this);


        rlReservationCompleted.setVisibility(View.GONE);
        rlReservationConfirmation.setVisibility(View.VISIBLE);

        User user = Configuration.getUser();

        placeId = getArguments().getString("PlaceId");
        reservationDate = getArguments().getString("ReservationDate");
        timeSlotId = getArguments().getString("TimeSlotId");
        noOfGuest = getArguments().getInt("NoOfGuest");
        additionalRequests = getArguments().getString("AdditionalRequests");
        etFullName.setText(user.getFirstName() + " " + user.getLastName());
        etEmailAddress.setText(user.getEmail());
        etMobileNo.setText(user.getMobileNo());


        container_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rlReservationCompleted.getVisibility() == View.VISIBLE) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, null);
                }
                getActivity().getSupportFragmentManager().popBackStack("ChangePerDet", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Confirm();
            }
        });

        btnViewReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TableBookingDetailActivity.class);
                intent.putExtra("ReservationId", tvReservationNo.getTag().toString());
                startActivity(intent);

                getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, null);
                getActivity().getSupportFragmentManager().popBackStack("ChangePerDet", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTargetFragment().onActivityResult(getTargetRequestCode(), getActivity().RESULT_OK, null);
                getActivity().getSupportFragmentManager().popBackStack("ChangePerDet", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cut_full_name:
                etFullName.setText("");
                break;
            case R.id.cut_email_id:
                etEmailAddress.setText("");
                break;
            case R.id.cut_mob_num:
                etMobileNo.setText("");
                break;
        }
    }

    private void Confirm() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                more_progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.AddTableReservation(Configuration.getUserId(), placeId, reservationDate, timeSlotId, noOfGuest, additionalRequests,
                        etFullName.getText().toString(), etEmailAddress.getText().toString(), etMobileNo.getText().toString());

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                more_progress.setVisibility(View.GONE);
                try {
                    final boolean status = jsonObject.getBoolean("Status");

                    if (status) {

                        tvReservationNo.setText("#[" + jsonObject.getString("ReservationNo") + "]");
                        tvReservationNo.setTag(jsonObject.getString("ReservationId"));

                        rlReservationConfirmation.setVisibility(View.GONE);
                        rlReservationCompleted.setVisibility(View.VISIBLE);

                    } else {
                        UIHelper.showErrorDialog(getActivity(), "", jsonObject.getString("Message"));
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "exception :: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }.execute();


    }

}
