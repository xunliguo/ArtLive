package com.example.rh.artlive.live;


import android.content.Context;

import com.example.rh.artlive.application.DemoApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CommonUtil {

    public static int dip2px(float dpValue) {
        float scale = DemoApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    //将旧目录中的文件全部复制到新目录
    public static void copyFilesFassets(Context context, String oldPath, String newPath) {
        try {

            // 获取assets目录下的所有文件及目录名
            String fileNames[] = context.getAssets().list(oldPath);

            // 如果是目录名，则将重复调用方法递归地将所有文件
            if (fileNames.length > 0) {
                File file = new File(newPath);
                file.mkdirs();
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            }
            // 如果是文件，则循环从输入流读取字节写入
            else {
                InputStream is = context.getAssets().open(oldPath);
                FileOutputStream fos = new FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, byteCount);
                }
                fos.flush();
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
