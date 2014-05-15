package com.cauzoeng.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FormActivity extends FragmentActivity {

    @InjectView(R.id.button)                    Button submitFormButton;
    @InjectView(R.id.buttonNew)                 Button buttonNew;
    @InjectView(R.id.editTextItem)              EditText editTextItem;
    @InjectView(R.id.editTextPrice)             EditText editTextPrice;
    @InjectView(R.id.spinnerCurrency)           Spinner spinnerCurrency;
    @InjectView(R.id.editTextDescription)       EditText editTextDescription;
    @InjectView(R.id.editTextEmailAddress)      EditText textEmailAddress;
    @InjectView(R.id.editTextPhone)             EditText textPhone;

    @InjectView(R.id.button_gps)                Button gpsButton;

    @InjectView(R.id.toggleButton)              ToggleButton toggleButton;
    @InjectView(R.id.radioGroupCondition)       RadioGroup radioConditionGroup;

    /*@InjectView(R.id.imageButton0)              ImageButton imageButton0;
    @InjectView(R.id.imageButton1)              ImageButton imageButton1;
    @InjectView(R.id.imageButton2)              ImageButton imageButton2;*/
    @InjectViews({ R.id.imageButton0, R.id.imageButton1, R.id.imageButton2 })   List<ImageButton> imageButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_form);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        final String id = intent.getStringExtra(Constants.EXTRA_MESSAGE_ITEM_ID);
        String title = intent.getStringExtra(Constants.EXTRA_MESSAGE_TITLE);
        Double price = intent.getDoubleExtra(Constants.EXTRA_MESSAGE_PRICE, 0.0);
        String currency = intent.getStringExtra(Constants.EXTRA_MESSAGE_CURRENCY);
        String description = intent.getStringExtra(Constants.EXTRA_MESSAGE_DESCRIPTION);

        editTextItem.setText(title);
        editTextPrice.setText(price.toString());
        editTextDescription.setText(description);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.currency_arrays, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrency.setAdapter(adapter);

        final String[] currency_arrays = getResources().getStringArray(R.array.currency_arrays);
        List<String> currency_arraysList = Arrays.asList(currency_arrays);

        if (currency != null) {
            int position = currency_arraysList.indexOf(currency);
            if (position != -1) spinnerCurrency.setSelection(position);
            Log.d(Constants.MESSAGE_TAG, "Currency: " + currency);
            Log.d(Constants.MESSAGE_TAG, "Currency position: " + position);
        } else {
            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(this);
            String defaultCurrency = sharedPrefs.getString("defaultCurrency", "NONE");
            Log.d(Constants.MESSAGE_TAG, "Default currency: " + defaultCurrency);

            int position = currency_arraysList.indexOf(defaultCurrency);
            if (position != -1) spinnerCurrency.setSelection(position);
            Log.d(Constants.MESSAGE_TAG, "Default currency position: " + position);
        }

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id)
            {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("defaultCurrency", currency_arrays[position]);
                editor.commit();

                Log.d(Constants.EVENT_TAG, "Currency selected");
            }

            public void onNothingSelected(AdapterView<?> arg0)
            {
                Log.d(Constants.EVENT_TAG, "Nothing selected");
            }
        });

        submitFormButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.i(Constants.EVENT_TAG, "click send form");

                String user = Utils.getMacAddress(getSystemService(Context.WIFI_SERVICE));

                int selectedId = radioConditionGroup.getCheckedRadioButtonId();
                RadioButton radioConditionButton = (RadioButton) findViewById(selectedId);

                final JsonObject json = new JsonObject();

                if (id != "") {
                    json.addProperty("id", id);
                }

                json.addProperty("user", user);

                json.addProperty("title", editTextItem.getText().toString());
                json.addProperty("price", editTextPrice.getText().toString());
                json.addProperty("currency", spinnerCurrency.getSelectedItem().toString());
                json.addProperty("description", editTextDescription.getText().toString());

                json.addProperty("second_hand", toggleButton.isChecked());
                json.addProperty("condition", radioConditionButton.getText().toString());
                json.addProperty("available_days", 15);

                Double[] lat_lon = Utils.getGpsLocation(getSystemService(Context.LOCATION_SERVICE));
                if (lat_lon[0] != -1 && lat_lon[1] != -1) {
                    JsonObject location = new JsonObject();
                    location.addProperty("lat", lat_lon[0]);
                    location.addProperty("lon", lat_lon[1]);
                    json.add("location", location);
                }

                Ion.with(getBaseContext())
                        .load(Constants.ITEM_API_URL)
                        .setJsonObjectBody(json)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                // do stuff with the result or error
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                Log.i(Constants.HTTP_TAG, "Form sent: " + json.toString());
                                // close the activity
                                finish();
                            }
                        });
            }
        });

        gpsButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textGps = (TextView)findViewById(R.id.textGps);

                Double[] lat_lon = Utils.getGpsLocation(getSystemService(LOCATION_SERVICE));
                textGps.setText("lat: " + lat_lon[0] + " lon: " + lat_lon[1]);
            }
        });

        buttonNew.setOnClickListener(new View.OnClickListener() {
            final CharSequence[] choice = {"Choose from Gallery","Capture a photo"};

            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Upload Photo")
                        .setSingleChoiceItems(choice, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int position) {
                                Log.d(Constants.MESSAGE_TAG, "Choose: " + position + choice[position]);
                                if (choice[position] == "Choose from Gallery") {
                                    selectPhoto();
                                } else if (choice[position] == "Capture a photo") {
                                    takePhoto();
                                }
                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(R.string.str_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {

                                    }
                                }
                        )
                        .show();
            }
        });

        for (final ImageButton imageButton: imageButtons) {
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ( imageButton.getDrawable() != null ) {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Remove?")
                                .setPositiveButton(R.string.str_ok,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialoginterface, int i) {
                                                Log.d(Constants.EVENT_TAG, "Click delete true.");
                                                imageButton.setImageDrawable(null);
                                            }
                                        }
                                )
                                .setNegativeButton(R.string.str_cancel,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialoginterface, int i) {
                                                Log.d(Constants.EVENT_TAG, "Click delete false.");
                                            }
                                        }
                                )
                                .show();
                    }
                }
            });
        }
    }

    String path = "";

    private void selectPhoto () {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File folder = new File(Environment.getExternalStorageDirectory() + "/LoadImg");

        if(!folder.exists())
        {
            folder.mkdir();
        }
        final Calendar c = Calendar.getInstance();
        String new_Date= c.get(Calendar.DAY_OF_MONTH)+"-"+((c.get(Calendar.MONTH))+1)   +"-"+c.get(Calendar.YEAR) +" " + c.get(Calendar.HOUR) + "-" + c.get(Calendar.MINUTE)+ "-"+ c.get(Calendar.SECOND);
        path = String.format(Environment.getExternalStorageDirectory() +"/LoadImg/%s.png","LoadImg("+new_Date+")");
        File photo = new File(path);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        startActivityForResult(intent, 2);
    }

    ArrayList<String> image_list = new ArrayList<String>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1)
        {
            if (data != null) {
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(photoUri, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();
                    Log.d(Constants.IMAGE_TAG, "Gallery File Path=====>>>" + filePath);
                    image_list.add(filePath);
                    Log.d(Constants.IMAGE_TAG, "Image List Size=====>>>" + image_list.size());

                    updateImageButtons();
                    //new GetImages().execute();
                }
            }
        }

        if(requestCode == 2)
        {
            Log.d(Constants.IMAGE_TAG, "Camera File Path=====>>>" + path);
            image_list.add(path);
            Log.d(Constants.IMAGE_TAG, "Image List Size=====>>>" + image_list.size());

            updateImageButtons();
            //new GetImages().execute();
        }
    }

    int count;

    public void updateImageButtons () {

        count = 0;
        for (String image: image_list) {
            File imgFile = new  File(image);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                int height = 100;
                int width = 80;
                Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, width, height, true);
                imageButtons.get(count).setImageBitmap(scaled);
            }
            count++;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }
}
