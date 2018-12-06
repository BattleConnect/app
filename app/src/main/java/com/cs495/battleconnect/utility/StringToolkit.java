package com.cs495.battleconnect.utility;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import java.util.Locale;

/**
 * This class contains static methods for manipulating text/strings.
 */
public class StringToolkit {
    /**
     * Makes a substring of a string bold.
     * @param text          Full text
     * @param textToBold    Text you want to make bold
     * @return              String with bold substring
     */
    public static SpannableStringBuilder makeSectionOfTextBold(String text, String textToBold){

        SpannableStringBuilder builder=new SpannableStringBuilder();

        if(textToBold.length() > 0 && !textToBold.trim().equals("")){

            //for counting start/end indexes
            String testText = text.toLowerCase(Locale.US);
            String testTextToBold = textToBold.toLowerCase(Locale.US);
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();
            //for counting start/end indexes

            if(startingIndex < 0 || endingIndex <0){
                return builder.append(text);
            }
            else if(startingIndex >= 0 && endingIndex >=0){

                builder.append(text);
                builder.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0);
            }
        }else{
            return builder.append(text);
        }

        return builder;
    }
}
