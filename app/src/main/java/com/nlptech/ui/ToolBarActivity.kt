package com.nlptech.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import com.android.inputmethod.latin.R

abstract class ToolBarActivity : AppCompatActivity() {

    private var mToolbar: Toolbar? = null

    protected abstract val layoutResource: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResource)

        mToolbar = findViewById(R.id.toolbar)
        setupSupportActionBar(mToolbar)
    }

    protected fun setupSupportActionBar(toolbar: Toolbar?) {
        if (toolbar != null) {
            setSupportActionBar(toolbar)
        }

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(enableDisplayHome())
        }
    }

    open fun enableDisplayHome() : Boolean {
        return true
    }
}
