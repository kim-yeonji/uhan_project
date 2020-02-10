package base.medium.uhan.ui.video

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import base.medium.uhan.ui.news.News
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.net.URLEncoder


class VideoViewModel : ViewModel() {

    private val mText: MutableLiveData<String>
    var list = MutableLiveData<List<News>>()
    var context : Context? = null
    var newsList = ArrayList<News>()

    val text: LiveData<String>
        get() = mText

    init {
        mText = MutableLiveData()
        mText.value = "This is home fragment"
    }

    fun getData() :LiveData<List<News>> {
        return list
    }


    fun setVideo() = viewModelScope.async(Dispatchers.IO) {

            var result = ""

            try {
                val keyword = URLEncoder.encode("코로나바이러스", "UTF-8")
                val url = URL("https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + keyword + "&maxResults=30&key=AIzaSyAarqxB-Ed5wVFXAOn04YHbQhJG7cJxRGY&sp=CAI%253D")
                val con = url.openConnection() as HttpURLConnection

                val `is` = con.inputStream
                val isr = InputStreamReader(`is`,"UTF-8")
                val reader = BufferedReader(isr)

                while (true) {
                    val data = reader.readLine() ?: break
                    result += data
                }

                val obj = JSONObject(result)
                val arr = obj.get("items") as JSONArray

                for (i in 0 until arr.length()) {
                    var news = News()

                    val item = arr.get(i) as JSONObject
                    val snippet = item.get("snippet") as JSONObject

                    news.title = snippet.get("title") as String
                    news.date = snippet.getString("publishedAt").substring(0, 10)
                    news.imgUrl = snippet.getJSONObject("thumbnails")
                            .getJSONObject("default").getString("url")
                    news.content = snippet.getString("description")
                    news.link = "https://www.youtube.com/watch?v=" + item.getJSONObject("id").getString("videoId")

                    newsList.add(news)


                }

            } catch (e: MalformedURLException) {
                e.printStackTrace()
                viewModelScope.launch{
                    Toast.makeText(context, "오류가 발생 했습니다.", Toast.LENGTH_SHORT).show()
                }

            } catch (e: IOException) {
                Log.d("아아아아아", "아아아아아")
                e.printStackTrace()
                viewModelScope.launch {
                    Toast.makeText(context, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                viewModelScope.launch {
                    Toast.makeText(context, "오류가 발생 했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            newsList
        }

    fun getOnClick() : View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.button_share.tag as String))
            ContextCompat.startActivity(context!!, intent, null)
        }
    }

    fun getOnshare() : View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it.tag as String)
            val chooser = Intent.createChooser(intent, "친구에게 공유하기")
            ContextCompat.startActivity(context!!, chooser, null)
        }
    }
}