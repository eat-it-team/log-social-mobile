package ru.ircover.socialmobile.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView

interface SubsidiesView : MvpView {

}

@InjectViewState
class SubsidiesPresenter : MvpPresenter<SubsidiesView>() {
}