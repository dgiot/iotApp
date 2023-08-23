package android.tx.com.dgiot_amis.bean;

/**
 * @author jie
 * @date 2022/8/10
 * @time 10:30
 */
public class ReceiveMsgBean {

    private String instruct = "";
    private String deviceid  = "";
    private String token = "";

    private String url = "";

    private String path = "";

    private String other = "";

    public String getInstruct() {
        return instruct;
    }

    public void setInstruct(String instruct) {
        this.instruct = instruct;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
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
