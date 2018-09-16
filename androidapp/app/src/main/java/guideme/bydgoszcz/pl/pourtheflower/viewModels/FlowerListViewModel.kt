package guideme.bydgoszcz.pl.pourtheflower.viewModels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import guideme.bydgoszcz.pl.pourtheflower.model.Library
import guideme.bydgoszcz.pl.pourtheflower.model.User

class FlowerListViewModel : ViewModel() {
    val library = MutableLiveData<Library>()
    val user = MutableLiveData<User>()
}

class UserLiveData : LiveData<User>() {
    private var user: User? = null
    override fun getValue(): User? {
        if (user == null) {

        }
        return user
    }
}