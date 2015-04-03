package hu.directinfo.kihivasnapja;

import android.annotation.TargetApi;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toolbar;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private AutoCompleteTextView cityAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillUpAutocomplete();


        /*final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> list2 = new ArrayList<>();

        final StableArrayAdapter adapter =
                new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        final StableArrayAdapter adapter2 =
                new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list2);





        for (int i = 0; i < cities.length; ++i) {
            list.add(cities[i]);
        }
        for (int i = 0; i < schools.length; ++i) {
            list2.add(schools[i]);
        }*/



/*        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Log.i("ClickedListItem", item);
            }

        });*/


        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowHomeEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setLogo(R.mipmap.ic_launcher);


        /*final DialogFragment dialog = new YesNoDialog();
        final Bundle args = new Bundle();*/
        /*args.putString("title", "Alert");
                args.putString("message", item);
                dialog.setArguments(args);
                dialog.setTargetFragment(this, YES_NO_CALL);
                dialog.show(getFragmentManager(), "tag");*/
        /*actionbar.hide();*/
        /*actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(true);*/

        /*mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText("My Own Title");
        ImageButton imageButton = (ImageButton) mCustomView
                .findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refresh Clicked!",
                        Toast.LENGTH_LONG).show();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);*/

        /*view.animate().setDuration(2000).alpha(0)
        .withEndAction(new Runnable() {
            @Override
            public void run() {
                list.remove(item);
                adapter.notifyDataSetChanged();
                view.setAlpha(1);
            }
        });*/
    }

    public void fillUpAutocomplete() {

        // Get the list of cities
        String[] cities = getResources().getStringArray(R.array.list_of_cities);

        // Create the array_adapter
        ArrayAdapter cities_adapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1,cities);

        // Get a reference on AutoCompleteTextView
        cityAutoComplete = (AutoCompleteTextView) findViewById(R.id.cityAutoComplete);

        // Set the data
        cityAutoComplete.setAdapter(cities_adapter);

        // Set other event listener
        cityAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(checkIfResourceExist(s)) {
                    enableSchoolSpinner(s,cityAutoComplete);
                }
                else
                {
                    disableSchoolSpinner();
                    disableForwardButton();
                }
            }
        });


        /*cityAutoComplete.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                Log.e("keyCode", "Value: " + keyCode);

                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    Log.d("keyCode", "Value: " + keyCode);
                    disableSchoolSpinner();
                    disableForwardButton();
                }
                return false;
            }
        });*/

        // Set event listener
        /*cityAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                enableSchoolSpinner(item);
            }
        });*/


    }

    public void disableSchoolSpinner() {

        // Spinner reference
        Spinner spinner = (Spinner) findViewById(R.id.schoolSpinner);

        // Disable spinner
        spinner.setEnabled(false);

        // Remove data from spinner
        spinner.setAdapter(null);

    }

    public void disableForwardButton() {

        // Button reference
        Button button = (Button) findViewById(R.id.registerForward);

        // Disable Button
        button.setEnabled(false);
    }

    public boolean checkIfResourceExist(Editable s) {

        // Normalize string
        String city = flattenToAscii(s.toString());

        // Spinner reference
        Spinner spinner = (Spinner) findViewById(R.id.schoolSpinner);

        // Get the array dynamically
        int getRes = getResources().getIdentifier(city, "array", getPackageName());
        if (getRes != 0) {
            return true;
        }
        return false;
    }

    public void enableSchoolSpinner(Editable item, AutoCompleteTextView cityAutoComplete) {

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cityAutoComplete.getWindowToken(), 0);

        // Normalize string
        String city = flattenToAscii(item.toString());

        // Spinner reference
        Spinner spinner = (Spinner) findViewById(R.id.schoolSpinner);

        // Get the array dynamically
        int getRes = getResources().getIdentifier(city, "array", getPackageName());
        String[] schools = getResources().getStringArray(getRes);

        // Create the adapter
        ArrayAdapter schools_adapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1,schools);

        // Set the spinner type
        schools_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter
        spinner.setAdapter(schools_adapter);

        // Enable spinner
        spinner.setEnabled(true);

        // Set event listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Button button = (Button) findViewById(R.id.registerForward);
                button.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void proceedToApp(View view) {

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    public static String flattenToAscii(String string) {
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out);
    }
}