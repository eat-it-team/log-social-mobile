package ru.ircover.socialmobile.view.activity

import android.os.Bundle
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.presenter.SubsidiesPresenter
import ru.ircover.socialmobile.presenter.SubsidiesView

class SubsidiesActivity : MvpAppCompatActivity(), SubsidiesView {
    private val presenter by moxyPresenter {
        SubsidiesPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subsidies)
    }
}