package com.itsol.ironsourcelib.utils.admod.callback;

import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.interstitial.InterstitialAd;

public interface AdsInterCallBack {
    void onStartAction();
    void onEventClickAdClosed();
    void onAdShowed();
    void onAdLoaded();
    void onAdFail(String error);
    void onPaid(InterstitialAd interAds, AdValue adValue, String adsId);
}
