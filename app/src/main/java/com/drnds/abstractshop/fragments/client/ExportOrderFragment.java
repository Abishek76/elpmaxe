package com.drnds.abstractshop.fragments.client;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.drnds.abstractshop.R;

/**
 * Created by Ajithkumar on 5/17/2017.
 */

import org.json.JSONObject;

public class ExportOrderFragment extends Fragment {
    Button write, read;
    static String TAG = "ExelLog";
    private String filename = "myExcel.xls";
    private static final String FILE_NAME = "/tmp/MyFirstExcel.xlsx";
    SharedPreferences sp;
    JSONObject jsonObject;
    private DownloadManager downloadManager;
    private ProgressDialog pDialog;
    String Data="Hello!!";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export, null);

        sp = getActivity().getApplicationContext().getSharedPreferences(
                "LoginActivity", 0);
        pDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        write = (Button) view.findViewById(R.id.write);

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        prepareProcessingOrder();

        return view;
    }

    public String getClientId() {

        return sp.getString("Client_Id", "");
    }

    private void prepareProcessingOrder() {
    }


}

