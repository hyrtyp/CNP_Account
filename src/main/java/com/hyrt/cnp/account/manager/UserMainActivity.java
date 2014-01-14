package com.hyrt.cnp.account.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.inject.Key;
import com.hyrt.cnp.R;
import com.hyrt.cnp.account.LoginActivity;
import com.hyrt.cnp.account.model.UserDetail;
import com.hyrt.cnp.account.request.UserDetailRequest;
import com.hyrt.cnp.account.requestListener.LoginRequestListener;
import com.hyrt.cnp.account.requestListener.UserDetailRequestListener;
import com.hyrt.cnp.account.utils.FaceUtils;
import com.jingdong.app.pad.product.drawable.HandlerRecycleBitmapDrawable;
import com.jingdong.app.pad.utils.InflateUtil;
import com.jingdong.common.frame.BaseActivity;
import com.jingdong.common.http.HttpGroup;
import com.jingdong.common.http.HttpGroupSetting;
import com.jingdong.common.utils.cache.GlobalImageCache;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import net.oschina.app.AppContext;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import roboguice.activity.RoboActivity;

public class UserMainActivity extends BaseActivity {

    private WeakReference<ImageView> weakImageView;

    public HttpGroup getHttpGroupaAsynPool() {
        return getHttpGroupaAsynPool(HttpGroupSetting.TYPE_JSON);
    }

    public HttpGroup getHttpGroupaAsynPool(int type) {
        HttpGroupSetting localHttpGroupSetting = new HttpGroupSetting();
        localHttpGroupSetting.setType(type);
        return getHttpGroupaAsynPool(localHttpGroupSetting);
    }

    public HttpGroup getHttpGroupaAsynPool(
            final HttpGroupSetting paramHttpGroupSetting) {
        HttpGroup.HttpGroupaAsynPool localHttpGroupaAsynPool = new HttpGroup.HttpGroupaAsynPool(
                paramHttpGroupSetting);
        return localHttpGroupaAsynPool;
    }

    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        findViewById(R.id.user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserMainActivity.this,UserInfoActivity.class));
            }
        });
        findViewById(R.id.update_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserMainActivity.this,UserFaceActivity.class));
            }
        });
        findViewById(R.id.update_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserMainActivity.this,UserPasswordActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    /**
     * 获取数据
     */
    private void initData() {
        UserDetailRequest userDetailRequest = new UserDetailRequest(this);
        UserDetailRequestListener userDetailRequestListener = new UserDetailRequestListener(this);
        spiceManager.execute(userDetailRequest, userDetailRequest.createCacheKey(),
                DurationInMillis.ONE_MINUTE, userDetailRequestListener.start());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public void updateUI(UserDetail.UserDetailModel userDetail) {
        String facePath = FaceUtils.getAvatar(104,FaceUtils.FACE_SMALL);
        ImageView imageView = (ImageView)findViewById(R.id.user_face);
        weakImageView = new WeakReference<ImageView>(imageView);
        ((TextView) findViewById(R.id.class_tv)).setText(userDetail.getData().getNurseryName());
        HandlerRecycleBitmapDrawable localHandlerRecycleBitmapDrawable = new HandlerRecycleBitmapDrawable(null, this);
        imageView.setImageDrawable(localHandlerRecycleBitmapDrawable);
        GlobalImageCache.BitmapDigest localBitmapDigest = new GlobalImageCache.BitmapDigest(facePath);
        localBitmapDigest.setWidth(imageView.getWidth());
        localBitmapDigest.setHeight(imageView.getHeight());
        Bitmap localBitmap = InflateUtil.loadImageWithCache(localBitmapDigest);
        if (localBitmap == null) {
            HandlerRecycleBitmapDrawable localHandlerRecycleBitmapDrawable2 = (HandlerRecycleBitmapDrawable) imageView.getDrawable();
            localHandlerRecycleBitmapDrawable2.setBitmap(null);
            localHandlerRecycleBitmapDrawable.invalidateSelf();
            InflateUtil.loadImageWithUrl(getHttpGroupaAsynPool(), localBitmapDigest, new InflateUtil.ImageLoadListener() {
                public void onError(GlobalImageCache.BitmapDigest paramAnonymousBitmapDigest) {
                }

                public void onProgress(GlobalImageCache.BitmapDigest paramAnonymousBitmapDigest, int paramAnonymousInt1, int paramAnonymousInt2) {
                }

                public void onStart(GlobalImageCache.BitmapDigest paramAnonymousBitmapDigest) {
                }

                public void onSuccess(GlobalImageCache.BitmapDigest paramAnonymousBitmapDigest, Bitmap paramAnonymousBitmap) {
                    if (weakImageView != null) {
                        ImageView targetIv = weakImageView.get();
                        if (targetIv != null) {
                            HandlerRecycleBitmapDrawable localHandlerRecycleBitmapDrawable = (HandlerRecycleBitmapDrawable) targetIv.getDrawable();
                            localHandlerRecycleBitmapDrawable.setBitmap(paramAnonymousBitmap);
                            localHandlerRecycleBitmapDrawable.invalidateSelf();
                        }
                    }
                }
            });
        }
    }


    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }
}
