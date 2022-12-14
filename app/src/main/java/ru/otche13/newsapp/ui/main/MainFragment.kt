package ru.otche13.newsapp.ui.main

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.otche13.newsapp.R
import ru.otche13.newsapp.databinding.FragmentMainBinding
import ru.otche13.newsapp.ui.adapters.NewsAdapter
import ru.otche13.newsapp.utils.Resource

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    private var package_name = "com.android.chrome";

    private val viewModel by viewModels<MainViewModel>()
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        var urlItem = ""
        val netState=viewModel.isNetworkAvailable(context)

        if (netState) {
            viewModel.webDataRoom.observe(viewLifecycleOwner) { webItem ->
                webItem.let { web ->
                    urlItem = web[0].url.toString()
                }
            }

            viewModel.webData.observe(viewLifecycleOwner) { list ->
                list.let { webItemFbs ->
                    if (urlItem == "") {
                        webItemFbs[0].url?.let {
                            viewModel.saveWebItem(
                                id = URL_KEY,
                                url = it
                            )
                        }
                    }
                }
            }


            newsAdapter.setOnItemClickListener {
                val bundle = bundleOf("article" to it)
                view.findNavController().navigate(
                    R.id.action_mainFragment_to_detailsFragment,
                    bundle
                )
            }


            viewModel.newsLiveData.observe(viewLifecycleOwner) { responce ->
                when (responce) {
                    is Resource.Success -> {
                        pag_progress_bar.visibility = View.INVISIBLE
                        responce.data?.let {
                            newsAdapter.differ.submitList(it.articles)
                        }
                    }
                    is Resource.Error -> {
                        pag_progress_bar.visibility = View.INVISIBLE
                        responce.data?.let {
                            Log.e("checkData", "MainFragment: error: ${it}")
                        }
                    }
                    is Resource.Loading -> {
                        pag_progress_bar.visibility = View.VISIBLE
                    }
                }
            }
            //
            //?????????????????? ?????????????? ????????????????
            fun isSIMInserted(context: Context): Boolean {
                return TelephonyManager
                    .SIM_STATE_ABSENT != (context.getSystemService
                    (Context.TELEPHONY_SERVICE) as TelephonyManager).simState
            }

            val telMgr =
                (requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager)

            //?????????????????? ???????????? ????????????????
            fun getSimCountryIso(): String {
                return telMgr.getSimCountryIso().toString()
            }

//        chome tabs
            if (isSIMInserted(requireActivity())
                && getSimCountryIso() != "us"
                && getSimCountryIso() != "in"
            ) {
                mBinding.buttonMain.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        val builder = CustomTabsIntent.Builder()

                        // to set the toolbar color use CustomTabColorSchemeParams
                        // since CustomTabsIntent.Builder().setToolBarColor() is deprecated

                        val params = CustomTabColorSchemeParams.Builder()
                        params.setToolbarColor(
                            ContextCompat.getColor(
                                requireActivity(),
                                R.color.black
                            )
                        )
                        builder.setDefaultColorSchemeParams(params.build())

                        // shows the title of web-page in toolbar
                        builder.setShowTitle(true)

                        // setShareState(CustomTabsIntent.SHARE_STATE_ON) will add a menu to share the web-page
                        builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)

                        // To modify the close button, use
                        // builder.setCloseButtonIcon(bitmap)

                        // to set weather instant apps is enabled for the custom tab or not, use
                        builder.setInstantAppsEnabled(true)

                        val customBuilder = builder.build()

                        if (requireActivity().isPackageInstalled(package_name)) {
                            // if chrome is available use chrome custom tabs
                            customBuilder.intent.setPackage(package_name)
                            customBuilder.launchUrl(requireActivity(), Uri.parse(urlItem))
                        }
                    }
                }
            }
        }
        else{
            mBinding.screenMain.visibility=View.INVISIBLE
            mBinding.screenMainError.visibility=View.VISIBLE
        }
    }




    private  fun initAdapter() {
        newsAdapter = NewsAdapter()
        news_adapter.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

   private fun Context.isPackageInstalled(packageName: String): Boolean {
        // check if chrome is installed or not
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    companion object {
        private val URL_KEY = 899
    }
}