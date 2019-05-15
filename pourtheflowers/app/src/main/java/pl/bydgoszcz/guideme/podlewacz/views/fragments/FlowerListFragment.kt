package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_list.*
import pl.bydgoszcz.guideme.podlewacz.MainActivityHelper
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.analytics.BundleFactory
import pl.bydgoszcz.guideme.podlewacz.features.PouredTheFlower
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.threads.runOnUi
import pl.bydgoszcz.guideme.podlewacz.utils.setMenu
import pl.bydgoszcz.guideme.podlewacz.utils.toVisibility
import pl.bydgoszcz.guideme.podlewacz.views.FabHelper
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.ItemsProvider
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject

class FlowerListFragment : Fragment() {
    private var listType: Int
        get() {
            return arguments?.getInt(ARG_LIST_TYPE_NAME) ?: return LIBRARY_LIST_TYPE
        }
        set(value) {
            arguments?.putInt(ARG_LIST_TYPE_NAME, value)
        }
    private var listener: OnListFragmentInteractionListener? = null
    private val analyticsName = "Flower List"

    @Inject
    lateinit var pouredTheFlower: PouredTheFlower
    @Inject
    lateinit var itemsProvider: ItemsProvider
    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flower_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setHasOptionsMenu(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAdapter(recyclerView)
        analytics.onViewCreated(BundleFactory()
                .putName(analyticsName)
                .putString(ARG_LIST_TYPE_NAME, getListTypeTitle()).build())
    }

    override fun onResume() {
        super.onResume()
        val act = activity ?: throw IllegalStateException("Flower list no activity")
        emptyTextView.setOnClickListener {
            listType = USER_LIST_TYPE
            (activity as MainActivityHelper).getViewChanger().showNewItemAdd()
        }
        FabHelper(act).show(FabHelper.Option.ADD)?.setOnClickListener {
            listType = USER_LIST_TYPE
            (activity as MainActivityHelper).getViewChanger().showNewItemAdd()
        }
        if (act is MainActivityHelper) {
            act.toolbar.title = getListTypeTitle()
        }
        refreshUi()

        val adapter = recyclerView.adapter as FlowerRecyclerViewAdapter? ?: return
        adapter.resume()
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

    private fun getListTypeTitle() : String {
        return when (listType) {
            LIBRARY_LIST_TYPE ->
                getString(R.string.all_flowers_title)
            USER_LIST_TYPE ->
                getString(R.string.user_flowers_title)
            else -> ""
        }
    }
    private fun loadAdapter(view: RecyclerView) {
        val context = context ?: return
        Log.d(LOG_TAG, "load adapter")
        runInBackground {
            Log.d(LOG_TAG, "empty adapter set")
            val items = itemsProvider.getItems(listType)
            runOnUi {
                Log.d(LOG_TAG, "setting adapter on ui")
                if (!isVisible) {
                    return@runOnUi
                }
                Log.d(LOG_TAG, "notifying adapter")
                view.adapter = FlowerRecyclerViewAdapter(items, context, listener, pouredTheFlower)
                view.adapter?.notifyDataSetChanged()

                refreshUi()
            }
        }
    }

    private fun refreshUi() {
        recyclerView.adapter ?: return

        val adapter = (recyclerView.adapter as FlowerRecyclerViewAdapter)
        val isEmpty = adapter.itemCount == 0 && listType == USER_LIST_TYPE

        emptyTextView.visibility = isEmpty.toVisibility()
        recyclerView.visibility = (!isEmpty).toVisibility()
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: UiItem)
    }

    companion object {
        const val BACK_STACK_NAME = "itemList"
        const val USER_LIST_TYPE = 1
        const val LIBRARY_LIST_TYPE = 2
        const val ARG_LIST_TYPE_NAME = "list_type"
        const val LOG_TAG = "FlowerListF"

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
