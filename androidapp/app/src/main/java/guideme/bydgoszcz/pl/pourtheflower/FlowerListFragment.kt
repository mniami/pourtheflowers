package guideme.bydgoszcz.pl.pourtheflower

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.SearchView
import guideme.bydgoszcz.pl.pourtheflower.model.Flower
import kotlinx.android.synthetic.main.fragment_flower_list.*
import javax.inject.Inject

class FlowerListFragment : Fragment() {
    private var listType = ALL_LIST_TYPE
    private var listener: OnListFragmentInteractionListener? = null
    @Inject
    lateinit var flowersProvider: FlowersProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)

        arguments?.let {
            listType = it.getInt(ARG_LIST_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flower_list, container, false)
        if (view !is RecyclerView) {
            return view
        }
        setHasOptionsMenu(true)
        loadAdapter(view)
        return view
    }

    private fun loadAdapter(view: RecyclerView) {
        with(view) {
            layoutManager = LinearLayoutManager(context)

            flowersProvider.load {
                var flowers: List<Flower> = when (listType) {
                    USER_LIST_TYPE -> it.getUser().flowers
                    ALL_LIST_TYPE -> it.getAllFlowers()
                    else -> it.getAllFlowers()
                }
                adapter = FlowerRecyclerViewAdapter(flowers, listener)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        menuInflater?.inflate(R.menu.main, menu)

        val search = menu?.findItem(R.id.search)?.actionView as SearchView?
        search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText === null) {
                    return false
                }
                val adapter = recyclerView?.adapter as? FlowerRecyclerViewAdapter ?: return false
                adapter.filter(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Flower)
    }

    companion object {
        const val USER_LIST_TYPE = 1
        const val ALL_LIST_TYPE = 2
        const val ARG_LIST_TYPE = "list_type"

        @JvmStatic
        fun newInstance(listType: Int) =
                FlowerListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_LIST_TYPE, listType)
                    }
                }
    }
}
