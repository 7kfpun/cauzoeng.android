package com.cauzoeng.android;


import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.lang.InterruptedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


/**
 * MainActivity.
 */
public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
	SectionsPagerAdapter mSectionsPagerAdapter;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

        // Enable global Ion logging
        Ion.getDefault(this).configure().setLogging("ion-sample", Log.DEBUG);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_new_item:
                Intent intent = new Intent(this, FormActivity.class);
                this.startActivity(intent);
                break;
            case R.id.action_filter:
                Toast.makeText(this, "Filter menu", Toast.LENGTH_SHORT).show();
                LayoutInflater inflater= LayoutInflater.from(this);
                View view=inflater.inflate(R.layout.filter, null);

                TextView textview=(TextView)view.findViewById(R.id.textmsg);
                textview.setText("Your really long message.");
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Title");
                //alertDialog.setMessage("Here is a really long message.");
                alertDialog.setView(view);
                alertDialog.setPositiveButton("OK", null);
                AlertDialog alert = alertDialog.create();
                alert.show();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings menu", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_about:
                Toast.makeText(this, "About menu", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_about_title)
                        .setMessage(R.string.app_about_message)
                        .setPositiveButton(R.string.str_ok,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialoginterface, int i)
                                    {
                                    }
                                })
                        .show();
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Home section fragment control.
     */
	public static class HomeSectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public HomeSectionFragment() {
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main_0_home, container, false);
            final ListView list = (ListView) rootView.findViewById(R.id.list);

            Ion.with(getActivity())
                    .load(Constants.ITEM_API_URL)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject obj) {
                            // do stuff with the result or error
                            if (e == null) {
                                Log.d(Constants.JSON_TAG, "json: " + obj);

                                JsonArray obj_array = obj.getAsJsonArray("items");

                                List<String> item_ids = new ArrayList<String>();
                                List<String> users = new ArrayList<String>();
                                List<String> titles = new ArrayList<String>();
                                List<Double> prices = new ArrayList<Double>();
                                List<String> currencies = new ArrayList<String>();
                                List<String> descriptions = new ArrayList<String>();
                                List<String> created_dates = new ArrayList<String>();
                                List<Integer> imageIds = new ArrayList<Integer>();

                                try {
                                    for (JsonElement result : obj_array) {
                                        JsonObject item = result.getAsJsonObject();
                                        String title = item.get("title").getAsString();

                                        if (item.has("id")) {
                                            item_ids.add(item.get("id").getAsString());
                                        } else {
                                            item_ids.add("");
                                        }

                                        if (item.has("user")) {
                                            users.add(item.get("user").getAsString());
                                        } else {
                                            users.add("");
                                        }

                                        if (item.has("title")) {
                                            titles.add(item.get("title").getAsString());
                                        } else {
                                            titles.add("");
                                        }

                                        if (item.has("price")) {
                                            prices.add(item.get("price").getAsDouble());
                                        } else {
                                            prices.add(0.0);
                                        }

                                        if (item.has("currency")) {
                                            currencies.add(item.get("currency").getAsString());
                                        } else {
                                            currencies.add("");
                                        }

                                        if (item.has("description")) {
                                            descriptions.add(item.get("description").getAsString());
                                        } else {
                                            descriptions.add("");
                                        }

                                        if (item.has("created_date")) {
                                            created_dates.add(item.get("created_date").getAsString());
                                        } else {
                                            created_dates.add("");
                                        }

                                        imageIds.add(R.drawable.ic_launcher);

                                        Log.i(Constants.JSON_TAG, ">> Title: " + title);
                                    }
                                    Log.d(Constants.JSON_TAG, "Successful parsing GSON.");
                                } catch (Throwable t) {
                                    Log.e(Constants.JSON_TAG, "Could not parse GSON: " + t.getMessage());

                                } finally {

                                    final String[] arr_item_ids = item_ids.toArray(new String[item_ids.size()]);
                                    final String[] arr_titles = titles.toArray(new String[titles.size()]);
                                    final Double[] arr_prices = prices.toArray(new Double[prices.size()]);
                                    final String[] arr_currencies = currencies.toArray(new String[currencies.size()]);
                                    final String[] arr_descriptions = descriptions.toArray(new String[descriptions.size()]);
                                    final String[] arr_created_dates = created_dates.toArray(new String[created_dates.size()]);
                                    final Integer[] arr_imageIds = imageIds.toArray(new Integer[imageIds.size()]);

                                    CurrentList adapter = new CurrentList(
                                            getActivity(), arr_titles, arr_prices, arr_currencies, arr_descriptions,
                                            arr_created_dates, arr_imageIds);
                                    list.setAdapter(adapter);
                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(
                                                AdapterView<?> parent, View view, int position, long id) {
                                            Toast.makeText(getActivity(), "You Clicked at " + arr_titles[position], Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                            intent.putExtra(Constants.EXTRA_MESSAGE_ITEM_ID, arr_item_ids[position]);
                                            intent.putExtra(Constants.EXTRA_MESSAGE_TITLE, arr_titles[position]);
                                            intent.putExtra(Constants.EXTRA_MESSAGE_PRICE, arr_prices[position]);
                                            intent.putExtra(Constants.EXTRA_MESSAGE_CURRENCY, arr_currencies[position]);
                                            intent.putExtra(Constants.EXTRA_MESSAGE_DESCRIPTION, arr_descriptions[position]);
                                            startActivity(intent);

                                            Log.i(Constants.EVENT_TAG, "Start an activity.");
                                        }
                                    });
                                }
                            } else {
                                Log.d(Constants.JSON_TAG, "error: " + e);
                            }
                        }
                    });

            return rootView;
        }
    }


    /**
     * Map fragment control.
     */
	public static class MapSectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public MapSectionFragment() {
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);

            dummyTextView.setText("Map view.");

            return rootView;
        }
	}

    /**
     * About section fragment control.
     */
    public static class AboutSectionFragment extends Fragment {
        public static final String ARG_SECTION_NUMBER = "section_number";

        public AboutSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_2_helps, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);

            WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            dummyTextView.setText("MAC address.......: " + wm.getConnectionInfo().getMacAddress());

            Button clickPopupButton = (Button) rootView.findViewById(R.id.button1);
            clickPopupButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(Constants.EVENT_TAG, "click open popup http");

                    View popUpView = getActivity().getLayoutInflater().inflate(R.layout.popup, null);
                    final PopupWindow mpopup = new PopupWindow(
                        popUpView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.showAtLocation(popUpView, Gravity.BOTTOM, 0, 0);

                    WebViewClient myWebClient = new WebViewClient()
                    {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView  view, String  url)
                        {
                            // This line we let me load only pages inside "com" Webpage
                            if ( url.contains("com") )
                                //Load new URL Don't override URL Link
                                return false;

                            return true;
                        }
                    };

                    try {
                        WebView webView = (WebView) popUpView.findViewById(R.id.webView1);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setBuiltInZoomControls(true);
                        webView.getSettings().setSupportZoom(true);
                        webView.setWebViewClient(myWebClient);
                        webView.loadUrl("http://www.google.com");

                    } catch (Exception e) {
                        Log.e(Constants.EVENT_TAG, "Web open " + e.toString());
                    }

                    Button btnOk = (Button)popUpView.findViewById(R.id.button1);
                    btnOk.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            Log.i(Constants.EVENT_TAG, "click ok popup http");
                            mpopup.dismiss();
                        }
                    });

                    Button btnCancel = (Button)popUpView.findViewById(R.id.button2);
                    btnCancel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            Log.i(Constants.EVENT_TAG, "click cancel popup http");
                            mpopup.dismiss();
                        }
                    });
                }
            });

            Button clickPopupFormButton = (Button) rootView.findViewById(R.id.button2);
            clickPopupFormButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(Constants.EVENT_TAG, "click open popup http");

                    View popUpView = getActivity().getLayoutInflater().inflate(R.layout.activity_form, null);
                    final PopupWindow mpopup = new PopupWindow(
                            popUpView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
                    mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
                    mpopup.showAtLocation(popUpView, Gravity.BOTTOM, 0, 0);

                    WebViewClient myWebClient = new WebViewClient()
                    {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView  view, String  url)
                        {
                            // This line we let me load only pages inside "com" Webpage
                            if ( url.contains("com") )
                                //Load new URL Don't override URL Link
                                return false;

                            return true;
                        }
                    };

                    try {
                        WebView webView = (WebView) popUpView.findViewById(R.id.webView1);
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.getSettings().setBuiltInZoomControls(true);
                        webView.getSettings().setSupportZoom(true);
                        webView.setWebViewClient(myWebClient);
                        webView.loadUrl("http://hk.yahoo.com/");

                    } catch (Exception e) {
                        Log.e(Constants.EVENT_TAG, "Web open " + e.toString());
                    }
                }
            });

            return rootView;
        }
    }

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			switch (position) {
			case 0:
				Log.d(Constants.FRAGMENT_TAG, "First fragment..(home)");
				Fragment fragment_0 = new HomeSectionFragment();
				Bundle args_0 = new Bundle();
				args_0.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_0.setArguments(args_0);
				return fragment_0;

			case 1:
				Log.d(Constants.FRAGMENT_TAG, "Second fragment..(previous)");
				Fragment fragment_1 = new MapSectionFragment();
				Bundle args_1 = new Bundle();
				args_1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_1.setArguments(args_1);
				return fragment_1;

            case 2:
                Log.d(Constants.FRAGMENT_TAG, "Third fragment..(helps)");
                Fragment fragment_3 = new AboutSectionFragment();
                Bundle args_3 = new Bundle();
                args_3.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment_3.setArguments(args_3);
                return fragment_3;

			default:
				Log.d(Constants.FRAGMENT_TAG, "Default fragment.");
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			// Show total pages.
			return Constants.NUMBER_OF_PAGES;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

}
