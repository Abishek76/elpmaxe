package com.abstractshop.absdesk.adapter.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.abstractshop.absdesk.R;
import com.abstractshop.absdesk.activity.client.NavigationActivity;
import com.abstractshop.absdesk.activity.client.orderqueueactivity.EditOrderActivity;

import com.abstractshop.absdesk.model.client.OrderQueue;
import com.abstractshop.absdesk.util.AppController;
import com.abstractshop.absdesk.util.Config;
import com.abstractshop.absdesk.util.CustomRequest;
import com.abstractshop.absdesk.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.abstractshop.absdesk.util.AppController.TAG;

/**
 * Created by ajithkumar on 6/28/2017.
 */
public class RecyclerOrderQueueAdapter extends RecyclerView.Adapter<RecyclerOrderQueueAdapter.MyViewHolder>implements Filterable {

    private ArrayList<OrderQueue> orderQueueList;
    private ArrayList<OrderQueue> mFilteredList;
    private Context ctx;
    private Activity activity;
    SharedPreferences sp;
    public static final String ORDERQUEUE = "orderadpter";
    private ProgressDialog pDialog;
    String orderid;



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView subclient,oderno,propertyaddress,producttype,state,county,status,barrowername,date;
        private List<OrderQueue> orderQueueList=new ArrayList<OrderQueue>();
        private  ItemClickListener itemClickListener;
        public ImageView cardmenu;
        //        private Button button;
        Context ctx;

        public MyViewHolder(View view, final Context ctx,final List<OrderQueue> orderQueueList) {
            super(view);
            this.ctx=ctx;

            this.orderQueueList=orderQueueList;
            subclient = (TextView) view.findViewById(R.id.text_subclient);
            oderno = (TextView) view.findViewById(R.id.text_orderno);
            producttype = (TextView) view.findViewById(R.id.producttype);
            state = (TextView) view.findViewById(R.id.statename);
            county = (TextView) view.findViewById(R.id.countyname);
            propertyaddress = (TextView) view.findViewById(R.id.property_address);
            status = (TextView) view.findViewById(R.id.status);
            barrowername = (TextView) view.findViewById(R.id.borrowername);
            date=(TextView) view.findViewById(R.id.text_date);
            cardmenu=(ImageView)view.findViewById(R.id.cardmenu);

            subclient.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            oderno.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            producttype.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            state.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            county.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            propertyaddress.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            status.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            barrowername.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
            view.setOnClickListener(this);


//            view.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position=getAdapterPosition();
//                    OrderQueue orderqueue=orderQueueList.get(position);
//                    orderid=orderqueue.getOrder_Id();
//                    showPopupMenu(cardmenu,getAdapterPosition());
//                    //Logger.getInstance().Log("selected order id is : " + orderqueue.getOrder_Id());
//
//                    return true;
//                }
//            });

            cardmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    OrderQueue orderqueue=orderQueueList.get(position);
                    orderid=orderqueue.getOrder_Id();
                    showPopupMenu(cardmenu,getAdapterPosition());
//                    Logger.getInstance().Log("selected order id is : " + orderqueue.getOrder_Id());

                }
            });


        }


        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener=ic;
        }
    }


    public RecyclerOrderQueueAdapter(ArrayList<OrderQueue>orderQueueList,Context context) {
        this. orderQueueList=   orderQueueList;
        this. mFilteredList=   orderQueueList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        this.ctx=context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderqueue_row, parent, false);
        ctx = parent.getContext();

        return new MyViewHolder(itemView,ctx,orderQueueList);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.subclient.setText(mFilteredList.get(position).getSubclient());
        holder.oderno.setText(mFilteredList.get(position).getOderno());
        holder.producttype.setText(mFilteredList.get(position).getProducttype());
        holder.propertyaddress.setText(mFilteredList.get(position).getPropertyaddress());
        holder.date.setText(orderQueueList.get(position).getDate());
        holder.state.setText(mFilteredList.get(position).getState());
        holder.county.setText(mFilteredList.get(position).getCounty());
        holder.barrowername.setText(mFilteredList.get(position).getBarrowername());
        holder.status.setText(mFilteredList.get(position).getStatus());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                OrderQueue orderQueue = mFilteredList.get(pos);
                Context context = v.getContext();
                Intent intent=new Intent(context, EditOrderActivity.class);
                sp = context.getSharedPreferences(ORDERQUEUE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();

                editor.putString("Order_Id",orderQueue.getOrder_Id());
                editor.putString("Sub_Client_Name",orderQueue.getSubclient());
//                Logger.getInstance().Log("selected subclient is : " + orderQueue.getSubclient());
//                Logger.getInstance().Log("selected orderqueue is : " + orderQueue.getOderno());

                editor.putString("Order_Number",orderQueue.getOderno());
                editor.putString("State",orderQueue.getState());
                editor.putString("County",orderQueue.getCounty());
                editor.putString("Order_Priority",orderQueue.getOrderPriority());
                editor.putString("Product_Type",orderQueue.getProducttype());
                editor.putString("Order_Status",orderQueue.getOrdertask());
                editor.putString("Order_Assign_Type",orderQueue.getCountytype());
                editor.putString("Progress_Status",orderQueue.getStatus());
                editor.putString("Barrower_Name",orderQueue.getBarrowername());
                editor.putString("Sub_Client_Id",orderQueue.getSubId());
//            intent.putExtra("Order_Id",orderQueue.getOrder_Id());
                editor.commit();

                context.startActivity(intent);
            }
        });

