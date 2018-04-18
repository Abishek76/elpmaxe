package com.drnds.abstractshop.fragments.vendor;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.drnds.abstractshop.R;
import com.drnds.abstractshop.activity.client.CompletedActivity;
import com.drnds.abstractshop.activity.vendor.CompletedVendorActivity;
import com.drnds.abstractshop.activity.vendor.VendorAllReportActivity;
import com.drnds.abstractshop.activity.vendor.VendorPendingReportActivity;
import com.drnds.abstractshop.activity.vendor.VendorReportActivity;
import com.drnds.abstractshop.util.AppController;
import com.drnds.abstractshop.util.Config;
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

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Ajith on 9/23/2017.
 */

public class VendorReportFragment extends Fragment {
    EditText frmDate, toDate;
    RadioGroup radioGroup;
    RadioButton pendingorder, completedorder, allorder;
    private TextInputLayout inputLayoutFromdate, inputLayoutTodate;
    Button pending,complete,all;
    String Report_Type,Client,SubClient_ID,SubClient,FromDate,ToDate;
    public String Client_ID1;
    SharedPreferences sp, sp1;
    private ProgressDialog pDialog;
    private SimpleDateFormat dateFormatter;
    JSONObject jsonObject;
    Spinner spinner1, spinner2;
    private ArrayList<String> client;
    private ArrayList<String> clientIds;
    private ArrayList<String> subclient;
    private ArrayList<String> subclientIds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /**
         *Inflate tab_layout and setup Views.
         */
        View view = inflater.inflate(R.layout.vendor_fragment_report, null);
        radioGroup = (RadioGroup) view.findViewById(R.id.vendor_myradiogroup);
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        frmDate = (EditText) view.findViewById(R.id.vendor_frm_date);
        toDate = (EditText) view.findViewById(R.id.vendor_to_date);
        inputLayoutFromdate = (TextInputLayout) view.findViewById(R.id.vendor_input_layout_frmdate);
        inputLayoutTodate = (TextInputLayout) view.findViewById(R.id.vendor_input_layout_todate);
        sp = getActivity().getApplicationContext().getSharedPreferences(
                "VendorLoginActivity", 0);
        sp1 = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);

        spinner1 = (Spinner) view.findViewById(R.id.vendor_clientspinner);
        spinner2 = (Spinner) view.findViewById(R.id.vendor_subclientspinner);
        client = new ArrayList<String>();
        clientIds = new ArrayList<>();
        subclient = new ArrayList<String>();
        subclientIds = new ArrayList<>();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Client_ID1 = clientIds.get(position);
                Client = client.get(position);
                Logger.getInstance().Log("position"+Client_ID1);



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

        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
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


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.datepicker,new DatePickerDialog.OnDateSetListener() {


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

        getClientSpinner();

        pendingorder = (RadioButton) view.findViewById(R.id.vendor_pendingorder);
        completedorder = (RadioButton) view.findViewById(R.id.vendor_completedorder);
        allorder = (RadioButton) view.findViewById(R.id.vendor_allorder);
//        submit = (Button) view.findViewById(R.id.vendor_submitreport);
        pending = (Button) view.findViewById(R.id.pendingcheck);
        complete = (Button) view.findViewById(R.id.completecheck);
        all= (Button) view.findViewById(R.id.allcheck);


        completedorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                complete.setVisibility(View.VISIBLE);
                pending.setVisibility(View.GONE);
                all.setVisibility(View.GONE);

            }
        });

        pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pending.setVisibility(View.VISIBLE);
                complete.setVisibility(View.GONE);
                all.setVisibility(View.GONE);
            }
        });

        allorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setVisibility(View.VISIBLE);
                pending.setVisibility(View.GONE);
                complete.setVisibility(View.GONE);

            }
        });



