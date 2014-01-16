package com.hyrt.cnp.account.manager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int FORDORA_IMAGE_ACTIVITY_REQUEST_CODE = 1;
    private  GlobalImageCache.BitmapDigest localBitmapDigest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_face);
        findViewById(R.id.upload_face).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra("noFaceDetection", true); // no face detection
                startActivityForResult(intent, FORDORA_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData(){
        UserDetail.UserDetailModel userDetail = (UserDetail.UserDetailModel) getIntent().getSerializableExtra("vo");
        String facePath = FaceUtils.getAvatar(userDetail.getData().getUser_id(), FaceUtils.FACE_BIG);
        ImageView imageView = (ImageView)findViewById(R.id.big_face);
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

    public void removeCacheFace(){
        GlobalImageCache.remove(localBitmapDigest);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == FORDORA_IMAGE_ACTIVITY_REQUEST_CODE){
            if(data != null){
                ImageView imageView = (ImageView) findViewById(R.id.big_face);
                Bitmap bitmap = data.getParcelableExtra("data");
                imageView.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                File targetFile = FileUtils.writeFile(baos.toByteArray(),"cnp","face.jpg");
                UserFaceRequest request = new UserFaceRequest(this, targetFile);
                String lastRequestCacheKey = request.createCacheKey();
                UserFaceRequestListener userFaceRequestListener = new UserFaceRequestListener(this);
                spiceManager.execute(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND,userFaceRequestListener.start());

            }
        }else if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
