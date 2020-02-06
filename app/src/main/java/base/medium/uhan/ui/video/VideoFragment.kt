package base.medium.uhan.ui.video

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

import base.medium.uhan.R
import kotlinx.coroutines.launch

class VideoFragment : Fragment() {

    private var videoViewModel: VideoViewModel? = null
    private var videoAdapter: VideoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        videoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        videoViewModel!!.context = context

        val root = inflater.inflate(R.layout.fragment_video, container, false)

        val layoutManager = LinearLayoutManager(context)

        val recyclerView = root.findViewById<View>(R.id.recycler_video) as RecyclerView
        recyclerView.layoutManager = layoutManager

        videoAdapter = VideoAdapter(activity)

        videoAdapter!!.setOnClick(videoViewModel!!.getOnClick())
        videoAdapter!!.setOnShare(videoViewModel!!.getOnshare())

        recyclerView.adapter = videoAdapter

        var hh = this
        lifecycleScope.launch {

            videoViewModel!!.list.value = videoViewModel!!.setVideo().await()
            videoViewModel!!.getData().observe(hh, Observer { s ->
                videoAdapter!!.setList(s)
                videoAdapter!!.setOnShare(videoViewModel!!.getOnshare())
                recyclerView.adapter!!.notifyDataSetChanged()
            })
        }




        return root
    }
}