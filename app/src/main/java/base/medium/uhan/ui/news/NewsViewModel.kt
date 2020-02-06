package base.medium.uhan.ui.news

import android.content.Context
import android.view.View

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.jsoup.Jsoup
import java.io.IOException
import java.util.ArrayList
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.coroutines.launch


class NewsViewModel : ViewModel() {

    private var mIndex = MutableLiveData<Int>()
    var context :Context? = null
    var list = MutableLiveData<List<News>>()
    private var htmlPageUrl = "https://search.daum.net/search?w=news&sort=accuracy&q=코로나 바이러스&cluster=n&DA=PGD&dc=STC&pg=1&r=1&rc=1&at=more&p="
    var page = 1
    var newsList = ArrayList<News>()


    fun getOnClick() :View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.button_share.tag as String))
            startActivity(context!!, intent, null)
        }
    }

    fun getOnshare() :View.OnClickListener {
        return View.OnClickListener {
            val intent = Intent(android.content.Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, it.tag as String)
            val chooser = Intent.createChooser(intent, "친구에게 공유하기")
            startActivity(context!!, chooser, null)
        }
    }





    fun setIndex(index: Int) {
        mIndex.value = index
    }

    fun setNews() = viewModelScope.async(Dispatchers.IO) {

            try {
                var doc = Jsoup.connect(htmlPageUrl + page).get()
                var items = doc.select("#newsResultUL > li")


                for (item in items) {

                    var newsItem = News()

                    newsItem.title = item.select(".wrap_tit > a").text()
                    newsItem.content = item.select(".f_eb").text()
                    newsItem.link = item.select(".wrap_tit > a").attr("href")
                    newsItem.imgUrl = item.select("img").attr("src")
                    newsItem.date = item.select(".f_nb").text()

                    newsList!!.add(newsItem)
                }



            } catch (e: IOException) {
                e.printStackTrace()
                viewModelScope.launch {
                    Toast.makeText(context, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            newsList
        }



    fun getData() :LiveData<List<News>> {

        return list
    }



}