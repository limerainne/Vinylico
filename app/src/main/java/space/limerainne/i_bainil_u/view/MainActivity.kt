package space.limerainne.i_bainil_u.view

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnFragmentInteractionListener
import space.limerainne.i_bainil_u.base.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Wishlist
import space.limerainne.i_bainil_u.view.dummy.DummyContent

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnListFragmentInteractionListener {
    val fragments: MutableMap<Int, Fragment> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView = findViewById(R.id.nav_view) as NavigationView?
        navigationView!!.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.placeholder_top, mainFragment, MainFragment.TAG)
                    //.addToBackStack(MainFragment.TAG)
                    .commit()

            val homeFragment = HomeFragment.newInstance("1", "1")
            val parentFrag = getActiveFragment()
            if (parentFrag is MainFragment)
                parentFrag.changeChildFragment(homeFragment, HomeFragment.TAG)
            fragments.put(R.id.nav_home, homeFragment)
        }

    }

    override fun onBackPressed() {
        var eventProcessed = false

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        val activeFragment = getActiveFragment()

        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            eventProcessed = true
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
            eventProcessed = true
        } else if (activeFragment is MainFragment)   {
            eventProcessed = activeFragment.onBackPressed()
        }

        if (!eventProcessed)
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true
        }
        if (id == android.R.id.home)    {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var hasToChangeMainFragmentsChild = false
        var fragmentTAG = ""

        Log.d("Test", item.itemId.toString())

        // Handle navigation view item clicks here.
        when (item.itemId)   {
            // change page fragment
            R.id.nav_home -> {
                hasToChangeMainFragmentsChild = true
                if (!fragments.containsKey(R.id.nav_home))   {
                    fragments[R.id.nav_home] = HomeFragment.newInstance("1", "1")
                }
                fragmentTAG = HomeFragment.TAG
            }
            R.id.nav_browse ->  {
                hasToChangeMainFragmentsChild = true
                if (!fragments.containsKey(R.id.nav_browse))   {
                    fragments[R.id.nav_browse] = BrowseListFragment.newInstance(1)
                }
                fragmentTAG = WishlistFragment.TAG            }
            R.id.nav_wishlist -> {
                hasToChangeMainFragmentsChild = true
                if (!fragments.containsKey(R.id.nav_wishlist))   {
                    fragments[R.id.nav_wishlist] = WishlistFragment.newInstance(1)
                }
                fragmentTAG = WishlistFragment.TAG
            }
            R.id.nav_purchased -> {
                hasToChangeMainFragmentsChild = true
                if (!fragments.containsKey(R.id.nav_purchased))   {
                    fragments[R.id.nav_purchased] = PurchasedFragment.newInstance(1)
                }
                fragmentTAG = PurchasedFragment.TAG
            }
            // new activity
            R.id.nav_downloading -> {

            }
            R.id.nav_setting -> {
                // NOTE have to put a Java class, not Kotlin class!
                // not SettingsActivity.class or SettingsActivity.javaClass
                startActivityForResult(Intent(this, SettingsActivity::class.java), 0)
            }
            R.id.nav_about -> {

            }
        }

        if (hasToChangeMainFragmentsChild)    {
            val targetFragment = fragments[item.itemId]

            val frag = getActiveFragment()
            if (frag is MainFragment && targetFragment != null)
                frag.changeChildFragment(targetFragment, fragmentTAG)
        }

        // close drawer
        (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        return true
    }

    fun getActiveFragment(): Fragment? {
        if (supportFragmentManager.backStackEntryCount === 0) {
            return supportFragmentManager?.findFragmentByTag(MainFragment.TAG)
        }

        val tag = supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount - 1).name
        return supportFragmentManager.findFragmentByTag(tag)
    }

    fun linkDrawerToToolbar(toolbar: Toolbar)   {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.setDrawerListener(toggle)
        toggle.syncState()
    }

    fun setNavigationViewCheckedItem(itemId: Int)   {
        val navigationView = nav_view

        Log.d("NavView", itemId.toString())

        navigationView?.setCheckedItem(itemId)
    }

    fun setToolbarColor(colorId: Int = 0, darkColorId: Int = 0)   {
        var targetColorId = R.color.colorPrimary
        if (colorId != 0)
            targetColorId = colorId

        var targetDarkColorId = R.color.colorPrimaryDark
        if (darkColorId != 0)
            targetDarkColorId = darkColorId

        // toolbar color
        val bar = supportActionBar
        bar?.setBackgroundDrawable(ColorDrawable(resources.getColor(targetColorId)))

        // statusbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(targetDarkColorId)
        }
    }

    override fun onFragmentInteraction(uri: Uri) {
        // throw UnsupportedOperationException()
    }

    override fun onListFragmentInteraction(item: Any) {
        // throw UnsupportedOperationException()

        if (item is AlbumEntry) {
            val albumInfoFragment = AlbumInfoFragment.newInstance(item)
            supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
                    .addToBackStack(AlbumInfoFragment.TAG)
                    .commit()
        }
    }
}
