package com.easit.floatingbuttonsnipingtool

import android.os.Build

object Utils {

    val isAndroidO: Boolean
        get() = Build.VERSION.SDK_INT >= 26
    val isAndroidP: Boolean
        get() = Build.VERSION.SDK_INT >= 28
}