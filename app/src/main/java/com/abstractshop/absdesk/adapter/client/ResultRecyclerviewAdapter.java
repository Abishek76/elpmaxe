package com.abstractshop.absdesk.adapter.client;

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
import com.abstractshop.absdesk.activity.client.gridactivity.EditGridViewActivity;
import com.abstractshop.absdesk.model.client.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajith on 9/14/2017.
 */

public class ResultRecyclerviewAdapter extends RecyclerView.Adapter<ResultRecyclerviewAdapter.MyViewHolder>  {
    private List<Search> searchList;
    private Activity activity;
    private Context ctx;
    private int lastPosition = -1;
    SharedPreferences sharedpreferences;
    public static final String GRID = "gridadpter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView subclient,oderno,propertyaddress,producttype,state,county,status,borrowername;
        private List<Search> searchList=new ArrayList<Search>();
        private ResultRecyclerviewAdapter.ItemClickListener itemClickListener;
        Context ctx;

        public MyViewHolder(View view, Context ctx, List<Search> searchList) {
            super(view);
            this.searchList=searchList;
            this.ctx=ctx;

            subclient = (TextView) view.findViewById(R.id.result_subclient);
            oderno = (TextView) view.findViewById(R.id.result_orderno);
            producttype = (TextView) view.findViewById(R.id.result_producttype);
            state = (TextView) view.findViewById(R.id.result_state);
            county = (TextView) view.findViewById(R.id.result_county);
            propertyaddress = (TextView) view.findViewById(R.id.result_address);
            status = (TextView) view.findViewById(R.id.result_status);
            borrowername = (TextView) view.findViewById(R.id.result_borrowername);


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
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }

        public void setItemClickListener(ResultRecyclerviewAdapter.ItemClickListener ic) {
            this.itemClickListener=ic;
        }
//        public void setItemClickListener(ResultRecyclerviewAdapter.ItemClickListener ic)
//        {
//            this.itemClickListener=ic;
//        }


    }
    public ResultRecyclerviewAdapter(List<Search>searchList) {
        this. searchList=   searchList;
        this.ctx=ctx;
    }

    @Override
    public ResultRecyclerviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);

        return new ResultRecyclerviewAdapter.MyViewHolder(itemView,ctx,searchList);
    }

    @Override
    public void onBindViewHolder(ResultRecyclerviewAdapter.MyViewHolder holder, int position) {
        Search report = searchList.get(position);
        holder.subclient.setText(  report .getSubclient());
        holder.oderno.setText(  report .getOderno());
        holder.producttype.setText(  report .getProducttype());
        holder.propertyaddress.setText(  report.getPropertyaddress());
        holder.state.setText(  report .getState());
        holder.county.setText(  report .getCounty());
        holder.status.setText(  report .getStatus());
        holder.borrowername.setText(  report .getBarrowername());
        //Logger.getInstance().Log("2323232"+report .getBarrowername());

//        setAnimation(holder.itemView, position);


        holder.setItemClickListener(new ResultRecyclerviewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Search search = searchList.get(pos);
                Context context = v.getContext();
                Intent intent=new Intent(context, EditGridViewActivity.class);

                sharedpreferences = context.getSharedPreferences(GRID, Context.MODE_PRIVATE);
                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedpreferences.edit();

                //Adding values to editor

                editor.putString("Order_Id",search.getOrder_Id());
                editor.putString("Sub_Client_Name", search.getSubclient());
                editor.putString("Product_Type", search.getProducttype());
                editor.putString("State", search.getState());
                editor.putString("County", search.getCounty());
                editor.putString("Order_Priority", search.getOrderpriority());
                editor.putString("Order_Assign_Type", search.getCountytype());
                editor.putString("Progress_Status", search.getStatus());
                editor.putString("Barrower_Name", search.getBarrowername());
                editor.putString("Order_Status", search.getOrdertask());
                editor.putString("Sub_Client_Id", search.getSubId());
                //Logger.getInstance().Log("subid22 " + gridItem.getSubId());
                editor.commit();
                context.startActivity(intent);
//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());

            }
        });


    }
    @Override
    public int getItemCount() {

        return  searchList.size();
    }

    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }
//    private void setAnimation(View viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        if (position > lastPosition) {
//            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.ABSOLUTE, 0.3f, Animation.ABSOLUTE, 0.3f);
//            anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
//            viewToAnimate.startAnimation(anim);
//            lastPosition = position;
//        }
//    }
}
