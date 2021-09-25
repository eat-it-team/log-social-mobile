package ru.ircover.socialmobile.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.model.Subsidy
import ru.ircover.socialmobile.presenter.SubsidiesPresenter
import ru.ircover.socialmobile.presenter.SubsidiesView
import ru.ircover.socialmobile.utils.ActivityExtras
import ru.ircover.socialmobile.utils.SocialApp
import ru.ircover.socialmobile.view.adapter.SubsidiesAdapter

class SubsidiesActivity : MvpAppCompatActivity(), SubsidiesView {
    private val presenter by moxyPresenter {
        SubsidiesPresenter(SocialApp.gson)
    }
    private val subsidiesAdapter = SubsidiesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subsidies)

        presenter.init(intent.getStringExtra(ActivityExtras.SUBSIDIES) ?: "[]")

        val subsidiesRecyclerView = findViewById<RecyclerView>(R.id.rvSubsidies)
        subsidiesRecyclerView.layoutManager = LinearLayoutManager(applicationContext,
            RecyclerView.VERTICAL, false)
        subsidiesRecyclerView.adapter = subsidiesAdapter
        subsidiesRecyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext, RecyclerView.VERTICAL).apply {
                setDrawable(resources.getDrawable(R.drawable.divider))
            })
    }

    override fun showSubsidies(subsidies: Array<Subsidy>) {
        subsidiesAdapter.subsidies = subsidies
    }
}