package guideme.bydgoszcz.pl.pourtheflower.utils

import android.content.Context
import javax.inject.Inject

interface ContentProvider {
    fun getString(id: Int): String
}

class ContentProviderImpl @Inject constructor(val context: Context) : ContentProvider {
    override fun getString(id: Int): String =
            context.getString(id)
}