/*
* Copyright (C) 2013 SlimRoms Project
*
* Modifications to original by Jeff Corcoran
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package ca.mimic.apphangar;

import android.graphics.Bitmap;

public class ColorHelper {

    public static Bitmap getColoredBitmap(Bitmap colorBitmap, int color) {
        Bitmap grayscaleBitmap = toGrayscale(colorBitmap);

        return grayscaleBitmap;
    }

    private static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        return bmpGrayscale;
    }

}
