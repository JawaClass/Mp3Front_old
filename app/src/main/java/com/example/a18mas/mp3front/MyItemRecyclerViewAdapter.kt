package com.example.a18mas.mp3front

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.example.a18mas.mp3front.UI.ItemFragment.OnListFragmentInteractionListener
import com.example.a18mas.mp3front.models.SearchResult

import kotlinx.android.synthetic.main.fragment_item.view.*
import android.support.v7.util.DiffUtil
import android.util.Log
import android.widget.*
import com.example.a18mas.mp3front.UI.myContext
import com.example.a18mas.mp3front.helper.streamMP3
import com.squareup.picasso.Picasso


//// import kotlinx.android.synthetic.main.item_search_result.view.*


/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(_mValues: Array<SearchResult>, //// List<DummyItem>
                                mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    //  private var mySpinnerAdapter: MyItemSpinnerAdapter = MyItemSpinnerAdapter()

    private val mOnClickListener: View.OnClickListener
    private var mValues: Array<SearchResult>? = null

    private var mViews: ArrayList<ViewHolder> = arrayListOf()

    fun checkAll() {

        this.mViews?.forEach {
            if (!it.mCheckBoxDownload.isChecked) {
                it.mCheckBoxDownload.toggle()
                it.mCheckBoxDownload.setOnCheckedChangeListener(null)
            }

        }
        this.mValues?.forEach {
            it.checked = true

        }
        update(this.mValues!!)

    }

    fun getToDownloadItems(): ArrayList<Pair<*, *>> {
        var downloads_ids = arrayListOf<Pair<*, *>>()


        this.mValues?.filter { it.checked }?.forEach { downloads_ids.add(Pair(it.video_id, it.title)) }
        return downloads_ids
    }

    fun update(data: Array<SearchResult>) {
        //mValues = data

        val diffCallback = SearchResultListDiffCallback(this.mValues, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.mValues = arrayOf<SearchResult>()
        this.mValues = data
        /// this.mValues = data
        diffResult.dispatchUpdatesTo(this)
    }


    init {
        mValues = _mValues
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as SearchResult // item_number tag as DummyItem
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            Log.i("stream","stream song $v , $item")
            streamMP3(item)



            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues!![position]
        //  model data
        holder.mIdView.text = position.toString()
        holder.mDurationView.text = item.duration_formatted
        holder.mTitleView.text = item.title///item.content
        holder.mChannelTitleView.text = item.channel_title///item.content


        mViews.add(holder)
        var stat = mValues!![position].checked
        //in some cases, it will prevent unwanted situations
        holder.mCheckBoxDownload.setOnCheckedChangeListener(null)

        holder.mCheckBoxDownload.isChecked = stat
        holder.mCheckBoxDownload.setOnCheckedChangeListener { compoundButton, b ->
            item.checked = !item.checked
        }

       /*
        holder.mView.setOnClickListener {
            Log.i("stream","stream song")
            streamMP3(mValues!![position])
        }
        */
        //


        // holder.mMoreVertView.setOnClickListener { this }
        // R.array.move_vert_arrays
        // mySpinnerAdapter
        // holder.mMoreSpinnerView.adapter = MyItemSpinnerAdapter()
        /*ArrayAdapter.createFromResource(myContext, null,
                 android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        holder.mMoreSpinnerView.adapter = adapter*/
        //

        Picasso.with(myContext).load(mValues!![position].thumbnail?.get("medium")).fit().into(holder.mThumbnailView.thumbnail)


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues!!.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mTitleView: TextView = mView.title
        val mThumbnailView: ImageView = mView.thumbnail
        val mDurationView: TextView = mView.duration
        val mChannelTitleView: TextView = mView.channel_title
        //
        val mCheckBoxDownload: CheckBox = mView.checkBox_Download

        //  val mMoreVertView: ImageView = mView.more_vert
        //  val mMoreSpinnerView: Spinner = mView.more_spinner

        override fun toString(): String {
            return super.toString() + " '" + mTitleView.text + "'"
        }
    }
}
