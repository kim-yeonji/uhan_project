package base.medium.uhan.ui.faq

import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import base.medium.uhan.databinding.FragmentFaqBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.io.IOException

class FaqViewModel : ViewModel() {

    private var url :String? = "http://ncov.mohw.go.kr/faqBoardList.do"
    var mapList = ArrayList<HashMap<String, String>>()
    private val mText: MutableLiveData<String>
    private val mList = MutableLiveData<List<HashMap<String, String>>>()
    var binding : FragmentFaqBinding? = null
    var activity :FragmentActivity? = null

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

    fun getParseData(order: Int) = viewModelScope.async(Dispatchers.Default) {

        try {
            var doc = Jsoup.connect(url).get()

            var link = doc.select(".faq_list")
            var items = link.get(order).select(".faq_list > ul > li")

            for (item in items) {
                var map = HashMap<String, String>()

                map.set("title", item.select(".fl_ttl").text())
                map.set("content", item.select(".s_descript").toString())

                mapList.add(map)
            }



        } catch (e: IOException) {
            e.printStackTrace()

            viewModelScope.launch {
                Toast.makeText(activity, "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        mapList
    }



}