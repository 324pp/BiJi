package com.mh.biji;

import android.graphics.Color;

/**
 * Created by MH on 2015-06-25.
 */
public class ColorItem {
    int item[];

    public ColorItem() {
        item = new int[64];
        int h = 0;
        for (int i=0; i<4; i++) {
            for (int j=0; j<4; j++) {
                for (int k=0; k<4; k++) {
                    item[h] = Color.rgb(i * 85, j * 85, k * 85);
                    h++;
                }
            }
        }
    }

    public int[] getList() {
        return item;
    }

    public int indexOf(int it) {
        for (int i=0; i<item.length; i++) {
            if (item[i] == it) return i;
        }
        return -1;
    }
}
