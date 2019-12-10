package com.example.floria.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;

import androidx.core.content.res.ResourcesCompat;

import com.example.floria.R;

import java.util.Map;

public class CustomTextView {

    public final static String NICKNAME_VALUE_CUSTOM = "@Nickname";
    public final static String DESCRIPTION_VALUE_CUSTOM = "@Description";

    private final static String FONT = "font";

    Map<String,String> KeyValues;

    String NicknameValue;
    String DescriptionValue;

    Context context;

    public CustomTextView() {

    }

    public void addKeyValues(Map<String, String> KeyValues){
        this.KeyValues = KeyValues;

        NicknameValue = KeyValues.get(NICKNAME_VALUE_CUSTOM);
        DescriptionValue = KeyValues.get(DESCRIPTION_VALUE_CUSTOM);
    }

    public void addContext(Context context){
        this.context = context;
    }

    public SpannableStringBuilder getCustomText(){

        SpannedString text = SpannedString.valueOf(context.getResources().getText(R.string.nickHTML));
        SpannableStringBuilder spannable = new SpannableStringBuilder(text);


        Annotation[] annotations  = spannable.getSpans(0,text.length(),Annotation.class);

        for (Annotation annotation : annotations){

            String key = annotation.getKey();

            if(key.equals(FONT)){

                String fontName = annotation.getValue();

                int redID= context.getResources().getIdentifier(fontName,FONT,context.getPackageName());

                Typeface typeface = ResourcesCompat.getFont(context,redID);
                spannable.setSpan(
                        new CustomTypefaceSpan("",typeface),
                        spannable.getSpanStart(annotation),
                        spannable.getSpanEnd(annotation),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                String valueSpan = SpannableString.valueOf(
                        spannable.subSequence(spannable.getSpanStart(annotation),spannable.getSpanEnd(annotation))).toString();

                if(valueSpan.equals(NICKNAME_VALUE_CUSTOM)){
                    spannable.replace(spannable.getSpanStart(annotation),spannable.getSpanEnd(annotation),NicknameValue);
                }else if(valueSpan.equals(DESCRIPTION_VALUE_CUSTOM)){
                    spannable.replace(spannable.getSpanStart(annotation),spannable.getSpanEnd(annotation),DescriptionValue);
                }
            }

        }

        return spannable;

    }

}


