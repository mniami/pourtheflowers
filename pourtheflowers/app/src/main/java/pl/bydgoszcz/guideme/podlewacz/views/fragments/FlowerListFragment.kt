package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import pl.bydgoszcz.guideme.podlewacz.MainActivityHelper
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.features.PouredTheFlower
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.ItemsProvider
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_list.*
import javax.inject.Inject

class FlowerListFragment : Fragment() {
    private var listType: Int
        get() {
            return arguments?.getInt(ARG_LIST_TYPE_NAME) ?: return ALL_LIST_TYPE
        }
        set(value) {
            arguments?.putInt(ARG_LIST_TYPE_NAME, value)
        }
    private var listener: OnListFragmentInteractionListener? = null

    @Inject
    lateinit var pouredTheFlower: PouredTheFlower
    @Inject
    lateinit var itemsProvider: ItemsProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)
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
        val adapter = recyclerView.adapter as FlowerRecyclerViewAdapter? ?: return
        adapter.resume()
        FabHelper(act).show(FabHelper.Option.ADD)?.setOnClickListener {
            listType = USER_LIST_TYPE
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

    override fun onPause() {
        super.onPause()
        val adapter = recyclerView.adapter as FlowerRecyclerViewAdapter? ?: return
        adapter.stop()
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

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun loadAdapter(view: RecyclerView) {
        val context = context ?: return
        view.adapter = FlowerRecyclerViewAdapter(itemsProvider.getItems(listType), context, listener, pouredTheFlower)
        view.adapter?.notifyDataSetChanged()
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: UiItem)
    }

    companion object {
        const val BACK_STACK_NAME = "itemList"
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

        fun changeListType(supportFragmentManager: FragmentManager, listType: Int) {
            val listFragment = supportFragmentManager.findFragmentByTag(BACK_STACK_NAME)
            listFragment?.arguments?.putInt(FlowerListFragment.ARG_LIST_TYPE_NAME, listType)
        }
    }
}
