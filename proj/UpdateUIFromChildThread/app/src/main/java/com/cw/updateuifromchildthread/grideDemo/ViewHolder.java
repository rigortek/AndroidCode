package com.cw.updateuifromchildthread.grideDemo;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Create by robin On 21-3-11
 */
// Your "view holder" that holds references to each subview
public class ViewHolder {
    public final TextView nameTextView;
    public final TextView authorTextView;
    public final ImageView imageViewCoverArt;
    public final ImageView imageViewFavorite;

    public ViewHolder(TextView nameTextView, TextView authorTextView, ImageView imageViewCoverArt, ImageView imageViewFavorite) {
        this.nameTextView = nameTextView;
        this.authorTextView = authorTextView;
        this.imageViewCoverArt = imageViewCoverArt;
        this.imageViewFavorite = imageViewFavorite;
    }
}

