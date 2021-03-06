package com.abstractshop.absdesk.activity.client.orderqueueactivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.abstractshop.absdesk.R;
import com.abstractshop.absdesk.activity.client.NavigationActivity;
import com.abstractshop.absdesk.util.AppController;
import com.abstractshop.absdesk.util.Config;
import com.abstractshop.absdesk.util.CustomRequest;
import com.abstractshop.absdesk.util.Logger;
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

import static com.abstractshop.absdesk.adapter.client.RecyclerOrderQueueAdapter.ORDERQUEUE;

public class EditOrderActivity extends AppCompatActivity {
    Spinner spinner1,spinner2,spinner3,spinner4,spinner5,spinner6,spinner7,spinner8;
    private EditText inputOrdernum, inputCity, inputZip, inputAddress, inputBorrowername, inputApn, inputDate,inputcustomername,inputtest;
    Button submit,upload,invoice;
    private TextInputLayout inputLayoutborrowername,inputLayoutordernum,inputLayoutapn,inputtestlayout;
    private String clientname,stringstate,stringcounty,stringorderpriority,stringproducttype,stringordertask,stringcountytype,stringstatus;
    private String Order_Id,Sub_Client_Name,state,producttype,Order_Assign_Type,Order_Task,countyname,statusname;
    private Toolbar toolbar;
    private String State,State_ID,County,County_ID,Order_Type_ID,Order_Type,Order_Status_ID,Order_Status,Order_Progress_Id,Progress_Status,
            Order_Priority_Id,Order_Priority,Sub_Client_Id,Order_Assign_Type_Id,Borrowername,Ordernum,Apn;
    private SimpleDateFormat dateFormatter;
    private TextView spinnercustomer;
    SharedPreferences sp,pref;
    private ArrayList<String> customer;
    private ArrayList<String> customerIds;
    private ArrayList<String> states;
    private ArrayList<String> stateIds;
    private ArrayList<String> county;
    private ArrayList<String> countyIds;
    private ArrayList<String> product;
    private ArrayList<String> productIds;
    private ArrayList<String> ordertask;
    private ArrayList<String> orderttaskIds;
    private ArrayList<String> status;
    private ArrayList<String> statusIds;
    private ArrayList<String> orderpriority;
    private ArrayList<String> orderpriorityIds;
    private ArrayList<String> countytype;
    private ArrayList<String> countytypeIds;
    private ProgressDialog pDialog;
    private FloatingActionButton fab;
    String cgheckOrder = "";
    public static final int PERMISSIONS_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);



        toolbar = (Toolbar) findViewById(R.id.toolbar_editorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Order");

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        if (toolbar != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        sp= getApplicationContext().getSharedPreferences(
                ORDERQUEUE, 0);
        //set Fragmentclass Arguments
        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        Order_Id = sp.getString("Order_Id","");
        state =sp.getString("State","");
        countyname = sp.getString("County","");
        producttype= sp.getString("Product_Type","");
        Sub_Client_Name = sp.getString("Sub_Client_Name","");
//        Logger.getInstance().Log("set subclient no " + Sub_Client_Name);

        Order_Priority = sp.getString("Order_Priority","");
        Order_Task =sp.getString("Order_Status","");
        Order_Assign_Type = sp.getString("Order_Assign_Type","");
        statusname =sp.getString("Progress_Status","");
//        fab = (FloatingActionButton) findViewById(R.id.fab_upload);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(EditOrderActivity.this, OrderUploadActivity.class);
//                startActivity(intent);
//            }
//        });
        invoice=(Button)findViewById(R.id.button_editorderinvoice);
        invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrderActivity.this, InvoiceActivity.class);
                startActivity(intent);
            }
        });

        upload=(Button)findViewById(R.id.button_orderupload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

                if (ContextCompat.checkSelfPermission(EditOrderActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditOrderActivity.this, permission)) {
                        showError();

                    } else {
                        ActivityCompat.requestPermissions(EditOrderActivity.this, new String[]{permission}, PERMISSIONS_REQUEST_CODE);
                    }
                } else {
                    Intent intent = new Intent(EditOrderActivity.this, OrderUploadActivity.class);
                    startActivity(intent);                }
            }
        });




        spinnercustomer=(TextView)findViewById(R.id.textviewcustomer);
