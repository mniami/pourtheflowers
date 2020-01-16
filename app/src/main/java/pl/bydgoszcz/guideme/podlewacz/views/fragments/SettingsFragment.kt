package pl.bydgoszcz.guideme.podlewacz.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.judemanutd.autostarter.AutoStartPermissionHelper
import kotlinx.android.synthetic.main.fragment_settings.*
import pl.bydgoszcz.guideme.podlewacz.R
import pl.bydgoszcz.guideme.podlewacz.injector
import pl.bydgoszcz.guideme.podlewacz.onClick

class SettingsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        injector { inject(this@SettingsFragment) }
        super.onViewCreated(view, savedInstanceState)

        btnAutostart.onClick {
            activity?.let {
                if (AutoStartPermissionHelper.getInstance().isAutoStartPermissionAvailable(it)) {
                    AutoStartPermissionHelper.getInstance().getAutoStartPermission(it)
                }
            }
        }
    }
}