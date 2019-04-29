package com.miu360.legworkwrit.util.printer;

import com.epson.isv.eprinterdriver.Common.EpsCapability;
import com.epson.isv.eprinterdriver.Ctrl.EPSetting;
import com.epson.isv.eprinterdriver.Ctrl.PageAttribute;

/**
 * 作者：wanglei on 2018/10/20.
 * 邮箱：forwlwork@gmail.com
 */
public class PrinterSetting {
    /**
     * 是否加粗
     */
    private boolean isBorderless;
    /**
     * 纸张大小
     * <p>
     * {@link android.print.PrintAttributes.MediaSize}
     */
    private int paperSize;
    /**
     * 纸张类型
     * <p>
     * {@link com.epson.isv.eprinterdriver.Ctrl.PageAttribute.MediaTypeID}
     */
    private int paperType;
    /**
     * 打印清晰度
     * <p>
     * {@link com.epson.isv.eprinterdriver.Ctrl.PageAttribute.PrintQuality}
     */
    private int printQuality;
    /**
     * 打印颜色
     * <p>
     * {@link com.epson.isv.eprinterdriver.Ctrl.EPSetting}
     */
    private int colorMode;
    /**
     * 纸路径
     * <p>
     * {@link com.epson.isv.eprinterdriver.Ctrl.EPSetting}
     */
    private int paperPath;
    /**
     * 是否双面打印
     */
    private boolean isDuplexPrint;
    /**
     * {@link EPSetting}
     */
    private int bidPrint;
    /**
     * 打印总张数
     */
    private int totalPages;

    public int getPaperSize() {
        return paperSize;
    }

    public void setPaperSize(int paperSize) {
        this.paperSize = paperSize;
    }

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }

    public int getPrintQuality() {
        return printQuality;
    }

    public void setPrintQuality(int printQuality) {
        this.printQuality = printQuality;
    }

    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }

    public int getPaperPath() {
        return paperPath;
    }

    public void setPaperPath(int paperPath) {
        this.paperPath = paperPath;
    }

    public boolean isDuplexPrint() {
        return isDuplexPrint;
    }

    public void setDuplexPrint(boolean duplexPrint) {
        isDuplexPrint = duplexPrint;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getBidPrint() {
        return bidPrint;
    }

    public void setBidPrint(int bidPrint) {
        this.bidPrint = bidPrint;
    }

    public void configDefaultSetting(int pages) {
        this.isBorderless = false;
        this.paperSize = PageAttribute.MediaSizeID.EPS_MSID_A4;
        this.paperType = PageAttribute.MediaTypeID.EPS_MTID_PLAIN;
        this.printQuality = PageAttribute.PrintQuality.EPS_MQID_NORMAL;
        this.colorMode = EPSetting.COLOR_MODE_MONOCHROME;
        this.paperPath = EPSetting.PAPER_SOURCE_NOT_SPEC;
        this.isDuplexPrint = false;
        this.bidPrint = EPSetting.PRINT_DIR_BI;
        this.totalPages = pages;

        EPSetting setting = EPSetting.instance();

        EpsCapability myCapability = new EpsCapability();
        PageAttribute attribute = new PageAttribute(paperSize, paperType, printQuality);
        setting.setSelPageAttri(attribute);
        setting.setPrintDirection(bidPrint);
        setting.setColorMode(colorMode);
        setting.setPaperSource(paperPath);
        setting.setBorderless(isBorderless);
        setting.setDuplexPrint(isDuplexPrint);
        setting.setTotalPages(totalPages);
        setting.setTemporaryImageFilePath("file:///sdcard/writ/SceneRecord1.html");
        myCapability.setResolution(4);
    }
}
