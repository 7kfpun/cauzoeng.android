package com.cauzoeng.android;

import java.util.HashMap;
import java.util.Locale;

import com.cauzoeng.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.util.Log;
import android.view.View.OnClickListener;

import com.loopj.android.http.*;

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
	
	/**
	 * constant
	 */
	private static final String FRAGMENT_TAG = "FRAGMENT LOG";
	private static final String EVENT_TAG = "EVENT LOG";
	
	private static final int NUMBER_OF_PAGES = 4;

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
				Fragment fragment_2 = new HelpsSectionFragment();
				Bundle args_2 = new Bundle();
				args_2.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
				fragment_2.setArguments(args_2);
				return fragment_2;

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

	public static class HomeSectionFragment extends Fragment {
		/**
		 * Previous fragment control
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public HomeSectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_0_home,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
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

	public static class PreviousSectionFragment extends Fragment {
		/**
		 * Previous fragment control
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public PreviousSectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_1_previous,
					container, false);
			
			ListView listView = (ListView) rootView.findViewById(R.id.listView1);
            
            // Defined Array values to show in ListView
			
            String[] values = new String[20];

            for(int i = 0; i < values.length; i++)
                values[i] = "Lucky draw #" + i;

            try {
                JSONObject obj = new JSONObject();

                for(int i = 0; i < 40; i++) {
	            	obj.put("name", "Lucky draw" + i);
	            	obj.put("age", new Integer(100) + i);
                }

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
            	getActivity(), R.layout.listview_row_items, R.id.textViewItem, values);

            // Assign adapter to ListView
            listView.setAdapter(adapter); 
            return rootView;
		}
	}

	
	public static class HelpsSectionFragment extends Fragment {
		/**
		 * Helps fragment control
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public HelpsSectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_2_helps,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);


			WifiManager wm = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
			dummyTextView.setText("Help section.............: " + wm.getConnectionInfo().getMacAddress());

			final Button clickPopupButton = (Button) rootView.findViewById(R.id.button1);
			
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
		        		webView.setWebViewClient(myWebClient);
		        		webView.loadUrl("http://www.google.com");
		        		
	                } catch (Exception e) {
	                    Log.e(EVENT_TAG, "Web open " + e.toString());
	                }

	                Button btnOk = (Button)popUpView.findViewById(R.id.button1);
	                btnOk.setOnClickListener(new OnClickListener() 
	                {                    
	                    @Override
	                    public void onClick(View v) 
	                    {
	    	            	Log.i(EVENT_TAG, "click ok popup http");
	                        mpopup.dismiss();
	                    }
	                });
	                
	                Button btnCancel = (Button)popUpView.findViewById(R.id.button2);
	                btnCancel.setOnClickListener(new OnClickListener() 
	                {                    
	                    @Override
	                    public void onClick(View v) 
	                    {
	    	            	Log.i(EVENT_TAG, "click cancel popup http");
	                        mpopup.dismiss();
	                    }
	                });
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

}
