package com.joe.customlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.joe.customlibrary.common.CommonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 *
 * 压缩/解压缩类
 *
 * Created by QiaoJF on 17/11/23.
 */

public class CompressUtil {

    /**
     * 压缩
     */
    public static byte[] compress(byte[] input) {
        if (input == null || input.length == 0) {
            return input;
        }
        byte[] output;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(input);
            gzip.close();
            output = out.toByteArray();
        } catch (IOException e) {
            output = new byte[0];
        } finally {
            try {
                gzip.close();
                out.close();
            } catch (IOException e) {
            }
        }
        return output;
    }

    /**
     * 解压缩
     * 出现异常则反回input
     */
    public static String unCompress(String input) {
        if (input == null || input.length() == 0) {
            return input;
        }

        //服务提供待解压字符串需先进行Base64解密
        byte[] inputByte = Base64.decode(input, Base64.DEFAULT);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(inputByte);
        GZIPInputStream gunzip = null;
        byte[] output = null;
        String outputStr = null;
        try {
            gunzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = gunzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            output = out.toByteArray();
            //输出解压后字符串
            outputStr = new String(output, "UTF-8");
            gunzip.close();
            in.close();
            out.close();
        } catch (IOException e) {
            outputStr = input;
            e.printStackTrace();
        } finally {
            try {
                if (gunzip != null) {
                    gunzip.close();
                }
                in.close();
                out.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return outputStr;
    }

    /**
     * BASE64 解密
     * @param str
     * @return
     */
    public static  String decryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("UTF-8");
            // base64 解密
            return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Base64解密得到byte[]并转为bitmap
     * @param str
     * @return
     */
    public static Bitmap decryptBase2Bitmap(String str){
        Bitmap bitmap = null;
        if(CommonUtils.isTextEmpty(str)){
            return bitmap;
        }
        try {
            byte[] encode = new byte[0];
            encode = str.getBytes("UTF-8");
            byte[] bytes = Base64.decode(encode, 0, encode.length, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
