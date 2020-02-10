package base.medium.uhan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;


import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class CustomDialog extends AlertDialog {

    Context context;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog);

        TextView quit = (TextView) findViewById(R.id.finish);
        TextView cancel = (TextView) findViewById(R.id.cancel);

        MobileAds.initialize(context, "ca-app-pub-4839780422288243~6698332535");
        AdLoader adLoader = new AdLoader.Builder(context, "ca-app-pub-4839780422288243/6140015051")
                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().build();

                        TemplateView template = findViewById(R.id.dialog_ad);
                        template.setStyles(styles);
                        template.setNativeAd(unifiedNativeAd);

                    }
                })
                .build();

        AdRequest request = new AdRequest.Builder().build();
        adLoader.loadAd(request);

        quit.setOnClickListener(v ->
                android.os.Process.killProcess(android.os.Process.myPid())
        );

        cancel.setOnClickListener(v ->
                dismiss());


    }
}
