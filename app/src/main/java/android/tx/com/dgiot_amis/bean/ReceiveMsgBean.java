package android.tx.com.dgiot_amis.bean;

/**
 * @author jie
 * @date 2022/8/10
 * @time 10:30
 */
public class ReceiveMsgBean {

    private String instruct = "";
    private String deviceid  = "";
    private String other = "";

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}
