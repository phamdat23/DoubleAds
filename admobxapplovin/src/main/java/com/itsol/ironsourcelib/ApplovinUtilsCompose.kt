package com.itsol.ironsourcelib

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdFormat
import com.applovin.mediation.MaxAdViewAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder
import com.applovin.sdk.AppLovinSdkUtils
import com.google.android.gms.ads.nativead.NativeAd
import com.itsol.ironsourcelib.ApplovinUtil.TAG
import com.itsol.ironsourcelib.ApplovinUtil.applovin_sdk
import com.itsol.ironsourcelib.ApplovinUtil.enableAds
import com.itsol.ironsourcelib.ApplovinUtil.isClickAds
import com.itsol.ironsourcelib.ApplovinUtil.isNetworkConnected
import com.itsol.ironsourcelib.callback_applovin.BannerCallback
import com.itsol.ironsourcelib.callback_applovin.NativeCallBackNew
import com.itsol.ironsourcelib.utils.NativeHolder
import com.itsol.ironsourcelib.utils.admod.BannerHolder

object ApplovinUtilsCompose {
    private var nativeAd: MaxAd? = null
    @Composable
    fun ShowBannerMax(
        modifier: Modifier = Modifier,
        context: Context,
        bannerId: String,
        callback: BannerCallback
    ) {
        if (!enableAds || !isNetworkConnected(context)) {
            callback.onBannerLoadFail("")
            return
        }
        if (applovin_sdk?.settings?.isVerboseLoggingEnabled == null) {
            callback.onBannerLoadFail("SDK not Initialized")
            return
        }
        val banner by remember { mutableStateOf(MaxAdView(bannerId, context)) }
        var isLoading by remember { mutableStateOf(true) }
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val heightPx = AppLovinSdkUtils.dpToPx(context, 50)
        Box(modifier = modifier) {
            AndroidView(factory = {
                banner.apply {
                    layoutParams = FrameLayout.LayoutParams(width, heightPx)
                    setExtraParameter("adaptive_banner", "true")
                    setListener(object : MaxAdViewAdListener {
                        override fun onAdLoaded(p0: MaxAd) {
                            isLoading = false
                        }

                        override fun onAdDisplayed(p0: MaxAd) {
                            callback.onBannerShowSucceed()
                        }

                        override fun onAdHidden(p0: MaxAd) {

                        }

                        override fun onAdClicked(p0: MaxAd) {
                            ApplovinUtil.isClickAds = true
                        }

                        override fun onAdLoadFailed(p0: String, p1: MaxError) {

                            isLoading = false
                            callback.onBannerLoadFail(p1.code.toString().replace("-", ""))
                        }

                        override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                            isLoading = false
                            callback.onBannerLoadFail(p1.code.toString().replace("-", ""))
                        }

                        override fun onAdExpanded(p0: MaxAd) {

                        }

                        override fun onAdCollapsed(p0: MaxAd) {

                        }

                    })
                    setRevenueListener {
                        callback.onAdRevenuePaid(it)
                    }
                    loadAd()
                    isLoading = true
                }
            })
            if (isLoading) {
                LayoutShimmerBanner()
            }
        }

    }

    @Composable
    fun ShowBannerMaxMERC(
        modifier: Modifier = Modifier,
        context: Context,
        bannerId: String,
        callback: BannerCallback
    ) {
        if (!enableAds || !isNetworkConnected(context)) {
            callback.onBannerLoadFail("")
            return
        }
        if (applovin_sdk?.settings?.isVerboseLoggingEnabled == null) {
            callback.onBannerLoadFail("SDK not Initialized")
            return
        }
        val banner by remember { mutableStateOf(MaxAdView(bannerId, MaxAdFormat.MREC, context)) }
        var isLoading by remember { mutableStateOf(true) }
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val heightPx = ViewGroup.LayoutParams.WRAP_CONTENT
        Box(modifier = modifier) {
            AndroidView(
                factory = {
                    banner.apply {
                        layoutParams = FrameLayout.LayoutParams(width, heightPx)
                        setExtraParameter("adaptive_banner", "true")
                        setListener(object : MaxAdViewAdListener {
                            override fun onAdLoaded(p0: MaxAd) {
                                isLoading = false
                            }

                            override fun onAdDisplayed(p0: MaxAd) {
                                callback.onBannerShowSucceed()
                            }

                            override fun onAdHidden(p0: MaxAd) {

                            }

                            override fun onAdClicked(p0: MaxAd) {
                                ApplovinUtil.isClickAds = true
                            }

                            override fun onAdLoadFailed(p0: String, p1: MaxError) {

                                isLoading = false
                                callback.onBannerLoadFail(p1.code.toString().replace("-", ""))
                            }

                            override fun onAdDisplayFailed(p0: MaxAd, p1: MaxError) {
                                isLoading = false
                                callback.onBannerLoadFail(p1.code.toString().replace("-", ""))
                            }

                            override fun onAdExpanded(p0: MaxAd) {

                            }

                            override fun onAdCollapsed(p0: MaxAd) {

                            }

                        })
                        setRevenueListener {
                            callback.onAdRevenuePaid(it)
                        }
                        loadAd()
                        isLoading = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .alpha(if (isLoading) 0f else 1f)
            )
            if (isLoading) {
                LayoutShimmerBanner()
            }
        }

    }

    @Composable
    fun LoadAndShowNativeMaxWithLayout(
        modifier: Modifier = Modifier,
        context: Context,
        nativeHolder: NativeHolder,
        @LayoutRes layout: Int,
        size: GoogleENative,
        callback: NativeCallBackNew
    ) {
        var isLoading by remember { mutableStateOf(true) }
        var maxAdView by remember { mutableStateOf<MaxNativeAdView?>(null) }
        if (!enableAds || !isNetworkConnected(context)) {
            callback.onAdFail("No internet")
            isLoading=false
            return
        }
        if (applovin_sdk?.settings?.isVerboseLoggingEnabled == null) {
            callback.onAdFail("SDK not Initialized")
            isLoading=false
            return
        }
        LaunchedEffect(true) {
            nativeHolder.nativeAdLoader = MaxNativeAdLoader(nativeHolder.adsId, context)

            nativeHolder.nativeAdLoader?.setRevenueListener { ad -> callback.onAdRevenuePaid(ad) }
            nativeHolder.nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
                override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd) {
                    super.onNativeAdLoaded(p0, p1)
                    if (nativeHolder.native != null) {
                        nativeHolder.nativeAdLoader?.destroy(nativeHolder.native)
                    }
                    nativeHolder.native = p1
                    maxAdView = createNativeAdView(context, layout, p1)
                    nativeHolder.nativeAdLoader?.render(maxAdView, nativeHolder.native)
                    callback.onNativeAdLoaded(nativeHolder.native, maxAdView)
                    isLoading = false
                }

                override fun onNativeAdLoadFailed(p0: String, p1: MaxError) {
                    super.onNativeAdLoadFailed(p0, p1)
                    isLoading = false
                    callback.onAdFail(p1.code.toString().replace("-", ""))
                }

                override fun onNativeAdClicked(p0: MaxAd) {
                    super.onNativeAdClicked(p0)
                    isClickAds = true
                }

                override fun onNativeAdExpired(p0: MaxAd) {
                    super.onNativeAdExpired(p0)
                    nativeHolder.nativeAdLoader?.loadAd()
                }
            })
            nativeHolder.nativeAdLoader?.loadAd(maxAdView)
        }

        Box(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            if (maxAdView != null) {
                AndroidView(factory = { context ->
                    maxAdView!!
                }, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                )
            }
            if (isLoading) {
                LayoutShimmerNative(size = size)
            }

        }
    }

    @Composable
    fun ShowNativeWithLayout(modifier: Modifier= Modifier, context: Context, nativeHolder: NativeHolder, @LayoutRes layout: Int, size: GoogleENative, callback: NativeCallBackNew){
        var isLoading by remember { mutableStateOf(true) }
        var maxAdView by remember { mutableStateOf<MaxNativeAdView?>(null) }
        if (!enableAds || !isNetworkConnected(context)) {
            callback.onAdFail("No internet")
            isLoading=false
            return
        }
        if (applovin_sdk?.settings?.isVerboseLoggingEnabled == null) {
            callback.onAdFail("SDK not Initialized")
            isLoading=false
            return
        }
        LaunchedEffect(true) {
            // Check if ad is expired before rendering
            if (true == nativeAd?.nativeAd?.isExpired) {
                Log.d("==Applovin", "isExpired")
                nativeHolder.nativeAdLoader?.destroy(nativeAd)
                nativeHolder.nativeAdLoader?.setNativeAdListener(object : MaxNativeAdListener() {
                    override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd) {
                        // Cleanup any pre-existing native ad to prevent memory leaks.
                        nativeHolder.nativeAdLoader?.destroy(nativeAd)
                        // Save ad to be rendered later.
                        callback.onNativeAdLoaded(ad, maxAdView)
                        maxAdView = createNativeAdView(context, layout, ad)
                        isLoading=false
                    }

                    override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError) {
                        callback.onAdFail(error.code.toString().replace("-", ""))
                        isLoading=false
                    }

                    override fun onNativeAdClicked(ad: MaxAd) {
                        isClickAds = true
                    }

                    override fun onNativeAdExpired(p0: MaxAd) {
                        nativeHolder.nativeAdLoader?.loadAd()
                    }
                })
                nativeHolder.nativeAdLoader?.loadAd()
                return@LaunchedEffect
            }
            if (!nativeHolder.isLoad) {
                if (nativeHolder.native != null) {
                    Log.d("==Applovin", "Load")
                    maxAdView = createNativeAdView(context, layout, nativeHolder.native!!)
                    nativeHolder.nativeAdLoader?.render(maxAdView, nativeHolder.native)
                    callback.onNativeAdLoaded(nativeHolder.native, maxAdView)
                    isLoading=false
                } else {
                    callback.onAdFail("NativeAd Null")
                    isLoading=false
                }
            }else{
                nativeHolder.native_mutable.observe(context as LifecycleOwner) {
                    if (it != null) {
                        if (it.nativeAd != null) {
                            nativeHolder.nativeAdLoader?.render(maxAdView, nativeHolder.native)
                            callback.onNativeAdLoaded(nativeHolder.native, maxAdView)
                            maxAdView = createNativeAdView(context, layout, it)
                            isLoading=false
                        } else {
                            callback.onAdFail("NativeAd null")
                            isLoading=false
                        }
                    } else {
                        callback.onAdFail("NativeAd null")
                        isLoading=false
                    }
                }
            }
        }



        Box(modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
            if (maxAdView != null) {
                AndroidView(factory = { context ->
                    maxAdView!!
                }, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                )
            }
            if (isLoading) {
                LayoutShimmerNative(size = size)
            }

        }

    }


    private fun createNativeAdView(context: Context, layout: Int, maxAd: MaxAd): MaxNativeAdView {
        val binder: MaxNativeAdViewBinder = MaxNativeAdViewBinder.Builder(layout)
            .setTitleTextViewId(R.id.title_text_view)
            .setBodyTextViewId(R.id.body_text_view)
            .setAdvertiserTextViewId(R.id.advertiser_text_view)
            .setIconImageViewId(R.id.icon_image_view)
            .setMediaContentViewGroupId(R.id.media_view_container)
            .setOptionsContentViewGroupId(R.id.options_view)
            .setStarRatingContentViewGroupId(R.id.star_rating_view)
            .setCallToActionButtonId(R.id.cta_button)
            .build()
        return MaxNativeAdView(binder, context).apply {
            nativeAd = maxAd
        }
    }
}