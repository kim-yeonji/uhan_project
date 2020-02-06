package base.medium.uhan

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Xml
import android.view.View
import android.webkit.WebView
import org.apache.http.util.EncodingUtils

class PubWebVIew : AppCompatActivity() {

    private var url = "http://ncov.mohw.go.kr/tcmBoardView.do"
    private var postData ="contSeq="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pub_web_view)

        postData += intent.getStringExtra("key")

        var webView = findViewById<View>(R.id.web) as WebView
        webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"))
    }
}
