package com.ashehata.sofra.helper;

import android.widget.ImageView;

import com.ashehata.sofra.R;
import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.fake_pic)
                .placeholder(R.drawable.fake_pic)
                .crossFade()
                .into(imageView);
    }
}
