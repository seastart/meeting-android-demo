/*
 * Copyright (c) 2015-2023 Hangzhou Freewind Technology Co., Ltd.
 *  All rights reserved.
 * http://www.seastart.cn
 *
 * This product is protected by copyright and distributed under
 * licenses restricting copying, distribution and decompilation.
 */

package com.freewind.seastarvideo.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * @author wiatt
 * @description: 文件管理工具
 */
public class FileUtil {

    private static String TAG = FileUtil.class.getSimpleName();

    /*
     * external storage
       外部存储	Environment.getExternalStorageDirectory()	SD根目录:/mnt/sdcard/ (6.0后写入需要用户授权)
                context.getExternalFilesDir(dir)	路径为:/mnt/sdcard/Android/data/< package name >/files/…
                context.getExternalCacheDir()	路径为:/mnt/sdcard//Android/data/< package name >/cach/…
                *
      internal storage
      内部存储
              context.getFilesDir()	路径是:/data/data/< package name >/files/…
              context.getCacheDir()	路径是:/data/data/< package name >/cach/…
    */

    /**
     * @return .外部储存sd卡 根路径
     */
    public static String getRootPath() {
        // /storage/emulated/0
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * @param context .
     * @return . 外部储存sd卡 :/mnt/sdcard/Android/data/< package name >/files/…
     */
    public static String getAppRootPth(Context context) {
        // /storage/emulated/0/Android/data/pack_name/files
        return context.getExternalFilesDir("").getAbsolutePath();
    }

    /**
     * @return .内部存储
     */
    public static String getInternalPath() {
        // /data
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * @param context .
     * @return .内部储存:/data/data/< package name >/files/
     */
    public static String getInternalAppPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }


    /**
     * @param path     路径
     * @param fileName 文件名称
     * @return .
     */
    public static boolean createFile(String path, String fileName) {
        File file = new File(path + File.separator + fileName);

        //先创建文件夹 保证文件创建成功
        createDirs(path);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            //
            return false;
        }
    }

