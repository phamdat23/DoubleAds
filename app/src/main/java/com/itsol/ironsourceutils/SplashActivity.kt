package com.itsol.ironsourceutils

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.itsol.ironsourcelib.*
import com.itsol.ironsourcelib.callback_applovin.NativeCallBackNew
import com.itsol.ironsourcelib.utils.Utils
import com.itsol.ironsourcelib.utils.admod.callback.NativeAdmobCallback
import com.itsol.ironsourceutils.MainActivityTestComposeAds.Companion.nativeHolderAdmob
import com.itsol.ironsourceutils.databinding.ActivitySplashBinding
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.nativead.NativeAd

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdmobUtils.initAdmob(this, 10000, isDebug = true, isEnableAds = true)
//        AppOpenManager.getInstance().init(application, getString(R.string.test_ads_admob_app_open_new))
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity::class.java)
        AppOpenManager.getInstance().setWaitingTime(0)
        AppOpenManager.getInstance().waitingTimeShowInter = 5000
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        AdmobUtils.loadAndGetNativeAds(
            this,
            nativeHolderAdmob,
            object : NativeAdmobCallback {
                override fun onLoadedAndGetNativeAd(ad: NativeAd?) {
                    Log.e("AAAAAAAAAAAAA", "onLoadedAndGetNativeAd:nativeAd1:${ad} ", )

                }

                override fun onNativeAdLoaded() {

                }

                override fun onAdFail(error: String?) {
                    Log.e("AAAAAAAAA", "onAdFail: loadnative fail 1", )
                }

                override fun onAdPaid(adValue: AdValue?, adUnitAds: String?) {

                }
            })
        setContentView(binding.root)
        if (ApplovinUtil.isNetworkConnected(this)){
            ApplovinUtil.initApplovin(application, "Hd8NW44NTx4ndvT7Pw2PIQR_omwB0DB00BKnHGXorX1hCETptrgiRyRCtDcZqbhU9Wi_l4R0Icd5N5SkKJFGIy",
                testAds = true,
                enableAds = true,
                initialization = object : ApplovinUtil.Initialization{
                    override fun onInitSuccessful() {
                        ApplovinUtil.loadNativeAds(this@SplashActivity,AdsManager.nativeHolder,object : NativeCallBackNew {
                            override fun onNativeAdLoaded(nativeAd: MaxAd?, nativeAdView: MaxNativeAdView?) {
                                Toast.makeText(this@SplashActivity,"Loaded", Toast.LENGTH_SHORT).show()
                            }

                            override fun onAdFail(error: String) {
                                Toast.makeText(this@SplashActivity,"Failed", Toast.LENGTH_SHORT).show()
                            }

                            override fun onAdRevenuePaid(ad: MaxAd?) {
                            }
                        })

                        Utils.getInstance().addActivity(this@SplashActivity, MainActivityTestComposeAds::class.java)

                    }
                })
        }else{
            Utils.getInstance().addActivity(this@SplashActivity, MainActivityTestComposeAds::class.java)

        }
        AOAManager(this, "", 1000, object :AOAManager.AppOpenAdsListener {
            override fun onAdsClose() {
                Toast.makeText(this@SplashActivity, "close app opend", Toast.LENGTH_SHORT).show()
            }

            override fun onAdsLoaded() {

            }

            override fun onAdsFailed(message: String) {
                Toast.makeText(this@SplashActivity, "load fail ", Toast.LENGTH_SHORT).show()
            }

            override fun onAdPaid(adValue: AdValue, adUnitAds: String) {

            }
        })
    }
}