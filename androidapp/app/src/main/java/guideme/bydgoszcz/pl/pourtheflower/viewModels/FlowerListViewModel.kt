package guideme.bydgoszcz.pl.pourtheflower.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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