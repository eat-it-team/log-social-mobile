package ru.ircover.socialmobile.presenter

import androidx.annotation.StringRes
import com.google.gson.Gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.api.Api
import ru.ircover.socialmobile.model.DisabledGroup
import ru.ircover.socialmobile.utils.UserSessionWorker

interface CalculatorView : MvpView {
    @AddToEndSingle
    fun setAge(age: Int)
    @AddToEndSingle
    fun setRetired(isRetired: Boolean)
    @AddToEndSingle
    fun setDisabled(isDisabled: Boolean)
    @AddToEndSingle
    fun setDisabledGroup(disabledGroup: DisabledGroup)
    @AddToEndSingle
    fun setDisabledGroupVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setMarried(isMarried: Boolean)
    @AddToEndSingle
    fun setMarriedVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setIncome(income: Int)
    @AddToEndSingle
    fun setIncomeVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setChildrenCount(childrenCount: Int)
    @AddToEndSingle
    fun setDisabledChildrenVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setHasDisabledChildren(hasDisabledChildren: Boolean)
    @AddToEndSingle
    fun setEmptyAgeWarningVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setEmptyIncomeWarningVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun showMessage(@StringRes stringId: Int)
    @AddToEndSingle
    fun showMessage(string: String)
    @AddToEndSingle
    fun openSubsidies(subsidies: String)
    @AddToEndSingle
    fun setMainProgressBarVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun finishActivity()
    @AddToEndSingle
    fun setIncompleteFamily(isIncompleteFamily: Boolean)
    @AddToEndSingle
    fun setIncompleteFamilyVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setYoungFamily(isYoungFamily: Boolean)
    @AddToEndSingle
    fun setYoungFamilyVisibility(isVisible: Boolean)
}

@InjectViewState
class CalculatorPresenter(private val api: Api,
                          private val gson: Gson,
                          private val userSessionWorker: UserSessionWorker) : MvpPresenter<CalculatorView>() {
    private var isInitialized = false

    var age: Int? = null
        set(value) {
            if(field != value) {
                field = value
                viewState.apply {
                    setMarriedVisibility((value ?: 0) > 18)
                    setIncomeVisibility((value ?: 0) > 18)
                    setEmptyAgeWarningVisibility(isInitialized && value == null)
                    setIncompleteFamilyVisibility(value != null && value < 18)
                    setYoungFamilyVisibility((value ?: 0) > 18 && isMarried)
                }
            }
        }
    var isRetired = false
    var isDisabled = false
        set(value) {
            field = value
            viewState.setDisabledGroupVisibility(value)
        }
    var disabledGroup: DisabledGroup? = null
    var isMarried = false
        set(value) {
            field = value
            viewState.setYoungFamilyVisibility((age ?: 0) > 18 && value)
        }
    var income: Int? = null
        set(value) {
            if(field != value) {
                field = value
                viewState.apply {
                    setEmptyIncomeWarningVisibility(isInitialized && value == null)
                }
            }
        }
    var childrenCount: Int? = null
        set(value) {
            field = value
            viewState.setDisabledChildrenVisibility((value ?: 0) > 0)
        }
    var hasDisabledChildren = false
    var isIncompleteFamily = false
    var isYoungFamily = false

    fun init() {
        viewState.apply {
            age?.let { setAge(it) }
            setMarriedVisibility((age ?: 0) > 18)
            setIncomeVisibility((age ?: 0) > 18)
            setIncompleteFamilyVisibility(age != null && (age ?: 0) < 18)
            setEmptyAgeWarningVisibility(isInitialized && age == null)
            setRetired(isRetired)
            setDisabled(isDisabled)
            setDisabledGroupVisibility(isDisabled)
            disabledGroup?.let { setDisabledGroup(it) }
            setMarried(isMarried)
            setYoungFamilyVisibility((age ?: 0) > 18 && isMarried)
            setYoungFamily(isYoungFamily)
            income?.let { setIncome(it) }
            setEmptyIncomeWarningVisibility(isInitialized && income == null && (age ?: 0) > 18)
            childrenCount?.let { setChildrenCount(it) }
            setDisabledChildrenVisibility((childrenCount ?: 0) > 0)
            setHasDisabledChildren(hasDisabledChildren)
            setIncompleteFamily(isIncompleteFamily)
        }
        isInitialized = true
    }

    fun sendRequest() {
        viewState.apply {
            if(age == null || (age ?: 0) > 18 && income == null) {
                showMessage(R.string.request_not_complete)
                setEmptyAgeWarningVisibility(age == null)
                setEmptyIncomeWarningVisibility(income == null && (age ?: 0) > 18)
            } else {
                //TODO: ?????????????????? ???????????????? ???? ?????????????????? ????????????, ?? ???? ???? ???????????????????????????? ??????????
                viewState.setMainProgressBarVisibility(true)
                Observable.just(1500L)
                    .map { millis ->
                        Thread.sleep(millis)//?????????????????? ???????????????????????????????? ??????????????
                        2
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { userId ->
                        viewState.setMainProgressBarVisibility(false)
                        showUserSubsidies(userId, closeActivity = false)
                    }
            }
        }
    }

    fun login() {
        //TODO: ?????????????? ?????????? api
        viewState.setMainProgressBarVisibility(true)
        Observable.just(500L)
            .map { millis ->
                Thread.sleep(millis)//?????????????????? ???????????????????????????????? ??????????????
                2
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { userId ->
                viewState.setMainProgressBarVisibility(false)
                userSessionWorker.setUserId(userId)
                showUserSubsidies(userId, closeActivity = true)
            }
    }

    fun showAllSubsidies() {
        viewState.setMainProgressBarVisibility(true)
        api.getAllSubsidies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ subsidies ->
                viewState.setMainProgressBarVisibility(false)
                viewState.openSubsidies(gson.toJson(subsidies))
            }, {
                viewState.setMainProgressBarVisibility(false)
                viewState.showMessage(it.toString())
            })
    }

    private fun showUserSubsidies(userId: Int, closeActivity: Boolean) {
        viewState.setMainProgressBarVisibility(true)
        api.getUserSubsidies(userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ subsidies ->
                viewState.setMainProgressBarVisibility(false)
                viewState.openSubsidies(gson.toJson(subsidies))
                if(closeActivity) {
                    viewState.finishActivity()
                }
            }, {
                viewState.setMainProgressBarVisibility(false)
                viewState.showMessage(it.toString())
            })
    }
}