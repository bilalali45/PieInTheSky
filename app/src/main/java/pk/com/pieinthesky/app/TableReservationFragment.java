package pk.com.pieinthesky.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.ReservationDay;
import pk.com.pieinthesky.app.beans.ReservationTimings;
import pk.com.pieinthesky.app.beans.User;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TableReservationFragment extends Fragment {

    LinearLayout container_back_button;

    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvAvgRating;
    private TextView tvTotalReview;
    private LinearLayout day_container;
    private LinearLayout people_container;
    private LinearLayout time_container;
    private LinearLayout container_day_and_date;
    private LinearLayout container_number_of_people;
    private LinearLayout container_timings;
    private LinearLayout lySelectedDate;
    private LinearLayout lySelectedNoOfPerson;
    private LinearLayout lySelectedTime;
    private ServiceManager serviceManager = new ServiceManager();

    private ProgressBar more_progress;
    private LinearLayout button_next;
    private ProgressBar more_progress_people;
    private TextView tvChange;
    private TextView tvFullName;
    private TextView tvEmailAddress;
    private EditText etAdditionalRequest;
    private TextView tvRestaurantNote;

    private String placeId;

    public TableReservationFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_reservation, container, false);

        touchListener(view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ivPlaceImage = getView().findViewById(R.id.ivPlaceImage);
        tvPlaceName = getView().findViewById(R.id.tvPlaceName);
        tvTags = getView().findViewById(R.id.tvTags);
        tvAvgRating = getView().findViewById(R.id.tvAvgRating);
        tvTotalReview = getView().findViewById(R.id.tvTotalReview);
        tvFullName = getView().findViewById(R.id.tvFullName);
        tvEmailAddress = getView().findViewById(R.id.tvEmailAddress);
        tvChange = getView().findViewById(R.id.tvChange);
        etAdditionalRequest = getView().findViewById(R.id.etAdditionalRequest);
        tvRestaurantNote = getView().findViewById(R.id.tvRestaurantNote);
        more_progress = getView().findViewById(R.id.more_progress);
        day_container = getView().findViewById(R.id.day_container);
        people_container = getView().findViewById(R.id.people_container);
        time_container = getView().findViewById(R.id.time_container);
        container_day_and_date = getView().findViewById(R.id.container_day_and_date);
        container_number_of_people = getView().findViewById(R.id.container_number_of_people);
        container_timings = getView().findViewById(R.id.container_timings);
        container_back_button = getView().findViewById(R.id.container_back_button);
        button_next = getView().findViewById(R.id.button_next);
        more_progress_people = getView().findViewById(R.id.more_progress_people);

        placeId = getArguments().getString("PlaceId");

        Glide.with(this)
                .load(getArguments().getString("PlaceImageURL"))
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);

        tvPlaceName.setText(getArguments().getString("PlaceName"));
        tvTags.setText(getArguments().getString("Tags"));
        tvAvgRating.setText(getArguments().getString("AvgRating"));
        tvTotalReview.setText(getArguments().getString("TotalReview"));
        ReservationTableDays(placeId);


        User user = Configuration.getUser();
        tvFullName.setText(user.getFirstName() + " " + user.getLastName());
        tvEmailAddress.setText(user.getEmail());

        container_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack("PlaceDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonalDetailsChange();
            }
        });
    }

    private void touchListener(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    FragmentManager fm = getActivity()
                            .getSupportFragmentManager();
                    fm.popBackStack("PlaceDetail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                return true;
            }
        });
    }


    private void goToPersonalDetailsChange() {

        if (lySelectedDate == null) {
            UIHelper.showShortToast(getActivity(), "Please select day");
            return;
        }

        if (lySelectedNoOfPerson == null) {
            UIHelper.showShortToast(getActivity(), "Please select no of people");
            return;
        }


        if (lySelectedTime == null) {
            UIHelper.showShortToast(getActivity(), "Please select time");
            return;
        }

        TableConfirmationFragment tableConfirmationFragment = new TableConfirmationFragment();

        Bundle bundle = new Bundle();
        bundle.putString("PlaceId", placeId);
        bundle.putString("ReservationDate", lySelectedDate.getTag().toString());
        bundle.putString("TimeSlotId", lySelectedTime.getTag().toString());
        bundle.putInt("NoOfGuest", Integer.valueOf(lySelectedNoOfPerson.getTag().toString()));
        bundle.putString("AdditionalRequests", etAdditionalRequest.getText().toString());
        tableConfirmationFragment.setArguments(bundle);
        tableConfirmationFragment.setTargetFragment(TableReservationFragment.this, ActivityRequest.REQUEST_TABLE_CONFIRMATION);
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.slide_in_up, R.anim.slide_out_up)
                .add(android.R.id.content, tableConfirmationFragment)
                .addToBackStack("ChangePerDet")
                .commit();
    }


    private void ReservationTableDays(final String PlaceId) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                day_container.setVisibility(View.GONE);
                more_progress.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ReservationTableDays(PlaceId);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                more_progress.setVisibility(View.GONE);
                day_container.setVisibility(View.VISIBLE);
                button_next.setVisibility(View.VISIBLE);
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {

                        type = new TypeToken<List<ReservationDay>>() {
                        }.getType();

                        List<ReservationDay> reservationDays = gson.fromJson(jsonObject.getJSONArray("ReservationTableDay").toString(), type);


                        for (ReservationDay reservationDay : reservationDays) {
                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.day_selection_item, null);
                            LinearLayout container_layout = view.findViewById(R.id.container_layout);
                            TextView tvDay = view.findViewById(R.id.tvDay);
                            TextView tvDate = view.findViewById(R.id.tvDate);
                            container_layout.setTag(reservationDay.getDate());
                            tvDay.setText(reservationDay.getDay());
                            tvDate.setText(reservationDay.getTitle());
                            container_layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (lySelectedDate != null) {
                                        lySelectedDate.setBackgroundResource(R.drawable.bg_gray_corner);
                                    }
                                    lySelectedDate = (LinearLayout) view;
                                    lySelectedDate.setBackgroundResource(R.color.colorLightBlue);
                                    BookingTableTimings(PlaceId, view.getTag().toString());
                                }
                            });

                            container_day_and_date.addView(view);

                        }

                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private void BookingTableTimings(final String PlaceId, final String ReservationDate) {
        container_number_of_people.removeAllViews();
        container_timings.removeAllViews();

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                people_container.setVisibility(View.GONE);
                more_progress_people.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ReservationTableTimings(PlaceId, ReservationDate);

                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                more_progress_people.setVisibility(View.GONE);
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    final boolean status = jsonObject.getBoolean("Status");
                    final int statusCode = jsonObject.getInt("StatusCode");
                    if (status) {

                        type = new TypeToken<List<ReservationTimings>>() {
                        }.getType();

                        List<ReservationTimings> reservationTimings = gson.fromJson(jsonObject.getJSONArray("ReservationTableTimings").toString(), type);


                        if (reservationTimings.size() > 0) {

                            people_container.setVisibility(View.VISIBLE);

                            for (int i = 1; i <= 10; i++) {
                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.text_selection_item, null);
                                LinearLayout container_layout = view.findViewById(R.id.container_layout);
                                container_layout.setTag(String.valueOf(i));
                                TextView tvDisplayText = view.findViewById(R.id.tvDisplayText);
                                tvDisplayText.setText(String.valueOf(i));

                                container_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (lySelectedNoOfPerson != null) {
                                            lySelectedNoOfPerson.setBackgroundResource(R.drawable.bg_gray_corner);
                                        }
                                        lySelectedNoOfPerson = (LinearLayout) view;
                                        lySelectedNoOfPerson.setBackgroundResource(R.color.colorLightBlue);

                                        time_container.setVisibility(View.VISIBLE);

                                    }
                                });

                                container_number_of_people.addView(view);
                            }

                            for (ReservationTimings reservationTiming : reservationTimings) {
                                View view = LayoutInflater.from(getActivity()).inflate(R.layout.text_selection_item, null);
                                LinearLayout container_layout = view.findViewById(R.id.container_layout);
                                TextView tvDisplayText = view.findViewById(R.id.tvDisplayText);

                                container_layout.setTag(reservationTiming.getTimeSlotId());
                                tvDisplayText.setText(reservationTiming.getTimeStart());

                                container_layout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (lySelectedTime != null) {
                                            lySelectedTime.setBackgroundResource(R.drawable.bg_gray_corner);
                                        }
                                        lySelectedTime = (LinearLayout) view;
                                        lySelectedTime.setBackgroundResource(R.color.colorLightBlue);
                                    }
                                });

                                container_timings.addView(view);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequest.REQUEST_TABLE_CONFIRMATION && resultCode == Activity.RESULT_OK) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

}
