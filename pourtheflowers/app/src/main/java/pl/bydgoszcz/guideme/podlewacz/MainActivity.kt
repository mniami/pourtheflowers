package pl.bydgoszcz.guideme.podlewacz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import androidx.work.Configuration
import androidx.work.WorkManager
import pl.bydgoszcz.guideme.podlewacz.loaders.DataLoader
import pl.bydgoszcz.guideme.podlewacz.model.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.model.UiItem
import pl.bydgoszcz.guideme.podlewacz.notifications.ItemsNotifications
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationChannelCreator
import pl.bydgoszcz.guideme.podlewacz.views.TakePicture
import pl.bydgoszcz.guideme.podlewacz.views.ViewChanger
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.TakingPictureThumbnail
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, FlowerListFragment.OnListFragmentInteractionListener, MainActivityHelper {
    override fun getViewChanger(): ViewChanger {
        return presenter
    }

    internal var onBackPressedCallback: () -> Boolean = { true }
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var presenter: MainActivityViewPresenter
    @Inject
    lateinit var repo: ItemsRepository
    @Inject
    lateinit var dataLoader: DataLoader
    @Inject
    lateinit var itemsNotifications: ItemsNotifications

    override fun onListFragmentInteraction(item: UiItem) {
        presenter.showItem(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Main)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

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

        privacyPolicyLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getString(R.string.privacy_policy_url))
            startActivity(intent)
        }

        if (savedInstanceState == null) {
            (application as PourTheFlowerApplication).component.inject(this)
            dataLoader.load {
                val user = repo.user
                if (user.items.isEmpty()) {
                    presenter.showAllItems()
                } else {
                    itemsNotifications.setUpNotifications(user.items)
                    presenter.showUserItems()
                }
            }
        }
        supportFragmentManager.addOnBackStackChangedListener {
            invalidateOptionsMenu()
            if (supportFragmentManager.backStackEntryCount == 0) {
                showBackButton(false)
            }
        }

        nav_view.setNavigationItemSelectedListener(this)
        NotificationChannelCreator.createNotificationChannel(this)
    }

    override fun onBackPressed() {
        if (!onBackPressedCallback()) {
            return
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TakePicture.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val currentFragment = presenter.getCurrentFragment()

            if (currentFragment is TakingPictureThumbnail) {
                currentFragment.onPictureCaptured()
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