//        holder.cardmenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopupMenu(holder.cardmenu,position);
//            }
//        });

    }

    private void showPopupMenu(View view,int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(view.getContext(),view );
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.card_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }


    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private int position;
        public MyMenuItemClickListener(int positon) {
            this.position=positon;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {

                case R.id.clientmenu1:
                    hold(5);
                    return true;
                case R.id.clientmenu2:
                    cancelled(4);
                    return true;
                case R.id.clientmenu3:
                    clarification(1);
                    return true;
                case R.id.clientmenu4:
                    workinprogress(1);
                    return true;
                default:
            }
            return false;
        }
    }

    private void hold(final int position){
//        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
//                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "5");
//                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }




    private void cancelled(final int position) {
//        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
//                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "4");
//                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private void clarification(final int position) {
//        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();

                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
//                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "1");
//                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }

    private void workinprogress(final int position){
//        Logger.getInstance().Log("in update client id");
        showDialog();

        pDialog.setMessage("Updating ...");

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.ORDER_STATUS_CHANGE , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean  error = response.getBoolean("error");
//                    boolean  duplicate = response.getBoolean("duplicate");
                    if (!error){
                        Toasty.success(ctx, "Order Updated Successfully", Toast.LENGTH_SHORT, true).show();
                        Intent intent= new Intent(ctx, NavigationActivity.class);
                        ctx.startActivity(intent);
                        hideDialog();
                    }else{
                        Toasty.success(ctx,"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideDialog();
                // Check for error node in json
//                Log.d(TAG, response.toString());


            }



        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Order_Id", orderid);
                params.put("Order_Progress_Id", "14");
//                Logger.getInstance().Log("parabola: " + params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }


    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = orderQueueList;
                } else {

                    ArrayList<OrderQueue> filteredList = new ArrayList<>();

                    for (OrderQueue orderQueue : orderQueueList) {

                        if (orderQueue.getSubclient().toLowerCase().contains(charString) ||
                                orderQueue.getOderno().toLowerCase().contains(charString) ||
                                orderQueue.getProducttype().toLowerCase().contains(charString)||
                                orderQueue.getPropertyaddress().toLowerCase().contains(charString)||
                                orderQueue.getState().toLowerCase().contains(charString)||
                                orderQueue.getCounty().toLowerCase().contains(charString)||
                                orderQueue.getSubclient().toLowerCase().contains(charString)||
                                orderQueue.getBarrowername().toLowerCase().contains(charString)||
                                orderQueue.getStatus().toLowerCase().contains(charString))
                        {

                            filteredList.add(orderQueue);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<OrderQueue>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public interface ItemClickListener {

        void onItemClick(View v,int pos);


    }


    @Override
    public long getItemId(int position)
    { return position; }


    @Override
    public int getItemViewType(int position)
    { return position; }





}