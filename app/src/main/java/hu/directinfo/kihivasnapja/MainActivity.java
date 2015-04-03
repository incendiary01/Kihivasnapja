package hu.directinfo.kihivasnapja;

import android.annotation.TargetApi;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toolbar;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillUpAutocomplete1();


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

    public void fillUpAutocomplete1() {

        // Get the list of cities
        String[] cities = getResources().getStringArray(R.array.list_of_cities);

        // Create the array_adapter
        ArrayAdapter cities_adapter =
                new ArrayAdapter(this,android.R.layout.simple_list_item_1,cities);

        // Get a reference on AutoCompleteTextView
        AutoCompleteTextView cityAutoComplete = (AutoCompleteTextView) findViewById(R.id.cityAutoComplete);

        // Set the data
        cityAutoComplete.setAdapter(cities_adapter);

        // Create onchange event
        cityAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                enableSchoolSpinner(item);
            }
        });
    }

    /**
     * Miután kiválasztottuk a várost, meghívjuk ezt a függvényt.
     * A kiválaszott város nevéből eltávolítjuk az ékezeteket.
     * Az új város névvel megkeressük a hozzá tartozó iskolák tömbjét
     * és betöltjük a schoolSpinnerbe az adatokat.
     * @param item
     */
    public void enableSchoolSpinner(String item) {

        // Normalize string
        String city = flattenToAscii(item);

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