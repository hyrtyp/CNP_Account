package com.hyrt.cnp.account.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.UserDetail;
import com.hyrt.cnp.account.request.UserDetailRequest;
import com.hyrt.cnp.account.requestListener.UserDetailRequestListener;
import com.hyrt.cnp.account.utils.FaceUtils;
import com.jingdong.app.pad.product.drawable.HandlerRecycleBitmapDrawable;
import com.jingdong.app.pad.utils.InflateUtil;
import com.jingdong.common.frame.BaseActivity;
import com.jingdong.common.utils.cache.GlobalImageCache;
import com.octo.android.robospice.persistence.DurationInMillis;

import net.oschina.app.AppContext;

import java.lang.ref.WeakReference;

public class UserMainActivity extends BaseActivity {

    private UserDetail.UserDetailModel userDetail;

    private WeakReference<ImageView> weakImageView;

    public final static int UPDATE_FACE = 1;

    private GlobalImageCache.BitmapDigest localBitmapDigest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        findViewById(R.id.user_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDetail!=null){
                    Intent intent = new Intent(UserMainActivity.this,UserInfoActivity.class);
                    intent.putExtra("vo",userDetail);
                    startActivity(intent);
                }
            }
        });
        findViewById(R.id.update_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDetail!=null){
                    Intent intent = new Intent(UserMainActivity.this,UserFaceActivity.class);
                    intent.putExtra("vo",userDetail);
                    startActivityForResult(intent,UPDATE_FACE);
                }
            }
        });
        findViewById(R.id.update_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userDetail!=null){
                    Intent intent = new Intent(UserMainActivity.this,UserPasswordActivity.class);
                    intent.putExtra("vo",userDetail);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppContext.getInstance().setBaseActivity(this);
        initData();
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

    /**
     * 更新页面元素数据
     * @param userDetail
     */
    public void updateUI(UserDetail.UserDetailModel userDetail) {
        this.userDetail = userDetail;
        String facePath = FaceUtils.getAvatar(userDetail.getData().getUser_id(),FaceUtils.FACE_SMALL);
        ImageView imageView = (ImageView)findViewById(R.id.user_face);
        weakImageView = new WeakReference<ImageView>(imageView);
        ((TextView) findViewById(R.id.class_tv)).setText(userDetail.getData().getNurseryName());
        ((TextView) findViewById(R.id.name_tv)).setText(userDetail.getData().getRenname());
        HandlerRecycleBitmapDrawable localHandlerRecycleBitmapDrawable = new HandlerRecycleBitmapDrawable(null, this);
        imageView.setImageDrawable(localHandlerRecycleBitmapDrawable);
        localBitmapDigest = new GlobalImageCache.BitmapDigest(facePath);
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
        } else {
            localHandlerRecycleBitmapDrawable.setBitmap(localBitmap);
            localHandlerRecycleBitmapDrawable.invalidateSelf();
        }
    }

    /**
     * 如果头像更新的话，则需要回收原来的图片，并从缓存中清除 localBitmapDigest
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppContext.getInstance().setBaseActivity(this);
        if(resultCode == 1 && requestCode == UPDATE_FACE){
            if(weakImageView.get() != null){
                weakImageView.get().setImageDrawable(null);
            }
            GlobalImageCache.getLruBitmapCache().get(localBitmapDigest).recycle();
            GlobalImageCache.remove(localBitmapDigest);
        }
    }


}
