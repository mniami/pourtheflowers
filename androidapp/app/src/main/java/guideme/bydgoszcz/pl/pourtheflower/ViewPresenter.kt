package guideme.bydgoszcz.pl.pourtheflower

import android.support.v4.app.FragmentManager
import guideme.bydgoszcz.pl.pourtheflower.model.Flower

class ViewPresenter(private val supportFragmentManager: FragmentManager,
                    private val frameLayoutId: Int) {
    private val flowerListBackStackName = "flowerList"
    private val flowerBackStackName = "flower"

    fun showFlower(flower: Flower) {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerFragment.create(flower), "flower")
                .addToBackStack(flowerBackStackName)
                .commit()
    }

    fun showAllFlowers() {
        supportFragmentManager.beginTransaction()
                .replace(frameLayoutId, FlowerListFragment(), flowerListBackStackName)
                .commit()
    }
}