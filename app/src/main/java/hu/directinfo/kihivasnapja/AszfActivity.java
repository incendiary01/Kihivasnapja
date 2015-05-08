package hu.directinfo.kihivasnapja;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

public class AszfActivity extends ActionBarActivity {

    private TextView longaszf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aszf);

        handleActionBar();

        longaszf = (TextView) findViewById(R.id.longAszf);
        longaszf.setText(Html.fromHtml(getResources().getString(R.string.kihivas_napja_aszf)));
    }

    public void handleActionBar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("  KIHÍVÁS NAPJA");
        getSupportActionBar().setSubtitle("  2015. május 20.");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo_white_transparent_smaller);
    }
}
