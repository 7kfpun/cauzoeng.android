package com.cauzoeng.android;


import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
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

import com.loopj.android.http.*;

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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item1 = menu.findItem(R.id.action_new_item);
        Intent intent1 = new Intent(this, FormActivity.class);
        item1.setIntent(intent1);
		return true;
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

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(Constants.ITEM_API_URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Log.i(Constants.HTTP_TAG, "Get item api" + response);

                    JSONObject obj = null;
                    JSONArray obj_array = null;
                    try {
                        obj = new JSONObject(response);
                        obj_array = obj.getJSONArray("items");

                        Log.d(Constants.JSON_TAG, "Parsed JSON: " + obj.toString());
                    } catch (Throwable t) {
                        Log.e(Constants.JSON_TAG, "Could not parse JSON: " + t.getMessage());
                    } finally {

                        try {
                            int array_length = obj_array.length();

                            final String[] ids = new String[array_length];
                            final String[] titles = new String[array_length];
                            final Double[] prices = new Double[array_length];
                            final String[] currencies = new String[array_length];
                            final String[] descriptions = new String[array_length];
                            final String[] created_dates = new String[array_length];
                            final String[] users = new String[array_length];
                            final int[] imageIds = new int[array_length];

                            for (int i = 0; i < array_length; i++) {
                                JSONObject row = obj_array.getJSONObject(i);

                                if (row.has("id")) {
                                    ids[i] = row.getString("id");
                                } else {
                                    ids[i] = "";
                                }

                                if (row.has("title")) {
                                    titles[i] = row.getString("title");
                                } else {
                                    titles[i] = "";
                                }

                                if (row.has("price")) {
                                    prices[i] = row.getDouble("price");
                                } else {
                                    prices[i] = 0.0;
                                }

                                if (row.has("currency")) {
                                    currencies[i] = row.getString("currency");
                                } else {
                                    currencies[i] = "";
                                }

                                if (row.has("description")) {
                                    descriptions[i] = row.getString("description");
                                } else {
                                    descriptions[i] = "";
                                }

                                if (row.has("created_date")) {
                                    created_dates[i] = row.getString("created_date");
                                } else {
                                    created_dates[i] = "";
                                }

                                imageIds[i] = R.drawable.ic_launcher;

                                if (row.has("user")) {
                                    users[i] = row.getString("user");
                                } else {
                                    users[i] = "";
                                }
                            }

                            Log.d(Constants.JSON_TAG, "Successful parse data: " + titles.toString());

                            CurrentList adapter = new CurrentList(
                                    getActivity(), titles, prices, currencies, descriptions,
                                    created_dates, imageIds);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(
                                        AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getActivity(), "You Clicked at " + titles[position], Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                                    intent.putExtra(Constants.EXTRA_MESSAGE_ID, ids[position]);
                                    intent.putExtra(Constants.EXTRA_MESSAGE_TITLE, titles[position]);
                                    intent.putExtra(Constants.EXTRA_MESSAGE_PRICE, prices[position]);
                                    intent.putExtra(Constants.EXTRA_MESSAGE_CURRENCY, currencies[position]);
                                    intent.putExtra(Constants.EXTRA_MESSAGE_DESCRIPTION, descriptions[position]);
                                    startActivity(intent);

                                    Log.i(Constants.EVENT_TAG, "Start an activity.");
                                }
                            });

                        } catch (Throwable t) {
                            Log.e(Constants.JSON_TAG, "Unknown: " + t.getMessage());
                        }
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
