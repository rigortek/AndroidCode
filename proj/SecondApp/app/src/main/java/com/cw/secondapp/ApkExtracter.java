package com.cw.secondapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.InvalidParameterException;

/*
    root@vbox86p:/data # ll
    drwxrwx--x system   system            2021-05-22 23:32 app
    /data/app目录的权限是rwxrwx - x, 对目录的缺少读取权限意味着您无法获取其内容的列表, 但是有执行权限，意味着您可以访问它，
    由于可以访问/data/app目录，只要其下方目录及文档有read权限，即可实现读取apk，而/data/app下目录权限是
    drwxr-xr-x
    所以可以继续入往下访问，并且base.apk权限是
    -rw-r--r-- system   system    4301039 2021-05-20 11:39 base.apk
    因而可以读取任意*.apk文件。
 */

public class ApkExtracter {

    /**
     * @param context Context of app
     * @param dstDir destination of apk
     * @param packageName source of app package name
     */
    public static void copyApk(Context context, String dstDir, String packageName) {
        String dest = (dstDir.endsWith("/")) ? (dstDir + packageName + ".apk") : (dstDir + "/" + packageName + ".apk");
        new Thread(new CopyAPKRunnable(context, getApkPath(context, packageName), dest, packageName)).start();
    }

    private static String getApkPath(Context context, String packageName) {
        String appDirPath = null;
        try {
            // 通过包名获取程序apk源文件路径
            appDirPath = context.getPackageManager().getApplicationInfo(packageName, 0).sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appDirPath;
    }

    /**
     * 将程序源文件Copy到指定目录
     */
    private static class CopyAPKRunnable implements Runnable {
        private final Context context;
        private final String source;
        private final String dest;

        public CopyAPKRunnable(Context context, String source, String dest, String key) {
            if (null == context) {
                throw new NullPointerException("invalid Context parameter");
            }

            if (TextUtils.isEmpty(source)) {
                throw new InvalidParameterException("invalid source parameter");
            }

            if (TextUtils.isEmpty(dest)) {
                throw new InvalidParameterException("invalid dest parameter");
            }
            
            this.context = context;
            this.source = source;
            this.dest = dest;
        }

        @Override
        public void run() {
            do {
                FileInputStream in = null;
                FileOutputStream out = null;

                try {
                    int length = 1024 * 1024;
                    String path = dest.substring(0, dest.lastIndexOf('/'));
                    if (!new File(path).exists()) {
                        boolean mk = new File(path).mkdirs();
                        if (!mk) {
                            Log.e("TAG", "run: mkdir fail " + path);
                            break;
                        }
                    }

                    File fDest = new File(dest);
                    if (fDest.exists()) {
                        fDest.delete();
                    }

                    fDest.createNewFile();
                    in = new FileInputStream(new File(source));
                    out = new FileOutputStream(fDest);
                    FileChannel inC = in.getChannel();
                    FileChannel outC = out.getChannel();

                    while (true) {
                        if (inC.position() == inC.size()) {
                            // success done
                            break;
                        }
                        if ((inC.size() - inC.position()) < 1024 * 1024) {
                            length = (int) (inC.size() - inC.position());
                        } else {
                            length = 1024 * 1024;
                        }
                        inC.transferTo(inC.position(), length, outC);
                        inC.position(inC.position() + length);
                    }
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                } finally {
                    try {
                        if (null != in) {
                            in.close();
                        }

                        if (null != out) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } while (false);
        }
    }
}