//        inputtest=(EditText) findViewById(R.id.input_test);
//        inputtestlayout = (TextInputLayout) findViewById(R.id.input_layout_test);

        inputcustomername=(EditText) findViewById(R.id.input_editcustomername);
        inputLayoutborrowername = (TextInputLayout) findViewById(R.id.input_layout_editborrowername);
        inputLayoutapn = (TextInputLayout) findViewById(R.id.input_layout_editapn);
        inputLayoutordernum = (TextInputLayout) findViewById(R.id.input_layout_editordernumber);
        inputOrdernum = (EditText) findViewById(R.id.input_editordernumber);
        inputCity = (EditText) findViewById(R.id.input_editcity);
        inputZip = (EditText) findViewById(R.id.input_editzip);
        inputAddress = (EditText) findViewById(R.id.input_editaddress);
        inputBorrowername = (EditText) findViewById(R.id.input_editborrowername);
        inputApn = (EditText) findViewById(R.id.input_editapn);
        inputDate = (EditText) findViewById(R.id.input_editdate);
        spinner1 = (Spinner) findViewById(R.id.customer_editspinner);
        spinner2 = (Spinner) findViewById(R.id.product_editspinner);
        spinner3 = (Spinner) findViewById(R.id.state_editspinner);
        spinner4 = (Spinner) findViewById(R.id.county_editspinner);
        spinner5 = (Spinner) findViewById(R.id.ordertask_editspinner);
        spinner6 = (Spinner) findViewById(R.id.status_editspinner);
        spinner7 = (Spinner) findViewById(R.id.orderpriority_editspinner);
        spinner8 = (Spinner) findViewById(R.id.countytype_editspinner);
        states = new ArrayList<String>();
        stateIds = new ArrayList<>();
        product = new ArrayList<String>();
        productIds = new ArrayList<>();
        county = new ArrayList<String>();
        countyIds = new ArrayList<String>();
        ordertask = new ArrayList<String>();
        orderttaskIds = new ArrayList<>();
        status = new ArrayList<String>();
        statusIds = new ArrayList<>();
        orderpriority = new ArrayList<String>();
        orderpriorityIds = new ArrayList<>();
        customer = new ArrayList<String>();
        customerIds = new ArrayList<>();
        countytype = new ArrayList<String>();
        countytypeIds = new ArrayList<>();

        inputBorrowername.addTextChangedListener(new MyTextWatcher(inputBorrowername));
        inputOrdernum.addTextChangedListener(new MyTextWatcher(inputOrdernum));
        inputApn.addTextChangedListener(new MyTextWatcher(inputApn));
        checkInternetConnection();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        spinner1.setVisibility(View.GONE);
        spinnercustomer.setVisibility(View.GONE);
        inputcustomername.setEnabled(false);

        submit=(Button)findViewById(R.id.button_editordersubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetConnection();   // checking internet connection
                Borrowername = inputBorrowername.getText().toString().trim();
                Ordernum = inputOrdernum.getText().toString().trim();
                Apn = inputApn.getText().toString().trim();
                checkInternetConnection();

                if (!validateOrdernum() || !validateBorrowername() || !validateApn())
                {
                    Toasty.error(EditOrderActivity.this, "Please enter all credentials!", Toast.LENGTH_SHORT,true).show();
                    return;
                }

                else{
                    SubmitEditOrder();

                }}
        });
        getCustomer();
        getState();
        getProduct();
        getOrdertask();
        getStatus();
        getOrderPriority();
        getcountyType();
        getDate();
        getOrderdetails();

        inputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditOrderActivity.this,R.style.datepicker,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        inputDate.setText(dateFormatter.format(newDate.getTime()));


                    }
                },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }

        });
        return ;

    }


    public String getInfo() {


        return sp.getString("Order_Id", "");
    }




    private void getCustomer(){

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.SUBCLIENTORDER_URL+getClientId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        customer.add(details.getString("Sub_Client_Name"));
                        customerIds.add(details.getString("Sub_Client_Id"));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner1.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, customer));
                spinner1.setSelection(customer.indexOf(Sub_Client_Name));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }


    private void getState() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.STATE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        states.add(details.getString("State"));
                        stateIds.add(details.getString("State_ID"));
