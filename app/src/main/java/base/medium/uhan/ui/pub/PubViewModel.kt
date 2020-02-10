package base.medium.uhan.ui.pub

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.io.OutputStream
import java.net.URL
import java.nio.file.Files.size
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.net.MalformedURLException


class PubViewModel : ViewModel() {

    private var url :String? = "http://ncov.mohw.go.kr/tcmBoardList.do?pageIndex="
    private var mapList = ArrayList<HashMap<String, String>>()
    private val mText: MutableLiveData<String>
    var mList = MutableLiveData<List<HashMap<String, String>>>()
    var context :Context? = null
    var page = 1

    val text: LiveData<String>
        get() = mText

    val list: LiveData<List<HashMap<String, String>>>
        get() {
            mList.value = mapList
            return mList
        }

    init {
        mText = MutableLiveData()
        mText.value = "This is dashboard fragment"
    }

    fun getParseData() = viewModelScope.async (Dispatchers.Default) {

        try {
            var doc = Jsoup.connect(url + page).post()

            var items = doc.select(".board_list tbody > tr")

            for (item in items) {
                var map = HashMap<String, String>()

                map.set("title", item.select(".bl_link").text())
                map.set("link", item.select(".bl_link").attr("href"))
                map.set("date", item.select("tr > td:eq(3)").text())
                map.set("key", item.select(".bl_link").attr("onclick").substring(50, 56))

                mapList.add(map)
            }



        } catch (e: IOException) {
            e.printStackTrace()
            viewModelScope.launch {
                Toast.makeText(context, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            }        }

        mapList

    }

    fun getOnshare() :View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(android.content.Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it.tag as String)
            val chooser = Intent.createChooser(intent, it.tag as String)
            ContextCompat.startActivity(context!!, chooser, null)
        }
    }

    fun getPost(_url :String) = viewModelScope.async (Dispatchers.Default) {
        // HttpURLConnection 참조 변수.
        var urlConn: HttpURLConnection? = null
        // URL 뒤에 붙여서 보낼 파라미터.

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try{
            var url = URL(_url);
            urlConn =  url.openConnection() as HttpURLConnection

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8");

            // [2-2]. parameter 전달 및 데이터 읽어오기.
            var os = urlConn.getOutputStream();
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            var ff = urlConn.getResponseCode()
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK) null

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            var reader = BufferedReader(InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            var line :String? = null
            var page = ""

            // 라인을 받아와 합친다.
            while (true){
                line = reader.readLine()

                if (line != null) {
                    page += line;
                } else {
                    break;
                }
            }

            page

        } catch (e :MalformedURLException) { // for URL.
            e.printStackTrace();
        } catch (e: IOException) { // for openConnection().
            e.printStackTrace();

        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        null

    }


}