package base.medium.uhan.ui.news

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import base.medium.uhan.R
import kotlinx.coroutines.launch


/**
 * A placeholder fragment containing a simple view.
 */
class NewsFragment : Fragment() {
    private var recyclerView :RecyclerView? = null
    private var newsViewModel: NewsViewModel? = null
    private var newsAdapter : NewsAdapter? = null
    private var th = this


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel::class.java)
        newsViewModel!!.context = activity

        val root = inflater.inflate(R.layout.fragment_news, container, false)

        val layoutManager = LinearLayoutManager(context)

        recyclerView = root.findViewById<View>(R.id.news_recycler) as RecyclerView
        recyclerView!!.layoutManager = layoutManager

        newsAdapter = NewsAdapter(activity)

        newsAdapter!!.setOnClick(newsViewModel!!.getOnClick())
        newsAdapter!!.setOnShare(newsViewModel!!.getOnshare())

        recyclerView!!.adapter = newsAdapter


        lifecycleScope.launch {

            newsViewModel!!.list.value = newsViewModel!!.setNews().await()
            newsViewModel!!.getData().observe(th, Observer { s ->
                newsAdapter!!.setList(s)
                recyclerView!!.adapter = newsAdapter

            })

        }

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {

                    var that = this
                    recyclerView.removeOnScrollListener(that)


                    lifecycleScope.launch {
                        newsViewModel!!.page += 1
                        newsAdapter!!.setList(newsViewModel!!.setNews().await())
                        recyclerView.adapter!!.notifyDataSetChanged()
                        recyclerView.addOnScrollListener(that)

                    }



                }





                }




        })

        return root
    }

    companion object {

        private val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(index: Int): NewsFragment {
            val fragment = NewsFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }


}