//                        num1=details.getString("State_ID");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner3.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, states));
                spinner3.setSelection(states.indexOf(state));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public String getStateId() {


        return sp.getString("State_ID", "");
    }
    public String getCountyId() {


        return sp.getString("County_ID", "");

    }
    public String getClientId() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Client_Id", "");


    }

    public String getorderID() {


        return sp.getString("Order_Id", "");
    }

    public String getSub_Client_Id() {
//         num3=sp.getString("Client_Id","");
//        Log.e("Client_Id of num3", num3);
        return sp.getString("Sub_Client_Id", "");


    }

    private void getCounty() {

        // Log.e("State_ID", getStateId());
        // Log.e("State", getStateId());


        // Log.e("County_Name", getCountyId());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTY_URL + getStateId(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("BescomPoliciesDetails");

                    county.clear();
                    countyIds.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        county.add(jsonObject.getString("County"));

                        countyIds.add(jsonObject.getString("County_ID"));
//                        num2=jsonObject.getString("County_ID");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                spinner4.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, county));
                spinner4.setSelection(county.indexOf(countyname));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void getProduct() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.PRODUCTTYPE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        product.add(details.getString("Order_Type"));
                        productIds.add(details.getString("Order_Type_ID"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner2.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, product));
                spinner2.setSelection(product.indexOf(producttype));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getOrdertask() {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERTASK_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        ordertask.add(details.getString("Order_Status"));
                        orderttaskIds.add(details.getString("Order_Status_ID"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner5.setEnabled(false);
                spinner5.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, ordertask));
                spinner5.setSelection(ordertask.indexOf(Order_Task));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getStatus(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERSTATUS_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        status.add(details.getString("Progress_Status"));
                        statusIds.add(details.getString("Order_Progress_Id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner6.setEnabled(false);
                spinner6.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, status));
                spinner6.setSelection(status.indexOf(statusname));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getOrderPriority(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config. ORDERPRIORITY_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        orderpriority.add(details.getString("Order_Priority"));
                        orderpriorityIds.add(details.getString("Order_Priority_Id"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner7.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, orderpriority));
                spinner7.setSelection(orderpriority.indexOf(Order_Priority));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    private void getcountyType(){
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.COUNTYTYPE_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("Client_master");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        countytype.add(details.getString("Order_Assign_Type"));
                        countytypeIds.add(details.getString("Order_Assign_Type_Id"));
                        hideDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spinner8.setAdapter(new ArrayAdapter<String>(EditOrderActivity.this, android.R.layout.simple_spinner_dropdown_item, countytype));
                spinner8.setSelection(countytype.indexOf(Order_Assign_Type));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    @Override
    public void onResume() {
        super.onResume();



//        statespinner
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                State_ID = stateIds.get(position);
                State = states.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected state id : " + State_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("State_ID", State_ID);
                editor.commit();


                getCounty();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        countyspinner
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                County_ID = countyIds.get(position);
                County = county.get(position);

                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected county id : " + County_ID);

//                Logger.getInstance().Log("selected county  : " + countyname);
                //editor.putString("State_Name", State_Name);
                editor.putString("County_ID",County_ID);

                getCountyId();
                editor.commit();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });
//        producttype
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Type_ID = productIds.get(position);
                Order_Type = product.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected product id : " + Order_Type_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Type_ID", Order_Type_ID);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        ordertask
        spinner5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Status_ID = orderttaskIds.get(position);
                Order_Status = ordertask.get(position);




                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected order task id : " + Order_Status_ID);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Status_ID", Order_Status_ID);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
        //Order Staus
        spinner6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Progress_Id = statusIds.get(position);
                Progress_Status = status.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected status id : " + Order_Progress_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Progress_Id", Order_Progress_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        orderpriority
        spinner7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Priority_Id = orderpriorityIds.get(position);
                Order_Priority = orderpriority.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected ordered priority id : " + Order_Priority_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Priority_Id", Order_Priority_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        customername
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Sub_Client_Id = customerIds.get(position);


                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected cuctomer id : " + Sub_Client_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Sub_Client_Id", Sub_Client_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });
