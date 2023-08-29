package pl.bydgoszcz.guideme.podlewacz.views.fragments.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.bydgoszcz.guideme.podlewacz.PourTheFlowerApplication
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.analytics.Analytics
import pl.bydgoszcz.guideme.podlewacz.analytics.BundleFactory
import pl.bydgoszcz.guideme.podlewacz.databinding.FragmentFlowerListBinding
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
    private var _binding: FragmentFlowerListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity?.application as PourTheFlowerApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentFlowerListBinding.inflate(inflater, container, false)
        val view = binding.root
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        setHasOptionsMenu(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = TagsAdapter(emptyList())
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analytics.onViewCreated(BundleFactory().putName(analyticsName).build())

        runInBackground {
            val tags = tagsProvider.getTags()
            runOnUi {
                binding.recyclerView.adapter = TagsAdapter(tags)
            }
        }
    }
}
