package com.itsol.ironsourceutils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.itsol.ironsourcelib.*
import com.itsol.ironsourcelib.callback_applovin.InterstititialCallback
import com.itsol.ironsourcelib.callback_applovin.InterstititialCallbackNew
import com.itsol.ironsourcelib.callback_applovin.NativeCallBackNew
import com.itsol.ironsourcelib.utils.InterHolder
import com.itsol.ironsourcelib.utils.NativeHolder
import com.itsol.ironsourcelib.utils.admod.BannerHolder

object AdsManager {
    var inter: MaxInterstitialAd?=null
    val mutable_inter: MutableLiveData<MaxInterstitialAd> = MutableLiveData()
    var check_inter = false
    var interHolder = InterHolder("9bdd558152f72465")
    var nativeHolder = NativeHolder("0f688c4e22b9688b")
    var banner = "f443c90308f39f17"
    var bannerHolder = BannerHolder("")

    fun showAdsNative(activity: Activity, nativeHolder: NativeHolder,viewGroup: ViewGroup){
        ApplovinUtil.loadAndShowNativeAds(activity,nativeHolder,viewGroup,GoogleENative.UNIFIED_MEDIUM,object :
            NativeCallBackNew {
            override fun onNativeAdLoaded(nativeAd: MaxAd?, nativeAdView: MaxNativeAdView?) {
                Toast.makeText(activity,"Loaded",Toast.LENGTH_SHORT).show()
            }

            override fun onAdFail(error: String) {
                Toast.makeText(activity,"LoadFailed",Toast.LENGTH_SHORT).show()
            }

            override fun onAdRevenuePaid(ad: MaxAd?) {

            }
        })
    }
    fun loadInter(context: Context){
        ApplovinUtil.loadAnGetInterstitials(context,interHolder,object : InterstititialCallbackNew {
            override fun onInterstitialReady(interstitialAd: MaxInterstitialAd) {
//                Toast.makeText(context,"Loaded",Toast.LENGTH_SHORT).show()
            }

            override fun onInterstitialClosed() {

            }

            override fun onInterstitialLoadFail(error: String) {
//                Toast.makeText(context,"LoadFailed",Toast.LENGTH_SHORT).show()
            }

            override fun onInterstitialShowSucceed() {

            }

            override fun onAdRevenuePaid(ad: MaxAd?) {

            }
        })
    }

    fun showInter(context: AppCompatActivity,interHolder: InterHolder,adsOnClick: AdsOnClick){

        AppOpenManager.getInstance().isAppResumeEnabled = true
        ApplovinUtil.showInterstitialsWithDialogCheckTimeNew(context, 800,interHolder ,object :
            InterstititialCallbackNew {
            override fun onInterstitialReady(interstitialAd : MaxInterstitialAd) {
                Toast.makeText(context,"Ready",Toast.LENGTH_SHORT).show()
            }

            override fun onInterstitialClosed() {
                loadInter(context)
                Toast.makeText(context,"Closed",Toast.LENGTH_SHORT).show()
                adsOnClick.onAdsCloseOrFailed()
            }


            override fun onInterstitialLoadFail(error: String) {
                loadInter(context)
                adsOnClick.onAdsCloseOrFailed()
                Toast.makeText(context, "Failed: $error",Toast.LENGTH_SHORT).show()
            }

            override fun onInterstitialShowSucceed() {
                Toast.makeText(context,"Show",Toast.LENGTH_SHORT).show()
            }

            override fun onAdRevenuePaid(ad: MaxAd?) {

            }
        })
    }

    interface AdsOnClick{
        fun onAdsCloseOrFailed()
    }

    var nativeAdLoader : MaxNativeAdLoader?=null
    var native: MaxAd? = null
    var isLoad = false
    var native_mutable: MutableLiveData<MaxAd> = MutableLiveData()

    fun loadNativeAds(activity: Activity, idAd: String) {
        isLoad = true
        nativeAdLoader = MaxNativeAdLoader(idAd, activity)
        nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if (native != null) {
                    nativeAdLoader?.destroy(native)
                }
                isLoad = false
                // Save ad to be rendered later.
                native = ad
                native_mutable.value = ad
                Toast.makeText(activity,"Loaded", Toast.LENGTH_SHORT).show()
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                isLoad = false
                native_mutable.value = null
                Toast.makeText(activity,"Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onNativeAdClicked(ad: MaxAd) {
            }

            override fun onNativeAdExpired(p0: MaxAd) {

            }
        })
        nativeAdLoader?.loadAd()
    }

    fun loadAndShowIntersial(activity: Activity, idAd: InterHolder,adsOnClick: AdsOnClick){
        ApplovinUtil.loadAndShowInterstitialsWithDialogCheckTime(activity as AppCompatActivity,idAd,1500, object :
            InterstititialCallback {
            override fun onInterstitialReady() {
                AppOpenManager.getInstance().isAppResumeEnabled = false
            }

            override fun onInterstitialClosed() {
                adsOnClick.onAdsCloseOrFailed()
            }

            override fun onInterstitialLoadFail(error: String) {
                Log.d("===Ads",error)
                adsOnClick.onAdsCloseOrFailed()
            }

            override fun onInterstitialShowSucceed() {
                AppOpenManager.getInstance().isAppResumeEnabled = false
            }

            override fun onAdRevenuePaid(ad: MaxAd?) {

            }
        })

    }
}
