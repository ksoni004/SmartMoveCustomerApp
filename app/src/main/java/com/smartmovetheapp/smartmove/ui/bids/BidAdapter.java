package com.smartmovetheapp.smartmove.ui.bids;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smartmovetheapp.smartmove.R;
import com.smartmovetheapp.smartmove.data.remote.model.OrderBid;
import com.smartmovetheapp.smartmove.util.CalenderUtil;

import java.text.DecimalFormat;
import java.util.Locale;

public class BidAdapter extends ListAdapter<OrderBid, BidAdapter.ViewHolder> {

    private static final DiffUtil.ItemCallback<OrderBid> DIFF_CALLBACK = new DiffUtil.ItemCallback<OrderBid>() {
        @Override
        public boolean areItemsTheSame(OrderBid oldItem, OrderBid newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(OrderBid oldItem, OrderBid newItem) {
            return false;
        }
    };

    private ActionListener actionListener;

    protected BidAdapter(ActionListener actionListener) {
        super(DIFF_CALLBACK);
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_bid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtAmount;
        private TextView txtDetails;
        private TextView txtDateTime;
        private TextView txtStatus;
        private TextView txtDriverName;
        private TextView txtAverageRating;
        private CardView cvSelect;

        public ViewHolder(View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txt_pickup_dest);
            txtDetails = itemView.findViewById(R.id.txt_drop_dest);
            txtDateTime = itemView.findViewById(R.id.txt_date_time);
            txtStatus = itemView.findViewById(R.id.txt_status);
            txtDriverName = itemView.findViewById(R.id.txt_driver_name);
            txtAverageRating = itemView.findViewById(R.id.txt_average_rating);
            cvSelect = itemView.findViewById(R.id.cv_select_button);

            cvSelect.setOnClickListener(button -> {
                OrderBid orderBid = (OrderBid) button.getTag();
                actionListener.onOrderBidSelectClick(orderBid);
            });
        }

        public void bindTo(OrderBid orderBid) {
            cvSelect.setTag(orderBid);

            if (orderBid.getBidStatus().equals("ACCEPTED")) {
                cvSelect.setVisibility(View.GONE);
            } else {
                cvSelect.setVisibility(View.VISIBLE);
            }

            DecimalFormat df2 = new DecimalFormat(".##");
            txtAmount.setText("Bid Amount\u0009\u0009: $" + df2.format(orderBid.getBidAmount()));
            txtDetails.setText("Require: " + orderBid.getNumberOfTrips() + " trips for " + orderBid.getNumberOfHours() + "Hrs");
            txtDateTime.setText("Delivery Time\u0009: " + CalenderUtil.getDisplayDateTime(orderBid.getTime()));
            txtStatus.setText(orderBid.getBidStatus());
            txtDriverName.setText("Driver Name\u0009\u0009: " + orderBid.getDriverName());
            txtAverageRating.setText("Average Rating\u0009: " + orderBid.getAverageRating());
        }
    }

    public interface ActionListener {
        void onOrderBidSelectClick(OrderBid orderBid);
    }
}
