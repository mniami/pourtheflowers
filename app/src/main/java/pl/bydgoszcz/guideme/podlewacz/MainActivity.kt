package pl.bydgoszcz.guideme.podlewacz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import pl.bydgoszcz.guideme.podlewacz.loaders.DataLoader
import pl.bydgoszcz.guideme.podlewacz.notifications.ItemAlarmScheduler
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationChannelCreator
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.views.TakePicture
import pl.bydgoszcz.guideme.podlewacz.views.ViewChanger
import pl.bydgoszcz.guideme.podlewacz.views.fragments.BackButtonHandler
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.TakingPictureThumbnail
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
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
    @Inject
    lateinit var itemAlarmScheduler: ItemAlarmScheduler

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
            itemAlarmScheduler.schedule()
            dataLoader.load {
                presenter.showUserItems()
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
        val currentFragment = supportFragmentManager.findFragmentById(frame_layout.id)

        if (currentFragment is BackButtonHandler) {
            currentFragment.onBack()
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
        super.onActivityResult(requestCode, resultCode, data)
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
            R.id.navSettings -> {
                presenter.showSettings()
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showBackButton(showBackButton: Boolean) {
        toggle.isDrawerIndicatorEnabled = !showBackButton
    }
}

