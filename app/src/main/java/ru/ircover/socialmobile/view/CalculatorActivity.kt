package ru.ircover.socialmobile.view

import android.os.Bundle
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.widget.changes
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChanges
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.model.DisabledGroup
import ru.ircover.socialmobile.presenter.CalculatorPresenter
import ru.ircover.socialmobile.presenter.CalculatorView
import ru.ircover.socialmobile.utils.toViewVisibility

class CalculatorActivity : MvpAppCompatActivity(), CalculatorView {
    private val presenter by moxyPresenter { CalculatorPresenter() }
    private lateinit var ageEditText: TextInputEditText
    private lateinit var isRetiredCheckBox: CheckBox
    private lateinit var isDisabledCheckBox: CheckBox
    private lateinit var disabledGroupsRadioGroup: RadioGroup
    private lateinit var isMarriedCheckBox: CheckBox
    private lateinit var incomeLayout: TextInputLayout
    private lateinit var incomeEditText: TextInputEditText
    private lateinit var childrenCountEditText: TextInputEditText
    private lateinit var disabledChildrenTitleTextView: TextView
    private lateinit var disabledChildrenSeekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        presenter.init()

        ageEditText = findViewById(R.id.etAge)
        ageEditText.textChanges()
            .subscribe { presenter.age = it.toString().toIntOrNull() }
        isRetiredCheckBox = findViewById(R.id.cbRetired)
        isRetiredCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.isRetired = isChecked
        }
        isDisabledCheckBox = findViewById(R.id.cbDisabled)
        isDisabledCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.isDisabled = isChecked
        }
        disabledGroupsRadioGroup = findViewById(R.id.rgDisabledGroups)
        disabledGroupsRadioGroup.checkedChanges()
            .subscribe { buttonId ->
                presenter.disabledGroup = when(buttonId) {
                    R.id.rbDisabledGroup1 -> DisabledGroup.I
                    R.id.rbDisabledGroup2 -> DisabledGroup.II
                    R.id.rbDisabledGroup3 -> DisabledGroup.III
                    else -> null
                }
            }
        isMarriedCheckBox = findViewById(R.id.cbMarried)
        isMarriedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.isMarried = isChecked
        }
        incomeLayout = findViewById(R.id.tilIncome)
        incomeEditText = findViewById(R.id.etIncome)
        incomeEditText.textChanges()
            .subscribe { presenter.income = it.toString().toIntOrNull() }
        childrenCountEditText = findViewById(R.id.etChildrenCount)
        childrenCountEditText.textChanges()
            .subscribe { presenter.childrenCount = it.toString().toIntOrNull() }
        disabledChildrenTitleTextView = findViewById(R.id.tvDisabledChildrenTitle)
        disabledChildrenSeekBar = findViewById(R.id.sbDisabledChildren)
        disabledChildrenSeekBar.changes()
            .subscribe { presenter.disabledChildrenCount = it }
    }

    override fun setAge(age: Int) {
        ageEditText.setText(age.toString())
    }

    override fun setRetired(isRetired: Boolean) {
        isRetiredCheckBox.isChecked = isRetired
    }

    override fun setDisabled(isDisabled: Boolean) {
        isDisabledCheckBox.isChecked = isDisabled
    }

    override fun setDisabledGroup(disabledGroup: DisabledGroup) {
        disabledGroupsRadioGroup.check(when(disabledGroup) {
            DisabledGroup.I -> R.id.rbDisabledGroup1
            DisabledGroup.II -> R.id.rbDisabledGroup2
            DisabledGroup.III -> R.id.rbDisabledGroup3
        })
    }

    override fun setDisabledGroupVisibility(isVisible: Boolean) {
        disabledGroupsRadioGroup.visibility = isVisible.toViewVisibility()
    }

    override fun setMarried(isMarried: Boolean) {
        isMarriedCheckBox.isChecked = isMarried
    }

    override fun setMarriedVisibility(isVisible: Boolean) {
        isMarriedCheckBox.visibility = isVisible.toViewVisibility()
    }

    override fun setIncome(income: Int) {
        incomeEditText.setText(income.toString())
    }

    override fun setIncomeVisibility(isVisible: Boolean) {
        incomeLayout.visibility = isVisible.toViewVisibility()
    }

    override fun setChildrenCount(childrenCount: Int) {
        childrenCountEditText.setText(childrenCount.toString())
    }

    override fun setDisabledChildrenVisibility(isVisible: Boolean) {
        isVisible.toViewVisibility().let { visibility ->
            disabledChildrenTitleTextView.visibility = visibility
            disabledChildrenSeekBar.visibility = visibility
        }
    }

    override fun setMaximumDisabledChildrenStates(max: Int) {
        disabledChildrenSeekBar.max = max
    }

    override fun setDisabledChildrenCount(count: Int) {
        disabledChildrenTitleTextView.text = getString(R.string.disabled_children_count, count)
    }
}