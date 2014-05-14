package com.cauzoeng.android;


import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.cauzoeng.android.model.ItemListReader;

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
    String order = "date";
    private static final int RESULT_SETTINGS = 1;

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

            case R.id.action_settings:
                Toast.makeText(this, "Settings menu", Toast.LENGTH_SHORT).show();

                Double[] lat_lon = Utils.getGpsLocation(getSystemService(Context.LOCATION_SERVICE));
                Toast.makeText(this, "lat: " + lat_lon[0] + " lon: " + lat_lon[1], Toast.LENGTH_SHORT).show();

                Intent intentSetting = new Intent(this, SettingsActivity.class);
                this.startActivity(intentSetting);

                break;

            case R.id.action_map:
                Intent map_intent = new Intent(this, MapActivity.class);
                this.startActivity(map_intent);
                break;

            case R.id.action_about:
                SharedPreferences sharedPrefs = PreferenceManager
                        .getDefaultSharedPreferences(this);

                String prefFilterOrder = sharedPrefs.getString("prefFilterOrder", "DATE");

                Toast.makeText(this, prefFilterOrder, Toast.LENGTH_SHORT).show();

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

        private void renderList (View rootView) {

            // Notify swipeRefreshLayout that the refresh has started (used for non-swiping)
            SwipeRefreshLayout swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
            swipe_refresh.setRefreshing(true);

            SharedPreferences sharedPrefs = PreferenceManager
                    .getDefaultSharedPreferences(rootView.getContext());

            final ListView list = (ListView) rootView.findViewById(R.id.list);

            String prefFilterOrder = sharedPrefs.getString("prefFilterOrder", "created_date");
            Log.d(Constants.EVENT_TAG, "prefFilterOrder" + prefFilterOrder);

            Ion.with(getActivity())
                    .load(Constants.ITEM_API_URL)
                    .addQuery("order", prefFilterOrder)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject obj) {

                            try {
                                ItemListReader itemList = new ItemListReader(obj);
                                final String[] arr_item_ids = itemList.getItemIds();
                                final String[] arr_titles = itemList.getTitles();
                                final Double[] arr_prices = itemList.getPrices();
                                final String[] arr_currencies = itemList.getCurrencies();
                                final String[] arr_descriptions = itemList.getDescriptions();
                                final String[] arr_created_dates = itemList.getCreateDates();
                                final Integer[] arr_imageIds = itemList.getImageIds();

                                CurrentListAdapter adapter = new CurrentListAdapter(
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
                            } catch (Throwable t) {
                                Log.e(Constants.HTTP_TAG, t.toString());
                            }
                        }
                    });

            // Notify swipeRefreshLayout that the refresh has finished
            swipe_refresh.setRefreshing(false);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_main_0_home, container, false);
            renderList(rootView);

            SwipeRefreshLayout swipe_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
            swipe_refresh.setColorScheme(R.color.blue, R.color.green, R.color.orange, R.color.purple);

            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Log.d(Constants.EVENT_TAG, "refresh");
                    renderList(rootView);
                }
            });

            final EditText search_bar = (EditText) rootView.findViewById(R.id.search_bar);
            search_bar.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    // If the event is a key-down event on the "enter" button
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                            (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        Toast.makeText(getActivity(), search_bar.getText(), Toast.LENGTH_SHORT).show();
                        renderList(rootView);
                        return true;
                    }
                    return false;
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

            String mac = Utils.getMacAddress(getActivity().getSystemService(Context.WIFI_SERVICE));
            dummyTextView.setText("MAC address.......: " + mac);

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
