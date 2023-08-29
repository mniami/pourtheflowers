package pl.bydgoszcz.guideme.podlewacz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import pl.bydgoszcz.guideme.podlewacz.databinding.ActivityMainBinding
import pl.bydgoszcz.guideme.podlewacz.loaders.DataLoader
import pl.bydgoszcz.guideme.podlewacz.notifications.ItemAlarmScheduler
import pl.bydgoszcz.guideme.podlewacz.notifications.NotificationChannelCreator
import pl.bydgoszcz.guideme.podlewacz.repositories.ItemsRepository
import pl.bydgoszcz.guideme.podlewacz.views.IToolbarTitleContainer
import pl.bydgoszcz.guideme.podlewacz.views.TakePicture
import pl.bydgoszcz.guideme.podlewacz.views.ViewChanger
import pl.bydgoszcz.guideme.podlewacz.views.fragments.BackButtonHandler
import pl.bydgoszcz.guideme.podlewacz.views.fragments.FlowerListFragment
import pl.bydgoszcz.guideme.podlewacz.views.fragments.TakingPictureThumbnail
import pl.bydgoszcz.guideme.podlewacz.views.model.UiItem
import javax.inject.Inject


class MainActivity : AppCompatActivity(), IToolbarTitleContainer, NavigationView.OnNavigationItemSelectedListener, FlowerListFragment.OnListFragmentInteractionListener, MainActivityHelper {
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
    private lateinit var binding: ActivityMainBinding

    override var toolbarTitle
        get() = binding.appBarMain.toolbar.title
        set(value) { binding.appBarMain.toolbar.title = value }
    override fun onListFragmentInteraction(item: UiItem) {
        presenter.showItem(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        super.onCreate(savedInstanceState)

        setContentView(view)
        setSupportActionBar(binding.appBarMain.toolbar)

        presenter = MainActivityViewPresenter(supportFragmentManager, binding.appBarMain.contentMain.frameLayout.id)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toggle = ActionBarDrawerToggle(
                this, binding.drawerLayout, binding.appBarMain.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        toggle.setToolbarNavigationClickListener {
            onBackPressed()
        }
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.privacyPolicyLink.setOnClickListener {
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

        binding.navView.setNavigationItemSelectedListener(this)
        NotificationChannelCreator.createNotificationChannel(this)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(binding.appBarMain.contentMain.frameLayout.id)

        if (currentFragment is BackButtonHandler) {
            currentFragment.onBack()
            return
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun showBackButton(showBackButton: Boolean) {
        toggle.isDrawerIndicatorEnabled = !showBackButton
    }
}

