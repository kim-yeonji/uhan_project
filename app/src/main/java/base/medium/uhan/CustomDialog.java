package base.medium.uhan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class CustomDialog extends AlertDialog {

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        AdRequest request = new AdRequest.Builder().build();

        AdView adView = findViewById(R.id.dialog_ad);
        adView.loadAd(request);

        TextView quit = (TextView) findViewById(R.id.finish);
        TextView cancel = (TextView) findViewById(R.id.cancel);

        quit.setOnClickListener(v ->
                android.os.Process.killProcess(android.os.Process.myPid()));

        cancel.setOnClickListener(v ->
                dismiss());


    }
}
