package com.drnds.abstractshop.activity.client.orderqueueactivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.drnds.abstractshop.R;
import com.drnds.abstractshop.util.AppController;
import com.drnds.abstractshop.util.Config;
import com.drnds.abstractshop.util.CustomRequest;
import com.drnds.abstractshop.util.Logger;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import es.dmoral.toasty.Toasty;

import static com.drnds.abstractshop.R.id.toolbar;
import static com.drnds.abstractshop.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

public class InvoiceActivity extends AppCompatActivity {

    private EditText inputsearchcost, inputcopycost, inputnoofpages, inputinvoicedate;
    private TextInputLayout inputlayoutsearchcost,inputlayoutcopycost;
    Button submit, preview,invoiceclient;
    SharedPreferences sp, pref;
    private String Order_Id;
    private ProgressDialog pDialog;
    private SimpleDateFormat dateFormatter;
    private int mYear, mMonth, mDay;
    private TextView inputordercost;
    double searchcost,copycost,ordercost;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Invoice");


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        inputlayoutcopycost=(TextInputLayout)findViewById(R.id.input_layout_copycost);
        inputlayoutsearchcost=(TextInputLayout)findViewById(R.id.input_layout_searchcost);
        inputordercost = (TextView) findViewById(R.id.input_ordercost);
        inputsearchcost = (EditText) findViewById(R.id.input_searchcost);
        inputcopycost = (EditText) findViewById(R.id.input_copycost);
        inputnoofpages = (EditText) findViewById(R.id.input_noofpages);
        inputinvoicedate = (EditText) findViewById(R.id.input_invoicedate);

//        submit = (Button) findViewById(R.id.button_orderinvoicesubmit);
        invoiceclient = (Button) findViewById(R.id.pathva);
        preview = (Button) findViewById(R.id.button_orderinvoicepreview);

        inputcopycost.setEnabled(false);
        inputsearchcost.setEnabled(false);
        inputinvoicedate.setEnabled(false);
        inputnoofpages.setEnabled(false);

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        checkInternetConnection();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        showDialog();

        sp = getApplicationContext().getSharedPreferences(
                ORDERQUEUE, 0);

        Order_Id = sp.getString("Order_Id", "");



        getOrderdetails();
        getPreviewdeatails();



        preview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v){
                checkInternetConnection();   // checking internet connection
                if (!validatesearchcost()) {
                    return;
                }

                if (!validatecopycost()) {
                    return;
                }

                else{
//                    Uri uri = Uri.parse(invoiceclient.getText().toString());
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
                    Intent intent = new Intent(InvoiceActivity.this, PdfActivity.class);
                    intent.putExtra("Pdfpath", invoiceclient.getText().toString());
                    startActivity(intent);
                    Logger.getInstance().Log("Id .... is"+invoiceclient.getText().toString());
                }}

        });



//        submit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                checkInternetConnection();   // checking internet connection
//
//
//                if (!validatesearchcost()) {
//                    return;
//                }
//
//                if (!validatecopycost()) {
//                    return;
//                }
//
//                else {
//
//                    searchcost = Double.parseDouble(inputsearchcost.getText().toString());
//                    copycost = Double.parseDouble(inputcopycost.getText().toString());
//                    ordercost = searchcost + copycost;
//                    inputordercost.setText(Double.toString(ordercost));
//
//                    Submit();
//
//                }
//
//
//
//            }
//        });

        inputordercost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchcost = Double.parseDouble(inputsearchcost.getText().toString());
                copycost = Double.parseDouble(inputcopycost.getText().toString());
                ordercost = searchcost + copycost;
                inputordercost.setText(Double.toString(ordercost));

            }
        });

