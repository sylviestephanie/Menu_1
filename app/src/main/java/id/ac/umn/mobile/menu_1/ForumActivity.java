package id.ac.umn.mobile.menu_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class ForumActivity extends AppCompatActivity {

    private WebView webView;
    Toolbar toolbar;
    @Override
    public void onBackPressed() {
        if (webView.copyBackForwardList().getCurrentIndex() > 0) {
            webView.goBack();
        }
        else {
            // Your exit alert code, or alternatively line below to finish
            // Finishes forum activity
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*

        // Adds Progress Bar Support
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        // Makes Progress Bar Visible
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
*/

        setContentView(R.layout.activity_forum);

        toolbar = (Toolbar) findViewById(R.id.toolbar_beginner);
        if (toolbar != null) {
            toolbar.setTitle("Learn It Forum");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        // Adds Zoom Control (You may not need this)
        webView.getSettings().setSupportZoom(true);

        // Enables Multi-Touch. if supported by ROM
        webView.getSettings().setBuiltInZoomControls(true);

        // Change to your own forum url
        webView.loadUrl("http://learnit-database.esy.es/q2a/");
        webView.setVisibility(View.INVISIBLE);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                // Adds Progress Bar Support
                super.onPageStarted(view, url, null);
                findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                /*if(url.contains("smf-media.com")) {
                    view.loadUrl(url);
                    // Adds Progress Bar Support
                    super.onPageStarted(view, url, null);
                    findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
                    // If they are not your domain, use browser instead
                } else {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }*/
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Removes Progress Bar
                findViewById(R.id.progressbar).setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
