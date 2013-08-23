/*
 * Copyright (C) 2013 The CyanogenMod Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyanogenmod.hardware;

import android.text.TextUtils;
import java.io.File;
import java.lang.Integer;
import org.cyanogenmod.hardware.util.FileUtils;

public class DisplayGammaCalibration {

    private static final int OFFSET_VALUE = 100;

    private static final String[] GAMMA_FILE_PATH = new String[] {
        "/sys/class/misc/samoled_color/red_v1_offset",
        "/sys/class/misc/samoled_color/green_v1_offset",
        "/sys/class/misc/samoled_color/blue_v1_offset"
    };

    public static boolean isSupported() {
        for (String filePath : GAMMA_FILE_PATH) {
            if (!new File(filePath).exists()) {
                return false;
            }
        }
        return true;
    }

    public static int getNumberOfControls() {
        return 1;
    }

    public static int getMaxValue()  {
        return OFFSET_VALUE * 2;
    }

    public static int getMinValue()  {
        return 0;
    }

    public static String getCurGamma(int control) {
        StringBuilder values = new StringBuilder();
        for (String filePath : GAMMA_FILE_PATH) {
            String currentValue = FileUtils.readOneLine(filePath);
            int offsetValue = Integer.parseInt(currentValue) + OFFSET_VALUE;
            values.append(offsetValue).append(" ");
        }
        return values.toString();
    }

    public static boolean setGamma(int control, String gamma)  {
        String[] valueSplit = gamma.split(" ");
        boolean result = true;
        for (int i = 0; i < valueSplit.length; i++) {
            String targetFile = GAMMA_FILE_PATH[i];
            int offsetValue = Integer.parseInt(valueSplit[i]) - OFFSET_VALUE;
            result &= FileUtils.writeLine(targetFile, String.valueOf(offsetValue));
        }
        return result;
    }
}

