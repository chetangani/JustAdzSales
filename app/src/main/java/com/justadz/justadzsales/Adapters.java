package com.justadz.justadzsales;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 25-May-16.
 */
public class Adapters {

    public class ConfirmedAddressAdapter extends RecyclerView.Adapter<ConfirmedAddressAdapter.AddressViewHolder> {

        ArrayList<MeetingAddresses> arrayList = new ArrayList<MeetingAddresses>();
        Context context;
        int expandedPosition = -1, checkposition = -1;

        public ConfirmedAddressAdapter(ArrayList<MeetingAddresses> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_address_list, parent, false);
            AddressViewHolder viewHolder = new AddressViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AddressViewHolder holder, int position) {
            final MeetingAddresses address = arrayList.get(position);
            holder.tvcustomername.setText(address.getCustomerName());
            holder.tvcustomermobile.setText(address.getCustomerMobile());
            holder.tvcustomeraddress.setText(address.getCustomerAddress());
            holder.tvmeetingdate.setText(address.getMeetingDate());
            holder.tvmeetingtime.setText(address.getMeetingTime());

            if (position == expandedPosition) {
                holder.confirm_layout.setVisibility(View.VISIBLE);
            } else {
                holder.confirm_layout.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvcustomername, tvcustomermobile, tvcustomeraddress, tvmeetingdate, tvmeetingtime;
            LinearLayout confirm_layout;

            public AddressViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tvcustomername = (TextView) itemView.findViewById(R.id.customer_name);
                tvcustomermobile = (TextView) itemView.findViewById(R.id.customer_mobile);
                tvcustomeraddress = (TextView) itemView.findViewById(R.id.customer_address);
                tvmeetingdate = (TextView) itemView.findViewById(R.id.meeting_date);
                tvmeetingtime = (TextView) itemView.findViewById(R.id.meeting_time);
                confirm_layout = (LinearLayout) itemView.findViewById(R.id.confirmation_box);
            }

            @Override
            public void onClick(View v) {
                // Check for an expanded view, collapse if you find one
                if (expandedPosition >= 0) {
                    int prev = expandedPosition;
                    notifyItemChanged(prev);
                }
                // Set the current position to "expanded"
                expandedPosition = getAdapterPosition();
                if (checkposition == expandedPosition) {
                    expandedPosition = -1;
                    checkposition = expandedPosition;
                    notifyItemChanged(expandedPosition);
                } else {
                    checkposition = expandedPosition;
                    notifyItemChanged(checkposition);
                }
            }
        }
    }

    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

        ArrayList<MeetingAddresses> arrayList = new ArrayList<MeetingAddresses>();
        Context context;

        public AddressAdapter(ArrayList<MeetingAddresses> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @Override
        public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.meetingaddress_card, parent, false);
            AddressViewHolder viewHolder = new AddressViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(AddressViewHolder holder, int position) {
            final MeetingAddresses address = arrayList.get(position);
            holder.tvcustomername.setText(address.getCustomerName());
            holder.tvcustomermobile.setText(address.getCustomerMobile());
            holder.tvcustomeraddress.setText(address.getCustomerAddress());
            holder.tvmeetingdate.setText(address.getMeetingDate());
            holder.tvmeetingtime.setText(address.getMeetingTime());
            holder.chkbox.setChecked(address.isSelected());
            holder.chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    address.setSelected(isChecked);
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tvcustomername, tvcustomermobile, tvcustomeraddress, tvmeetingdate, tvmeetingtime;
            CheckBox chkbox;

            public AddressViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                tvcustomername = (TextView) itemView.findViewById(R.id.customer_name);
                tvcustomermobile = (TextView) itemView.findViewById(R.id.customer_mobile);
                tvcustomeraddress = (TextView) itemView.findViewById(R.id.customer_address);
                tvmeetingdate = (TextView) itemView.findViewById(R.id.meeting_date);
                tvmeetingtime = (TextView) itemView.findViewById(R.id.meeting_time);
                chkbox = (CheckBox) itemView.findViewById(R.id.chkSelected);
            }

            @Override
            public void onClick(View v) {

            }
        }

        public ArrayList<MeetingAddresses> getMeetingid() {
            return arrayList;
        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;
        private int mOrientation;

        /**
         * Sole constructor. Takes in a {@link Drawable} to be used as the interior
         * divider.
         *
         * @param divider A divider {@code Drawable} to be drawn on the RecyclerView
         */
        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }

        /**
         * Draws horizontal or vertical dividers onto the parent RecyclerView.
         *
         * @param canvas The {@link Canvas} onto which dividers will be drawn
         * @param parent The RecyclerView onto which dividers are being added
         * @param state The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                drawHorizontalDividers(canvas, parent);
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                drawVerticalDividers(canvas, parent);
            }
        }

        /**
         * Determines the size and location of offsets between items in the parent
         * RecyclerView.
         *
         * @param outRect The {@link Rect} of offsets to be added around the child
         *                view
         * @param view The child view to be decorated with an offset
         * @param parent The RecyclerView onto which dividers are being added
         * @param state The current RecyclerView.State of the RecyclerView
         */
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            mOrientation = ((LinearLayoutManager) parent.getLayoutManager()).getOrientation();
            if (mOrientation == LinearLayoutManager.HORIZONTAL) {
                outRect.left = mDivider.getIntrinsicWidth();
            } else if (mOrientation == LinearLayoutManager.VERTICAL) {
                outRect.top = mDivider.getIntrinsicHeight();
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented horizontally.
         *
         * @param canvas The {@link Canvas} onto which horizontal dividers will be
         *               drawn
         * @param parent The RecyclerView onto which horizontal dividers are being
         *               added
         */
        private void drawHorizontalDividers(Canvas canvas, RecyclerView parent) {
            int parentTop = parent.getPaddingTop();
            int parentBottom = parent.getHeight() - parent.getPaddingBottom();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentLeft = child.getRight() + params.rightMargin;
                int parentRight = parentLeft + mDivider.getIntrinsicWidth();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }

        /**
         * Adds dividers to a RecyclerView with a LinearLayoutManager or its
         * subclass oriented vertically.
         *
         * @param canvas The {@link Canvas} onto which vertical dividers will be
         *               drawn
         * @param parent The RecyclerView onto which vertical dividers are being
         *               added
         */
        private void drawVerticalDividers(Canvas canvas, RecyclerView parent) {
            int parentLeft = parent.getPaddingLeft();
            int parentRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int parentTop = child.getBottom() + params.bottomMargin;
                int parentBottom = parentTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(parentLeft, parentTop, parentRight, parentBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
