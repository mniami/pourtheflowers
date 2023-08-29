package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.judemanutd.autostarter.AutoStartPermissionHelper
import pl.bydgoszcz.guideme.podlewacz.databinding.FragmentSettingsBinding
import pl.bydgoszcz.guideme.podlewacz.injector
import pl.bydgoszcz.guideme.podlewacz.onClick

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@SettingsFragment) }
        super.onViewCreated(view, savedInstanceState)

        binding.btnAutostart.onClick {
            activity?.let {
                if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(it)) {
                    AutoStartPermissionHelper.getInstance().getAutoStartPermission(it)
                }
            }
        }
    }
}