    /**
     * @param folder 创建多级文件夹
     * @return .
     */
    public static boolean createDirs(String folder) {
        File file = new File(folder);

        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            LogUtil.i(TAG, "createDirs: 不存在文件夹 开始创建" + mkdirs + "--" + folder);
            return true;
        } else {
            LogUtil.i(TAG, "createDirs: 文件夹已存在");
        }
        return false;
    }


    /**
     * =======================================文件读写=============================================
     *
     * @param content   写入字符串
     * @param path      .    目录
     * @param fileName  .文件名
     * @param isRewrite 是否覆盖
     */
    //1.RandomAccessFile 读写
    public static void writeFileR(String content, String path, String fileName, boolean isRewrite) {

        File file = new File(path + fileName);
        if (!file.exists()) {
            createFile(path, fileName);
        }

        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");

            if (isRewrite) {
                randomAccessFile.setLength(content.length());
                randomAccessFile.seek(0);
            } else {
                randomAccessFile.seek(randomAccessFile.length());
            }
            randomAccessFile.write(content.getBytes());

            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFileR(String content, String path, String fileName) {

        File file = new File(path + fileName);
        if (!file.exists()) {
            createFile(path, fileName);
        }

        RandomAccessFile randomAccessFile;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");
            //默认覆盖
            randomAccessFile.setLength(content.length());
            randomAccessFile.seek(0);

            randomAccessFile.write(content.getBytes());

            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读文件
     *
     * @param path     .
     * @param fileName .
     * @return .
     */
    public static String readFileR(String path, String fileName) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            LogUtil.i(TAG, "readFileR: return null");
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            randomAccessFile.seek(0);
            byte[] buf = new byte[(int) randomAccessFile.length()];
            if (randomAccessFile.read(buf) != -1) {
                buffer.append(new String(buf));
                LogUtil.i(TAG, "readFileR: length" + randomAccessFile.length());
                //buffer.append(new String(buf, StandardCharsets.UTF_8));
            }
            randomAccessFile.close();
        } catch (IOException e) {
            LogUtil.i(TAG, "readFileR: " + e.getMessage());
            e.printStackTrace();
        }

        return buffer.toString();
    }


    /**
     * 读文件
     *
     * @param path   .文件路径
     * @param name   .名称
     * @return .
     */
    public static String readFileI(String path, String name ) {
        //默认编码格式 StandardCharsets.UTF_8;
        File file = new File(path,name);
        if (!file.exists()) {
            return null;
        }
        StringBuilder builder = new StringBuilder("");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8 ));
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();

    }


    /**
     * 写入文件
     *
     * @param content  内容.
     * @param path     目录.
     * @param fileName 文件名   .
     */
    public static void writeFileO(String content, String path, String fileName) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            //文件目录不存在  先创建
            createFile(path, fileName);
        }
        try {

            FileOutputStream ops = new FileOutputStream(file, false);
            OutputStreamWriter opsw = new OutputStreamWriter(ops, StandardCharsets.UTF_8);
            // byte[] bytes = content.getBytes();
            opsw.write(content);
            opsw.close();

            ops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param content   .
     * @param path      .
     * @param fileName  .
     * @param isReWrite .是否追加
     */
    public static void writeFileO(String content, String path, String fileName, boolean isReWrite) {
        File file = new File(path + fileName);
        if (!file.exists()) {
            //文件目录不存在  先创建
            createFile(path, fileName);
        }

        try {

            FileOutputStream ops = new FileOutputStream(file, isReWrite);
            OutputStreamWriter opsw = new OutputStreamWriter(ops, StandardCharsets.UTF_8);
            // byte[] bytes = content.getBytes();
            opsw.write(content);
            opsw.close();

            ops.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片到文件中
     * @param path
     * @param bitmap
     */
    public static void saveBitmap(String path, Bitmap bitmap) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(path));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件中读取图片
     * @param path
     * @return
     */
    public static Bitmap openBitmap(String path) {
        Bitmap bitmap = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(path));
            bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void copyfile(File fromFile, File toFile) throws Exception{

        if(!fromFile.exists()){
            return;
        }

        if(!fromFile.isFile()){
            return;
        }

        if(!fromFile.canRead()){
            return;
        }

        if(!toFile.getParentFile().exists()){
            toFile.getParentFile().mkdirs();
        }

        if(toFile.exists()){
            toFile.delete();
        }
        FileInputStream fosfrom = new FileInputStream(fromFile);
        FileOutputStream fosto = new FileOutputStream(toFile);
        byte[] bt = new byte[1024];
        int c;
        while((c=fosfrom.read(bt)) > 0){
            fosto.write(bt,0,c);
        }
        //关闭输入、输出流
        fosfrom.close();
        fosto.close();
    }

    /**
     * 删除重复项，不考虑后缀
     * @param file
     */
    public static void delete(File file) {
        if (file.getParentFile() == null) {
            return;
        }

        File[] files = file.getParentFile().listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (File _file : files) {
            if (_file.isFile() && _file.getName().startsWith(file.getName().split("\\.")[0])) {
                _file.delete();
            }
        }
    }

    /**
     * 查询内容解析器，找到文件存储地址
     * <p>ef: android中转换content://media/external/images/media/539163为/storage/emulated/0/DCIM/Camera/IMG_20160807_123123.jpg
     * <p>把content://media/external/images/media/X转换为file:///storage/sdcard0/Pictures/X.jpg
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            Log.i(TAG, "getRealPathFromUri: " + contentUri);
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null && cursor.getColumnCount() > 0) {
                cursor.moveToFirst();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(column_index);
                Log.i(TAG, "getRealPathFromUri: column_index=" + column_index + ", path=" + path);
                return path;
            } else {
                Log.w(TAG, "getRealPathFromUri: invalid cursor=" + cursor + ", contentUri=" + contentUri);
            }
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromUri failed: " + e  + ", contentUri=" + contentUri, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    /**
     * 获取完整文件名(包含扩展名)
     * @param filePath
     * @return
     */
    public static String getFilenameWithExtension(String filePath) {
        if (filePath == null || filePath.length() == 0) {
            return "";
        }
        int lastIndex = filePath.lastIndexOf(File.separator);
        String filename = filePath.substring(lastIndex + 1);
        return filename;
    }

    /**
     * 判断文件路径的文件名是否存在文件扩展名 eg: /external/images/media/2283
     * @param filePath
     * @return
     */
    public static boolean isFilePathWithExtension(String filePath) {
        String filename = getFilenameWithExtension(filePath);
        return filename.contains(".");
    }
}
