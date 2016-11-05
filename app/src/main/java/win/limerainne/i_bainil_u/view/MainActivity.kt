package win.limerainne.i_bainil_u.view

import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.os.EnvironmentCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.transition.TransitionInflater
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.CookieSyncManager
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import com.tsengvn.typekit.TypekitContextWrapper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import win.limerainne.i_bainil_u.ThisApp
import win.limerainne.i_bainil_u.R
import win.limerainne.i_bainil_u.base.*
import win.limerainne.i_bainil_u.credential.LoginCookie
import win.limerainne.i_bainil_u.credential.UserInfo
import win.limerainne.i_bainil_u.domain.model.*
import win.limerainne.i_bainil_u.extension.DelegatesExt
import win.limerainne.i_bainil_u.extension.Preference
import win.limerainne.i_bainil_u.extension.context
import win.limerainne.i_bainil_u.toolbox.BainilLauncher
import win.limerainne.i_bainil_u.toolbox.DownloadTool
import win.limerainne.i_bainil_u.view.detail.AlbumInfoFragment
import win.limerainne.i_bainil_u.view.main.*
import win.limerainne.i_bainil_u.view.webview.LoginWebviewFragment
import win.limerainne.i_bainil_u.view.webview.LogoutWebviewFragment
import win.limerainne.i_bainil_u.view.webview.WebviewFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnListFragmentInteractionListener {
    val fragments: MutableMap<Int, Fragment> = mutableMapOf()

    lateinit var header_account_photo: ImageView
    lateinit var header_account_name: TextView
    lateinit var header_account_email: TextView

    lateinit private var mFirebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // clear login info if...
        val loginInfoCleared = clearLoginTokenIf()
        getJSESSIONTokenIf()

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

        if (ThisApp.CommonPrefs.showMenu)
            (findViewById(R.id.drawer_layout) as DrawerLayout).openDrawer(GravityCompat.START)

        supportFragmentManager.addOnBackStackChangedListener {
            val activeFragment = activeFragment
            if (activeFragment is HavingToolbar)
                activeFragment.initToolbar()
            if (activeFragment is InteractWithMainActivity)
                activeFragment.interactTo()
        }

        // start Firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    }

    override fun onDestroy()    {
        super.onDestroy()

        // clear login info if user did not check auto-login
        clearLoginTokenIf()
    }

    fun clearLoginTokenIf(): Boolean {
        // return true if info cleared

        // remove cookie in webview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().removeAllCookies {  }
        else {
            CookieSyncManager.createInstance(context)
            CookieManager.getInstance().removeAllCookie()
        }

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

    fun getJSESSIONTokenIf()  {
        val loginToken = LoginCookie(this)

        fun setCookieTo(tag: String, value: String) {
            CookieManager.getInstance().setCookie(ThisApp.COOKIE_URL, "${tag}=${value}")
        }

        if (loginToken.isAutoLogin) {
            setCookieTo("auth_token", loginToken.auth_token)
            setCookieTo("email", loginToken.email)
            setCookieTo("remember", loginToken.remember)
        }

        doAsync {
            loginToken.getToken()
            uiThread {
                if (loginToken.haveLoginCookie) {
                    setCookieTo("JSESSIONID", loginToken.JSESSIONID)
                    setCookieTo("AWSELB", loginToken.AWSELB)
                }

                // TODO TEST
//                val dlTool = DownloadTool.newInstance(12657L, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), "DownloadTest", "Youngjoon")
//                dlTool.doDownload(this@MainActivity.context)
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase))
    }

    override fun onBackPressed() {
        try {
            var eventProcessed = false

            val drawer = findViewById(R.id.drawer_layout) as DrawerLayout?
            val activeFragment = activeFragment

            Log.v("Activity", "BackStack: " + supportFragmentManager.backStackEntryCount.toString())
            if (drawer!!.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
                eventProcessed = true
            } else if (activeFragment is WebviewFragment && activeFragment.canGoBack())    {
                Log.v("Activity", "Webview: Can go back!")
                activeFragment.goBack()
                eventProcessed = true
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

    private var searchKeyword: String? = null
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)

        val searchMenu = menu.findItem(R.id.action_search)
        val searchView = searchMenu.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener    {
            override fun onQueryTextChange(newText: String?): Boolean {
                // TODO implement autocomplete
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                val query = query
                if (query == null)
                    return false

                searchView.setIconified(true);
                searchView.clearFocus();

                MenuItemCompat.collapseActionView(searchMenu)

                searchKeyword = query

                val menu = this@MainActivity.nav_view.menu.findItem(R.id.nav_search_result)
                this@MainActivity.onNavigationItemSelected(menu)

                return true
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_bainil_app)   {
            val topFrag = activeFragment
            when    {
                topFrag is AlbumInfoFragment -> {
                    if (topFrag.albumId > 0)
                        BainilLauncher.executeBainilAppAlbumScreen(this, topFrag.albumId)
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
            R.id.nav_search_result -> {
                hasToChangeMainFragmentsChild = true
                if (!fragments.containsKey(R.id.nav_search_result))   {
                    fragments[R.id.nav_search_result] = SearchResultFragment.newInstance(searchKeyword ?: "")
                }   else    {
                    val searchFragment = fragments[R.id.nav_search_result] as SearchResultFragment
                    searchFragment.refresh(searchKeyword ?: "")
                }
                searchKeyword = null
                fragmentTAG = SearchResultFragment.TAG
            }
            // new activity
            R.id.nav_downloading -> {
                // open download manager
                // ref: http://stackoverflow.com/questions/9676786/how-to-launch-download-manager-from-broadcast-receiver
                val dm = Intent(DownloadManager.ACTION_VIEW_DOWNLOADS)
                dm.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(dm)
            }
            R.id.nav_setting -> {
                // NOTE have to put a Java class, not Kotlin class!
                // not SettingsActivity.class or SettingsActivity.javaClass
//                startActivityForResult(Intent(this, SettingsActivity::class.java), 0)

                hasToChangeMainFragmentsChild = false
                if (!fragments.containsKey(R.id.nav_setting))   {
                    fragments[R.id.nav_setting] = SettingsFragment.newInstance()
                }
                fragmentTAG = SettingsFragment.TAG

                val frag = fragments[R.id.nav_setting]
                if (frag != null && activeFragment !is SettingsFragment)
                    transitToFragment(R.id.placeholder_top, frag, SettingsFragment.TAG)
            }
            R.id.nav_about -> {

            }
            R.id.nav_login_logout ->    {
                if (activeFragment !is LoginWebviewFragment && activeFragment !is LogoutWebviewFragment) {
                    UserInfo.checkLoginThenRun4(this, {
                        // Logout
                        openLogoutPage()
                    }, {
                        // Login
                        openLoginPage()
                    })
                }
            }
        }

        if (hasToChangeMainFragmentsChild)    {
            val targetFragment = fragments[item.itemId]

            if (targetFragment != null) {
                if (activeFragment !is MainFragment)   {
                    supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
                val frag = activeFragment
                if (frag is MainFragment)
                    frag.changeChildFragment(targetFragment, fragmentTAG)
            }
        }

        // close drawer
        (findViewById(R.id.drawer_layout) as DrawerLayout).closeDrawer(GravityCompat.START)

        return true
    }

    val activeFragment: Fragment?
        get() {
            return supportFragmentManager.findFragmentById(R.id.placeholder_top)
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

        val navigationView = nav_view
        // TODO toggle login/logout msg
        val menu = navigationView.menu
        val logInOutMenu = menu.findItem(R.id.nav_login_logout)

        if (userInfo.userId > 0) {
            Picasso.with(this).load(userInfo.userImageURLFull).into(header_account_photo)
            header_account_name.text = userInfo.userName
            header_account_email.text = "${userInfo.userEmail} (#${userInfo.userId})"

            logInOutMenu.setTitle(R.string.nav_logout)
        }
        else    {
            account_photo.setImageDrawable(getDrawable(android.R.drawable.sym_def_app_icon))
            // TODO move into resource
            account_name.text = "Bainil"
            account_email.text = "please.login@bainil.com"

            logInOutMenu.setTitle(R.string.nav_login)
        }
    }

    fun unsetNavigationViewCheckedItem()   {
        val navigationView = nav_view
        navigationView?.setCheckedItem(R.id.nav_top_none)
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

        when (item) {
            is AlbumEntry -> {
                val albumInfoFragment = AlbumInfoFragment.newInstance(item)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                animatedTransitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG, true)
//            } else  {
                transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
//            }
            }
            is SearchArtist ->  {
                val albumInfoFragment = AlbumInfoFragment.newInstance(item.albumId, item.albumName, item.artistName)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                animatedTransitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG, true)
//            } else  {
                transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
//            }
            }
            is SearchAlbum ->  {
                val albumInfoFragment = AlbumInfoFragment.newInstance(item.albumId, item.albumName, item.artistName)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                animatedTransitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG, true)
//            } else  {
                transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
//            }
            }
            is SearchTrack ->  {
                val albumInfoFragment = AlbumInfoFragment.newInstance(item.albumId, "", item.artistName)

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                animatedTransitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG, true)
//            } else  {
                transitToFragment(R.id.placeholder_top, albumInfoFragment, AlbumInfoFragment.TAG)
//            }
            }
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun animatedTransitToFragment(targetPlaceHolder: Int, targetFragment: Fragment, targetTag: String, addToBackStack: Boolean)  {
        val changeTransform = TransitionInflater.from(this).inflateTransition(R.transition.change_image_transform)
        val explodeTransform = TransitionInflater.from(this).inflateTransition(android.R.transition.explode)

        // Setup exit transition on first fragment
        activeFragment?.setSharedElementReturnTransition(changeTransform)
        activeFragment?.setExitTransition(explodeTransform)

        // Setup enter transition on second fragment
        targetFragment.setSharedElementEnterTransition(changeTransform)
        targetFragment.setEnterTransition(explodeTransform)

        // Find the shared element (in Fragment A)
        val album_cover = findViewById(R.id.album_cover) as ImageView

        val transaction = supportFragmentManager.beginTransaction()
//                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(targetPlaceHolder, targetFragment, targetTag)
        if (addToBackStack)
            transaction.addToBackStack(targetTag)

        // shared elem
        transaction.addSharedElement(album_cover, "album_cover")
        transaction.commit()
    }

    fun openWebpage(url: String, title: String)    {
        val webviewFragment = WebviewFragment.newInstance(url, title)
        transitToFragment(R.id.placeholder_top, webviewFragment, WebviewFragment.TAG)
    }

    fun openLoginPage() {
        val webviewFragment = LoginWebviewFragment.newInstance()
        transitToFragment(R.id.placeholder_top, webviewFragment, LoginWebviewFragment.TAG)
    }

    fun openLogoutPage() {
        val webviewFragment = LogoutWebviewFragment.newInstance()
        transitToFragment(R.id.placeholder_top, webviewFragment, LogoutWebviewFragment.TAG)
    }

    // A method to find height of the status bar
    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}