package com.itsol.ironsourceutils

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itsol.ironsourcelib.AdmobUtils
import com.itsol.ironsourcelib.AdmobUtilsCompose
import com.itsol.ironsourcelib.CollapsibleBanner
import com.itsol.ironsourcelib.GoogleENative
import com.itsol.ironsourcelib.utils.admod.BannerHolder
import com.itsol.ironsourcelib.utils.admod.NativeHolderAdmob
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.AdView

class MainActivityTestComposeAds : ComponentActivity() {
    companion object{
        val nativeHolderAdmob = NativeHolderAdmob("ca-app-pub-3940256099942544/2247696110")
        val nativeHolderAdmob2 = NativeHolderAdmob("ca-app-pub-3940256099942544/2247696110")
        val bannerHolder = BannerHolder("")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)) {
                    AdmobUtilsCompose.ShowNativeAdsWithLayout(
                        this@MainActivityTestComposeAds,
                        nativeHolderAdmob,
                        R.layout.ad_template_medium,
                        GoogleENative.UNIFIED_MEDIUM,
                        object : AdmobUtils.AdsNativeCallBackAdmod {
                            override fun NativeLoaded() {

                            }

                            override fun NativeFailed(massage: String) {

                            }

                            override fun onPaidNative(adValue: AdValue, adUnitAds: String) {

                            }
                        })
                    Spacer(modifier = Modifier.height(20.dp))
                    AdmobUtilsCompose.LoadAndShowNativeAdsWithLayout(
                        this@MainActivityTestComposeAds,
                        nativeHolderAdmob2,
                        R.layout.ad_template_small,
                        GoogleENative.UNIFIED_SMALL,
                        object : AdmobUtils.AdsNativeCallBackAdmod {
                            override fun NativeLoaded() {

                            }

                            override fun NativeFailed(massage: String) {

                            }

                            override fun onPaidNative(adValue: AdValue, adUnitAds: String) {

                            }
                        })
                    Spacer(modifier = Modifier.height(20.dp))
                    AdmobUtilsCompose.ShowBanner(this@MainActivityTestComposeAds,"", object : AdmobUtils.BannerCallBack{
                        override fun onClickAds() {

                        }

                        override fun onLoad() {

                        }

                        override fun onFailed(message: String) {

                        }

                        override fun onPaid(adValue: AdValue?, mAdView: AdView?) {

                        }
                    })
                    Spacer(modifier = Modifier.height(20.dp))
                    AdmobUtilsCompose.ShowBannerCollapsibleNotReload(this@MainActivityTestComposeAds, bannerHolder, CollapsibleBanner.TOP, object : AdmobUtils.BannerCollapsibleAdCallback{
                        override fun onClickAds() {

                        }

                        override fun onBannerAdLoaded(adSize: AdSize) {

                        }

                        override fun onAdFail(message: String) {

                        }

                        override fun onAdPaid(adValue: AdValue, mAdView: AdView) {

                        }
                    })

                }
            }

        }
    }
}