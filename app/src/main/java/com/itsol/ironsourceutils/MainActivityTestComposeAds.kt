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
    companion object {
        val nativeHolderAdmob = NativeHolderAdmob("")
        val nativeHolderAdmob2 = NativeHolderAdmob("")
        val bannerHolder = BannerHolder("")
        val nativeHolder = NativeHolder("0f688c4e22b9688b")
        val nativeHolder2 = NativeHolder("0f688c4e22b9688b")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdmobUtils.loadAndGetNativeAds(this, nativeHolderAdmob, object : NativeAdmobCallback {
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
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            AdmobUtilsCompose.LoadAndShowNativeFullScreen(
                                context = this@MainActivityTestComposeAds,
                                nativeHolder = nativeHolderAdmob2,
                                layout = R.layout.ad_unified_medium,
                                callback = object :
                                    AdmobUtils.AdsNativeCallBackAdmod {
                                    override fun NativeLoaded() {

                                    }

                                    override fun NativeFailed(massage: String) {

                                    }

                                    override fun onPaidNative(
                                        nativeAd: NativeAd,
                                        adValue: AdValue,
                                        adUnitAds: String
                                    ) {

                                    }
                                })

                        }

                    }
                }
            }
        }
    }
}