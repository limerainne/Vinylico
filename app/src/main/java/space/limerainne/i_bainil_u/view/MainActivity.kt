package space.limerainne.i_bainil_u.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.OnFragmentInteractionListener
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.domain.model.Wishlist
import space.limerainne.i_bainil_u.view.WishlistFragment.OnListFragmentInteractionListener
import space.limerainne.i_bainil_u.view.dummy.DummyContent

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnListFragmentInteractionListener {
    private val fragments: MutableMap<Int, Fragment> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton?
        fab!!.setOnClickListener { view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show() }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer!!.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView?
        navigationView!!.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            val homeFragment = HomeFragment.newInstance("1", "1")
            supportFragmentManager.beginTransaction().add(R.id.content_main, homeFragment, HomeFragment.TAG).commit()
            navigationView.setCheckedItem(R.id.nav_home)
            fragments.put(R.id.nav_home, homeFragment)

//            val wishlistFragment = WishlistFragment.newInstance(1)
//            supportFragmentManager.beginTransaction().add(R.id.content_main, wishlistFragment, WishlistFragment.TAG).commit()
//            navigationView.setCheckedItem(R.id.nav_wishlist)
//            fragments.put(R.id.nav_wishlist, wishlistFragment)

//            fragments = mutableMapOf(R.id.nav_wishlist to wishlistFragment) // TODO not correct way
        }

    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
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

        return super.onOptionsItemSelected(item)
    }

    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var hasToChangeFragment = false
        var fragmentTAG = ""

        Log.d("Test", item.itemId.toString())

        // Handle navigation view item clicks here.
        when (item.itemId)   {
            // change page fragment
            R.id.nav_home -> {
                hasToChangeFragment = true
                if (!fragments.containsKey(R.id.nav_home))   {
                    fragments[R.id.nav_home] = HomeFragment.newInstance("1", "1")
                }
                fragmentTAG = HomeFragment.TAG
            }
            R.id.nav_browse ->  {
                hasToChangeFragment = false
            }
            R.id.nav_wishlist -> {
                hasToChangeFragment = true
                if (!fragments.containsKey(R.id.nav_wishlist))   {
                    fragments[R.id.nav_wishlist] = WishlistFragment.newInstance(1)
                }
                fragmentTAG = WishlistFragment.TAG
            }
            R.id.nav_purchased -> {
                hasToChangeFragment = false
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

        if (hasToChangeFragment)    {
            var targetFragment = fragments[item.itemId]

            Log.d("Test", targetFragment.toString())

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.content_main, targetFragment, fragmentTAG)
            transaction.commit()
        }

        // close drawer
        (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        // throw UnsupportedOperationException()
    }

    override fun onListFragmentInteraction(item: AlbumEntry) {
        // throw UnsupportedOperationException()
    }
}
