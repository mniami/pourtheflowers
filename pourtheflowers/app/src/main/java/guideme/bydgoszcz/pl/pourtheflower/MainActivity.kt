package guideme.bydgoszcz.pl.pourtheflower

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import guideme.bydgoszcz.pl.pourtheflower.loaders.DataLoader
import guideme.bydgoszcz.pl.pourtheflower.model.ItemsRepository
import guideme.bydgoszcz.pl.pourtheflower.model.UiItem
import guideme.bydgoszcz.pl.pourtheflower.notifications.ItemsNotifications
import guideme.bydgoszcz.pl.pourtheflower.notifications.NotificationMonitor
import guideme.bydgoszcz.pl.pourtheflower.views.ViewChanger
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.FlowerListFragment
import guideme.bydgoszcz.pl.pourtheflower.views.fragments.TakingPictureThumbnail
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FlowerListFragment.OnListFragmentInteractionListener, MainActivityHelper {
    override fun getViewChanger(): ViewChanger {
        return presenter
    }

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var presenter: MainActivityViewPresenter
    @Inject
    lateinit var repo: ItemsRepository
    @Inject
    lateinit var dataLoader: DataLoader

    override fun onListFragmentInteraction(item: UiItem) {
        presenter.showItem(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            presenter.showNewItemAdd()
        }
        presenter = MainActivityViewPresenter(supportFragmentManager, frame_layout.id)

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
                    presenter.showAllItems()
                } else {
                    ItemsNotifications(this).setUpNotifications(user.items)
                    presenter.showUserItems()
                }
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                showBackButton(false)
            }
        }

        nav_view.setNavigationItemSelectedListener(this)
        NotificationMonitor(this)
                .createNotificationChannel()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TakingPicture.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val currentFragment = presenter.getCurrentFragment()

            if (currentFragment is TakingPictureThumbnail) {
                currentFragment.onThumbnail(imageBitmap)
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.navList -> {
                presenter.showAllItems()
            }
            R.id.navMyList -> {
                presenter.showUserItems()
            }
            R.id.navAddNew -> {
                presenter.showNewItemAdd()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showBackButton(showBackButton: Boolean) {
        toggle.isDrawerIndicatorEnabled = !showBackButton
    }
}

