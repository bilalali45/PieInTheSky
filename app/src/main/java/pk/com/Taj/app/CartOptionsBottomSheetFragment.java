package pk.com.Taj.app;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pk.com.Taj.app.widget.IconTextView;

public class CartOptionsBottomSheetFragment extends BottomSheetDialogFragment {

    private IconTextView tvClose;
    private OnOptionItemClickListener onOptionItemClickListener;
    private TextView tvRemoveOrder;
    private TextView tvCancel;

    public void setOnItemClickListener(OnOptionItemClickListener listener) {
        this.onOptionItemClickListener = listener;
    }

    public CartOptionsBottomSheetFragment() {
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
        View view = inflater.inflate(R.layout.fragment_cart_options_dialog, container, false);

        initViews(view);

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tvRemoveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionItemClickListener.onRemoveOrder(view);
                dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initViews(View view) {
        tvClose = view.findViewById(R.id.tvClose);
        tvRemoveOrder = view.findViewById(R.id.tvRemoveOrder);
        tvCancel = view.findViewById(R.id.tvCancel);
    }

    public interface OnOptionItemClickListener {
        public void onRemoveOrder(View view);
    }

}
