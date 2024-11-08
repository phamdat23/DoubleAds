package com.itsol.ironsourcelib

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.itsol.ironsourcelib.AdmobUtils.AdsNativeCallBackAdmod
import com.itsol.ironsourcelib.AdmobUtils.BannerCollapsibleAdCallback
import com.itsol.ironsourcelib.AdmobUtils.adRequest
import com.itsol.ironsourcelib.AdmobUtils.isNetworkConnected
import com.itsol.ironsourcelib.AdmobUtils.isShowAds
import com.itsol.ironsourcelib.utils.admod.BannerHolder
import com.itsol.ironsourcelib.utils.admod.NativeHolderAdmob
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.valentinilk.shimmer.shimmer

object AdmobUtilsCompose {

    @JvmStatic
    @Composable
    fun ShowNativeAdsWithLayout(
        context: Context,
        nativeHolder: NativeHolderAdmob,
        layout: Int,
        size: GoogleENative,
        callback: AdsNativeCallBackAdmod
    ) {
        val isConnectInternet by remember { mutableStateOf(isNetworkConnected(context)) }
        val activity = context as Activity
        var nativeAds by remember { mutableStateOf<NativeAd?>(null) }
        if (!isConnectInternet || !isShowAds) {
            callback.NativeFailed("no internet")
            return
        }
        if (adRequest == null) {
            callback.NativeFailed("not init admob")
            return
        }
        if (!nativeHolder.isLoad) {
            if (nativeHolder.nativeAd != null) {
                val adView by remember { mutableStateOf(LayoutInflater.from(context).inflate(layout, null) as NativeAdView) }
                PopulateNativeAdViewCompose(
                    nativeAd = nativeHolder.nativeAd,
                    adView,
                    size
                )
                nativeHolder.native_mutable.removeObservers((activity as LifecycleOwner))
                nativeAds?.setOnPaidEventListener {
                    callback.onPaidNative(it, nativeHolder.ads)
                }

                callback.NativeLoaded()
            } else {

                nativeHolder.native_mutable.removeObservers((activity as LifecycleOwner))
                callback.NativeFailed("None Show")
            }
        } else {
            nativeHolder.native_mutable.observe((activity as LifecycleOwner)) { nativeAd: NativeAd? ->
                if (nativeAd != null) {
                    nativeAds = nativeAd
                    nativeAds?.setOnPaidEventListener {
                        callback.onPaidNative(it, nativeHolder.ads)
                    }
                    callback.NativeLoaded()
                    nativeHolder.native_mutable.removeObservers((activity as LifecycleOwner))
                } else {
                    nativeAds = null
                    callback.NativeFailed("None Show")
                    nativeHolder.native_mutable.removeObservers((activity as LifecycleOwner))
                }
            }
            val adView by remember { mutableStateOf(LayoutInflater.from(context).inflate(layout, null) as NativeAdView) }
            PopulateNativeAdViewCompose(nativeAds, adView, size)
        }
    }

