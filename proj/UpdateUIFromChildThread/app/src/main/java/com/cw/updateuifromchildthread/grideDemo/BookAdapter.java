package com.cw.updateuifromchildthread.grideDemo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cw.updateuifromchildthread.R;
import com.squareup.picasso.Picasso;

/**
 * Create by robin On 21-3-11
 */
public class BookAdapter extends BaseAdapter {

    private final Context mContext;
    private final Book[] mBooks;

    public BookAdapter(Context context, Book[] books) {
        super();
        mContext = context;
        mBooks = books;
    }

    @Override
    public int getCount() {
        return mBooks.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return getSimpleView(convertView, position, parent);

        final Book book = mBooks[position];

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layout_book, null);

            final ImageView imageViewCoverArt = (ImageView)convertView.findViewById(R.id.imageview_cover_art);
            final TextView nameTextView = (TextView)convertView.findViewById(R.id.textview_book_name);
            final TextView authorTextView = (TextView)convertView.findViewById(R.id.textview_book_author);
            final ImageView imageViewFavorite = (ImageView)convertView.findViewById(R.id.imageview_favorite);

            final ViewHolder viewHolder = new ViewHolder(nameTextView, authorTextView, imageViewCoverArt, imageViewFavorite);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
        if (!TextUtils.isEmpty(book.getImageUrl())) {
//            Picasso.with(mContext).load(book.getImageUrl()).into(viewHolder.imageViewCoverArt);
            Glide.with(mContext).load(book.getImageUrl()).into(viewHolder.imageViewCoverArt);
        } else {
            viewHolder.imageViewCoverArt.setImageResource(book.getImageResource());
        }
        viewHolder.nameTextView.setText(book.getName());
        viewHolder.authorTextView.setText(book.getAuthor());
        viewHolder.imageViewFavorite.setImageResource(book.getImageViewFavorite());

        return convertView;
    }

    private View getSimpleView(View convertView, int position, ViewGroup parent) {
        if (null != convertView) {
            ((TextView)convertView).setText(String.valueOf(position));
            return convertView;
        } else {
            TextView dummyTextView = new TextView(mContext);
            dummyTextView.setText(String.valueOf(position));
            return dummyTextView;
        }
    }
}
