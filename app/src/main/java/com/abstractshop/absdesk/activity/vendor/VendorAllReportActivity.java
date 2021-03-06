package com.abstractshop.absdesk.activity.vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.abstractshop.absdesk.R;
import com.abstractshop.absdesk.adapter.vendor.VendorCompletedAdapter;
import com.abstractshop.absdesk.model.vendor.VendorGridItem;
import com.abstractshop.absdesk.util.AppController;
import com.abstractshop.absdesk.util.Config;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class VendorAllReportActivity extends AppCompatActivity {

    private ArrayList<VendorGridItem> vendorgridItemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private VendorCompletedAdapter mAdapter;
    SharedPreferences sp;
    SharedPreferences grid;
    private TextView emptyView;
    private ImageView emptyViewimg;
    LinearLayout linearLayout;

    SharedPreferences.Editor editor;
    private Toolbar toolbar,toolbarh;
    Button select,accept;

    Boolean flag = false;
    ArrayList asss = new ArrayList();
    String jsonStr;

    String vendorID;
    String vId,Report_Type,From_Date,To_Date,Client_Id,Sub_Client_Id;
    JSONArray array = new JSONArray();

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_all_report);
        recyclerView = (RecyclerView) findViewById(R.id.recycle_venall);
        toolbar = (Toolbar) findViewById(R.id.toolbar_vendorcomp);
        toolbarh = (Toolbar) findViewById(R.id.toolbar_hide);
        emptyView = (TextView) findViewById(R.id.empty_view);
        emptyViewimg = (ImageView) findViewById(R.id.empty_view_image);
        pDialog = new ProgressDialog(this,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);




        sp = getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        vendorID = sp.getString("Vendor_Id","");
        //Logger.getInstance().Log("vendor 22"+vendorID);

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Order Details");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        Intent intent = getIntent();
        vId=intent.getStringExtra("Vendor_Id");
        Report_Type=intent.getStringExtra("3");
        From_Date=intent.getStringExtra("From_Date");
        To_Date=intent.getStringExtra("To_Date");
        Client_Id=intent.getStringExtra("Client_Id");
        Sub_Client_Id=intent.getStringExtra("Sub_Client_Id");
        showDialog();

        //Logger.getInstance().Log("date"+To_Date);
        fireEvent();

//        if (vendorgridItemList.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//            emptyView.setVisibility(View.VISIBLE);
//            emptyViewimg.setVisibility(View.VISIBLE);
//
//        }
//        else {
//            recyclerView.setVisibility(View.VISIBLE);
//            emptyView.setVisibility(View.GONE);
//            emptyViewimg.setVisibility(View.GONE);
//
//        }
    }

    public void fireEvent(){
        pDialog.setMessage("Fetching Data ...");
        showDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.VENDORREPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                // Log.e("hiii9", response.toString());
//                 hideDialog();
                stopDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
//                    Log.d(TAG, response.toString());
                    boolean error = jObj.getBoolean("error");

                    //Logger.getInstance().Log("in error response" + response);
                    // Check for error node in json

                    if (jObj.has("message"))
                    {
                        String aJsonString = jObj.getString("message");
                        TastyToast.makeText(getBaseContext(), aJsonString, TastyToast.LENGTH_LONG,TastyToast.ERROR);
                        recyclerView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        emptyViewimg.setVisibility(View.VISIBLE);
                        toolbarh.setVisibility(View.VISIBLE);
                        toolbarh.setTitle("Order Details");
                    }

                    if (!error) {
                        JSONArray jsonArray=jObj.getJSONArray("Orders");
                        int num = 1;
                        if(jsonArray.isNull(0)){
                            stopDialog();
                            TastyToast.makeText( VendorAllReportActivity.this,"No  Orders Found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                            recyclerView.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                            emptyViewimg.setVisibility(View.VISIBLE);
                            toolbar.setVisibility(View.GONE);
                            toolbarh.setVisibility(View.VISIBLE);
                            toolbarh.setTitle("Order Details");
                        }
                        else {
                            num = 5000;

                        }
                        for(int i=0;i<num;i++) {

                            JSONObject details = jsonArray.getJSONObject(i);

                            VendorGridItem vendorgridItem = new VendorGridItem();
                            String subname = details.getString("Sub_Client_Name");
                            vendorgridItem.setSubclient(subname);
                            String orderid = details.getString("Order_Id");

                            vendorgridItem.setOrderId(orderid);

                            String orderno = details.getString("Order_Number");
                            vendorgridItem.setOderno(orderno);
                            String state = details.getString("State");
                            vendorgridItem.setState(state);
                            String county = details.getString("County");
                            vendorgridItem.setCounty(county);
                            String borrowername = details.getString("Barrower_Name");
                            vendorgridItem.setBarrowername(borrowername);
                            String address = details.getString("Property_Address");
                            String orderstatus = details.getString("Order_Status");
                            vendorgridItem.setOrdertask(orderstatus);
                            String producttype = details.getString("Order_Type");
                            vendorgridItem.setProducttype(producttype);
                            vendorgridItem.setPropertyaddress(address);

                            String progressstatus = details.getString("Order_Status");
                            vendorgridItem.setStatus(progressstatus);
                            String order_priority = details.getString("Order_Priority");
                            vendorgridItem.setOrderPriority(order_priority);
                            String Order_Assign_Type = details.getString("Order_Assign_Type");
                            vendorgridItem.setCountytype(Order_Assign_Type);
//

                            vendorgridItemList.add(vendorgridItem);

                            mAdapter = new VendorCompletedAdapter(vendorgridItemList);
                            recyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }

//                        hideDialog();
                        stopDialog();




                    }
                } catch (JSONException e) {
                    hideDialog();
                    e.printStackTrace();
//                    displayExceptionMessage(e.getMessage());

                }
            }
//            public void displayExceptionMessage(String msg)
//            {
//                Toast.makeText(CompletedVendorActivity.this, msg, Toast.LENGTH_SHORT).show();
//            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Vendor_Id", vId);
                params.put("Report_Type", "3");
                params.put("From_Date", From_Date);
                params.put("To_Date", To_Date);
                params.put("Client_Id",Client_Id);
                params.put("Sub_Client_Id",Sub_Client_Id);


                //Logger.getInstance().Log("params " +params);
                return params;
            }
        };



        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

//        System.out.print("on resume"+ selectedOrders);




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Search Orders");
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);

                return true;

            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private String getVendorId() {

        return sp.getString("Vendor_Id", "");
    }

    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}


}

