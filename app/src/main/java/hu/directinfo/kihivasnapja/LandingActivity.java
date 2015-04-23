package hu.directinfo.kihivasnapja;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import java.text.Normalizer;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LandingActivity extends ActionBarActivity {

    private AutoCompleteTextView cityAutoComplete;
    private ArrayAdapter cities_adapter;
    private String selectedCity;
    private String selectedSchool;
    public static final String PREFS_NAME = "UserPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/gothamroundedbook.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // If they have already registered we are redirecting them to the start activity
        if (checkIfRegistered()) {

            // Start the main activity
            Intent intent = new Intent(LandingActivity.this, MainActivity.class);
            startActivity(intent);

            finish();

        } else {

            setContentView(R.layout.activity_landing);
            handleActionBar();
            fillUpAutocomplete();



           /* android.support.v7.app.ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setDisplayUseLogoEnabled(true);
            actionbar.setLogo(R.drawable.logo);*/

           /* android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);

            LayoutInflater mInflater = LayoutInflater.from(this);

            View mCustomView = mInflater.inflate(R.layout.custom_actionbar,null);

            ImageButton imageButton = (ImageButton) mCustomView.findViewById(R.id.imageButton);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Menu Clicked!",
                            Toast.LENGTH_LONG).show();
                }
            });

            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);*/
        }

    }

    public void handleActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("  KIHÍVÁS NAPJA");
        getSupportActionBar().setSubtitle("  2015. május 20.");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_white_transparent_smaller);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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

    public void fillUpAutocomplete() {

        // Get the list of cities
        String[] cities = getResources().getStringArray(R.array.list_of_cities);

        // Create the array_adapter
        cities_adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, cities);

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
                if (checkIfResourceExist(s)) {
                    enableSchoolSpinner(s, cityAutoComplete);
                } else {
                    disableSchoolSpinner();
                    disableForwardButton();
                }
            }
        });

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

        // Set the button style back to default
        button.setBackgroundResource(android.R.drawable.btn_default);

        // Disable Button
        button.setEnabled(false);

    }

    public boolean checkIfResourceExist(Editable s) {

        // Normalize string
        String city = flattenToAscii(s.toString().toLowerCase());

        // Get the array dynamically
        int getRes = getResources().getIdentifier(city, "array", getPackageName());
        return getRes != 0;
    }

    public boolean checkIfRegistered() {

        // Get Shared Preferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("city", null);
        return restoredText != null;

    }

    public void enableSchoolSpinner(Editable item, AutoCompleteTextView cityAutoComplete) {

        // Set the selected city
        this.selectedCity = flattenToAscii(item.toString().toLowerCase());

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cityAutoComplete.getWindowToken(), 0);

        // Normalize string
        String city = flattenToAscii(item.toString().toLowerCase());

        // Spinner reference
        Spinner spinner = (Spinner) findViewById(R.id.schoolSpinner);

        // Get the array dynamically
        final int getRes = getResources().getIdentifier(city, "array", getPackageName());
        String[] schools = getResources().getStringArray(getRes);

        // Create the adapter
        final ArrayAdapter schools_adapter =
                new ArrayAdapter(this, android.R.layout.simple_list_item_1, schools);

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

                String temp_school = (String) schools_adapter.getItem(position);
                selectedSchool = flattenToAscii(temp_school.toString().toLowerCase());

                Button button = (Button) findViewById(R.id.registerForward);
                button.setBackgroundColor(getResources().getColor(R.color.active_button));
                button.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void proceedToApp() {

        // Get the shared preferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();

        // Saving data in shared preferences
        if (this.selectedCity != null && !this.selectedCity.isEmpty()) {

            editor.putString("city", this.selectedCity);

        }

        if (this.selectedSchool != null && !this.selectedSchool.isEmpty()) {

            editor.putString("school", this.selectedSchool);

        }

        // Commit the changes to preferences file
        editor.commit();

        // Disable this activity to never come back
        /*setActivityEnabled(this,LandingActivity.class,false);*/
        finish();

        // Start the start activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void createAndShowAlertDialog(View view) {

        /*AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Figyelem!");
        alertDialogBuilder.setMessage("Ha egyszer beállította a települését és az iskoláját, később már nem térhet vissza erre a képernyőre. Folytatja?");

        alertDialogBuilder.setPositiveButton("Folytatás",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                proceedToApp();
            }
        });

        alertDialogBuilder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();*/

        proceedToApp();

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