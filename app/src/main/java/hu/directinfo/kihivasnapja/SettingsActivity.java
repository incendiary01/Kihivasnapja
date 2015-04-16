package hu.directinfo.kihivasnapja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.Normalizer;


public class SettingsActivity extends ActionBarActivity {

    private Spinner schoolSpinner;
    private AutoCompleteTextView cityAutoComplete;
    private Button savePreferences;
    private ArrayAdapter cities_adapter;

    private TextView currentCity;
    private TextView currentSchool;

    private String selectedCity;
    private String selectedSchool;

    public static final String PREFS_NAME = "UserPreferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savePreferences = (Button) findViewById(R.id.savePreferences);
        cityAutoComplete = (AutoCompleteTextView) findViewById(R.id.cityAutoComplete);
        schoolSpinner = (Spinner) findViewById(R.id.schoolSpinner);
        currentCity = (TextView) findViewById(R.id.currentCity);
        currentSchool = (TextView) findViewById(R.id.currentSchool);

        fillUpAutocomplete();
        writeOutCurrentSettings();

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayUseLogoEnabled(true);
        actionbar.setLogo(R.mipmap.ic_launcher);

        /*
        * Capture savePreferences click event
        * */
        savePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePreferences();
            }
        });

     }

    private void writeOutCurrentSettings() {

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String city = prefs.getString("city", null);
        String school = prefs.getString("school", null);

        this.currentCity.setText("Jelenlegi város: " + city);
        this.currentSchool.setText("Jelenlegi iskola: " +school);

    }

    public void fillUpAutocomplete() {

        // Get the list of cities
        String[] cities = getResources().getStringArray(R.array.list_of_cities);

        // Create the array_adapter
        cities_adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,cities);

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

    private void savePreferences() {

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
        if (editor.commit())
        {
            notifySaveSuccess();
        }
        else
        {
            notifySaveFail();
        }
        writeOutCurrentSettings();

    }

    private void notifySaveSuccess() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Kihívás napja");
        alertDialogBuilder.setMessage("Az adatokat sikeresen elmentettük!");

        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
    }

    private void notifySaveFail() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Kihívás napja");
        alertDialogBuilder.setMessage("Az adatokat sajnos nem sikerült elmenteni. Kérem próbálja meg később!");

        alertDialogBuilder.setNeutralButton("OK",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();
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
        Button button = (Button) findViewById(R.id.savePreferences);

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

    public void enableSchoolSpinner(Editable item, AutoCompleteTextView cityAutoComplete) {

        // Set the selected city
        this.selectedCity = flattenToAscii(item.toString().toLowerCase());

        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cityAutoComplete.getWindowToken(), 0);

        // Normalize string
        String city = flattenToAscii(item.toString().toLowerCase());

        // Spinner reference
        Spinner spinner = (Spinner) findViewById(R.id.schoolSpinner);

        // Get the array dynamically
        int getRes = getResources().getIdentifier(city, "array", getPackageName());
        String[] schools = getResources().getStringArray(getRes);

        // Create the adapter
        final ArrayAdapter schools_adapter =
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

                String temp_school = (String) schools_adapter.getItem(position);
                selectedSchool = flattenToAscii(temp_school.toString().toLowerCase());

                Button button = (Button) findViewById(R.id.savePreferences);
                button.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
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
