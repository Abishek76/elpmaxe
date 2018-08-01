package com.abstractshop.absdesk.adapter.vendor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.abstractshop.absdesk.R;
import com.abstractshop.absdesk.activity.vendor.gridactivity.EditGridViewVendorActivity;
import com.abstractshop.absdesk.model.vendor.VendorSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajith on 10/25/2017.
 */
public class VendorResultRecyclerviewAdapter extends RecyclerView.Adapter<VendorResultRecyclerviewAdapter.MyViewHolder>  {
    private List<VendorSearch> searchList;
    private Activity activity;
    private Context ctx;

    SharedPreferences sp;
    public static final String VENDORGRID = "vendorgridadpter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
        private List<VendorSearch> searchList=new ArrayList<VendorSearch>();
        private ItemClickListener itemClickListener;

        Context ctx;
        public MyViewHolder(View view, Context ctx, List<VendorSearch> searchList) {
            super(view);
            this.searchList=searchList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.vendor_result_subclient);
            oderno = (TextView) view.findViewById(R.id.vendor_result_orderno);
            producttype = (TextView) view.findViewById(R.id.vendor_result_producttype);
            state = (TextView) view.findViewById(R.id.vendor_result_state);
            county = (TextView) view.findViewById(R.id.vendor_result_county);
            propertyaddress = (TextView) view.findViewById(R.id.vendor_result_address);
            status = (TextView) view.findViewById(R.id.vendor_result_status);
            borrowername = (TextView) view.findViewById(R.id.vendor_result_borrowername);


            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            oderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            county.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            borrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

    }


    public VendorResultRecyclerviewAdapter(List<VendorSearch>searchList) {
        this. searchList=   searchList;
        this.ctx=ctx;
    }

    @Override
    public VendorResultRecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_search_row, parent, false);

        return new VendorResultRecyclerviewAdapter.MyViewHolder(itemView,ctx,searchList);
    }

    @Override
    public void onBindViewHolder(VendorResultRecyclerviewAdapter.MyViewHolder holder, int position) {
        VendorSearch report = searchList.get(position);

        holder.subclient.setText(  report .getSubclient());
        //Logger.getInstance().Log("2323232"+report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getBarrowername());


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                VendorSearch report = searchList.get(pos);
                Context context = v.getContext();
                Intent intent = new Intent(context, EditGridViewVendorActivity.class);
                sp = context.getApplicationContext().getSharedPreferences(
                        "vendorgridadpter", 0);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id", report.getOrder_Id());
                //Logger.getInstance().Log("orderidadapter " + vendorgridItem.getOrderId());
                editor.putString("Sub_Client_Name", report.getSubclient());
                editor.putString("Order_Type", report.getProducttype());
                editor.putString("State_Name", report.getState());
                editor.putString("County_Name", report.getCounty());
                editor.putString("State", report.getStateid());
                editor.putString("Order_Priority", report.getOrderPriority());
                editor.putString("Order_Assign_Type", report.getCountytype());
                editor.putString("Progress_Status", report.getStatus());
                editor.putString("Barrower_Name", report.getBarrowername());
                editor.putString("Order_Number", report.getOderno());
                editor.putString("Order_Status", report.getOrdertask());
                //Logger.getInstance().Log("Order_Status of " + vendorgridItem.getOrdertask());

                editor.putString("Order_Status", report.getOrdertask());

                editor.commit();
                //Logger.getInstance().Log("status888 " + vendorgridItem.getStatus());
                context.startActivity(intent);
            }
        });



    }
    @Override
    public int getItemCount() {

        return  searchList.size();
    }

    public interface ItemClickListener {

        void onItemClick(View v, int pos);


    }

}

