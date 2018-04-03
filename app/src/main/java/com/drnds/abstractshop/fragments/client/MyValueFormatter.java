package com.drnds.abstractshop.fragments.client;

import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * Created by ajithkumar on 8/9/2017.
 */

public class MyValueFormatter implements ValueFormatter {




//
//    @Override
//    public String getFormattedValue(float value) {
//        return Math.round(value)+"";
//    }
//
//
//    private DecimalFormat mFormat;
//
//    public MyValueFormatter() {
//        mFormat = new DecimalFormat("###,###,###"); // use no decimals
//    }


    @Override
    public String getFormattedValue(float value) {
        return "" + ((int) value);
    }
}