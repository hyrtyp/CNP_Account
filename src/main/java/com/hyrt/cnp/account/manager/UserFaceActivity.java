package com.hyrt.cnp.account.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyrt.cnp.R;
import com.hyrt.cnp.account.model.UserDetail;
import com.hyrt.cnp.account.request.UserFaceRequest;
import com.hyrt.cnp.account.requestListener.UserFaceRequestListener;
import com.hyrt.cnp.account.utils.FaceUtils;
import com.hyrt.cnp.account.utils.FileUtils;
import com.hyrt.cnp.account.utils.PhotoUpload;
import com.jingdong.app.pad.product.drawable.HandlerRecycleBitmapDrawable;
import com.jingdong.app.pad.utils.InflateUtil;
import com.jingdong.common.frame.BaseActivity;
import com.jingdong.common.utils.cache.GlobalImageCache;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.WeakReference;

import roboguice.activity.RoboActivity;

public class UserFaceActivity extends BaseActivity {

    private static final String TAG = "UserFaceActivity";
    private WeakReference<ImageView> weakImageView;
    private PhotoUpload photoUpload;
    private Uri faceFile;
    private Bitmap bitmap;
    private GlobalImageCache.BitmapDigest localBitmapDigest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_face);
        findViewById(R.id.upload_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceFile = Uri.fromFile(FileUtils.createFile("cnp", "face.jpg"));
                photoUpload = new PhotoUpload(UserFaceActivity.this, faceFile);
                photoUpload.choiceItem();
            }
        });
        initData();

    }


    /**
     * 加载图片头像
     */
    private void initData() {
        UserDetail.UserDetailModel userDetail = (UserDetail.UserDetailModel) getIntent().getSerializableExtra("vo");
        String facePath = FaceUtils.getAvatar(userDetail.getData().getUser_id(), FaceUtils.FACE_BIG);
        ImageView imageView = (ImageView) findViewById(R.id.big_face);
        weakImageView = new WeakReference<ImageView>(imageView);
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
     * 监听剪切好的图片并上传|剪切保存好的图片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUpload.PHOTO_ZOOM && data != null) {

            //保存剪切好的图片
            bitmap = data.getParcelableExtra("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            File targetFile = FileUtils.writeFile(baos.toByteArray(), "cnp", "face.jpg");

            //上传图片资源
            UserFaceRequest request = new UserFaceRequest(this, targetFile);
            String lastRequestCacheKey = request.createCacheKey();
            UserFaceRequestListener userFaceRequestListener = new UserFaceRequestListener(this);
            spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND, userFaceRequestListener.start());
        } else if (requestCode == PhotoUpload.FROM_CAMERA) {
            photoUpload.startPhotoZoom(faceFile);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传图片成功后,更新缓存中的图片
     */
    public void updateCacheAndUI() {
        GlobalImageCache.getLruBitmapCache().put(localBitmapDigest,bitmap);
        setResult(1,new Intent());
        this.finish();
    }
}
