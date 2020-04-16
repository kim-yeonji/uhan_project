package base.medium.uhan

import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.util.Xml
import android.view.View
import android.webkit.WebView
import org.apache.http.util.EncodingUtils

class PubWebVIew : AppCompatActivity() {

    private var url = "http://ncov.mohw.go.kr/tcmBoardView.do"
    private var postData ="&contSeq="
    private var dd = "brdId=3&brdGubun=31&dataGubun=&board_id=311&ncvContSeq="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pub_web_view)

        var jj = intent.getStringExtra("key")
        postData = postData + jj
        dd = dd + jj
        postData = dd + postData

        var webView = findViewById<View>(R.id.web) as WebView
        webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"))
    }
}
