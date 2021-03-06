package com.abstractshop.absdesk.adapter.vendor;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abstractshop.absdesk.R;

import com.abstractshop.absdesk.activity.client.orderqueueactivity.PdfActivity;
import com.abstractshop.absdesk.model.vendor.VendorUpload;
import com.abstractshop.absdesk.util.Logger;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;



/**
 * Created by Ajithkumar on 8/4/2017.
 */

public class VendorUploadAdapter extends RecyclerView.Adapter< VendorUploadAdapter.MyViewHolder>  {
    public ArrayList<VendorUpload> uploadList;
    public ArrayList<VendorUpload> selected_usersList=new ArrayList<>();
    private Activity activity;
    private Context ctx;
    private DownloadManager downloadManager;
    private long File_DownloadId;
    private ProgressDialog pDialog;
    VendorUploadAdapter madapter;
    String url;
    String uploadUri = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {


        private List<VendorUpload> uploadList=new ArrayList<VendorUpload>();
        private TextView document,date;
        private ImageView download;
        public LinearLayout ll_listitem;
        Context ctx;

        public MyViewHolder(View view, final Context ctx, final List<VendorUpload>  uploadList) {
            super(view);
            this.uploadList = uploadList;
            this.ctx = ctx;
            document=(TextView)view.findViewById(R.id.vendor_doumenttype);
            date=(TextView)view.findViewById(R.id.vendor_uploadeddate);
            download=(ImageView) view.findViewById(R.id.vendor_img_docdownload);
            ll_listitem=(LinearLayout)view.findViewById(R.id.vendor_ll_listitem);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    VendorUpload upload=uploadList.get(position);
                    Context context = view.getContext();
                    Intent intent=new Intent(context, PdfActivity.class);

                    intent.putExtra("Pdfpath",upload.getDoumentpath());

                    context.startActivity(intent);
                }
            });


            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position=getAdapterPosition();
                    VendorUpload upload=uploadList.get(position);
                    Context context = v.getContext();
                    url=upload.getDoumentpath();
                    Uri file_uri = Uri.parse(url);
                    URLUtil.guessFileName(url, null, null);
                    String fileName = url.substring(url.lastIndexOf('/') + 1);


                    //Logger.getInstance().Log("url   : " + url);
//                    File_DownloadId = DownloadData(file_uri, v);
                    downloadManager=(DownloadManager)ctx.getSystemService(ctx.DOWNLOAD_SERVICE);
//                    Uri file_uri = Uri.parse("http://maven.apache.org/archives/maven-1.x/maven.pdf");
                    DownloadManager.Request request=new DownloadManager.Request(file_uri);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, String.valueOf(fileName));
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference=downloadManager.enqueue(request);
                }
            });
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            ctx.registerReceiver(downloadReceiver, filter);

        }

    }



    public VendorUploadAdapter(ArrayList<VendorUpload>uploadList, ArrayList<VendorUpload> selectedList, Context context) {
        this. uploadList=   uploadList;
        this.selected_usersList = selectedList;
        pDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);
        pDialog.setCancelable(false);
        this.ctx=context;
    }

    @Override
    public VendorUploadAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_uploadrow, parent, false);

        return new VendorUploadAdapter.MyViewHolder(itemView,ctx,uploadList);
    }

    @Override
    public void onBindViewHolder(VendorUploadAdapter.MyViewHolder holder, int position) {
        VendorUpload upload = uploadList.get(position);
        uploadUri = upload.getDoumentpath();
        holder.document.setText(  upload .getDocumentType());
        holder.date.setText( upload .getUploadedDate());


//        Logger.getInstance().Log("uploauri"+uploadUri);

        if(selected_usersList.contains(uploadList.get(position)))
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(ctx, R.color.list_item_selected_state));
        else
            holder.ll_listitem.setBackgroundColor(ContextCompat.getColor(ctx, R.color.list_item_normal_state));



    }









    @Override
    public int getItemCount() {
        return  uploadList.size();
    }



    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    private long DownloadData (Uri uri, View v) {

        long downloadReference;

        downloadManager = (DownloadManager)ctx.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        //Setting title of request
        request.setTitle("Data Download");

        //Setting description of request
        request.setDescription("Android Data download using DownloadManager.");

        //Set the local destination for the downloaded file to a path within the application's external files directory
        if(v.getId() == R.id.img_docdownload)
            request.setDestinationInExternalFilesDir(ctx, Environment.DIRECTORY_DOWNLOADS,"Titelogy.pdf");


        //Enqueue download and save the referenceId
        downloadReference = downloadManager.enqueue(request);



        return downloadReference;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if(referenceId == File_DownloadId) {

                Toast toast = Toast.makeText(ctx,
                        "File Download Complete", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }


        }
    };


}

