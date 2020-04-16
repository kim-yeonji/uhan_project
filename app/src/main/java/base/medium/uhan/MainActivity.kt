package base.medium.uhan

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.formats.UnifiedNativeAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException


class MainActivity : AppCompatActivity() {

    internal var permission_list = arrayOf(Manifest.permission.INTERNET)
    internal var url = "http://ncov.mohw.go.kr/index_main.jsp"
    internal var dialog :CustomDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration.Builder(
                R.id.navigation_pub, R.id.navigation_news, R.id.navigation_video, R.id.navigation_faq)
                .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        checkPermission()


        MobileAds.initialize(this, "ca-app-pub-7936537457822660~5853445724")
        val adLoader = AdLoader.Builder(this, "ca-app-pub-7936537457822660/3520985332")
                .forUnifiedNativeAd { unifiedNativeAd ->
                    val styles = NativeTemplateStyle.Builder().build()

                    val template = findViewById<TemplateView>(R.id.my_template)
                    template.setStyles(styles)
                    template.setNativeAd(unifiedNativeAd)
                }
                .build()

        val request = AdRequest.Builder().build()
        adLoader.loadAd(request)



        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork == null) {
            Toast.makeText(this, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        var th = this

        lifecycleScope.launch {

            var num : Elements? = null
            var job = lifecycleScope.async(Dispatchers.IO) {

                try {
                    var item = Jsoup.connect(url).get()
                    num = item.select(".num")

                } catch (e :IOException) {
                    Log.d("호호호호호", "호호호호호")
                    e.printStackTrace()
                    Toast.makeText(th, "질병관리본부 메인 홈페이지에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

                num
            }

            num = job.await()

            var one = findViewById<View>(R.id.state_one) as TextView
            var two = findViewById<View>(R.id.state_two) as TextView
            var three = findViewById<View>(R.id.state_three) as TextView

            try {
                one.setText("확진: " + num!!.get(0).text())
                two.setText("퇴원: " + num!!.get(1).text())
                three.setText("사망: " + num!!.get(3).text())
            } catch (e :IndexOutOfBoundsException) {
                Toast.makeText(th, "질병관리본부 메인 홈페이지에 접속할 수 없습니다.", Toast.LENGTH_SHORT).show()

            }



        }



    }

    fun checkPermission() {

        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return

        for (permission in permission_list) {
            //권한 허용 여부를 확인한다.
            val chk = checkCallingOrSelfPermission(permission)

            if (chk == PackageManager.PERMISSION_DENIED) {

                ActivityCompat.requestPermissions(this, permission_list, 0)
            }
        }


    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0) {

            for (i in grantResults.indices) {
                //허용됬다면
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "앱권한설정하세요", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }


    override fun onBackPressed() {

        dialog = CustomDialog(this)
        dialog!!.show()


    }


}
