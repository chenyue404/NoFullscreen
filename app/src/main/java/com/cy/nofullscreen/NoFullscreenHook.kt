package com.cy.nofullscreen

import android.view.View
import android.view.Window
import android.view.WindowManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class NoFullscreenHook : IXposedHookLoadPackage {
    private val TAG = "全屏-hook-"

    private val PROP_FULLSCREEN = "AppSettings-Fullscreen"
    private val PROP_IMMERSIVE = "AppSettings-Immersive"
    private val PROP_KEEP_SCREEN_ON = "AppSettings-KeepScreenOn"
    private val PROP_LEGACY_MENU = "AppSettings-LegacyMenu"
    private val PROP_ORIENTATION = "AppSettings-Orientation"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            View::class.java,
            "setSystemUiVisibility",
            Int::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val visibility = param.args[0] as Int
                    var newValue = visibility
                    newValue = newValue and View.SYSTEM_UI_FLAG_FULLSCREEN.inv()
                    newValue = newValue and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
                    param.args[0] = newValue
                    super.beforeHookedMethod(param)
                }
            })

        XposedHelpers.findAndHookMethod(
            Window::class.java,
            "setFlags",
            Int::class.java,
            Int::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val visibility0 = param.args[0] as Int
                    val visibility1 = param.args[1] as Int
                    param.args[0] = visibility0 and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                    param.args[1] = visibility1 and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
                    super.beforeHookedMethod(param)
                }
            })
    }
}