    @Composable
    fun LoadAndShowNativeAdsWithLayout(
        context: Context,
        nativeHolder: NativeHolderAdmob,
        layout: Int,
        size: GoogleENative,
        callback: AdsNativeCallBackAdmod
    ) {
        if (!isNetworkConnected(context)) {
            callback.NativeFailed("No Internet")
            return
        }
        if (!isShowAds) {
            callback.NativeFailed("Non Show")
        }
        if (AdmobUtils.isTesting) {
            nativeHolder.ads = context.getString(R.string.test_ads_admob_native_id)
        }
        val nativeHolderAdmob by remember { mutableStateOf(nativeHolder) }
        var nativeAd by rememberSaveable { mutableStateOf<NativeAd?>(null) }
        val builder by remember { mutableStateOf(AdLoader.Builder(context, nativeHolder.ads)) }
        builder.forNativeAd { it ->
            nativeAd = it
            Log.e("AAAAAAAAAAAAAA", "LoadAndShowNativeAdsWithLayout:nativeAd1:${nativeAd} ", )
            nativeHolderAdmob.nativeAd = it
            nativeHolderAdmob.native_mutable.value = it
            it.setOnPaidEventListener {
                callback.onPaidNative(it, nativeHolderAdmob.ads)
            }

        }
        val adLoader by remember {
            mutableStateOf(
                builder
                    .withAdListener(object : AdListener() {
                        override fun onAdFailedToLoad(adError: LoadAdError) {
                            nativeAd = null
                            Log.e("AAAAAAAAAA", "onAdFailedToLoad: nativeAd2:${nativeAd}", )
                            nativeHolder.nativeAd = null
                            nativeHolder.native_mutable.value = null
                            callback.NativeFailed("load ad fail")
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            callback.NativeLoaded()
                        }
                    })
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()
            )
        }
        if (adRequest != null) {
            adLoader.loadAd(adRequest!!)
        }
        val adView by remember { mutableStateOf(LayoutInflater.from(context).inflate(layout, null) as NativeAdView) }
        PopulateNativeAdViewCompose(nativeAd, adView, size)
    }

