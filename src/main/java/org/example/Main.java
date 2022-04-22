package org.example;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PageMode;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageFitWidthDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        // pdfbox工具类
        PDFMergerUtility mergerUtility = new PDFMergerUtility();

        FileUtils fileUtils = new FileUtils();

        // 创建空pdf文件
        PDDocument document = new PDDocument();
        PDPage blankPage = new PDPage();
        document.addPage(blankPage);
        document.save("result.pdf");

        // 获取pdf文件流
        final InputStream resourcesFile1 = fileUtils.getResourcesFile("1.pdf");
        final PDDocument document1 = PDDocument.load(resourcesFile1);
        final int p1 = document1.getNumberOfPages();
        final InputStream resourcesFile2 = fileUtils.getResourcesFile("2.pdf");
        final PDDocument document2 = PDDocument.load(resourcesFile2);
        final int p2 = document2.getNumberOfPages();

        // 按顺序添加需要合并的pdf文件
        mergerUtility.appendDocument(document, document1);
        mergerUtility.appendDocument(document, document2);

        mergerUtility.setDestinationFileName("result.pdf");

        // 合并pdf文件
        mergerUtility.mergeDocuments(null);

        resourcesFile1.close();
        resourcesFile2.close();
        document1.close();
        document2.close();


        ArrayList<Integer> pageList = new ArrayList<>();
        pageList.add(p1);
        pageList.add(p2);

        createBookMark(document, pageList);

    }

    private static void createBookMark(PDDocument document, ArrayList<Integer> pageList) throws IOException {
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
            bookmark.setTitle("PDF " + (i + 1));
            pagesOutline.addLast(bookmark);
            currentPage += pageList.get(i);
        }
        pagesOutline.openNode();
        outline.openNode();
        document.getDocumentCatalog().setPageMode(PageMode.USE_OUTLINES);
        document.save(new File("merged-pdf-with-bookmark.pdf"));

        document.close();
    }
}