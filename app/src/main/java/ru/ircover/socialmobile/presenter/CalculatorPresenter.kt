package ru.ircover.socialmobile.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import ru.ircover.socialmobile.model.DisabledGroup

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
}

@InjectViewState
class CalculatorPresenter : MvpPresenter<CalculatorView>() {
    var age: Int? = null
        set(value) {
            field = value
            viewState.setMarriedVisibility((value ?: 0) > 18)
            viewState.setIncomeVisibility((value ?: 0) > 18)
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
            setRetired(isRetired)
            setDisabled(isDisabled)
            setDisabledGroupVisibility(isDisabled)
            disabledGroup?.let { setDisabledGroup(it) }
            setMarried(isMarried)
            income?.let { setIncome(it) }
            childrenCount?.let { setChildrenCount(it) }
            setDisabledChildrenVisibility((childrenCount ?: 0) > 0)
            setMaximumDisabledChildrenStates(childrenCount ?: 0)
            disabledChildrenCount?.let { setDisabledChildrenCount(it) }
        }
    }
}