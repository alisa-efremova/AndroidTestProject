package com.alice.a7blankproject.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.alice.a7blankproject.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.alice.a7blankproject.R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.alice.a7blankproject.R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == com.alice.a7blankproject.R.id.action_settings) {
            item.setIntent(new Intent(this, PrefActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

}
