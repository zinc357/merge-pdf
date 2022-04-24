package org.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.UUID;

public class PDFUtils {

    /**
     * 合并pdf
     *
     * @param pdfList      pdf文件流
     * @param bookmarkList pdf文件对应的书签列表 pdfList与bookmarkList长度需一致
     * @return
     */
    public static ByteArrayOutputStream mergePDF(ArrayList<InputStream> pdfList, ArrayList<String> bookmarkList) {
        if (pdfList.size() != bookmarkList.size()) {
            throw new RuntimeException("pdf文件对应的书签列表 pdfList与bookmarkList长度不一致!!!");
        }

        // pdfbox工具类
        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        // 创建空pdf文件
        try (PDDocument document = new PDDocument()) {

            ArrayList<PDDocument> docsList = new ArrayList<>();
            ArrayList<Integer> pagesList = new ArrayList<>();

            for (InputStream pdfStream : pdfList) {
                // 文件流转document
                final PDDocument d = PDDocument.load(pdfStream);
                // 获取pdf文件的页数
                final int numberOfPages = d.getNumberOfPages();

                docsList.add(d);
                pagesList.add(numberOfPages);
            }

            for (PDDocument pdDocument : docsList) {
                mergerUtility.appendDocument(document, pdDocument);
            }

            // 合并pdf
            mergerUtility.mergeDocuments(null);

            createBookMark(document, pagesList, bookmarkList);

            // 保存文件流
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            document.save(output);

            // 关闭document列表
            for (PDDocument pdDocument : docsList) {
                pdDocument.close();
            }

            return output;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createBookMark(PDDocument document, ArrayList<Integer> pageList, ArrayList<String> bookmarkList) throws IOException {
        // 添加书签
        PDDocumentOutline outline = new PDDocumentOutline();
        document.getDocumentCatalog().setDocumentOutline(outline);

        // 添加一级书签
        PDOutlineItem pagesOutline = new PDOutlineItem();
        pagesOutline.setTitle("目录");
        pagesOutline.setDestination(document.getPage(0));
        outline.addLast(pagesOutline);
        int currentPage = 0;

        // 遍历每个pdf添加二级书签
        for (int i = 0; i < pageList.size(); i++) {
            PDPageDestination dest = new PDPageFitWidthDestination();
            // 获取需要添加书签的页面
            final PDPage page = document.getPage(currentPage);
            dest.setPage(page);
            PDOutlineItem bookmark = new PDOutlineItem();
            bookmark.setDestination(dest);
            // 添加书签
            bookmark.setTitle(bookmarkList.get(i));
            pagesOutline.addLast(bookmark);
            currentPage += pageList.get(i);
        }
        pagesOutline.openNode();
        outline.openNode();
        document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);
    }
}