//        clk.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                searchcost = Double.parseDouble(inputsearchcost.getText().toString());
//                copycost = Double.parseDouble(inputcopycost.getText().toString());
//                ordercost = searchcost + copycost;
//                inputordercost.setText(Double.toString(ordercost));
//            }
//        });


        inputinvoicedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();



                DatePickerDialog datePickerDialog = new DatePickerDialog(InvoiceActivity.this,R.style.datepicker,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        inputinvoicedate.setText(dateFormatter.format(newDate.getTime()));


                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });
        return ;


    }



    public String getorderID() {


        return sp.getString("Order_Id", "");
    }


    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }

    public String getClientUserId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_User_Id", "");


    }

    public String getSubprocessId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Sub_Client_Id", "");


    }






    public void  getOrderdetails() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.INVOICE_URL +getorderID(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                     Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Invoice_Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Order_Cost=details.getString("Order_Cost");
                        inputordercost.setText(Order_Cost);
                        String Search_Cost=details.getString("Search_Cost");
                        inputsearchcost.setText(Search_Cost);
                        String Copy_Cost=details.getString("Copy_Cost");
                        inputcopycost.setText(Copy_Cost);
                        String No_Of_Pages=details.getString("No_Of_Pages");
                        inputnoofpages.setText(No_Of_Pages);
                        String Invoice_Date=details.getString("Invoice_Date");
                        inputinvoicedate.setText(Invoice_Date);
                        //Logger.getInstance().Log("set Order cost " + Order_Cost);

                        hideDialog();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }



    public void  getPreviewdeatails() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.INVOIC_PREVIEW_URL +getorderID(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Invoice_Details");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Document_Path=details.getString("Document_Path");
//                        preview.setText(Document_Path);
                        invoiceclient.setText(Document_Path);
                        Logger.getInstance().Log("invoice path " + Document_Path);
                        hideDialog();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }




    public void  Submit(){
        showDialog();
        final String Order_Cost = inputordercost.getText().toString().trim();
        final String  Search_Cost = inputsearchcost.getText().toString().trim();
        final String  Copy_Cost = inputcopycost.getText().toString().trim();
        final String  No_Of_Pages = inputnoofpages.getText().toString().trim();
        final String  Invoice_Date = inputinvoicedate.getText().toString().trim();

        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.INVOICE_EDIT_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();

                //Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");

                    //Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error)
                    {
                        Toasty.success(InvoiceActivity.this.getApplicationContext(),"Updated  Sucessfully...", Toast.LENGTH_SHORT).show();
                        hideDialog();

//                        onBackPressed();
                    }else {
                        Toasty.success(InvoiceActivity.this.getApplicationContext(),"Not updated...",Toast.LENGTH_SHORT).show();
                        hideDialog();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hideDialog();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Order_Id",getorderID());
                params.put("Clinet_Id",getClientId());
                //Logger.getInstance().Log("Id .... is"+getClientId());
                params.put("Order_Cost",Order_Cost);
                params.put("Search_Cost",Search_Cost);
                params.put("Copy_Cost",Copy_Cost);
                params.put("No_Of_Pages",No_Of_Pages);
                params.put("Invoice_Date",Invoice_Date);
                params.put("Client_User_Id",getClientUserId());
                params.put("Subprocess_ID",getSubprocessId());
                //Logger.getInstance().Log("Id .... is"+getorderID());
                //Logger.getInstance().Log("Id .... is"+getClientId());
                //Logger.getInstance().Log("Id .... is"+getClientUserId());
                //Logger.getInstance().Log("Id .... is"+getSubprocessId());




                return params;
            }


        };

        AppController.getInstance().addToRequestQueue(customRequest);
    }


    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED  ) {
            TastyToast.makeText( InvoiceActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
            return false;
        }
        return false;
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    private boolean validatesearchcost() {
        String searchcost = inputsearchcost.getText().toString().trim();

        if (searchcost.isEmpty() ) {
            inputlayoutsearchcost.setError(getString(R.string.err_msg_searchcost));
            requestFocus(inputsearchcost);
            return false;
        } else {
            inputlayoutsearchcost.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatecopycost() {
        String copycost = inputcopycost.getText().toString().trim();

        if (copycost.isEmpty() ) {
            inputlayoutcopycost.setError(getString(R.string.err_msg_copycost));
            requestFocus(inputcopycost);
            return false;
        } else {
            inputlayoutcopycost.setErrorEnabled(false);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Logger.getInstance().Log("calling");
        toolbar.setVisibility(View.VISIBLE);

//        toolbar.setVisibility(View.VISIBLE);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}

