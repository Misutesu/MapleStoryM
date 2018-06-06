package com.misutesu.maplestorym.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/30 0030 15:59
 * @description :
 */
public class FileUtils {
    public static void copyFile(File oldFile, File newFile) throws IOException {
        FileInputStream input = new FileInputStream(oldFile);
        FileOutputStream output = new FileOutputStream(newFile);
        byte[] bt = new byte[1024];
        int readByte;
        while ((readByte = input.read(bt)) > 0) {
            output.write(bt, 0, readByte);
        }

        input.close();
        output.close();
    }
}
