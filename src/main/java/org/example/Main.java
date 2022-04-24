package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws Exception {
        FileUtils fileUtils = new FileUtils();
        // 添加demo数据
        final InputStream resourcesFile1 = fileUtils.getResourcesFile("1.pdf");
        final InputStream resourcesFile2 = fileUtils.getResourcesFile("2.pdf");
//        ArrayList<InputStream> streamList = new ArrayList<>();
//        streamList.add(resourcesFile1);
//        streamList.add(resourcesFile2);
//        ArrayList<String> bookmarkList = new ArrayList<>();
//        bookmarkList.add("first");
//        bookmarkList.add("second");
        ArrayList<PDFFile> list = new ArrayList<>();

        PDFFile pdfFile1 = new PDFFile();
        pdfFile1.setBookmark("1.pdf");
        pdfFile1.setBase64(inputStream2Base64(resourcesFile1));

        PDFFile pdfFile2 = new PDFFile();
        pdfFile2.setBookmark("2.pdf");
        pdfFile2.setBase64(inputStream2Base64(resourcesFile2));

        list.add(pdfFile1);
        list.add(pdfFile2);

        // 调用工具类
        final String base64 = PDFUtils.mergePDF(list);


//        System.out.println(base64);
//        // 文件流转document
//        final PDDocument d = PDDocument.load(Base64.getDecoder().decode(base64));
//        // 获取pdf文件的页数
//        final int numberOfPages = d.getNumberOfPages();
//        System.out.println(numberOfPages);
    }

    private static String inputStream2Base64(InputStream is) throws Exception {
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = is.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            data = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new Exception("输入流关闭异常");
                }
            }
        }

        return Base64.getEncoder().encodeToString(data);
    }



}