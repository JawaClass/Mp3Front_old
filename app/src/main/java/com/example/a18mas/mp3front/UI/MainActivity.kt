package com.example.a18mas.mp3front.UI

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.a18mas.mp3front.R
import com.example.a18mas.mp3front.helper.*
import com.example.a18mas.mp3front.models.SearchResult
import kotlinx.android.synthetic.main.activity_main.*


var myContext: Context? = null
var myActivity: Activity? = null


class MainActivity : AppCompatActivity(),
        SearchView.OnQueryTextListener,
        MyEventListener,
        MyHttpClientListener,
        ItemFragment.OnListFragmentInteractionListener {


    override fun onListFragmentInteraction(item: SearchResult?) {
        Log.i(TAG, "onListFragmentInteraction: $item")
    }


    override fun OnHaveToHide(hide: Boolean, function: () -> Unit) {
        ////Log.i(TAG, "onHaveToHide $hide")
        hideBottomPlayer(hide)
    }


    override fun OnSetNewDataSource(function: () -> Unit) {
        ///Log.i(TAG, "OnSetNewDataSource")
    }


    override fun OnCompletion(function: () -> Unit) {
        ///Log.i(TAG, "OnCompletion")
    }

    private var item_Fragment: ItemFragment? = null

    var bottomPlayer_Fragment: BottomPlayer? = null
    private var main_Fragment: MainActivityFragment? = null
    private var searchResultList_Fragment: SearchResultList? = null
    private var TAG = "MAIN-ACTIVITY"
    private var searchView: SearchView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        myContext = this
        myActivity = this

        // Configure the search info and add any event listeners...

        /// for child activities....
        // my_child_toolbar is defined in the layout file
        ///setSupportActionBar(findViewById(R.id.my_child_toolbar))
        // Get a support ActionBar corresponding to this toolbar and enable the Up button
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)


        fab.setOnClickListener { view ->
            view?.makeSnackbarMessage("make message!!!!!!!")
            ///hideBottomPlayer()
        }


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (fragment_container_bottom != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return
            }

            // Create a new Fragment to be placed in the activity layout
            bottomPlayer_Fragment = BottomPlayer()

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            bottomPlayer_Fragment?.setArguments(intent.extras)

            // Add the fragment to the 'fragment_container' FrameLayout
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_bottom, bottomPlayer_Fragment as BottomPlayer).commit()

            main_Fragment = MainActivityFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container_center, main_Fragment as MainActivityFragment).commit()
        }


    }

    override fun OnFailed(exception: Exception, function: () -> Unit) {
        Log.i(TAG, "OnFailed")
        this@MainActivity.runOnUiThread(java.lang.Runnable {
            myContext?.makeLongToastMessage("Connection Error")

        })
    }

    /// one query done
    override fun OnDoneSuccess(data: Array<SearchResult>, function: () -> Unit) {
        this@MainActivity.runOnUiThread(java.lang.Runnable {

            myContext?.makeLongToastMessage("Update Search Result")

            Log.i(TAG, "OnDoneSuccess")
            if (item_Fragment == null) {
                Log.i(TAG, "INIT...")

                item_Fragment = ItemFragment()
                val args = Bundle()

                args.putParcelableArray("init-data", data)
                item_Fragment?.arguments = args
                val transaction = supportFragmentManager.beginTransaction()
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction?.replace(R.id.fragment_container_center, item_Fragment as ItemFragment)
                transaction?.addToBackStack(null)
                // Commit the transaction
                Log.i(TAG, "commit")

                transaction?.commit()
            } else {
                Log.i(TAG, "UPDATE...")
                item_Fragment?.update(data)
            }
            // }
        })
    }

    /// handle query and start search result fragment
    override fun onQueryTextSubmit(p0: String?): Boolean {
        // data
        if (httpClient == null) {
            httpClient = HttpClient()
            httpClient?.setOnDoneSuccess(this)

        }

        httpClient?.fetchSearchResultData(p0 as String)
        // TODO Loader

        Log.i("search", "onSubmit")
        closeKeyboard()


        // Create fragment and give it an argument specifying the article it should
        if (searchResultList_Fragment == null) {
            searchResultList_Fragment = SearchResultList()
            searchResultList_Fragment?.setOnHaveToHideListener(this)


        }
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        Log.i("search", "onChange")
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)


        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        searchView?.setOnQueryTextListener(this)
        searchView?.queryHint = "Title:"

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.action_home -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_center, main_Fragment as MainActivityFragment).commit()
            true
        }

        R.id.action_settings -> {
            true
        }

        R.id.action_back -> {
            supportFragmentManager.popBackStackImmediate()
            true
        }


        R.id.by_title -> {
            Log.i("by_title", "${item.itemId}")
            searchMode = 0
            searchView?.queryHint = "Title:"

            this@MainActivity.runOnUiThread(java.lang.Runnable {
                myContext?.makeLongToastMessage("Title Search")

            })


            true
        }
        R.id.by_artist -> {
            Log.i("by_artist", "${item.itemId}")
            searchMode = 1
            searchView?.queryHint = "Artist:"
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                myContext?.makeLongToastMessage("Artist Search")

            })
            true
        }

        R.id.action_download -> {
            httpClient?.download(item_Fragment?.download()!!)
            true
        }

        R.id.action_check_all -> {
            item_Fragment?.check_all()

            true
        }

        R.id.action_search -> {
            // search logic...

            //
            super.onOptionsItemSelected(item)
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }


    }

    fun closeKeyboard() {
        val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus.windowToken, InputMethodManager.SHOW_FORCED)
    }

    fun hideBottomPlayer(hide: Boolean) {
        ///Log.i(TAG, "visible=${bottomPlayer_Fragment?.isVisible}")

        when (hide) {
            true -> {
                if (bottomPlayer_Fragment?.isVisible == true)
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in).hide(bottomPlayer_Fragment as Fragment).commit()
            }
            false -> {
                ////Log.i(TAG, "SHOW")

                if (bottomPlayer_Fragment?.isVisible == false)
                    supportFragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).show(bottomPlayer_Fragment as Fragment).commit()

            }

        }

    }
}


