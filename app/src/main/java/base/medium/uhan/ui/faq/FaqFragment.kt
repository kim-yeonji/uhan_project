package base.medium.uhan.ui.faq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import base.medium.uhan.R
import base.medium.uhan.databinding.FragmentFaqBinding
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.coroutines.launch

class FaqFragment : Fragment(), LifecycleOwner {

    private var faqViewModel: FaqViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var binding :FragmentFaqBinding? = null
    private val th : FaqFragment = this


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq, container, false)

        faqViewModel = ViewModelProviders.of(this).get(FaqViewModel::class.java)
        faqViewModel!!.binding = binding
        faqViewModel!!.activity = activity
        binding!!.vm = this

        val root = binding!!.root





        return root
    }

    fun viewFaq(v : View) {

        faqViewModel!!.mapList = ArrayList<HashMap<String, String>>()

        var button = binding!!.root.findViewById<View>(v.id) as ToggleButton
        var type = Integer.parseInt(button.tag as String)

        when(type) {

            0 -> recyclerView = binding!!.one
            1 -> recyclerView = binding!!.three
            2 -> recyclerView = binding!!.four
            3 -> recyclerView = binding!!.five
            4 -> recyclerView = binding!!.six
            5 -> recyclerView = binding!!.seven
            6 -> recyclerView = binding!!.eight
            7 -> recyclerView = binding!!.nine
        }

        if (button.isChecked) {

            recyclerView!!.visibility = View.VISIBLE

            val layoutManager = LinearLayoutManager(context)
            recyclerView!!.layoutManager = layoutManager

            lifecycleScope.launch {
                faqViewModel!!.getParseData(type).await()
                faqViewModel!!.list.observe(th, Observer { list ->
                    var faqAdapter = FaqAdapter(activity)
                    faqAdapter.setList(list)
                    faqAdapter.setOnClick(View.OnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.button_share.tag as String))
                        startActivity(intent)
                    })
                    recyclerView!!.adapter = faqAdapter

                })
            }
        } else {
            faqViewModel!!.mapList = ArrayList<HashMap<String, String>>()
            recyclerView!!.visibility = View.GONE
        }



    }
}