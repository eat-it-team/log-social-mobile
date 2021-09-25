package ru.ircover.socialmobile.presenter

import com.google.gson.Gson
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.ircover.socialmobile.model.Subsidy

interface SubsidiesView : MvpView {
    @AddToEndSingle
    fun showSubsidies(subsidies: Array<Subsidy>)
}

@InjectViewState
class SubsidiesPresenter(private val gson: Gson) : MvpPresenter<SubsidiesView>() {
    private var subsidies: Array<Subsidy> = arrayOf()

    fun init(subsidiesJson: String) {
        subsidies = gson.fromJson(subsidiesJson, Array<Subsidy>::class.java)
        viewState.showSubsidies(subsidies)
    }
}