package org.example;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        FileUtils fileUtils = new FileUtils();
        // 添加demo数据
        final InputStream resourcesFile1 = fileUtils.getResourcesFile("1.pdf");
        final InputStream resourcesFile2 = fileUtils.getResourcesFile("2.pdf");
        ArrayList<InputStream> streamList = new ArrayList<>();
        streamList.add(resourcesFile1);
        streamList.add(resourcesFile2);
        ArrayList<String> bookmarkList = new ArrayList<>();
        bookmarkList.add("first");
        bookmarkList.add("second");

        // 调用工具类
        final ByteArrayOutputStream byteArrayOutputStream = PDFUtils.mergePDF(streamList, bookmarkList);

        // 测试输出
//        FileOutputStream outputStream = new FileOutputStream("result.pdf");
//        outputStream.write(byteArrayOutputStream.toByteArray());
//        outputStream.close();
    }


}