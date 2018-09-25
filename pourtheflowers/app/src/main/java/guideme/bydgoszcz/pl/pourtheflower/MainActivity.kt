package guideme.bydgoszcz.pl.pourtheflower

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import guideme.bydgoszcz.pl.pourtheflower.loaders.DataLoader
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FlowerListFragment.OnListFragmentInteractionListener, MainActivityHelper {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var mainActivityViewPresenter: MainActivityViewPresenter
    @Inject
    lateinit var repo: ItemsRepository
    @Inject
    lateinit var dataLoader: DataLoader

    override fun onListFragmentInteraction(item: UiItem) {
        mainActivityViewPresenter.showFlower(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Dodanie nowej rośliny, w aktualnej wersji nie jest dostępne", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        mainActivityViewPresenter = MainActivityViewPresenter(supportFragmentManager, frame_layout.id)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.setToolbarNavigationClickListener {
            onBackPressed()
        }
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            (application as PourTheFlowerApplication).component.inject(this)
            dataLoader.load {
                val user = repo.user
                if (user.items.isEmpty()) {
                    mainActivityViewPresenter.showAllItems()
                } else {
                    mainActivityViewPresenter.showUserItems()
                }
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                showBackButton(false)
            }
        }

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount == 0) {
                finish()
            } else {
                supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.navList -> {
                mainActivityViewPresenter.showAllItems()
            }
            R.id.navMyList -> {
                mainActivityViewPresenter.showUserItems()
            }
            R.id.navAddNew -> {
                Snackbar.make(nav_view, "Dodanie nowego kwiatu", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showBackButton(showBackButton: Boolean) {
        toggle.isDrawerIndicatorEnabled = !showBackButton
    }
}

interface MainActivityHelper {
    fun showBackButton(showBackButton: Boolean)
}
