package com.example.apicalldemo.synTabList

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout

class TabbedListMediator (private val mRecyclerView: RecyclerView,
private val mTabLayout: TabLayout,
private var mIndices: List<Int>,
private var mIsSmoothScroll: Boolean = false
) {

    private var mIsAttached = false

    private var mRecyclerState = RecyclerView.SCROLL_STATE_IDLE
    private var mTabClickFlag = false

    private val smoothScroller: RecyclerView.SmoothScroller =
        object : LinearSmoothScroller(mRecyclerView.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }

    private var tabViewCompositeClickListener: TabViewCompositeClickListener =
        TabViewCompositeClickListener(mTabLayout)

    fun attach() {
        mRecyclerView.adapter
            ?: throw RuntimeException("Cannot attach with no Adapter provided to RecyclerView")

        if (mTabLayout.tabCount == 0)
            throw RuntimeException("Cannot attach with no tabs provided to TabLayout")

        if (mIndices.size > mTabLayout.tabCount)
            throw RuntimeException("Cannot attach using more indices than the available tabs")

        notifyIndicesChanged()
        mIsAttached = true
    }
    private fun notifyIndicesChanged() {
        tabViewCompositeClickListener.addListener { _, _ -> mTabClickFlag = true }
        tabViewCompositeClickListener.build()
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener)
        mRecyclerView.addOnScrollListener(onScrollListener)
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {

            if (!mTabClickFlag) return

            val position = tab.position
            Log.e(javaClass.simpleName, "onTabSelected: $position")
            if (mIsSmoothScroll){
                if (position == 0){
                    smoothScroller.targetPosition = mIndices[0]
                    mRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
                else if (position ==1){
                    smoothScroller.targetPosition = mIndices[1]
                    mRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
                else if (position ==2){
                    smoothScroller.targetPosition = mIndices[2]
                    mRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
                else if (position ==3){
                    smoothScroller.targetPosition = mIndices[3]
                    mRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                }
            }
            else{
                (mRecyclerView.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(mIndices[position], 0)
                mTabClickFlag = false
            }
            /*if (mIsSmoothScroll) {
                smoothScroller.targetPosition = mIndices[position]
                mRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
            } else {
                (mRecyclerView.layoutManager as LinearLayoutManager?)?.scrollToPositionWithOffset(mIndices[position], 0)
                mTabClickFlag = false
            }*/
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            mRecyclerState = newState
            if (mIsSmoothScroll && newState == RecyclerView.SCROLL_STATE_IDLE) {
                mTabClickFlag = false
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (mTabClickFlag) {
                return
            }

            val linearLayoutManager: LinearLayoutManager =
                recyclerView.layoutManager as LinearLayoutManager?
                    ?: throw RuntimeException("No LinearLayoutManager attached to the RecyclerView.")

            var itemPosition =
                linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            Log.e(javaClass.simpleName, "onScrolled: $itemPosition")
            if (itemPosition == -1) {
                itemPosition = linearLayoutManager.findFirstVisibleItemPosition()
                Log.e(javaClass.simpleName, "onScrolled if: $itemPosition")
            }

            if (mRecyclerState == RecyclerView.SCROLL_STATE_DRAGGING
                || mRecyclerState == RecyclerView.SCROLL_STATE_SETTLING
            ) {
                for (i in mIndices.indices) {
                    if (itemPosition == mIndices[i]) {
                        if (!mTabLayout.getTabAt(i)!!.isSelected) {
                            mTabLayout.getTabAt(i)!!.select()
                        }
                        if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == mIndices[mIndices.size - 1]) {
                            if (!mTabLayout.getTabAt(mIndices.size - 1)!!.isSelected) {
                                mTabLayout.getTabAt(mIndices.size - 1)!!.select()
                            }
                            return
                        }
                    }
                }
            }
        }
    }
}