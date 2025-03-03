package com.itsol.ironsourceutils

import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.itsol.ironsourcelib.AdmobUtils
import com.itsol.ironsourcelib.AdmobUtilsCompose
import com.itsol.ironsourcelib.CollapsibleBanner
import com.itsol.ironsourcelib.GoogleENative
import com.itsol.ironsourcelib.utils.admod.BannerHolder
import com.itsol.ironsourcelib.utils.admod.NativeHolderAdmob
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdValue
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.nativead.NativeAd
import com.itsol.ironsourcelib.ApplovinUtilsCompose
import com.itsol.ironsourcelib.callback_applovin.BannerCallback
import com.itsol.ironsourcelib.callback_applovin.NativeCallBackNew
import com.itsol.ironsourcelib.utils.NativeHolder
import com.itsol.ironsourcelib.utils.admod.callback.NativeAdmobCallback

class MainActivityTestComposeAds : ComponentActivity() {
    companion object{
        val nativeHolderAdmob = NativeHolderAdmob("ca-app-pub-5760268661978943/7386320402")
        val nativeHolderAdmob2 = NativeHolderAdmob("ca-app-pub-5760268661978943/7386320402")
        val bannerHolder = BannerHolder("")
        val nativeHolder = NativeHolder("0f688c4e22b9688b")
        val nativeHolder2 = NativeHolder("0f688c4e22b9688b")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdmobUtils.loadAndGetNativeAds(this, nativeHolderAdmob, object : NativeAdmobCallback{
            override fun onLoadedAndGetNativeAd(ad: NativeAd?) {

            }

            override fun onNativeAdLoaded() {

            }

            override fun onAdFail(error: String?) {

            }

            override fun onAdPaid(adValue: AdValue?, adUnitAds: String?) {

            }
        })
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    item {
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
                                Log.e("AAAAAAAA", "NativeFailed: $massage", )
                            }

                            override fun onPaidNative(adValue: AdValue, adUnitAds: String) {

                            }
                        })
//                            ApplovinUtilsCompose.ShowNativeWithLayout(context = this@MainActivityTestComposeAds, nativeHolder = nativeHolder2,  layout = R.layout.native_custom_ad_view, size = GoogleENative.UNIFIED_SMALL , callback = object : NativeCallBackNew{
//                                override fun onNativeAdLoaded(
//                                    nativeAd: MaxAd?,
//                                    nativeAdView: MaxNativeAdView?
//                                ) {
//
//                                }
//
//                                override fun onAdFail(error: String) {
//                                    Log.e("AAAAAAAA", "onAdFail: $error", )
//
//                                }
//
//                                override fun onAdRevenuePaid(ad: MaxAd?) {
//
//                                }
//                            })
                            Spacer(modifier = Modifier.height(20.dp))

//                    AdmobUtilsCompose.LoadAndShowNativeAdsWithLayout(
//                        this@MainActivityTestComposeAds,
//                        nativeHolderAdmob2,
//                        R.layout.ad_template_small,
//                        GoogleENative.UNIFIED_SMALL,
//                        object : AdmobUtils.AdsNativeCallBackAdmod {
//                            override fun NativeLoaded() {
//
//                            }
//
//                            override fun NativeFailed(massage: String) {
//
//                            }
//
//                            override fun onPaidNative(adValue: AdValue, adUnitAds: String) {
//
//                            }
//                        })
//
//                            ApplovinUtilsCompose.LoadAndShowNativeMaxWithLayout(modifier = Modifier, context = this@MainActivityTestComposeAds, nativeHolder, R.layout.native_custom_ad_view, GoogleENative.UNIFIED_MEDIUM, object :NativeCallBackNew{
//                                override fun onNativeAdLoaded(
//                                    nativeAd: MaxAd?,
//                                    nativeAdView: MaxNativeAdView?
//                                ) {
//
//                                }
//
//                                override fun onAdFail(error: String) {
//
//                                }
//
//                                override fun onAdRevenuePaid(ad: MaxAd?) {
//
//                                }
//                            })

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
//                    item{
//                        ApplovinUtilsCompose.ShowBannerMax(context = this@MainActivityTestComposeAds, bannerId = "AAAAAAAAAAAA0", callback = object : BannerCallback{
//                            override fun onBannerLoadFail(error: String) {
//                                Log.e("AAAAAAAAAA", "onBannerLoadFail: ${error}" )
//                            }
//
//                            override fun onBannerShowSucceed() {
//
//                            }
//
//                            override fun onAdRevenuePaid(ad: MaxAd?) {
//
//                            }
//                        })
//                        Spacer(modifier = Modifier.height(20.dp))
//                        ApplovinUtilsCompose.ShowBannerMaxMERC(context = this@MainActivityTestComposeAds, bannerId = "AAAAAAAAAAAA0", callback = object : BannerCallback{
//                            override fun onBannerLoadFail(error: String) {
//                                Log.e("AAAAAAAAAA", "onBannerLoadFail: ${error}" )
//                            }
//
//                            override fun onBannerShowSucceed() {
//
//                            }
//
//                            override fun onAdRevenuePaid(ad: MaxAd?) {
//
//                            }
//                        })
//                    }
                }

            }

        }
    }
}