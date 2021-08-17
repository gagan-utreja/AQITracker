package com.proximity.aqi

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.proximity.aqi.ui.CityListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CityListFragment.newInstance())
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {

            fm.popBackStack()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder
            .setMessage("Are you sure yow want to exit ? ")
            .setPositiveButton(
                "Yes"
            ) { dialog, which -> finish() }
            .setNegativeButton(
                "No"
            ) { dialog, which -> dialog.dismiss() }
            .show()
    }
}

}
