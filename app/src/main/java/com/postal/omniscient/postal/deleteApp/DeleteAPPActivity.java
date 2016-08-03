package com.postal.omniscient.postal.deleteApp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.postal.omniscient.R;


public class DeleteAPPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityelete_app);
        //deleteAPP();

    }
    private void deleteAPP() {
        Uri packageURI = Uri.parse("package:com.postal.omniscient");
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        startActivity(uninstallIntent);
    }
}
