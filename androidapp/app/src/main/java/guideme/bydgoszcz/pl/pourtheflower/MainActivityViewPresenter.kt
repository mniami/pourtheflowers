package guideme.bydgoszcz.pl.pourtheflower

import android.support.v4.app.FragmentManager
import guideme.bydgoszcz.pl.pourtheflower.model.Flower

class MainActivityViewPresenter(private val supportFragmentManager: FragmentManager,
                                private val frameLayoutId: Int) {
    private val flowerListBackStackName = "flowerList"
    private val flowerBackStackName = "flower"

    fun showFlower(flower: Flower) {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerFragment.create(flower), "flower")
                .addToBackStack(flowerBackStackName)
                .commit()
    }

    fun showUserFlowers() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.USER_LIST_TYPE), flowerListBackStackName)
                .commit()
    }

    fun showAllFlowers() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment.newInstance(FlowerListFragment.ALL_LIST_TYPE), flowerListBackStackName)
                .commit()
    }
}