//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                checkInternetConnection();   // checking internet connection
//
//                int selectedId = radioGroup.getCheckedRadioButtonId();
//
//                // find the radiobutton by returned id
//                if (!validateFromdate() || !validateTodate()) {
//                    Toasty.error(getActivity(), "Please select Dates!", Toast.LENGTH_SHORT, true).show();
//                    return;
//                } else if (selectedId == R.id.vendor_pendingorder) {
//
//                    Report_Type = "1";
//                    fireReport();
//                } else if (selectedId == R.id.vendor_completedorder) {
//                    Report_Type = "2";
//                    fireReport();
//
//                } else if (selectedId == R.id.vendor_allorder) {
//                    Report_Type = "3";
//                    fireReport();
//
//                }
//
//
//            }
//        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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
                    Intent intent = new Intent(getActivity(), VendorAllReportActivity.class);
                    intent.putExtra("Vendor_Id", getVendorId());
                    intent.putExtra("Report_Type", "3");
                    intent.putExtra("From_Date", FromDate);
                    intent.putExtra("To_Date", ToDate);
                    intent.putExtra("Client_Id",Client_ID1);
                    intent.putExtra("Sub_Client_Id",SubClient_ID);
                    Logger.getInstance().Log("datein"+ToDate);
                    startActivity(intent);
                    hideDialog();
                }
                // print the value of selected super star

            }
        });


        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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
                    Intent intent = new Intent(getActivity(), VendorPendingReportActivity.class);
                    intent.putExtra("Vendor_Id", getVendorId());
                    intent.putExtra("Report_Type", "1");
                    intent.putExtra("From_Date", FromDate);
                    intent.putExtra("To_Date", ToDate);
                    intent.putExtra("Client_Id",Client_ID1);
                    intent.putExtra("Sub_Client_Id",SubClient_ID);
                    Logger.getInstance().Log("datein"+ToDate);
                    startActivity(intent);
                    hideDialog();
                }
                // print the value of selected super star

            }
        });


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
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
                    Intent intent = new Intent(getActivity(), CompletedVendorActivity.class);
                    intent.putExtra("Vendor_Id", getVendorId());
                    intent.putExtra("Report_Type", "2");
                    intent.putExtra("From_Date", FromDate);
                    intent.putExtra("To_Date", ToDate);
                    intent.putExtra("Client_Id",Client_ID1);
                    intent.putExtra("Sub_Client_Id",SubClient_ID);
                    Logger.getInstance().Log("datein"+ToDate);
                    startActivity(intent);
                    hideDialog();
                }
                // print the value of selected super star

            }
        });




        return view;
    }

    private void getClientSpinner() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.CLIIENT_SPINNER + getVendorId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        client.add(details.getString("Client_Name"));
                        clientIds.add(details.getString("Client_Id"));
                        Logger.getInstance().Log("ids1"+clientIds);

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

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIIENT_SPINNER + Client_ID1, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                // Log.e(TAG, response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        subclient.add(jsonObject.getString("Sub_Client_Name"));

                        subclientIds.add(jsonObject.getString("Sub_Client_Id"));
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




    public void fireReport() {
        pDialog.setMessage("Fetching Data ...");
        showDialog();
        final String FromDate = frmDate.getText().toString().trim();
        final String ToDate = toDate.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.VENDORREPORT_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

//                 hideDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    Logger.getInstance().Log("in error response" + response);
                    stopDialog();
                    if (jObj.has("message"))
                    {
                        String aJsonString = jObj.getString("message");
                        TastyToast.makeText(getActivity(), aJsonString, TastyToast.LENGTH_LONG,TastyToast.ERROR);
                    }
                    // Check for error node in json
                    if (!error) {
                        JSONArray jsonArray = jObj.getJSONArray("Orders");
                        Logger.getInstance().Log("11" + jsonArray.length());
                        if (jsonArray.length() > 0) {
                            Intent intent = new Intent(getActivity(), VendorReportActivity.class);
                            intent.putExtra("JsonReport", response.toString());
                            startActivity(intent);
                            Logger.getInstance().Log("Report5555" + response.toString());
                            stopDialog();
                        }


                        else {
                            stopDialog();
                            Logger.getInstance().Log("ghxgfd" + jsonArray.length());
                            TastyToast.makeText(getActivity().getApplicationContext(), "No orders found", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                        }


//                        hideDialog();


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
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
                params.put("Report_Type", Report_Type);
                params.put("From_Date", FromDate);
                params.put("To_Date", ToDate);
                params.put("Client_Id",Client_ID1);
                params.put("Sub_Client_Id",SubClient_ID);
                Logger.getInstance().Log("params...." +params);

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

    public String getVendorId() {
        return sp.getString("Vendor_Id", "");
    }

    private void stopDialog() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                pDialog.dismiss();
            }
        }, 500);
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


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.vendor_frm_date:
                    validateFromdate();
                    break;

                case R.id.vendor_to_date:
                    validateTodate();
                    break;

            }
        }
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    //         check internet connection
    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            TastyToast.makeText(getActivity(), "Check Internet Connection", TastyToast.LENGTH_SHORT, TastyToast.INFO);
            return false;
        }
        return false;
    }


}