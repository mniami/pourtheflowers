package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.PourTheFlowerApplication
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.features.PouredTheFlower
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.providers.ItemsProvider
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_list.*
import javax.inject.Inject

class FlowerListFragment : Fragment() {
    private var listType = ALL_LIST_TYPE
    private var listener: OnListFragmentInteractionListener? = null

    @Inject
    lateinit var pouredTheFlower: PouredTheFlower
    @Inject
    lateinit var itemsProvider: ItemsProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)

        arguments?.let {
            listType = it.getInt(ARG_LIST_TYPE_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flower_list, container, false)
        if (view !is RecyclerView) {
            return view
        }
        setHasOptionsMenu(true)
        view.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAdapter(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        val act = activity ?: return
        FabHelper(act).show(FabHelper.Option.ADD)?.setOnClickListener {
            (activity as MainActivityHelper).getViewChanger().showNewItemAdd()
        }
        if (act is MainActivityHelper) {
            when (listType) {
                ALL_LIST_TYPE ->
                    act.toolbar.title = getString(R.string.all_flowers_title)
                USER_LIST_TYPE ->
                    act.toolbar.title = getString(R.string.user_flowers_title)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, menuInflater: MenuInflater?) {
        setMenu(menu, menuInflater, R.menu.main)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnListFragmentInteractionListener")
        }
    }

    override fun onPause() {
        super.onPause()
        (recyclerView?.adapter as FlowerRecyclerViewAdapter?)?.stop()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun loadAdapter(view: RecyclerView) {
        view.adapter = FlowerRecyclerViewAdapter(itemsProvider.getItems(listType), requireContext(), listener, pouredTheFlower)
        view.adapter?.notifyDataSetChanged()
    }
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: UiItem)
    }

    companion object {
        const val USER_LIST_TYPE = 1
        const val ALL_LIST_TYPE = 2
        const val ARG_LIST_TYPE_NAME = "list_type"

        @JvmStatic
        fun newInstance(listType: Int) =
                FlowerListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_LIST_TYPE_NAME, listType)
                    }
                }
    }
}
