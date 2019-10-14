package com.nlptech.function.theme.theme_manage

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.ViewholderThemeBinding
import com.nlptech.common.utils.DisplayUtil

class ThemeManageAdapter(val context: Context, val listener: Listener) : RecyclerView.Adapter<ThemeManageItemViewHolder>() {

    interface Listener {
        fun onItemClick(view: View, themeManageItem: ThemeManageItem, position: Int)
        fun onThemeApply(view: View, position: Int)
        fun onKeyBorderSwitchChanged(buttonView: CompoundButton?, checked: Boolean)
        fun onDarkModeChanged(buttonView: CompoundButton, checked: Boolean)
    }

    companion object {
        const val SPAN_COUNT = 2
        const val IMAGE_HEIGHT = 0.8f // of item width
    }

    private var mList: List<ThemeManageItem>
    private var mItemWidth: Int
    private var mItemHeight: Int

    init {
        mList = ArrayList<ThemeManageItem>()
        var itemDecorationWidth = context.resources.getDimensionPixelSize(R.dimen.theme_manage_item_decoration_width)
        mItemWidth = (DisplayUtil.getScreenWidth(context) - itemDecorationWidth * (SPAN_COUNT + 1)) / SPAN_COUNT
        mItemHeight = (mItemWidth * IMAGE_HEIGHT).toInt()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ThemeManageItemViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.getContext())
        val binding = ViewholderThemeBinding.inflate(layoutInflater, viewGroup, false)
        return ThemeManageItemViewHolder(binding, mItemWidth, mItemHeight)
    }

    override fun onBindViewHolder(holder: ThemeManageItemViewHolder, position: Int) {
        val themeManagerItem = mList[position]
        holder.bind(themeManagerItem, listener, position)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(themes: List<ThemeManageItem>) {
        mList = themes
        notifyDataSetChanged()
    }

}