//        countytype
        spinner8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Order_Assign_Type_Id = countytypeIds.get(position);



                //String State_Name = states.get(position);

                SharedPreferences.Editor editor = sp.edit();

//                Logger.getInstance().Log("selected countytype id : " + Order_Assign_Type_Id);


                //editor.putString("State_Name", State_Name);
                editor.putString("Order_Assign_Type_Id", Order_Assign_Type_Id);
                editor.commit();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {



            }


        });



    }
    public void getDate(){
        StringRequest stringRequest=new StringRequest(Request.Method.GET, Config.DATE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
//                Logger.getInstance().Log("in response");


                try {
                    JSONObject jObj = new JSONObject(response);
                    String date=jObj.getString("Order_Date");
                    inputDate.setText(date);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        });
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    public void  getOrderdetails() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Config.ORDERDETAILS_URL +getorderID(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    // Log.e("responce : ", "" + response.toString());
                    JSONArray jsonArray = response.getJSONArray("View_Order_Info");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject details = jsonArray.getJSONObject(i);
                        String Order_Number=details.getString("Order_Number");
                        cgheckOrder = Order_Number;
                        inputOrdernum.setText(Order_Number);

//                        String producttype=details.getString("Order_Type");
//                        inputtest.setText(producttype);


                        String customername=details.getString("Sub_Client_Name");
                        inputcustomername.setText(customername);

                        String Order_Date=details.getString("Order_Date");
                        inputDate.setText(Order_Date);
                        String City=details.getString("City");
                        inputCity.setText(City);
                        String Zip_Code=details.getString("Zip_Code");
                        inputZip.setText(Zip_Code);
                        String Property_Address=details.getString("Property_Address");
                        inputAddress.setText(Property_Address);
                        String Barrower_Name=details.getString("Barrower_Name");
//                        Logger.getInstance().Log("set barr no " + Barrower_Name);
//                        Logger.getInstance().Log("selected product id : " + Order_Type_ID);
                        inputBorrowername.setText(Barrower_Name);
                        String APN=details.getString("APN");
//                        Logger.getInstance().Log("set apn no " + APN);

                        inputApn.setText(APN);
//                        Logger.getInstance().Log("set order no " + Order_Number);
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


    public void SubmitEditOrder(){
        showDialog();
        final String Order_Number = inputOrdernum.getText().toString().trim();

        final String  City = inputCity.getText().toString().trim();
        final String  Zip_Code = inputZip.getText().toString().trim();
        final String  Property_Address = inputAddress.getText().toString().trim();
        final String  Barrower_Name = inputBorrowername.getText().toString().trim();
        final String  APN = inputApn.getText().toString().trim();
        final String  Order_Date = inputDate.getText().toString().trim();
        pDialog.setMessage("Updating ...");
        CustomRequest customRequest = new CustomRequest(Request.Method.POST, Config.EDITORDERSUBMIT_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
//                Logger.getInstance().Log("sucess string");
                try {

                    boolean  error = response.getBoolean("error");
                    boolean  duplicate = response.getBoolean("duplicate");
//                    Logger.getInstance().Log("in error response"+error);
                    // Check for error node in json
                    if (!error&&!duplicate)
                    {

                        Toasty.success( EditOrderActivity.this.getApplicationContext(),"Updated  Sucessfully...",Toast.LENGTH_SHORT).show();
                        hideDialog();
                        goToNavigationActivity();
                    }else if(duplicate) {
                        Toasty.success( EditOrderActivity.this.getApplicationContext(),"duplicate order",Toast.LENGTH_SHORT).show();
                        hideDialog();
                    }else{
                        Toasty.success(EditOrderActivity.this.getApplicationContext(),"Not updated...",Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Order_Id",getorderID());
                params.put("Clinet_Id",getClientId());
//                Logger.getInstance().Log("Id .... is"+getClientId());
                params.put("Sub_Client_Id",getSub_Client_Id());
                params.put("State_ID",State_ID);
//                Logger.getInstance().Log("state id is"+getStateId());
                params.put("State",State);
//                Logger.getInstance().Log("state name is is"+State);
                params.put("County",County);
//                Logger.getInstance().Log("county id is"+getCountyId());
                params.put("County_ID",County_ID);
                params.put("Order_Type_ID",Order_Type_ID);
//                Logger.getInstance().Log("Order_Type_ID id is"+Order_Type_ID);
                params.put("Order_Type",Order_Type);
                params.put("Order_Status",Order_Status);
                params.put("Order_Status_ID",Order_Status_ID);
                params.put("Order_Progress_Id",Order_Progress_Id);
                params.put("Progress_Status",Progress_Status);
                params.put("Order_Priority_Id",Order_Priority_Id);
                params.put("Order_Priority",Order_Priority);
                params.put("Order_Assign_Type_Id",Order_Assign_Type_Id);
                params.put("Order_Number",Order_Number);
                params.put("City",City);
                params.put("Zip_Code",Zip_Code);
                params.put("Property_Address",Property_Address);
                params.put("Barrower_Name",Barrower_Name);
                params.put("APN",APN);
                params.put("Order_Date",Order_Date);
                final String NAME_Num_check = inputOrdernum.getText().toString().trim();
                if (cgheckOrder.equals(NAME_Num_check) )
                {
                    params.put("Duplicate_Check","0");
                }else {
                    params.put("Duplicate_Check","1");
                }

//                Logger.getInstance().Log("para222"+params);



                return params;
            }


        };
        customRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(customRequest);
    }



    private boolean validateBorrowername() {
        if (inputBorrowername.getText().toString().trim().isEmpty()) {
            inputLayoutborrowername.setError(getString(R.string.err_msg_borrowername));
            requestFocus(inputBorrowername);
            return false;
        } else {
            inputLayoutborrowername.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateOrdernum() {
        if (inputOrdernum.getText().toString().trim().isEmpty()) {
            inputLayoutordernum.setError(getString(R.string.err_msg_orderno));
            requestFocus(inputOrdernum);
            return false;
        } else {
            inputLayoutordernum.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateApn() {
        if (inputApn.getText().toString().trim().isEmpty()) {
            inputLayoutapn.setError(getString(R.string.err_msg_apn));
            requestFocus(inputApn);
            return false;
        } else {
            inputLayoutapn.setErrorEnabled(false);
        }

        return true;
    }



    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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

                case R.id.input_borrowername:
                    validateBorrowername();
                    break;

                case R.id.input_ordernumber:
                    validateOrdernum();
                    break;

                case R.id.input_apn:
                    validateApn();
                    break;
            }
        }
    }



    //         check internet connection
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
            TastyToast.makeText( EditOrderActivity.this,"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.INFO);
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
    private void goToNavigationActivity()
    {
        Intent intent = new Intent(EditOrderActivity.this, NavigationActivity.class);
        intent.putExtra("refresh","yes");
        intent.putExtra("position",2);
        setResult(1006,intent);
        finish();
    }


    @Override
    public void onBackPressed() {

//        super.onBackPressed();
        new AlertDialog.Builder(this,R.style.MyAlertDialogStyle)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Activity")
                .setMessage("Are you sure you want to close this activity?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(EditOrderActivity.this, NavigationActivity.class);
                        intent.putExtra("refresh","no");
                        intent.putExtra("position",2);
                        setResult(1005,intent);
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void showError() {
//        Toast.makeText(this, "Allow external storage reading", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("App Permissions")
                .setMessage("App requires to read and write storage, please turn on the permissions")
                .setCancelable(true)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }




}



