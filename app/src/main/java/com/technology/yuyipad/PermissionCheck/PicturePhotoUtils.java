package com.technology.yuyipad.PermissionCheck;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.technology.yuyipad.code.RSCode;

import java.io.File;
import java.util.Date;

/**
 * Created by wanyu on 2017/10/9.
 */

public class PicturePhotoUtils {
    static PicturePhotoUtils utils;
    private PicturePhotoUtils(){

    }
    public static PicturePhotoUtils getInstance(){
        if (utils==null){
            utils=new PicturePhotoUtils();
        }
        return utils;
    }

    // 拍照
    public void takePhoto(Activity ac, File f){
        if (Build.VERSION.SDK_INT>=23){
            if (PermissionCheck.getInstance().isPermissionGet(new String[]{PermissionNames.CAMERA,PermissionNames.READ_SD,PermissionNames.WRITE_SD},ac)){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                ac.startActivityForResult(intent,RSCode.rCode_TakePhoto);
            }
            else {
                ac.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},RSCode.priCode_TakePhoto);
            }
        }
      else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            ac.startActivityForResult(intent,RSCode.rCode_TakePhoto);
        }
    }

    //浏览图库
    public void searchPhto(Activity ac, File f){
        if (Build.VERSION.SDK_INT>=23){
            if (PermissionCheck.getInstance().isPermissionGet(new String[]{PermissionNames.READ_SD,PermissionNames.WRITE_SD},ac)) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                ac.startActivityForResult(intent, RSCode.rCode_SearchPicture);
            }
            else {
                ac.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},RSCode.priCode_SearchPicture);
            }
        }
    else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            ac.startActivityForResult(intent, RSCode.rCode_SearchPicture);
        }
    }
    //浏览相册后的选取
    public void cutPhoto_Search(Activity ac,File outputImage,Intent data){
        //此处启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data.getData(), "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);//宽度
        intent.putExtra("outputY", 400);//高度
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        ac.startActivityForResult(intent, RSCode.rCode_CutPicture);
    }
    //拍照后的选取
    public void cutPhoto_Camera(Activity ac,Uri uri,File file){
        //此处启动裁剪程序
        Intent intent2 = new Intent("com.android.camera.action.CROP");
        intent2.setDataAndType(uri, "image/*");
        intent2.putExtra("scale", true);
        intent2.putExtra("aspectX", 1);
        intent2.putExtra("aspectY", 1);
        intent2.putExtra("outputX", 400);//宽度
        intent2.putExtra("outputY", 400);//高度
        intent2.putExtra("return-data", true);
        intent2.putExtra("noFaceDetection", true);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ac.startActivityForResult(intent2,RSCode.rCode_CutPicture);
    }

    //-----------------------以下在fragment中使用-------------------------
    // 拍照
    public void takePhotoFrag(Fragment ac, File f){
        if (Build.VERSION.SDK_INT>=23){
            if (PermissionCheck.getInstance().isPermissionGet(new String[]{PermissionNames.CAMERA,PermissionNames.READ_SD,PermissionNames.WRITE_SD},ac.getActivity())){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                ac.startActivityForResult(intent,RSCode.rCode_TakePhoto);
            }
            else {
                ac.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},RSCode.priCode_TakePhoto);
            }
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            ac.startActivityForResult(intent,RSCode.rCode_TakePhoto);
        }
    }

    //浏览图库
    public void searchPhtoFrag(Fragment ac, File f){
        if (Build.VERSION.SDK_INT>=23){
            if (PermissionCheck.getInstance().isPermissionGet(new String[]{PermissionNames.READ_SD,PermissionNames.WRITE_SD},ac.getActivity())) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                ac.startActivityForResult(intent, RSCode.rCode_SearchPicture);
            }
            else {
                ac.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},RSCode.priCode_SearchPicture);
            }
        }
        else {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            ac.startActivityForResult(intent, RSCode.rCode_SearchPicture);
        }
    }
    //浏览相册后的选取
    public void cutPhoto_SearchFrag(Fragment ac,File outputImage,Intent data){
        //此处启动裁剪程序
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data.getData(), "image/*");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 400);//宽度
        intent.putExtra("outputY", 400);//高度
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        ac.startActivityForResult(intent, RSCode.rCode_CutPicture);
    }
    //拍照后的选取
    public void cutPhoto_CameraFrag(Fragment ac, Uri uri, File file){
        //此处启动裁剪程序
        Intent intent2 = new Intent("com.android.camera.action.CROP");
        intent2.setDataAndType(uri, "image/*");
        intent2.putExtra("scale", true);
        intent2.putExtra("aspectX", 1);
        intent2.putExtra("aspectY", 1);
        intent2.putExtra("outputX", 400);//宽度
        intent2.putExtra("outputY", 400);//高度
        intent2.putExtra("return-data", true);
        intent2.putExtra("noFaceDetection", true);
        intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        ac.startActivityForResult(intent2,RSCode.rCode_CutPicture);
    }
    //以下在fragment中使用
}
