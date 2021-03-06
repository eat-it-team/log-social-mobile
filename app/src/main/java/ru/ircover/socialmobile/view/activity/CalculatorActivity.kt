package ru.ircover.socialmobile.view.activity

import android.app.NotificationManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.widget.changes
import com.jakewharton.rxbinding4.widget.checkedChanges
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import ru.ircover.socialmobile.R
import ru.ircover.socialmobile.model.DisabledGroup
import ru.ircover.socialmobile.presenter.CalculatorPresenter
import ru.ircover.socialmobile.presenter.CalculatorView
import ru.ircover.socialmobile.utils.ActivityExtras
import ru.ircover.socialmobile.utils.SocialApp
import ru.ircover.socialmobile.utils.startActivity
import ru.ircover.socialmobile.utils.toViewVisibility

class CalculatorActivity : MvpAppCompatActivity(), CalculatorView {
    private val presenter by moxyPresenter {
        CalculatorPresenter(SocialApp.api, SocialApp.gson, SocialApp.userSessionWorker)
    }
    private lateinit var ageEditText: TextInputEditText
    private lateinit var isRetiredCheckBox: CheckBox
    private lateinit var isDisabledCheckBox: CheckBox
    private lateinit var disabledGroupsRadioGroup: RadioGroup
    private lateinit var isMarriedCheckBox: CheckBox
    private lateinit var incomeLayout: TextInputLayout
    private lateinit var incomeEditText: TextInputEditText
    private lateinit var childrenCountEditText: TextInputEditText
    private lateinit var emptyAgeWarningTextView: TextView
    private lateinit var emptyIncomeWarningTextView: TextView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var disabledChildrenCheckBox: CheckBox
    private lateinit var incompleteFamilyCheckBox: CheckBox
    private lateinit var youngFamilyCheckBox: CheckBox

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
        findViewById<Button>(R.id.bSendRequest).clicks()
            .subscribe { presenter.sendRequest() }
        emptyAgeWarningTextView = findViewById(R.id.tvEmptyAgeWarning)
        emptyIncomeWarningTextView = findViewById(R.id.tvEmptyIncomeWarning)
        mainProgressBar = findViewById(R.id.pbMain)
        disabledChildrenCheckBox = findViewById(R.id.cbDisabledChildren)
        disabledChildrenCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.hasDisabledChildren = isChecked
        }
        incompleteFamilyCheckBox = findViewById(R.id.cbIncompleteFamily)
        incompleteFamilyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.isIncompleteFamily = isChecked
        }
        youngFamilyCheckBox = findViewById(R.id.cbYoungFamily)
        youngFamilyCheckBox.setOnCheckedChangeListener { _, isChecked ->
            presenter.isYoungFamily = isChecked
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_login -> {
            presenter.login()
            true
        }
        R.id.action_show_all_subsidies -> {
            presenter.showAllSubsidies()
            true
        }
        R.id.action_help -> {
            val editText = EditText(this)
            AlertDialog.Builder(this)
                .setTitle(R.string.action_help)
                .setView(editText)
                .setPositiveButton(R.string.button_send) { dialog, _ ->
                    dialog.dismiss()
                    showMessage(R.string.message_sent_successfully)
                }
                .setNegativeButton(R.string.button_cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
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
        disabledChildrenCheckBox.visibility = isVisible.toViewVisibility()
    }

    override fun setHasDisabledChildren(hasDisabledChildren: Boolean) {
        disabledChildrenCheckBox.isChecked = hasDisabledChildren
    }

    override fun setEmptyAgeWarningVisibility(isVisible: Boolean) {
        emptyAgeWarningTextView.visibility = isVisible.toViewVisibility()
    }

    override fun setEmptyIncomeWarningVisibility(isVisible: Boolean) {
        emptyIncomeWarningTextView.visibility = isVisible.toViewVisibility()
    }

    override fun showMessage(stringId: Int) {
        Toast.makeText(applicationContext, stringId, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(string: String) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
    }

    override fun openSubsidies(subsidies: String) {
        startActivity<SubsidiesActivity> {
            putExtra(ActivityExtras.SUBSIDIES, subsidies)
        }
    }

    override fun setMainProgressBarVisibility(isVisible: Boolean) {
        mainProgressBar.visibility = isVisible.toViewVisibility()
    }

    override fun finishActivity() {
        //TODO: ?????????????????? ???????? - ???????????? ???????????????? ????????????
        Observable.just(10000L)
            .map { millis ->
                Thread.sleep(millis)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                var channel = NotificationChannelCompat.Builder("1", NotificationManager.IMPORTANCE_MAX)
                    .build()
                val notificationBuilder = NotificationCompat.Builder(applicationContext, channel.id)
                    .setContentTitle(getString(R.string.notification_subsidy_approved))
                    .setSmallIcon(R.drawable.icon_notification)
                    .setLargeIcon(resources.getDrawable(R.drawable.icon_notification).toBitmap())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                val notificationManager = NotificationManagerCompat.from(applicationContext)
                notificationManager.createNotificationChannel(channel)
                notificationManager.notify(1, notificationBuilder.build())
            }
        finish()
    }

    override fun setIncompleteFamily(isIncompleteFamily: Boolean) {
        incompleteFamilyCheckBox.isChecked = isIncompleteFamily
    }

    override fun setIncompleteFamilyVisibility(isVisible: Boolean) {
        incompleteFamilyCheckBox.visibility = isVisible.toViewVisibility()
    }

    override fun setYoungFamily(isYoungFamily: Boolean) {
        youngFamilyCheckBox.isChecked = isYoungFamily
    }

    override fun setYoungFamilyVisibility(isVisible: Boolean) {
        youngFamilyCheckBox.visibility = isVisible.toViewVisibility()
    }
}