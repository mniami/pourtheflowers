package guideme.bydgoszcz.pl.pourtheflower.views.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.SearchView
import guideme.bydgoszcz.pl.pourtheflower.MainActivityHelper
import guideme.bydgoszcz.pl.pourtheflower.MainActivityViewPresenter
import guideme.bydgoszcz.pl.pourtheflower.PourTheFlowerApplication
import guideme.bydgoszcz.pl.pourtheflower.R
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.utils.findActionViewItem
import guideme.bydgoszcz.pl.pourtheflower.utils.setMenu
import guideme.bydgoszcz.pl.pourtheflower.views.FabHelper
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_flower_list.*
import javax.inject.Inject


class FlowerListFragment : Fragment() {
    private var listType = ALL_LIST_TYPE
    private var listener: OnListFragmentInteractionListener? = null

    @Inject
    lateinit var repo: ItemsRepository

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

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                // Remove item from backing list here
                recyclerView.adapter.notifyDataSetChanged()
            }
        })

        itemTouchHelper.attachToRecyclerView(recyclerView)

        loadAdapter(view)
        return view
    }

    override fun onResume() {
        super.onResume()
        FabHelper(activity).show(true)?.setOnClickListener {
            (activity as MainActivityHelper).getViewChanger().showNewItemAdd()
        }

        val act = activity
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
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    private fun loadAdapter(view: RecyclerView) {
        with(view) {
            val lib = repo.itemsStore
            val user = repo.user

            val flowers: List<UiItem> = when (listType) {
                USER_LIST_TYPE -> user.items
                ALL_LIST_TYPE -> lib
                else -> lib
            }
            adapter = FlowerRecyclerViewAdapter(flowers, listener)
            adapter.notifyDataSetChanged()
        }
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
