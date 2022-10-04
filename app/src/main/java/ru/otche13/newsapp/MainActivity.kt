package ru.otche13.newsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            ?: return

//        val telMgr=PackageManager.has
//
//        val simStateMain: Int = PackageManager.FEATURE_TELEPHONY
//        val simStateSecond: Int = telMgr.getSimState(1)

//        fun isSimSupport(context: Activity):Boolean
////        {
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
//            return !(tm.getSimState() == TelephonyManager.SIM_STATE_ABSENT);


    }

}