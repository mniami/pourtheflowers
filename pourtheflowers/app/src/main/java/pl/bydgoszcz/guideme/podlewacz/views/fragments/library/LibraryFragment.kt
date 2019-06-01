package pl.bydgoszcz.guideme.podlewacz.views.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_flower_list.*
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.analytics.BundleFactory
import pl.bydgoszcz.guideme.podlewacz.threads.runInBackground
import pl.bydgoszcz.guideme.podlewacz.threads.runOnUi
import pl.bydgoszcz.guideme.podlewacz.views.fragments.providers.TagsProvider
import javax.inject.Inject

class LibraryFragment : Fragment() {
    @Inject
    lateinit var analytics: Analytics
    @Inject
    lateinit var tagsProvider: TagsProvider

    private val analyticsName = "Library"

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

        analytics.onViewCreated(BundleFactory().putName(analyticsName).build())

        runInBackground {
            val tags = tagsProvider.getTags()
            runOnUi {
                recyclerView.adapter = TagsAdapter(tags)
            }
        }

    }
}