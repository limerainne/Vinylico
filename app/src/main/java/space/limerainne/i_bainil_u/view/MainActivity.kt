package space.limerainne.i_bainil_u.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
import android.webkit.CookieManager
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.tsengvn.typekit.TypekitContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.toast
import space.limerainne.i_bainil_u.R
import space.limerainne.i_bainil_u.base.*
import space.limerainne.i_bainil_u.domain.model.AlbumEntry
import space.limerainne.i_bainil_u.view.detail.AlbumInfoFragment
import space.limerainne.i_bainil_u.view.main.*
import space.limerainne.i_bainil_u.view.webview.LoginWebviewFragment
import space.limerainne.i_bainil_u.view.webview.LogoutWebviewFragment
import space.limerainne.i_bainil_u.view.webview.WebviewFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnListFragmentInteractionListener {
    val fragments: MutableMap<Int, Fragment> = mutableMapOf()

    lateinit var header_account_photo: ImageView
    lateinit var header_account_name: TextView
    lateinit var header_account_email: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // clear login info if...
        val loginInfoCleared = clearLoginTokenIf()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)
        val navigationViewInit = navigationView.getHeaderView(0)
        header_account_photo = navigationViewInit.account_photo
        header_account_name = navigationViewInit.account_name
        header_account_email = navigationViewInit.account_email

        if (!loginInfoCleared) {
            updateNavigationViewUserInfoArea()
        }

        if (savedInstanceState == null) {
            val mainFragment = MainFragment.newInstance()
            transitToFragment(R.id.placeholder_top, mainFragment, MainFragment.TAG, false)
            supportFragmentManager.executePendingTransactions()

//            val homeFragment = HomeFragment.newInstance("1", "1")
//            mainFragment.changeChildFragment(homeFragment, HomeFragment.TAG)
//            fragments.put(R.id.nav_home, homeFragment)
            val browseFragment = BrowseFragment.newInstance()
            mainFragment.changeChildFragment(browseFragment, BrowseFragment.TAG)
            fragments.put(R.id.nav_browse, browseFragment)
        }

        (findViewById(R.id.drawer_layout) as DrawerLayout).openDrawer(GravityCompat.START)
    }

    override fun onDestroy()    {
        super.onDestroy()

        // clear login info if user did not check auto-login
        clearLoginTokenIf()
    }

    fun clearLoginTokenIf(): Boolean {
        // true if info cleared
        // clear login info if user did not check auto-login
        val loginToken = LoginCookie(this)
        if (!loginToken.isAutoLogin)    {
            loginToken.clearCookie()
            UserInfo(this).clearInfo()
            return true
        }
        else {
            loginToken.clearCookieWithoutAutoLogin()
            return false
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase))
    }

    override fun onBackPressed() {
        try {
            var eventProcessed = false

            val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
            val activeFragment = getActiveFragment()

            Log.v("Activity", "BackStack: " + supportFragmentManager.backStackEntryCount.toString())
            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
                eventProcessed = true
            } else if (activeFragment is WebviewFragment && activeFragment.canGoBack())    {
                activeFragment.goBack()
            } else if (supportFragmentManager.backStackEntryCount >= 1) {
                popBackStack()
                eventProcessed = true
            } else if (activeFragment is MainFragment) {
                eventProcessed = activeFragment.onBackPressed()
            }

            Log.v("Activity", eventProcessed.toString())
            if (!eventProcessed)
                super.onBackPressed()
        }   catch (e: Exception) {
            e.printStackTrace()

            super.onBackPressed()
        }
    }

    fun popBackStack()  {
        supportFragmentManager.popBackStack()
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
        if (id == R.id.action_bainil_app)   {
            val topFrag = getActiveFragment()
            when    {
                topFrag is AlbumInfoFragment -> {
                    val albumEntry = topFrag.albumEntry
                    if (albumEntry != null)
                        BainilLauncher.executeBainilAppAlbumScreen(this, albumEntry.albumId)
                    else
                        BainilLauncher.executeBainilApp(this)
                }
                else -> {
                    // just open Bainil App.
                    BainilLauncher.executeBainilApp(this)
                }
            }

            return true;
        }
        if (id == R.id.action_settings) {
            return true
        }
        if (id == android.R.id.home)    {
            // up/back button in toolbar
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
                    fragments[R.id.nav_browse] = BrowseFragment.newInstance()
                }
                fragmentTAG = BrowseFragment.TAG
            }
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
            R.id.nav_login_logout ->    {
                UserInfo.checkLoginThenRun4(this, {
                    // Logout
                    // TODO
                    toast("Sorry, logout feature is in development...")
                    openLogoutPage()
                }, {
                    // Login
                    openLoginPage()
                })
            }
        }

        if (hasToChangeMainFragmentsChild)    {
            val targetFragment = fragments[item.itemId]

            if (targetFragment != null) {
                if (getActiveFragment() !is MainFragment)   {
                    supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                val frag = getActiveFragment()
                if (frag is MainFragment)
                    frag.changeChildFragment(targetFragment, fragmentTAG)
            }
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

    fun updateNavigationViewUserInfoArea()  {
        val userInfo = UserInfo(this)

        if (userInfo.userId > 0) {
            Picasso.with(this).load("http://cloud.bainil.com/upload/user" + userInfo.userImageURL).into(header_account_photo)
            header_account_name.text = userInfo.userName
            header_account_email.text = "${userInfo.userEmail} (#${userInfo.userId})"
        }
        else    {
            account_photo.setImageDrawable(getDrawable(android.R.drawable.sym_def_app_icon))
            // TODO move into resource
            account_name.text = "Bainil"
            account_email.text = "please.login@bainil.com"
        }
    }

    fun unsetNavigationViewCheckedItem()   {
        val navigationView = nav_view
        navigationView?.setCheckedItem(R.id.nav_none)
    }

    fun setNavigationViewCheckedItem(itemId: Int)   {
        val navigationView = nav_view

        Log.d("NavView", itemId.toString())

        navigationView?.setCheckedItem(itemId)
    }

    fun setToolbarColor(colorId: Int = 0, darkColorId: Int = 0)   {
        Log.v("MainActivity", "setToolbarColor")

        return  // do nothing!

        var targetColorId = R.color.colorPrimary
        if (colorId != 0)
            targetColorId = colorId

        var targetDarkColorId = R.color.colorPrimaryDark
        if (darkColorId != 0)
            targetDarkColorId = darkColorId

        // toolbar color
        val bar: Toolbar? = findViewById(R.id.toolbar) as Toolbar?
        if (bar != null)
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
            transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
        }
    }

    fun transitToFragment(targetPlaceHolder: Int, targetFragment: Fragment, targetTag: String)  {
        transitToFragment(targetPlaceHolder, targetFragment, targetTag, true)
    }

    fun transitToFragment(targetPlaceHolder: Int, targetFragment: Fragment, targetTag: String, addToBackStack: Boolean)  {
        val transaction = supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(targetPlaceHolder, targetFragment, targetTag)
        if (addToBackStack)
            transaction.addToBackStack(targetTag)
        transaction.commit()
    }

    fun openLoginPage() {
        val webviewFragment = LoginWebviewFragment.newInstance()
        transitToFragment(R.id.placeholder_top, webviewFragment, LoginWebviewFragment.TAG)
    }

    fun openLogoutPage() {
        val webviewFragment = LogoutWebviewFragment.newInstance()
        transitToFragment(R.id.placeholder_top, webviewFragment, LogoutWebviewFragment.TAG)
    }
}
