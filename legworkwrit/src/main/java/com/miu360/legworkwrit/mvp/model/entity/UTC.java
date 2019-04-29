package com.miu360.legworkwrit.mvp.model.entity;

/**
 * Created by Murphy on 2018/10/23.
 */
public class UTC {

    /**
     * name : T_BJSJTZFZD_ZPDZ
     * startUTC : 2018-10-22 19:16:00
     * endUTC : 2018-10-22 19:25:00
     * "flag":"0",
     * "printTimes":"0"
     */

    public UTC() {

    }

    public UTC(String name, String startUTC, String endUTC) {
        this.name = name;
        this.startUTC = startUTC;
        this.endUTC = endUTC;
        this.flag = "1";
    }

    private String name;
    private String startUTC;
    private String endUTC;
    private String flag;
    private String printTimes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartUTC() {
        return startUTC;
    }

    public void setStartUTC(String startUTC) {
        this.startUTC = startUTC;
    }

    public String getEndUTC() {
        return endUTC;
    }

    public void setEndUTC(String endUTC) {
        this.endUTC = endUTC;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPrintTimes() {
        return printTimes;
    }

    public void setPrintTimes(String printTimes) {
        this.printTimes = printTimes;
    }

    @Override
    public String toString() {
        return "UTC{" +
                "name='" + name + '\'' +
                ", startUTC='" + startUTC + '\'' +
                ", endUTC='" + endUTC + '\'' +
                ", flag='" + flag + '\'' +
                ", printTimes='" + printTimes + '\'' +
                '}';
    }
}
