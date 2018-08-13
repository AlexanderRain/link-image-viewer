package com.example.b.presenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.b.model.interactors.MainInteractor;
import com.example.b.ui.fragments.MainFragmentView;
import com.example.b.utils.Constants;

public class MainPresenter {

    private MainInteractor interactor;
    private MainFragmentView view;
    private Context context;

    public MainPresenter(MainFragmentView view, Context context) {
        interactor = new MainInteractor(context);
        this.view = view;
        this.context = context;
    }

    public void receiveExtras(String imageUrl, int imageStatus, long imageId) {

        switch (imageStatus) {
            case Constants.DEFAULT:
                // Alexander Rain: imageUrl == nul, when app start from launcher
                if(imageUrl == null) {
                    view.closeApp();
                    break;
                }
                // Alexander Rain: this case for links, comes from test fragment
                view.showImage(imageUrl, getInsertRequestListener(imageUrl));
                break;

            case Constants.INSERTED:
                view.saveOnPath(imageUrl);
                view.showImage(imageUrl, null);
                deleteWithDelayed(imageUrl, imageId);
                // Alexander Rain: this case for successful loaded links
                break;

            default:
                // Alexander Rain: this case for UNDEFINED and ERROR links
                view.showImage(imageUrl, getUpdateRequestListener(imageUrl, imageId));
        }
    }

    // Alexander Rain:
    // this listener for insert links
    // https://bumptech.github.io/glide/javadocs/400/com/bumptech/glide/request/RequestListener.html
    public RequestListener<Bitmap> getInsertRequestListener(final String imageUrl) {
        return new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                checkForInternetConnection(imageUrl);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                interactor.insertImage(imageUrl, Constants.INSERTED);
                return false;
            }
        };
    }

    // Alexander Rain:
    // this listener for update links
    public RequestListener<Bitmap> getUpdateRequestListener(final String imageUrl, final long imageId) {
        return new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                checkForInternetConnection(imageUrl);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                interactor.updateImage(imageUrl, Constants.INSERTED, imageId);
                return false;
            }
        };
    }

    private void deleteWithDelayed(final String imageUrl, final long imageId) {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                interactor.deleteImage(imageUrl, Constants.INSERTED, imageId);
            }
        },10000);
    }

    private void checkForInternetConnection(String imageUrl) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();

        if (network != null && network.isConnected()) {
            interactor.insertImage(imageUrl, Constants.UNDEFINED);
        } else {
            interactor.insertImage(imageUrl, Constants.ERROR);
        }
    }
}