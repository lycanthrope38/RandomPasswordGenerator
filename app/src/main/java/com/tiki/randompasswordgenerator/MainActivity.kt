package com.tiki.randompasswordgenerator

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.base.activity.BaseInjectingActivity
import com.data.word.PasswordDomain
import com.tiki.randompasswordgenerator.databinding.ActivityMainBinding
import java.util.*
import javax.inject.Inject


class MainActivity : BaseInjectingActivity<ActivityMainBinding, MainViewModel, MainComponent>(),
    MainView {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun getViewModelClass(): Class<MainViewModel> {
        return MainViewModel::class.java
    }

    override fun createComponent(): MainComponent? {
        return DaggerMainComponent.builder().appComponent(App.get(this)?.component()).build()
    }

    override fun onInject(component: MainComponent) {
        component.inject(this)
    }

    @Inject
    fun setViewModelAttributes(passwordDomain: PasswordDomain) {
        mViewModel?.mPasswordDomain = passwordDomain
    }

    private var mOldPos = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding.viewModel = mViewModel
        mViewDataBinding.spinnerLength.onItemSelectedListener = this
        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getIntArray(R.array.length).toMutableList())
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mViewDataBinding.spinnerLength.adapter = dataAdapter
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position != mOldPos) {
            mOldPos = position
            mViewModel?.passwordLength(parent?.getItemAtPosition(position) as Int)
        }
    }
}