    @Composable
    fun ShowBannerCollapsibleNotReload(context: Context, bannerId: BannerHolder, collapsibleBanner: CollapsibleBanner, callBack: BannerCollapsibleAdCallback) {
        val scope = rememberCoroutineScope()
        val activity = context as Activity
        var bannerAdsId by remember { mutableStateOf(bannerId) }
        var isLoading by rememberSaveable {
            mutableStateOf(true)
        }
        if(!isNetworkConnected(context)){
            callBack.onAdFail("No Internet")
            return
        }
        if(!isShowAds){
            callBack.onAdFail("no show admob")
            return
        }
        if (AdmobUtils.isTesting) {
           bannerAdsId.ads=context.getString(R.string.test_ads_admob_banner_collapsible_id)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isLoading) 0f else 1f),
                factory = { context ->
                    bannerAdsId.mAdView = AdView(context)
                    bannerAdsId.mAdView?.setAdSize(AdmobUtils.getAdSize(activity))
                    bannerAdsId.mAdView?.adUnitId = bannerAdsId.ads
                    bannerAdsId.mAdView?.adListener = object : AdListener() {
                        override fun onAdClicked() {
                            super.onAdClicked()
                            ApplovinUtil.isClickAds = true
                            callBack.onClickAds()
                        }

                        override fun onAdClosed() {
                            super.onAdClosed()
                        }

                        override fun onAdFailedToLoad(p0: LoadAdError) {
                            super.onAdFailedToLoad(p0)
                            isLoading = false
                            callBack.onAdFail(p0.message)
                        }

                        override fun onAdLoaded() {
                            super.onAdLoaded()
                            isLoading = false
                            bannerAdsId.mAdView?.setOnPaidEventListener {
                                callBack.onAdPaid(it, bannerAdsId.mAdView!!)
                            }
                            bannerAdsId.mAdView?.adSize?.let { callBack.onBannerAdLoaded(it) }

                        }

                        override fun onAdOpened() {
                            super.onAdOpened()

                        }
                    }
                    val extras = Bundle()
                    if(collapsibleBanner==CollapsibleBanner.TOP){
                        extras.putString("collapsible", "top")
                    }else{
                        extras.putString("collapsible", "bottom")
                    }
                    bannerAdsId.mAdView?.loadAd(AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
                        .build()
                    )
                    bannerAdsId.mAdView!!
                }
            )
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .shimmer() // <- Affects all subsequent UI elements
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.LightGray),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(Color.LightGray),
                        )
                        Box(
                            modifier = Modifier
                                .size(120.dp, 20.dp)
                                .background(Color.LightGray),
                        )
                    }
                }
            }
        }
    }
    @Composable
    fun ShowBanner(context: Context, bannerId: String, callBack: AdmobUtils.BannerCallBack) {
        val scope = rememberCoroutineScope()
        val activity = context as Activity
        var bannerAdsId by remember { mutableStateOf(bannerId) }
        var isLoading by rememberSaveable {
            mutableStateOf(true)
        }
        if(!isNetworkConnected(context)){
            callBack.onFailed("No Internet")
            return
        }
        if(!isShowAds){
            callBack.onFailed("no show admob")
            return
        }
        if (AdmobUtils.isTesting) {
            bannerAdsId=context.getString(R.string.test_ads_admob_banner_id)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (isLoading) 0f else 1f),
                factory = { context ->
                    AdView(context).apply {
                        setAdSize(AdmobUtils.getAdSize(activity))
                        adUnitId = bannerAdsId
                        setOnPaidEventListener {
                            callBack.onPaid(it, this)
                        }
                        adListener= object : AdListener(){
                            override fun onAdLoaded() {
                                super.onAdLoaded()
                                callBack.onLoad()
                                isLoading=false
                            }

                            override fun onAdClicked() {
                                super.onAdClicked()
                                ApplovinUtil.isClickAds = true
                                callBack.onClickAds()
                            }

                            override fun onAdClosed() {
                                super.onAdClosed()

                            }

                            override fun onAdFailedToLoad(p0: LoadAdError) {
                                super.onAdFailedToLoad(p0)
                                callBack.onFailed(p0.message)
                                isLoading=false
                            }
                        }
                        if(adRequest!=null){
                            loadAd(adRequest!!)
                        }

                    }
                }
            )
            if (isLoading) {
                Row(
                    modifier = Modifier
                        .shimmer() // <- Affects all subsequent UI elements
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.LightGray),
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(Color.LightGray),
                        )
                        Box(
                            modifier = Modifier
                                .size(120.dp, 20.dp)
                                .background(Color.LightGray),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PopulateNativeAdViewCompose(
        nativeAd: NativeAd?,
        adView: NativeAdView,
        size: GoogleENative
    ) {
        var isLoading by remember { mutableStateOf(true) }
        var nativeAds by remember { mutableStateOf<NativeAd?>(nativeAd) }
        LaunchedEffect(nativeAd) {
            nativeAds = nativeAd
            if (nativeAds != null) {
                isLoading = false
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if (isLoading) {
                Column(
                    modifier = Modifier
                        .shimmer() // <- Affects all subsequent UI elements
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (size == GoogleENative.UNIFIED_MEDIUM) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(195.dp)
                                .background(Color.LightGray),
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(Color.LightGray),
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .background(Color.LightGray),
                        )
                        Box(
                            modifier = Modifier
                                .size(120.dp, 20.dp)
                                .background(Color.LightGray),
                        )
                    }
                }

            } else {
                AndroidView(
                    factory = { context ->
                        val headlineView = adView.findViewById<TextView>(R.id.ad_headline)
                        val ctaView = adView.findViewById<AppCompatButton>(R.id.ad_call_to_action)
                        val icon = adView.findViewById<ImageView>(R.id.ad_app_icon)
                        val secondary = adView.findViewById<TextView>(R.id.ad_body)
                        val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
                        val rating = adView.findViewById<RatingBar>(R.id.ad_stars)
                        adView.headlineView = headlineView
                        adView.callToActionView = ctaView
                        adView.iconView = icon
                        adView.bodyView = secondary
                        adView.mediaView = mediaView
                        adView.starRatingView = rating
                        nativeAds?.let {
                            adView.setNativeAd(it)
                            headlineView.text = it.headline
                            ctaView.text = it.callToAction
                            secondary.text = it.body
                            if (size == GoogleENative.UNIFIED_MEDIUM) {
                                adView.mediaView?.mediaContent = it.mediaContent
                            }
                            it.icon?.drawable?.let { it1 ->
                                (adView.iconView as ImageView).setImageDrawable(it1)
                            }
                            it.starRating?.let { it1 ->
                                (adView.starRatingView as RatingBar).rating = it1.toFloat()
                            }
                        }
                        adView
                    },
                    update = {

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )

            }

        }


    }

}