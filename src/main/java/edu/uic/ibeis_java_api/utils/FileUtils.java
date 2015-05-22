package edu.uic.ibeis_java_api.utils;

import java.io.*;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static File zipFile(File fileToZip, File zipFilePath) throws IOException{
        byte[] buffer = new byte[2048];

        FileOutputStream dest = new FileOutputStream(zipFilePath);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        FileInputStream in = new FileInputStream(fileToZip);

        ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
        out.putNextEntry(zipEntry);

        int len;
        while ((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        in.close();
        out.closeEntry();
        out.close();
        return zipFilePath;
    }

    public static File zipFiles(Collection<File> filesToZip, File zipFilePath) throws IOException{
        byte[] buffer = new byte[2048];

        FileOutputStream dest = new FileOutputStream(zipFilePath);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

        for(File file : filesToZip) {
            FileInputStream in = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            out.putNextEntry(zipEntry);
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.closeEntry();
        }
        out.close();
        return zipFilePath;
    }
}
