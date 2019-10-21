package com.nlptech.function.gifsending.search

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.inputmethod.latin.R
import com.android.inputmethod.latin.databinding.GifSearchBinding
import com.bumptech.glide.Glide
import com.nlptech.common.utils.DensityUtil
import com.nlptech.common.utils.DisplayUtil
import com.nlptech.common.utils.ViewUtils
import com.nlptech.function.gifsending.GifSendingManager
import com.nlptech.function.gifsending.dataclass.GifItem
import com.nlptech.keybaordwidget.KeyboardWidget
import com.nlptech.keybaordwidget.KeyboardWidgetManager
import com.nlptech.keyboardview.theme.KeyboardThemeManager
import com.nlptech.ui.StaggeredGridLayoutEqualGapItemDecoration
import kotlinx.android.synthetic.main.gif_search.view.*

class GifSearchWidget : KeyboardWidget(),
        View.OnClickListener,
        GifSearchAdapter.Listener,
        TextView.OnEditorActionListener {

    private var binding: GifSearchBinding? = null
    private var viewModel: GifSearchViewModel? = null
    private var adapter: GifSearchAdapter? = null

    private var viewList = ArrayList<View>()
    private val mAttachListener = object : RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            viewList.add(view)
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            Glide.with(context).clear(view)
            Glide.get(context).clearMemory()
            viewList.remove(view)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?): View {
        binding = GifSearchBinding.inflate(inflater!!)
        return binding!!.root
    }

    override fun onViewCreated(intent: Intent?) {
        super.onViewCreated(intent)
        view.gif_search_btn.setOnClickListener(this)
        var keyboardHeight = KeyboardWidgetManager.getInstance().getTotalKeyboardHeight(context)
        val suggestionStripViewHeight = KeyboardWidgetManager.getInstance().getSuggestionStripViewHeight(context)
        val editTextLayoutHeight = resources.getDimensionPixelSize(R.dimen.gif_search_edit_text_layout_height)

        val editTextLayoutMarginVertical = resources.getDimensionPixelSize(R.dimen.gif_search_edit_text_layout_margin_vertical)

        // Layout
        view.gif_search_btn.setOnClickListener(this)
        view.gif_search_clear_layout.setOnClickListener(this)
        view.gif_search_clear_layout.visibility = View.GONE
        view.gif_search_background.setOnClickListener(this)

        // Search EditText
        view.gif_search_edit_text.imeOptions = EditorInfo.IME_ACTION_SEARCH
        view.gif_search_edit_text.setOnEditorActionListener(this)
        view.gif_search_suggestions_background.setOnClickListener(this)
        KeyboardThemeManager.getInstance().setUiModuleTitleBackground(view.gif_search_suggestions_background)
        KeyboardThemeManager.getInstance().setUiModuleContentBackground(view.gif_search_result_list)
        KeyboardThemeManager.getInstance().setUiModuleContentBackground(view.gif_search_no_result_layout)
        KeyboardThemeManager.getInstance().setUiModuleContentBackground(view.gif_search_fail_result_layout)
        KeyboardThemeManager.getInstance().setUiModuleContentBackground(view.gif_search_progress_layout)
        KeyboardThemeManager.getInstance().colorUiModuleIcon(view.gif_search_close_btn)

        // Suggestions
        val suggestionsLayoutLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, suggestionStripViewHeight)
        suggestionsLayoutLP.addRule(RelativeLayout.ABOVE, R.id.gif_search_result_list)
        view.gif_search_suggestions_layout.layoutParams = suggestionsLayoutLP
        view.gif_search_close_btn.setOnClickListener(this)

        // Result list
        val resultListLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight - suggestionStripViewHeight)
        resultListLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        view.gif_search_result_list.layoutParams = resultListLP
        view.gif_search_result_list.layoutManager = StaggeredGridLayoutManager(GifSearchAdapter.SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL)
        view.gif_search_result_list.setHasFixedSize(true)
        view.gif_search_result_list.addItemDecoration(StaggeredGridLayoutEqualGapItemDecoration(GifSearchAdapter.SPAN_COUNT, DensityUtil.dp2px(context, 6f)))
        adapter = GifSearchAdapter(context, this)
        view.gif_search_result_list.adapter = adapter
        view.gif_search_result_list.addOnChildAttachStateChangeListener(mAttachListener)
        view.gif_search_result_list.visibility = View.INVISIBLE

        // Progress
        val progressSubLP = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, keyboardHeight - suggestionStripViewHeight)
        progressSubLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        view.gif_search_progress_sub_layout.layoutParams = progressSubLP

        // No result
        KeyboardThemeManager.getInstance().colorUiModuleIcon(view.gif_search_no_result_image_view, 0xFF000000.toInt())
        KeyboardThemeManager.getInstance().colorUiModuleIcon(view.gif_search_fail_result_image_view, 0xFF000000.toInt())

        // ViewModel
        settingViewModel()
    }

    /**
     * 小米手機下方的Navigation bar高度
     * 若小米手機的Navigation為隱藏狀態，
     * 那麼Screen Height需要減掉此高度，
     * 才會是正確的
     */
    fun getMiSupplementHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier(
                "navigation_bar_height",
                "dimen",
                "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun settingViewModel() {
        viewModel = GifSearchViewModel(context.applicationContext as Application)
        // observe live data
        val gifItemsObserver = GifItemsObserver(viewModel!!, adapter!!)
        viewModel!!.getGifItemsLiveData().observe(this, gifItemsObserver)
        viewModel!!.getStatusLiveData().observe(this, Observer { status ->
            when (status) {
                GifSearchRepository.STATUS_WAIT_TO_INPUT -> {
                    view.gif_search_clear_layout.visibility = View.GONE
                    view.gif_search_result_list.visibility = View.INVISIBLE
                    view.gif_search_progress_layout.visibility = View.GONE
                    view.gif_search_no_result_layout.visibility = View.GONE
                    view.gif_search_fail_result_layout.visibility = View.GONE
                }
                GifSearchRepository.STATUS_IN_INPUTTING -> {
                    view.gif_search_clear_layout.visibility = View.VISIBLE
                    view.gif_search_result_list.visibility = View.INVISIBLE
                    view.gif_search_progress_layout.visibility = View.GONE
                    view.gif_search_no_result_layout.visibility = View.GONE
                    view.gif_search_fail_result_layout.visibility = View.GONE
                }
                GifSearchRepository.STATUS_IN_SEARCHING -> {
                    view.gif_search_clear_layout.visibility = View.VISIBLE
                    view.gif_search_result_list.visibility = View.INVISIBLE
                    view.gif_search_progress_layout.visibility = View.VISIBLE
                    view.gif_search_no_result_layout.visibility = View.GONE
                    view.gif_search_fail_result_layout.visibility = View.GONE
                }
                GifSearchRepository.STATUS_ON_SUCCESSFUL_RESULT -> {
                    view.gif_search_clear_layout.visibility = View.VISIBLE
                    view.gif_search_result_list.visibility = View.VISIBLE
                    view.gif_search_progress_layout.visibility = View.GONE
                    view.gif_search_no_result_layout.visibility = View.GONE
                    view.gif_search_fail_result_layout.visibility = View.GONE
                }
                GifSearchRepository.STATUS_ON_NO_RESULT -> {
                    view.gif_search_clear_layout.visibility = View.VISIBLE
                    view.gif_search_result_list.visibility = View.INVISIBLE
                    view.gif_search_progress_layout.visibility = View.GONE
                    view.gif_search_no_result_layout.visibility = View.VISIBLE
                    view.gif_search_fail_result_layout.visibility = View.GONE
                }
                GifSearchRepository.STATUS_ON_NO_NETWORK -> {
                    view.gif_search_clear_layout.visibility = View.VISIBLE
                    view.gif_search_result_list.visibility = View.INVISIBLE
                    view.gif_search_progress_layout.visibility = View.GONE
                    view.gif_search_no_result_layout.visibility = View.GONE
                    view.gif_search_fail_result_layout.visibility = View.VISIBLE
                }
            }
        })
        // set view model to binding
        binding!!.viewModel = viewModel

        view.gif_search_edit_text.enableImeEditText()

    }

    override fun onPause() {
        super.onPause()
        view.gif_search_edit_text.disableImeEditText()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewList.forEach {
            Glide.with(it).clear(it)
        }
        viewList.clear()
        Glide.get(context).clearMemory()
    }

    override fun onItemClick(view: View, gifItem: GifItem, position: Int) {
        ViewUtils.disableViewTemp(view)
        GifSendingManager.instance.send(gifItem)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.gif_search_background -> {
                KeyboardWidgetManager.getInstance().close(javaClass)
            }
            R.id.gif_search_clear_layout -> {
                val keyword = view.gif_search_edit_text.text.toString()
                if (keyword == "") {
                    return
                }
                view.gif_search_edit_text.setText("")
                adapter!!.submitList(null)
                viewModel!!.search("")
            }
            R.id.gif_search_btn -> {
                performSearch()
            }
            R.id.gif_search_close_btn -> {
                KeyboardWidgetManager.getInstance().close(javaClass)
            }
            R.id.gif_search_suggestions_background -> {
            }
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            performSearch()
            return true
        }
        return false
    }

    private fun performSearch() {
        val keyword = view.gif_search_edit_text.text.toString()
        if (keyword == "") {
            return
        }
        adapter!!.submitList(null)
        viewModel!!.search(keyword)
    }

    fun onHideSuggestionView() {
        view.gif_search_suggestions_background.visibility = View.VISIBLE
    }

    fun onShowSuggestionView() {
        view.gif_search_suggestions_background.visibility = View.GONE
    }

    override fun isExtendedInFloatingKeyboard(): Boolean {
        return true
    }

    class GifItemsObserver(val viewModel: GifSearchViewModel, val adapter: GifSearchAdapter) : Observer<PagedList<GifItem>> {
        var h = Handler()
        override fun onChanged(items: PagedList<GifItem>?) {
            adapter.submitList(items)
            if (items!!.size > 0) {
                viewModel.setStatus(GifSearchRepository.STATUS_ON_SUCCESSFUL_RESULT)
            } else {
                if (viewModel.getKeyword() == "") {
                    viewModel.setStatus(GifSearchRepository.STATUS_WAIT_TO_INPUT)
                } else {
                    viewModel.setStatus(GifSearchRepository.STATUS_ON_NO_RESULT)
                }
            }
        }
    }
}