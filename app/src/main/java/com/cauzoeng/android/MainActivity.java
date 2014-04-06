package com.cauzoeng.android;

import java.util.HashMap;
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
	 * constant.
	 */
	private static final String FRAGMENT_TAG = "FRAGMENT";
    private static final String EVENT_TAG = "EVENT";
    private static final String JSON_TAG = "JSON";
    private static final String HTTP_TAG = "HTTP";

    public final static String EXTRA_MESSAGE_LOTTERY = "0";
    public final static String EXTRA_MESSAGE_SUBJECT = "dummy subject";
    public final static String EXTRA_MESSAGE_URL = "http://www.google.com";

    public final static String LOTTERY_API_URL = "https://cauzoeng.appspot.com/_ah/api/lottery/v1/lottery/";

	private static final int NUMBER_OF_PAGES = 4;
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
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
            client.get(LOTTERY_API_URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Log.i(HTTP_TAG, "Get http" + response);

                    JSONObject obj = null;
                    JSONArray obj_array = null;
                    try {
                        obj = new JSONObject(response);
                        obj_array = obj.getJSONArray("items");

                        Log.d(JSON_TAG, "Parsed JSON: " + obj.toString());
                    } catch (Throwable t) {
                        Log.e(JSON_TAG, "Could not parse JSON: " + t.getMessage());
                    } finally {

                        try {
                            int array_length = obj_array.length();

                            final String[] ids = new String[array_length];
                            final String[] subjects = new String[array_length];
                            final String[] descriptions = new String[array_length];
                            final String[] urls = new String[array_length];
                            final String[] finish_dates = new String[array_length];
                            final String[] users = new String[array_length];
                            final int[] imageIds = new int[array_length];

                            for (int i = 0; i < array_length; i++) {
                                JSONObject row = obj_array.getJSONObject(i);

                                if (row.has("id")) {
                                    ids[i] = row.getString("id");
                                } else {
                                    ids[i] = "";
                                }

                                if (row.has("subject")) {
                                    subjects[i] = row.getString("subject");
                                } else {
                                    subjects[i] = "";
                                }

                                if (row.has("description")) {
                                    descriptions[i] = row.getString("description");
                                } else {
                                    descriptions[i] = "";
                                }

                                if (row.has("url")) {
                                    urls[i] = row.getString("url");
                                } else {
                                    urls[i] = "";
                                }

                                if (row.has("finish_date")) {
                                    finish_dates[i] = row.getString("finish_date");
                                } else {
                                    finish_dates[i] = "";
                                }

                                imageIds[i] = R.drawable.ic_launcher;

                                if (row.has("user")) {
                                    users[i] = row.getString("user");
                                } else {
                                    users[i] = "";
                                }
                            }

                            Log.d(JSON_TAG, "Successful parse data: " + subjects.toString());

                            CurrentList adapter = new CurrentList(
                                    getActivity(), subjects, descriptions, finish_dates, imageIds);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(
                                        AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getActivity(), "You Clicked at " + subjects[position], Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), LotteryDescriptionActivity.class);
                                    intent.putExtra(EXTRA_MESSAGE_LOTTERY, ids[position]);
                                    intent.putExtra(EXTRA_MESSAGE_SUBJECT, subjects[position]);
                                    intent.putExtra(EXTRA_MESSAGE_URL, urls[position]);
                                    startActivity(intent);

                                    Log.i(EVENT_TAG, "Start an activity.");
                                }
                            });

                        } catch (Throwable t) {
                            Log.e(JSON_TAG, "Unknown: " + t.getMessage());
                        }
                    }
                }
            });

            return rootView;
        }
    }


    /**
     * Previous section fragment control.
     */
    public static class PreviousSectionFragment extends Fragment {
        public static final String ARG_SECTION_NUMBER = "section_number";

        public PreviousSectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main_1_previous, container, false);
            final ListView list = (ListView) rootView.findViewById(R.id.previousListView);

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(LOTTERY_API_URL, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Log.i(EVENT_TAG, "Get http" + response);

                    JSONObject obj = null;
                    JSONArray obj_array = null;
                    try {
                        obj = new JSONObject(response);
                        obj_array = obj.getJSONArray("items");

                        Log.d(JSON_TAG, "Parsed JSON: " + obj.toString());
                    } catch (Throwable t) {
                        Log.e(JSON_TAG, "Could not parse JSON: " + t.getMessage());
                    } finally {

                        try {
                            int array_length = obj_array.length();

                            final String[] ids = new String[array_length];
                            final String[] subjects = new String[array_length];
                            final String[] descriptions = new String[array_length];
                            final String[] urls = new String[array_length];
                            final String[] finish_dates = new String[array_length];
                            final String[] users = new String[array_length];
                            final Integer[] imageIds = new Integer[array_length];

                            for (int i = 0; i < array_length; i++) {
                                JSONObject row = obj_array.getJSONObject(i);

                                if (row.has("id")) {
                                    ids[i] = row.getString("id");
                                } else {
                                    ids[i] = "";
                                }

                                if (row.has("subject")) {
                                    subjects[i] = row.getString("subject");
                                } else {
                                    subjects[i] = "";
                                }

                                if (row.has("description")) {
                                    descriptions[i] = row.getString("description");
                                } else {
                                    descriptions[i] = "";
                                }

                                if (row.has("url")) {
                                    urls[i] = row.getString("url");
                                } else {
                                    urls[i] = "";
                                }

                                if (row.has("finish_date")) {
                                    finish_dates[i] = row.getString("finish_date");
                                } else {
                                    finish_dates[i] = "";
                                }

                                imageIds[i] = R.drawable.ic_launcher;

                                if (row.has("user")) {
                                    users[i] = row.getString("user");
                                } else {
                                    users[i] = "";
                                }

                                /*if (row.has("has_taken")) {
                                    has_takens[i] = row.getString("has_taken");
                                } else {
                                    has_takens[i] = "";
                                }*/
                            }

                            Log.d(JSON_TAG, "Successful parse data: " + subjects.toString());

                            PreviousList adapter = new PreviousList(getActivity(), subjects, descriptions, finish_dates, users);
                            list.setAdapter(adapter);
                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(
                                        AdapterView<?> parent, View view, int position, long id) {

                                    WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
                                    String macAddress = wm.getConnectionInfo().getMacAddress();
                                    if ( macAddress == null || macAddress.isEmpty() ) {
                                        macAddress = "FAKE_USER";
                                    }

                                    if ( macAddress.equals(users[position]) ) {
                                        Toast.makeText(getActivity(), "You Clicked at " + subjects[position], Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getActivity(), FormActivity.class);
                                        intent.putExtra(EXTRA_MESSAGE_LOTTERY, ids[position]);
                                        intent.putExtra(EXTRA_MESSAGE_SUBJECT, subjects[position]);
                                        intent.putExtra(EXTRA_MESSAGE_URL, urls[position]);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(getActivity(), "You not the winner.", Toast.LENGTH_SHORT).show();
                                        Log.i(EVENT_TAG, "You are not the winner.");
                                    }
                                }
                            });

                        } catch (Throwable t) {
                            Log.e(JSON_TAG, "Unknown: " + t.getMessage());
                        }
                    }
                }
            });

            return rootView;
        }
	}


    /**
     * Help fragment control.
     */
	public static class HelpSectionFragment extends Fragment {
		public static final String ARG_SECTION_NUMBER = "section_number";

		public HelpSectionFragment() {
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_3_about, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            dummyTextView.setText("Home section.............");

            Button clickGetButton = (Button) rootView.findViewById(R.id.button1);
            Button clickPostButton = (Button) rootView.findViewById(R.id.button2);

            clickGetButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(EVENT_TAG, "Click get!!");
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get("http://httpbin.org/get", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i(EVENT_TAG, "Get http" + response);
                        }
                    });
                }
            });

            clickPostButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(EVENT_TAG, "Click post!!");
                    HashMap<String, String> paramMap = new HashMap<String, String>();
                    paramMap.put("key", "value");
                    RequestParams params = new RequestParams(paramMap);

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.post("http://httpbin.org/post", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            Log.i(EVENT_TAG, "Post http" + response);
                        }
                    });
                }
            });

            return rootView;
        }
	}

    /**
     * Help section fragment control.
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
            dummyTextView.setText("MAC addr.......: " + wm.getConnectionInfo().getMacAddress());

            Button clickPopupButton = (Button) rootView.findViewById(R.id.button1);
            clickPopupButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(EVENT_TAG, "click open popup http");

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
                            if ( url.contains("com") == true )
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
                        Log.e(EVENT_TAG, "Web open " + e.toString());
                    }

                    Button btnOk = (Button)popUpView.findViewById(R.id.button1);
                    btnOk.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            Log.i(EVENT_TAG, "click ok popup http");
                            mpopup.dismiss();
                        }
                    });

                    Button btnCancel = (Button)popUpView.findViewById(R.id.button2);
                    btnCancel.setOnClickListener(new OnClickListener()
                    {
                        @Override
                        public void onClick(View v) {
                            Log.i(EVENT_TAG, "click cancel popup http");
                            mpopup.dismiss();
                        }
                    });
                };
            });

            Button clickPopupFormButton = (Button) rootView.findViewById(R.id.button2);
            clickPopupFormButton.setOnClickListener( new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.i(EVENT_TAG, "click open popup http");

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
                            if ( url.contains("com") == true )
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
                        Log.e(EVENT_TAG, "Web open " + e.toString());
                    }
                };
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
				Log.d(FRAGMENT_TAG, "First fragment..(home)");
				Fragment fragment_0 = new HomeSectionFragment();
				Bundle args_0 = new Bundle();
				args_0.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_0.setArguments(args_0);
				return fragment_0;

			case 1:
				Log.d(FRAGMENT_TAG, "Second fragment..(previous)");
				Fragment fragment_1 = new PreviousSectionFragment();
				Bundle args_1 = new Bundle();
				args_1.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_1.setArguments(args_1);
				return fragment_1;

			case 2:
				Log.d(FRAGMENT_TAG, "Third fragment..(helps)");
				Fragment fragment_2 = new HelpSectionFragment();
				Bundle args_2 = new Bundle();
				args_2.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_2.setArguments(args_2);
				return fragment_2;

            case 3:
                Log.d(FRAGMENT_TAG, "Third fragment..(helps)");
                Fragment fragment_3 = new AboutSectionFragment();
                Bundle args_3 = new Bundle();
                args_3.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
                fragment_3.setArguments(args_3);
                return fragment_3;

			default:
				Log.d(FRAGMENT_TAG, "Default fragment.");
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
			return NUMBER_OF_PAGES;
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
			case 3:
				return getString(R.string.title_section4).toUpperCase(l);
			}
			return null;
		}
	}
}
