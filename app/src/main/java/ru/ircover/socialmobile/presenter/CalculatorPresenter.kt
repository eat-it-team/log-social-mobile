package ru.ircover.socialmobile.presenter

import androidx.annotation.StringRes
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
    fun setMaximumDisabledChildrenStates(max: Int)
    @AddToEndSingle
    fun setDisabledChildrenCount(count: Int)
    @AddToEndSingle
    fun setEmptyAgeWarningVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun setEmptyIncomeWarningVisibility(isVisible: Boolean)
    @AddToEndSingle
    fun showMessage(@StringRes stringId: Int)
    @AddToEndSingle
    fun openSubsidies()
}

@InjectViewState
class CalculatorPresenter(private val api: Api,
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
            viewState.setMaximumDisabledChildrenStates(value ?: 0)
            if(disabledChildrenCount != null) {
                disabledChildrenCount = if(value == null) {
                    null
                } else {
                    minOf(value, disabledChildrenCount ?: 0)
                }
            }
        }
    var disabledChildrenCount: Int? = null
        set(value) {
            field = value
            viewState.setDisabledChildrenCount(value ?: 0)
        }

    fun init() {
        viewState.apply {
            age?.let { setAge(it) }
            setMarriedVisibility((age ?: 0) > 18)
            setIncomeVisibility((age ?: 0) > 18)
            setEmptyAgeWarningVisibility(isInitialized && age == null)
            setRetired(isRetired)
            setDisabled(isDisabled)
            setDisabledGroupVisibility(isDisabled)
            disabledGroup?.let { setDisabledGroup(it) }
            setMarried(isMarried)
            income?.let { setIncome(it) }
            setEmptyIncomeWarningVisibility(isInitialized && income == null && (age ?: 0) > 18)
            childrenCount?.let { setChildrenCount(it) }
            setDisabledChildrenVisibility((childrenCount ?: 0) > 0)
            setMaximumDisabledChildrenStates(childrenCount ?: 0)
            disabledChildrenCount?.let { setDisabledChildrenCount(it) }
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
                //TODO: запросить субсидии по введённым данным
            }
        }
    }

    fun login() {
        //TODO: сделать вызов api
        viewState.openSubsidies()
    }
}