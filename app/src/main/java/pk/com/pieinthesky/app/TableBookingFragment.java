package pk.com.pieinthesky.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import pk.com.pieinthesky.app.adapter.ReservationAdapter;
import pk.com.pieinthesky.app.beans.ReservationDetail;
import pk.com.pieinthesky.app.helper.ActivityRequest;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TableBookingFragment extends Fragment {

    private ListView list_Reservation;
    private ReservationAdapter reservationAdapter = null;
    private List<ReservationDetail> reservationDetailList;
    private int reservationTypeId;

    public TableBookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_table_booking, container, false);

        list_Reservation = view.findViewById(R.id.list_Reservation);

        list_Reservation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), TableBookingDetailActivity.class);
                intent.putExtra("ReservationId", reservationDetailList.get(position).getReservationId());
                intent.putExtra("ReservationTypeId", reservationTypeId);
                getActivity().startActivityForResult(intent, ActivityRequest.REQUEST_TABLE_DETAIL);
            }
        });

        setAdapter(reservationDetailList);
        return view;
    }

    public void setAdapter(List<ReservationDetail> reservationDetailList) {
        if (reservationDetailList != null) {
            this.reservationDetailList = reservationDetailList;
            reservationAdapter = new ReservationAdapter(getActivity(), reservationDetailList);
            list_Reservation.setAdapter(reservationAdapter);
        }
    }

    public List<ReservationDetail> getReservationDetailList() {
        return reservationDetailList;
    }

    public void setReservationDetailList(List<ReservationDetail> reservationDetailList) {
        this.reservationDetailList = reservationDetailList;
    }

    public int getReservationTypeId() {
        return reservationTypeId;
    }

    public void setReservationTypeId(int reservationTypeId) {
        this.reservationTypeId = reservationTypeId;
    }
}
