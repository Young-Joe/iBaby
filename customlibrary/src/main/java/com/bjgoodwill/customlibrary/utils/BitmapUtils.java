package com.bjgoodwill.customlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by QiaoJF on 17/2/23.
 */

public class BitmapUtils {

    /**
     * 压缩图片,转成成字符串达到服务的要求
     *
     * @param bitmap
     * @param isScale 是否进行缩放压缩
     * @param sampleSize 采样压缩比率(应为2的倍数才有效)
     * @return 进行过Base64压缩的字符串
     */
    public static String compressBitmap(Bitmap bitmap, boolean isScale, int sampleSize){
        bitmap = convertToBinaryImg(bitmap,180);
        if (isScale) {bitmap = scaleBitmap(bitmap);}
        bitmap = sampCompress(bitmap, sampleSize);
        String strImg = convertToBMW(bitmap, 180);
        //对图像字符串进行GZIP压缩后再进行Base64压缩
        return Base64.encodeToString(CompressUtil.compress(strImg.getBytes()),Base64.DEFAULT);
    }

    /**
     * 转为二值图像
     *
     * @param bmp
     *            原图bitmap
     * @return
     */
    public static Bitmap convertToBinaryImg(Bitmap bmp, int tmp) {
        int width = bmp.getWidth();   // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为100
        // tmp = 180;
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8
                        | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                } else {
                    pixels[width * i + j] = -16777216;
                }
            }
        }
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        // 获取缩略图
        //Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
        return newBmp;
    }

    /**
     * 手写签名(800*400)
     * 按比例缩放图片
     *
     * @param origin 原图
     * @return 新的bitmap
     */
    public static Bitmap scaleBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        float sx = 800f / width;
        float sy = 400f / height;
        matrix.preScale(sx, sy);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, true);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 采样压缩和质量压缩
     * @param bitmap
     * @param sampleSize 应为2的倍数
     * @return
     */
    public static Bitmap sampCompress(Bitmap bitmap, int sampleSize){
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
            newOpts.inJustDecodeBounds = true;
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
            newOpts.inSampleSize = sampleSize;
            newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
            newOpts.inJustDecodeBounds = false;
            // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
            isBm = new ByteArrayInputStream(baos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

            //存图片到本地
//            ImageUtils.saveBitmap2SD(AppConstant.getNurseAvatarPath() + TimeUtils.getCurrentTime() + ".png", bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 转为二值图像
     *
     * @param bmp
     *            原图bitmap
     * @return
     */
    public static String convertToBMW(Bitmap bmp, int tmp) {
        int width = bmp.getWidth();   // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int imgLength = width * height;
        String strImg = width+"*"+height;
        //最后记录的像素值，初始为白（-1）
        int lastColor = -1;
        //每种颜色的个数
        int colorCount = 0;
        int[] pixels = new int[imgLength]; // 通过位图的大小创建像素点数组
        // 设定二值化的域值，默认值为100
        // tmp = 180;
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];
                // 分离三原色
                alpha = ((grey & 0xFF000000) >> 24);
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);
                if (red > tmp) {
                    red = 255;
                } else {
                    red = 0;
                }
                if (blue > tmp) {
                    blue = 255;
                } else {
                    blue = 0;
                }
                if (green > tmp) {
                    green = 255;
                } else {
                    green = 0;
                }
                pixels[width * i + j] = alpha << 24 | red << 16 | green << 8 | blue;
                if (pixels[width * i + j] == -1) {
                    pixels[width * i + j] = -1;
                } else {
                    pixels[width * i + j] = -16777216;
                }

                if(pixels[width * i + j] == lastColor){
                    colorCount++;
                }else {
                    strImg = strImg + colorCount + " ";
                    //复位个数
                    colorCount = 1;
                    //记录最后的像素值
                    lastColor = pixels[width * i + j];
                }
                if (width * i + j == imgLength - 1) {
                    strImg = strImg + colorCount;
                }

            }
        }
        return strImg;
    }

    /**
     * 解压后strImg为 200*100 开始
     * @param strImg
     * @return
     */
    public static Bitmap string2bitmap(String strImg) {
        strImg = CompressUtil.unCompress(strImg);
        String strSize = strImg.substring(0,7);
        String strBitmap = strImg.substring(7);
        int width = 200;   // 获取位图的宽
        int height = 100; // 获取位图的高
        try {
            String[] size = strSize.split("\\*");
            width = Integer.parseInt(size[0]);
            height = Integer.parseInt(size[1]);
        }catch (Exception e){
            e.printStackTrace();
        }
        int imgLength = width * height;
        //最后记录的像素值，初始为白（-1）
        int lastColor = -1;
        //每种颜色的个数
        int colorCount = 0;
        int[] pixels = new int[imgLength]; // 通过位图的大小创建像素点数组

        String[] strImgs = strBitmap.split(" ");

        int addNum = 0;
        int addColorPx = -1;
        for(int i = 0;i < strImgs.length;i++){
            int num = Integer.parseInt(strImgs[i]);
            int numPx = i % 2;
            if(numPx == 0){
                addColorPx = -1;
            }else {
                addColorPx = -16777216;
            }

            for(int j = addNum; j < (addNum+num);j++){
                pixels[j] = addColorPx;
            }
            addNum = addNum+num;
        }
        // 新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        // 设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        // 获取缩略图
        //Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(newBmp, width, height);
        return newBmp;
    }

}
