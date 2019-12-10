package com.example.floria.adapters;

import android.content.Context;

import android.graphics.Typeface;
import android.text.Annotation;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.floria.R;
import com.example.floria.modelo.Card_Post;
import com.example.floria.modelo.Comments;
import com.example.floria.utils.CustomTextView;
import com.example.floria.utils.CustomTypefaceSpan;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ContactoAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Card_Post> lista;

    public ContactoAdapter(Context context, int layout, ArrayList<Card_Post> lista) {
        this.context = context;
        this.layout = layout;
        this.lista = lista;
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int position) {
        return lista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        v = layoutInflater.inflate(layout, null);

        Card_Post item = lista.get(position);

        TextView tvView = v.findViewById(R.id.tvView);
        TextView tvMessage = v.findViewById(R.id.tvMessage);
        TextView tvLike = v.findViewById(R.id.tvLike);
        TextView tvNickBottom = v.findViewById(R.id.tvNickBottom);
        ImageView ivCard = v.findViewById(R.id.ivCard);

        TextView textView3 = v.findViewById(R.id.textView3);
        TextView textView4 = v.findViewById(R.id.textView4);
        TextView textView5 = v.findViewById(R.id.textView5);

        /*First Three Comments*/

        List<TextView> textViews = new ArrayList<>();

        textViews.add(textView3);
        textViews.add(textView4);
        textViews.add(textView5);


        for (TextView actuaTextView : textViews) {

            int index = textViews.indexOf(actuaTextView);

            if (index < item.listaComments.size()) {

                Comments reg = item.listaComments.get(index);

                if (!reg.equals(null)) {

                    /*CustomTextView*/

                    CustomTextView custom = new CustomTextView();

                    Map<String, String> mapValues = new HashMap<>();

                    mapValues.put(custom.NICKNAME_VALUE_CUSTOM, reg.getNick());
                    mapValues.put(custom.DESCRIPTION_VALUE_CUSTOM, reg.getComment());

                    custom.addKeyValues(mapValues);
                    custom.addContext(context);

                    /* ---- */

                    SpannableStringBuilder spannable = custom.getCustomText();

                    actuaTextView.setText(spannable);

                    actuaTextView.setVisibility(View.VISIBLE);


                }
            }

        }

        /* ---- */

        int countComments = item.getCountComments();

        System.out.println(countComments);

        tvView.setText(String.valueOf(item.getViews()));
        tvMessage.setText(String.valueOf(item.listaComments.size()));
        tvLike.setText(String.valueOf(item.getLike()));

        /* CustomTextView */

        CustomTextView custom = new CustomTextView();

        Map<String, String> mapValues = new HashMap<>();

        mapValues.put(custom.NICKNAME_VALUE_CUSTOM, "Carlitos");
        mapValues.put(custom.DESCRIPTION_VALUE_CUSTOM, "Hoy hace un buen dia");

        custom.addKeyValues(mapValues);
        custom.addContext(context);

        /* ---- */

        SpannableStringBuilder spannable = custom.getCustomText();

        tvNickBottom.setText(spannable);

        ivCard.setImageResource(R.drawable.imagecard);

        Picasso.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.ic_undraw_loading_frh4)
                .fit()
                .centerCrop()
                .into(ivCard);

        return v;
    }
}
