package pl.bydgoszcz.guideme.podlewacz.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class Analytics @Inject constructor(context: Context) {
    private val analytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }
    fun onViewCreated(bundle: Bundle){
        analytics.logEvent(FRAGMENT_SHOWED, bundle)
    }
    fun onViewCreated(name: String){
        analytics.logEvent(FRAGMENT_SHOWED, BundleFactory().putName(name).build())
    }
}
class BundleFactory {
    private val bundle = Bundle()
    companion object {
        fun create() : BundleFactory {
            return BundleFactory()
        }
    }
    fun putName(value:String) : BundleFactory {
        bundle.putString(NAME, value)
        return this
    }

    fun putString(name: String, value: String) : BundleFactory{
        bundle.putString(name, value)
        return this
    }
    fun putBoolean(name: String, value: Boolean) : BundleFactory {
        bundle.putBoolean(name, value)
        return this
    }
    fun putInt(name: String, value: Int) : BundleFactory {
        bundle.putInt(name, value)
        return this
    }
    fun build() : Bundle {
        return bundle
    }
}

private const val FRAGMENT_SHOWED = "FRAGMENT_SHOWED"
private const val NAME = "NAME"
private const val ITEM_DETAILS = "Item details"
private const val OPTION = "OPTION"