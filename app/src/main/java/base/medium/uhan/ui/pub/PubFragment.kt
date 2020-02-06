package base.medium.uhan.ui.pub

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import base.medium.uhan.PubWebVIew

import base.medium.uhan.R
import kotlinx.coroutines.launch

class PubFragment : Fragment() {

    private var pubViewModel: PubViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var pubAdapter: PubAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {


        pubViewModel = ViewModelProviders.of(this).get(PubViewModel::class.java)
        pubViewModel!!.context = context

        val root = inflater.inflate(R.layout.fragment_pub, container, false)

        val layoutManager = LinearLayoutManager(context)

        recyclerView = root.findViewById<View>(R.id.recycler_pub) as RecyclerView
        recyclerView!!.layoutManager = layoutManager

        val th = this

        lifecycleScope.launch {
            pubViewModel!!.getParseData().await()
            pubViewModel!!.list.observe(th, Observer { list ->
                pubAdapter = PubAdapter(activity)
                pubAdapter!!.setList(list)
                pubAdapter!!.setOnClick(View.OnClickListener {
                    var intent = Intent(context, PubWebVIew::class.java)
                    intent.putExtra("key", it.tag as String)
                    startActivity(intent)
                })
                pubAdapter!!.setShareClick(pubViewModel!!.getOnshare())
                recyclerView!!.adapter = pubAdapter

            })
        }

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                val totalItemCount = recyclerView!!.layoutManager!!.itemCount
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                if (totalItemCount == lastVisibleItemPosition + 1) {

                    var that = this
                    recyclerView.removeOnScrollListener(that)


                    var job = lifecycleScope.launch {
                        pubViewModel!!.page += 1
                        pubAdapter!!.setList(pubViewModel!!.getParseData().await())
                        recyclerView.adapter!!.notifyDataSetChanged()
                        recyclerView.addOnScrollListener(that)

                    }



                }





            }




        })








        return root
    }
}