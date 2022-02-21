package pk.com.pieinthesky.app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import pk.com.pieinthesky.app.beans.Queue;
import pk.com.pieinthesky.app.beans.QueueDetail;
import pk.com.pieinthesky.app.connectivity.ServiceManager;
import pk.com.pieinthesky.app.helper.ActivityRequest;
import pk.com.pieinthesky.app.helper.UIHelper;
import pk.com.pieinthesky.app.utils.BackgroundRequest;
import pk.com.pieinthesky.app.widget.RoundCornerImageView;

import org.json.JSONObject;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ActiveQueueFragment extends Fragment {


    ServiceManager serviceManager = new ServiceManager();

    ProgressDialog dialog;
    Dialog queueChangeDialog;

    private QueueDetail queueDetail;
    private LinearLayout lyLoading;
    private RelativeLayout containerAddQueue;
    private LinearLayout containerQueueDetail;
    private Button btnScanQR;

    private TextView tvTokenNo;
    private TextView tvEstimatedTime;
    private RoundCornerImageView ivPlaceImage;
    private TextView tvPlaceName;
    private TextView tvTags;
    private TextView tvPlaceLocation;
    private Button btnChange;
    private LinearLayout lyMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_queue, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnScanQR = getView().findViewById(R.id.btnScanQR);
        tvTokenNo = getView().findViewById(R.id.tvTokenNo);
        tvEstimatedTime = getView().findViewById(R.id.tvEstimatedTime);
        ivPlaceImage = getView().findViewById(R.id.ivPlaceImage);
        tvPlaceName = getView().findViewById(R.id.tvPlaceName);
        tvTags = getView().findViewById(R.id.tvTags);
        tvPlaceLocation = getView().findViewById(R.id.tvPlaceLocation);
        btnChange = getView().findViewById(R.id.btnChange);
        lyMenu = getView().findViewById(R.id.lyMenu);

        lyLoading = getView().findViewById(R.id.lyLoading);
        containerAddQueue = getView().findViewById(R.id.containerAddQueue);
        containerQueueDetail = getView().findViewById(R.id.containerQueueDetail);

        lyLoading.setVisibility(View.VISIBLE);
        containerAddQueue.setVisibility(View.GONE);
        containerQueueDetail.setVisibility(View.GONE);

        btnChange.setPaintFlags(btnChange.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanQR_onClick(view);
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQueueChangeDialog();
            }
        });

        ActiveQueues();
    }


    private void showQueueChangeDialog() {
        queueChangeDialog = new Dialog(getActivity());
        queueChangeDialog.setContentView(R.layout.change_queue_dialog);

        Button btnAdd10Min = queueChangeDialog.findViewById(R.id.btnAdd10Min);
        Button btnAdd20Min = queueChangeDialog.findViewById(R.id.btnAdd20Min);
        Button btnShiftToNextToken = queueChangeDialog.findViewById(R.id.btnShiftToNextToken);
        Button btnCancelToken = queueChangeDialog.findViewById(R.id.btnCancelToken);
        Button btnClose = queueChangeDialog.findViewById(R.id.btnClose);

        btnAdd10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showConfirmDialog(getActivity(), "", "Are you sure to add 10 min?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeInQueue(queueDetail.getQueueId(), queueDetail.getTokenNo(), "Time", 10);
                    }
                });

            }
        });

        btnAdd20Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showConfirmDialog(getActivity(), "", "Are you sure to add 20 min?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeInQueue(queueDetail.getQueueId(), queueDetail.getTokenNo(), "Time", 20);
                    }
                });
            }
        });

        btnShiftToNextToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showConfirmDialog(getActivity(), "", "Are you sure to shift to next token?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeInQueue(queueDetail.getQueueId(), queueDetail.getTokenNo(), "Token", 1);
                    }
                });

            }
        });


        btnCancelToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showConfirmDialog(getActivity(), "", "Are you sure to cancel token?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChangeInQueue(queueDetail.getQueueId(), queueDetail.getTokenNo(), "Cancel", 0);
                    }
                });

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                queueChangeDialog.dismiss();
            }
        });

        queueChangeDialog.show();

    }


    private void ActiveQueues() {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                lyLoading.setVisibility(View.VISIBLE);
                containerAddQueue.setVisibility(View.GONE);
                containerQueueDetail.setVisibility(View.GONE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ActiveQueues(Configuration.getUserId());
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                lyLoading.setVisibility(View.GONE);
                boolean isActiveQueue = false;
                try {
                    Type type;
                    Gson gson = new GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        type = new TypeToken<List<QueueDetail>>() {
                        }.getType();

                        List<QueueDetail> queueDetailList = gson.fromJson(jsonObject.getJSONArray("ActiveQueues").toString(), type);

                        if (queueDetailList.size() > 0) {
                            isActiveQueue = true;

                            queueDetail = queueDetailList.get(0);
                            setQueueDetails(queueDetail);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (isActiveQueue) {
                    containerQueueDetail.setVisibility(View.VISIBLE);
                } else {
                    containerAddQueue.setVisibility(View.VISIBLE);
                }

            }
        }.execute();
    }


    private void ChangeInQueue(final String QueueId, final int TokenNo, final String ChangeType, final int ChangeValue) {

        new BackgroundRequest<String, Void, JSONObject>() {

            protected void onPreExecute() {
                dialog = new ProgressDialog(getActivity());
                dialog.setMessage("Please wait...");
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                JSONObject jobject;
                jobject = serviceManager.ChangeInQueue(QueueId, TokenNo, ChangeType, ChangeValue, Configuration.getUserId());
                return jobject;
            }

            protected void onPostExecute(JSONObject jsonObject) {
                dialog.dismiss();

                try {

                    final boolean status = jsonObject.getBoolean("Status");
                    if (status) {
                        queueDetail.setTokenNo(jsonObject.getJSONObject("ChangeInQueue").getInt("TokenNo"));
                        queueDetail.setEstimatedTime(jsonObject.getJSONObject("ChangeInQueue").getString("EstimatedTime"));

                        tvTokenNo.setText(String.valueOf(queueDetail.getTokenNo()));
                        tvEstimatedTime.setText("Estimated time : " + queueDetail.getEstimatedTime());

                        if (ChangeType.equals("Cancel")) {
                            containerAddQueue.setVisibility(View.VISIBLE);
                            containerQueueDetail.setVisibility(View.GONE);
                        }

                        queueChangeDialog.dismiss();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.execute();
    }


    public void btnScanQR_onClick(View view) {
        Intent intent = new Intent(getActivity(), AddQueueActivity.class);
        startActivityForResult(intent, ActivityRequest.REQUEST_ADD_QUEUE);
    }

    private void setQueueDetails(QueueDetail queueDetail) {

        this.queueDetail = queueDetail;
        tvTokenNo.setText(String.valueOf(queueDetail.getTokenNo()));
        tvEstimatedTime.setText("Estimated time : " + queueDetail.getEstimatedTime());
        Glide.with(getActivity())
                .load(queueDetail.getProfileImageURL())
                .centerCrop()
                .placeholder(R.drawable.image_place_loading)
                .into(ivPlaceImage);

        tvPlaceName.setText(queueDetail.getPlaceName());
        tvTags.setText(queueDetail.getCuisines());
        tvPlaceLocation.setText(queueDetail.getPlaceLocation());

        //10dp margin in pixel
        int menuImageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int menuImageSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams menuLayoutParams = new LinearLayout.LayoutParams(menuImageSize, menuImageSize);

        menuLayoutParams.setMargins(0, 0, menuImageMargin, 0);

        final ArrayList<String> menuList = new ArrayList<String>();

        lyMenu.removeAllViews();
        if (queueDetail.getPlaceMenu() != null) {
            for (int i = 0; i < queueDetail.getPlaceMenu().size(); i++) {
                menuList.add(queueDetail.getPlaceMenu().get(i).getUrl());

                ImageView ivMenuImage = new ImageView(getActivity());

                final int pictureIndex = i;
                ivMenuImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
                        intent.putStringArrayListExtra("PictureList", menuList);
                        intent.putExtra("PictureIndex", pictureIndex);
                        startActivity(intent);
                    }
                });

                Glide.with(getActivity())
                        .load(queueDetail.getPlaceMenu().get(i).getUrl())
                        .centerCrop()
                        .placeholder(R.drawable.image_slider_loading)
                        .into(ivMenuImage);

                lyMenu.addView(ivMenuImage, menuLayoutParams);
            }
        }

        containerAddQueue.setVisibility(View.GONE);
        containerQueueDetail.setVisibility(View.VISIBLE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ActivityRequest.REQUEST_ADD_QUEUE && resultCode == getActivity().RESULT_OK) {
            QueueDetail queueDetail = (QueueDetail) data.getSerializableExtra("QueueDetail");
            setQueueDetails(queueDetail);
        } else if (requestCode == ActivityRequest.REQUEST_CHANGE_QUEUE && resultCode == getActivity().RESULT_OK) {

        }
    }

}
