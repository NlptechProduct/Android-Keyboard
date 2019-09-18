package com.nlptech.function.keyboardclipboard

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AbsListView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.inputmethod.latin.LatinIME
import com.android.inputmethod.latin.R
import com.nlptech.common.utils.DisplayUtil
import com.nlptech.keybaordwidget.KeyboardWidgetManager
import com.nlptech.keybaordwidget.draggable.DraggableKeyboardWidget
import com.nlptech.keybaordwidget.draggable.DraggableLayout
import com.nlptech.keyboardview.theme.KeyboardThemeManager


class KeyboardClipboardWidget : DraggableKeyboardWidget(), DraggableLayout.Callback {
    lateinit var mAdapter: Adapter
    lateinit var mRecyclerView: RecyclerView
    lateinit var mTips: LinearLayout
    var mToolBatHeight: Int = 0
    var mTotalKeyboardHeight: Int = 0
    var mScreenHeight: Int = 0
    var mScreenWidth: Int = 0
    var mRecyclerViewAtTop: Boolean = true

    lateinit var listener: ViewTreeObserver.OnGlobalLayoutListener

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?): DraggableLayout {
        val context = container!!.context
        val view = super.onCreateView(inflater, container)
        mScreenHeight = DisplayUtil.getScreenHeight(context)
        mScreenWidth = DisplayUtil.getScreenWidth(context)
        if (mScreenWidth > mScreenHeight) {
            mScreenWidth -= DisplayUtil.getNavigationBarHeight(context)
        }
        mTotalKeyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(context)

        return view
    }

    override fun onCreateContentView(inflater: LayoutInflater?): View {
        val view = inflater!!.inflate(R.layout.keyboard_clipboard, null)
        // background
        KeyboardThemeManager.getInstance().setUiModuleBackground(view)
        return view
    }

    override fun onDragStart() {
        super.onDragStart()
        mAdapter.closeMenuOpenedVH()
    }

    override fun isEnableHeightMode(context: Context): Boolean {
        return DisplayUtil.isOrientationPortrait(context)
    }

    override fun onViewCreated(intent: Intent?) {
        super.onViewCreated(intent)

        // title
        KeyboardThemeManager.getInstance().colorUiModuleTitleText(view.findViewById(R.id.keyboard_clipboard_tv))

        // close btn
        val close = view.findViewById<ImageView>(R.id.keyboard_selector_top_close_btn)
       KeyboardThemeManager.getInstance().colorUiModuleIcon(close, 0xFF000000.toInt())
        close.setOnClickListener { close() }

        mToolBatHeight = context.resources.getDimensionPixelOffset(R.dimen.keyboard_toolbar_height)
        mAdapter = Adapter(context, mScreenWidth, listener = object : ViewHolderListener {
            override fun onItemMenuOpened(vh: ViewHolder) {
                if (vh != mAdapter.menuOpenedVH) {
                    mAdapter.closeMenuOpenedVH()
                }
                mAdapter.menuOpenedVH = vh
            }

            override fun onItemClick(s: String) {
                LatinIME.getInstance().inputLogic.commitText(s, true)
            }

            override fun onItemDelete(position: Int) {
                ClipManager.getInstance().removeClipStringAt(position)
                mAdapter.removeItem(position)
                mAdapter.notifyItemRemoved(position)

                if (mAdapter.itemCount == 0) {
                    reloadData()
                }
            }
        })
        mRecyclerView = view.findViewById(R.id.recyclerView)

        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView.layoutManager = linearLayoutManager
        mRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    mAdapter.closeMenuOpenedVH()
                }
            }

            var currentScrollPosition = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentScrollPosition += dy
                mRecyclerViewAtTop = currentScrollPosition == 0
            }
        })

        mTips = view.findViewById(R.id.tips)
        mTips.layoutParams.height = mTotalKeyboardHeight - mToolBatHeight

        if (!DisplayUtil.isOrientationPortrait(context)) {
            mRecyclerView.layoutParams.height = mTotalKeyboardHeight - mToolBatHeight
        }
        setRecyclerView(mRecyclerView)
    }

    private fun reloadData() {
        val clipData = ClipManager.getInstance().clipData
        mTips.visibility = if (clipData.size == 0) View.VISIBLE else View.GONE
        mRecyclerView.visibility = if (clipData.size != 0) View.VISIBLE else View.GONE
        mAdapter.setItems(clipData)
    }

    override fun onResume() {
        super.onResume()
        listener = ViewTreeObserver.OnGlobalLayoutListener {
            mRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
            // setStateHighTranslationY by content
            val totalContentHeight = mRecyclerView.computeVerticalScrollRange() + 2 * mToolBatHeight
            if (totalContentHeight > mTotalKeyboardHeight) {
                var maxHeight = mScreenHeight - totalContentHeight
                if (maxHeight < 0) maxHeight = 0
                setStateHighTranslationY(maxHeight)
            } else {
                setEnableHighMode(false)
            }
        }
        mRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        reloadData()
        mRecyclerView.adapter = mAdapter
    }

    class Adapter(val context: Context, val screenWidth: Int, var listener: ViewHolderListener) : RecyclerView.Adapter<ViewHolder>() {

        private var items = ArrayList<String>()
        var menuOpenedVH: ViewHolder? = null

        fun setItems(items: List<String>) {
            this.items.clear()
            this.items.addAll(items)
        }

        fun removeItem(position: Int) {
            if (position >= this.items.size) {
                return
            }
            this.items.removeAt(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
            val li = LayoutInflater.from(context)
            val view = li.inflate(R.layout.keyboard_clipboard_rv_item, parent, false)
            return ViewHolder(view, screenWidth)
        }

        override fun getItemCount(): Int {
            return this.items.size
        }

        override fun onBindViewHolder(vh: ViewHolder, position: Int) {
            vh.bind(items[position], listener)
        }

        fun closeMenuOpenedVH() {
            menuOpenedVH?.closeMenu()
        }
    }

    interface ViewHolderListener {
        fun onItemClick(s: String)
        fun onItemDelete(position: Int)
        fun onItemMenuOpened(vh: ViewHolder)
    }

    class ViewHolder(view: View, private val screenWidth: Int) : RecyclerView.ViewHolder(view), SlidingButtonView.IonSlidingButtonListener {

        private var slidingButtonView: SlidingButtonView = view as SlidingButtonView
        lateinit var listener: ViewHolderListener
        private val contentText: TextView
        private val deleteItem: ImageView
        private var menu: SlidingButtonView? = null

        init {
            slidingButtonView.setSlidingButtonListener(this)
            contentText = slidingButtonView.findViewById(R.id.text)
            KeyboardThemeManager.getInstance().colorUiModuleText(contentText)
            deleteItem = slidingButtonView.findViewById(R.id.delete_icon)
        }

        fun bind(text: String, listener: ViewHolderListener) {
            contentText.text = text
            contentText.layoutParams.width = screenWidth
            contentText.setOnClickListener {
                if (menuIsOpen()) {
                    closeMenu()
                } else {
                    this.listener.onItemClick(contentText.text.toString())
                }
            }

            this.deleteItem.setOnClickListener {
                this.listener.onItemDelete(adapterPosition)
            }

            this.listener = listener
        }

        override fun onMenuIsOpen(view: View) {
            menu = view as SlidingButtonView
            listener.onItemMenuOpened(this)
        }

        override fun onDownOrMove(slidingButtonView: SlidingButtonView) {
            if (menuIsOpen()) {
                if (this.menu != slidingButtonView) {
                    slidingButtonView.closeMenu()
                }
            }
        }

        private fun menuIsOpen(): Boolean {
            if (menu != null) {
                return true
            }
            return false
        }

        fun closeMenu() {
            menu?.closeMenu()
            menu = null
        }
    }
}