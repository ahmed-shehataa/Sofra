package com.ashehata.sofra.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.model.DateModel;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HelperMethod {

    private static ProgressDialog checkDialog;
    public static AlertDialog alertDialog;
    private static Toast toast;
    public static String ORDER_PENDING = "pending";
    public static String ORDER_CURRENT = "current";
    public static String ORDER_COMPLETED = "completed";
    private static LinearLayoutManager linearLayoutManager;

    //public static Snackbar snackbar;

    public static void ReplaceFragment(FragmentManager supportFragmentManager, Fragment fragment, int container_id
            /*, TextView toolbarTitle, String title*/, boolean enableTransaction) {

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();

        if(enableTransaction == true){
            transaction.setCustomAnimations(R.anim.enter,R.anim.exit,R.anim.pop_enter,R.anim.pop_exit);
        }
        transaction.replace(container_id, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        /*
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }

         */

    }
    public static void changeLang(Context context, String lang) {
        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }

    public static MultipartBody.Part convertFileToMultipart(String pathImageFile, String Key) {
        if (pathImageFile != null) {
            File file = new File(pathImageFile);

            RequestBody reqFileselect = RequestBody.create(MediaType.parse("image/*"), file);

            MultipartBody.Part Imagebody = MultipartBody.Part.createFormData(Key, file.getName(), reqFileselect);

            return Imagebody;
        } else {
            return null;
        }
    }


    public static RequestBody convertToRequestBody(String part) {
        try {
            if (!part.equals("")) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), part);
                return requestBody;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void onLoadImageFromUrl(CircleImageView circleImageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
               // .placeholder(R.drawable.loading_image)
                .into(circleImageView);
    }
    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    /*
    public static void createSnackBar(View view, String message, Context context) {
        final Snackbar snackbar = Snackbar.make(view, message, 50000);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        })
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))

                .show();
    }

     */

    /*
    public static void createSnackBar(View view, String message, Context context, View.OnClickListener action, String Title) {
        snackbar = Snackbar.make(view, message, 50000);
        snackbar.setAction(Title, action)
                .setActionTextColor(context.getResources().getColor(android.R.color.holo_red_light))
                .show();
    }

     */


    //Calender
    public static void showCalender(Context context, String title, final TextView text_view_data, final DateModel data1) {

        DatePickerDialog mDatePicker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedYear, int selectedMonth, int selectedDay) {

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat mFormat = new DecimalFormat("00", symbols);
                String data = selectedYear + "-" + String.format(new Locale("en"), mFormat.format(Double.valueOf((selectedMonth + 1)))) + "-"
                        + mFormat.format(Double.valueOf(selectedDay));
                data1.setDateTxt(data);
                data1.setDay(mFormat.format(Double.valueOf(selectedDay)));
                data1.setMonth(mFormat.format(Double.valueOf(selectedMonth + 1)));
                data1.setYear(String.valueOf(selectedYear));
                if (text_view_data != null) {
                    text_view_data.setText(data);
                }
            }
        }, Integer.parseInt(data1.getYear()), Integer.parseInt(data1.getMonth()) - 1, Integer.parseInt(data1.getDay()));
        mDatePicker.setTitle(title);
        mDatePicker.show();
    }

    public static void showProgressDialog(Activity activity, String title) {
        try {
            checkDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
            checkDialog.setMessage(title);
            checkDialog.setIndeterminate(false);
            checkDialog.setCancelable(false);
            checkDialog.show();

        } catch (Exception e) {

        }
    }

    public static void dismissProgressDialog() {
        try {
            if (checkDialog != null && checkDialog.isShowing()) {
                checkDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    public static void disappearKeypad(Activity activity, View v) {
        try {
            if (v != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        } catch (Exception e) {

        }
    }


    public static void setSpinner(Activity activity, Spinner spinner, List<String> names) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, names);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView =(TextView) view;
                if (textView !=null){
                    //textView.setTextColor(activity.getResources().getColor(R.color.colorWhite));

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner.setAdapter(adapter);
    }

    public static void createToast(Context context , String title , int duration){
        if (toast != null){
            toast.cancel();
        }
        if (context != null){
            toast = Toast.makeText(context, title, duration);
            toast.show();
        }

    }
    public static void checkGalleryPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }

    }

    public static void pickFromGallery(Activity activity) {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        activity.startActivityForResult(intent, 0);
    }

    public static MultipartBody.Part convertImageToPart(String filePath) {
        //Create a file object using file path
        File file = new File(filePath);
        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(), fileReqBody);



        return  part ;
    }

    public static RequestBody convertTextToPart(String text){
        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), text);

        return description ;
    }

    public static String getImagePath(Activity activity ,Uri uri){

        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        // Get the cursor
        Cursor cursor = activity.getContentResolver().query(uri, filePathColumn, null, null, null);
        // Move to first row
        cursor.moveToFirst();
        //Get the column index of MediaStore.Images.Media.DATA
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        //Gets the String value in the column
        String imgDecodableString = cursor.getString(columnIndex);
        cursor.close();

        return imgDecodableString;
    }

    public static boolean checkPermission(Context context) {
        boolean check = false;

        String permission3 = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        int res3 = context.checkCallingOrSelfPermission(permission3);
        if (res3 == PackageManager.PERMISSION_GRANTED) {
            check = true;
        }
        /*
        String permission4 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res4 = context.checkCallingOrSelfPermission(permission4);
        if (res4 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        String permission6 = android.Manifest.permission.CALL_PHONE;
        int res6 = context.checkCallingOrSelfPermission(permission6);
        if (res6 != PackageManager.PERMISSION_GRANTED) {
            check = true;
        }
         */
        return check;
    }

    public static void onPermission(Activity activity) {
        String[] perms = {
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_CONTACTS,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.CALL_PHONE
        };
        ActivityCompat.requestPermissions(activity,
                perms,
                100);
    }

    public static void openAlbum(int Counter, Context context, final ArrayList<AlbumFile> ImagesFiles, Action<ArrayList<AlbumFile>> action) {
        Album album = new Album();
        Album.initialize(AlbumConfig.newBuilder(context)
                .setAlbumLoader(new MediaLoader())
                .setLocale(Locale.ENGLISH).build());
                album.image(context)// Image and video mix options.
                .multipleChoice()// Multi-Mode, Single-Mode: singleChoice().
                .columnCount(3) // The number of columns in the page list.
                .selectCount(Counter)  // Choose up to a few images.
                .camera(true) // Whether the camera appears in the Item.
                .checkedList(ImagesFiles) // To reverse the list.
                .widget(
                        Widget.newLightBuilder(context)
                                .title("")
                                .statusBarColor(Color.WHITE) // StatusBar color.
                                .toolBarColor(Color.WHITE) // Toolbar color.
                                .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
                                .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // Image or video selection box.
                                .bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
                                .build()
                )
                .onResult(action)
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                    // The Client canceled the operation.
                        createToast(context,context.getString(R.string.choose_photo),Toast.LENGTH_SHORT);
                    }
                })
                .start();
    }

    public static void onLoadImageFromUri(ImageView imageView, String URl, Context context) {
        Glide.with(context)
                .load(URl)
                .into(imageView);
    }

    public static void setEtTypeFace(TextInputEditText textInputEditText) {
        textInputEditText.setTypeface(Typeface.DEFAULT);
        textInputEditText.setTransformationMethod(new PasswordTransformationMethod());
    }

    public static void showAlertDialog(Activity activity
            , String title
            , String messageBody
            , boolean cancelable
            , DialogInterface.OnClickListener positiveButtonAction
            , DialogInterface.OnClickListener negativeButtonAction
            , String positiveTitle
            , String negativeTitle){

        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(messageBody);
        alertDialog.setCancelable(cancelable);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,positiveTitle,positiveButtonAction);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,negativeTitle,negativeButtonAction);
        alertDialog.show();

    }
    public static void hideAlertDialog(Context context){

        if(alertDialog.isShowing()){
            alertDialog.hide();
        }


    }
    //set recycler view configuration
    public static void setRecyclerConfig(RecyclerView recyclerView , Context context) {

        linearLayoutManager = new LinearLayoutManager(context);
        // Set items on linear manager
        recyclerView.setLayoutManager(linearLayoutManager);

        // Fixed size
        recyclerView.setHasFixedSize(true);
    }
}
