package com.xxm.mmd.wxfx.glide;


import android.support.v7.graphics.Palette;

/**
 * Created by MaDeng on 2018/1/10.
 */

public interface OnPaletteProcessingCallback {

    /**
     * The [Palette] finishes its work successfully.
     */
    void onPaletteGenerated(Palette palette);

    /**
     * The [Palette] finished its work with a failure.
     */
    void onPaletteNotAvailable();
}
