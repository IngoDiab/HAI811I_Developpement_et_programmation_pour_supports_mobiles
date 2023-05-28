package com.mobile.reverleaf;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.function.Consumer;

public class TargetWrapper {

    Context mContext;
    Resources mResources;
    Consumer<Drawable> mOnImageLoaded;
    String mPathImage;

    private final Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Drawable _imageDrawable = new BitmapDrawable(mResources, bitmap);
            mOnImageLoaded.accept(_imageDrawable);
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            ViewHelper.PrintToast(mContext, "Image loading failed : "+mPathImage);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public Target GetTarget() { return mTarget; }

    public void SetContext(Context _context) { mContext = _context; }
    public void SetResources(Resources _resources) { mResources = _resources; }
    public void SetCallbackLoaded(Consumer<Drawable> _onImageLoaded) { mOnImageLoaded = _onImageLoaded; }
    public void SetPathImage(String _pathImage) { mPathImage = _pathImage; }

    public TargetWrapper()
    {
    }

    public TargetWrapper(Context _context, Resources _resources, Consumer<Drawable> _onImageLoaded, String _pathImage)
    {
        mContext = _context;
        mResources = _resources;
        mOnImageLoaded = _onImageLoaded;
        mPathImage = _pathImage;
    }
}
