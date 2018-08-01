package com.abstractshop.absdesk.fragments.vendor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.abstractshop.absdesk.R;
import com.abstractshop.absdesk.activity.vendor.VendorInvoiceReportActivity;
import com.abstractshop.absdesk.util.AppController;
import com.abstractshop.absdesk.util.Config;
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

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by ajith on 4/6/2018.
 */

public class VendorInvoiceReportFragment extends Fragment {
    EditText frmDate, toDate;
    private TextInputLayout inputLayoutFromdate, inputLayoutTodate;
    Button submit;
    private SimpleDateFormat dateFormatter;
    SharedPreferences sp, sp1;
    private String Report_Type, Client_ID,Client,SubClient_ID,SubClient,FromDate,ToDate;
    Spinner spinner1, spinner2;
    private ArrayList<String> client;
    private ArrayList<String> clientIds;
    private ArrayList<String> subclient;
    private ArrayList<String> subclientIds;
    private ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vendor_invoice_report, container, false);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        frmDate = (EditText) view.findViewById(R.id.invoicecomven_frm_date);
        toDate = (EditText) view.findViewById(R.id.invoicecomven_to_date);
        inputLayoutFromdate = (TextInputLayout) view.findViewById(R.id.invoicecomtabven_input_layout_frmdate);
        inputLayoutTodate = (TextInputLayout) view.findViewById(R.id.invoicecomtabven_input_layout_todate);
        frmDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.datepicker,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        frmDate.setText(dateFormatter.format(newDate.getTime()));


                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.datepicker, new DatePickerDialog.OnDateSetListener() {


                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        toDate.setText(dateFormatter.format(newDate.getTime()));


                    }


                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();


            }

        });
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
//        sp1 = getActivity().getApplicationContext().getSharedPreferences(
//                "LoginActivity", 0);

        spinner1 = (Spinner) view.findViewById(R.id.invoicevendorcom_clientspinner);
        spinner2 = (Spinner) view.findViewById(R.id.invoicevendorcom_subclientspinner);
        client = new ArrayList<String>();
        clientIds = new ArrayList<>();
        subclient = new ArrayList<String>();
        subclientIds = new ArrayList<>();
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        submit=(Button)view.findViewById(R.id.invoicecom_vensubmittab);
        //Logger.getInstance().Log("venid"+getVendorId());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                pDialog.setMessage("Fetching Data ...");

                // checking internet connection

                FromDate = frmDate.getText().toString().trim();
                ToDate = toDate.getText().toString().trim();

                // find the radiobutton by returned id
                if (!validateFromdate() || !validateTodate())
                {
                    stopDialog();
                    Toasty.error(getActivity(), "Please select Dates!", Toast.LENGTH_SHORT,true).show();
                    return;
                }
                else {
                    Intent intent = new Intent(getActivity(), VendorInvoiceReportActivity.class);

                    intent.putExtra("Vendor_Id", getVendorId());
                    intent.putExtra("Report_Type", "1");
                    intent.putExtra("From_Date", FromDate);
                    intent.putExtra("To_Date", ToDate);
                    intent.putExtra("Client_Id",Client_ID);
                    intent.putExtra("Sub_Client_Id",SubClient_ID);
                    //Logger.getInstance().Log("datein"+ToDate);
                    startActivity(intent);
                    hideDialog();
                }
                // print the value of selected super star

            }
        });
        getClientSpinner();

        return view;

    }
    private void getClientSpinner() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.CLIIENT_SPINNER + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        client.add(details.getString("Client_Name"));
                        clientIds.add(details.getString("Client_Id"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, client));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getSubClientSpinner() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIIENT_SPINNER + Client_ID, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
//                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    subclient.clear();
                    subclientIds.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        subclient.add(details.getString("Sub_Client_Name"));
                        subclientIds.add(details.getString("Sub_Client_Id"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, subclient));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void stopDialog(){

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);}


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private boolean validateFromdate() {
        if (frmDate.getText().toString().trim().isEmpty()) {
            inputLayoutFromdate.setError(getString(R.string.err_msg_frmdate));
            requestFocus(frmDate);
            return false;
        } else {
            inputLayoutFromdate.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateTodate() {
        if (toDate.getText().toString().trim().isEmpty()) {
            inputLayoutTodate.setError(getString(R.string.err_msg_todate));
            requestFocus(toDate);
            return false;
        } else {
            inputLayoutTodate.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    public String getVendorId() {
        return sp.getString("Vendor_Id", "");
    }

    @Override
    public void onResume() {
        super.onResume();
//        statespinner
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Client_ID = clientIds.get(position);
                Client = client.get(position);


                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();




                //editor.putString("State_Name", State_Name);


                getSubClientSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }


        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SubClient_ID = subclientIds.get(position);
                SubClient = subclient.get(position);







            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }


        });
    }
    public void fireApi() {
        pDialog.setMessage("Fetching Data ...");
        showDialog();
        final String FromDate = frmDate.getText().toString().trim();
        final String ToDate = toDate.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.VENDORINVOICEREPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

//                 hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Logger.getInstance().Log("in error response" + response);
                    stopDialog();

                    if(jObj.has("error")){
                        String aJsonString = jObj.getString("error");
                        Toast.makeText(getActivity(), aJsonString, Toast.LENGTH_LONG).show();
                    }
                    // Check for error node in json
                    if (!error) {

                        JSONArray jsonArray = jObj.getJSONArray("Orders");
                        //Logger.getInstance().Log("11" + jsonArray.length());
                        if (jsonArray.length() > 0) {
                            Intent intent = new Intent(getActivity(), VendorInvoiceReportActivity.class);
                            intent.putExtra("JsonReport", response.toString());
                            startActivity(intent);
                            //Logger.getInstance().Log("Report5555" + response.toString());
                            stopDialog();
                        } else {
                            stopDialog();
                            //Logger.getInstance().Log("ghxgfd" + jsonArray.length());
                            TastyToast.makeText(getActivity(), "No orders found", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        }
//                        hideDialog();


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    displayExceptionMessage(e.getMessage());

                }
            }
            public void displayExceptionMessage(String msg)
            {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Vendor_Id", getVendorId());
                params.put("Report_Type", "1");
                params.put("From_Date", FromDate);
                params.put("To_Date", ToDate);
                params.put("Client_Id",Client_ID);
                params.put("Sub_Client_Id",SubClient_ID);
                //Logger.getInstance().Log("params...." +params);

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
}