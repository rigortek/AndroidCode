package com.cw.updateuifromchildthread.grideDemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.cw.updateuifromchildthread.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class GridFullscreenActivity extends AppCompatActivity {
    private GridView mGrideView;
    private Book[] books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grid_fullscreen);

        mGrideView = findViewById(R.id.gridview);
        books = new Book[1000];
        for (int i = 0; i < books.length; i++) {
            books[i] = new Book();
            if (i < 34) {
                books[i].setImageUrl("http://10.27.65.173/testimg/" + i + (i < 26 ? ".jpg" : ".png"));
            }
            books[i].setImageResource(R.drawable.book)
                    .setImageViewFavorite(0 == i % 2 ? R.drawable.star_enable : R.drawable.star_disabled)
                    .setName("New book of baby" + i)
                    .setAuthor("ShanghaiPudong");
        }
        BookAdapter bookAdapter = new BookAdapter(this, books);
        mGrideView.setAdapter(bookAdapter);

        mGrideView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridFullscreenActivity.this, "You click: " + books[position].getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}