package android.tx.com.dgiot_amis.bean;

/**
 * @author jie
 * @date 2022/8/5
 * @time 15:04
 */
public class UpImgBackBean {


    /**
     * url : http://121.5.171.21/dgiot_file/uni_app/png/1659682896723.png
     * md5 : 02e43118011da1c0347fbfb638192b96
     * path : /dgiot_file/uni_app/png/1659682896723.png
     * domain : http://121.5.171.21
     * scene : app
     * size : 1090206
     * mtime : 1659682898
     * scenes : app
     * retmsg :
     * retcode : 0
     * src : /dgiot_file/uni_app/png/1659682896723.png
     */

    private String url = "";
    private String md5;
    private String path;
    private String domain;
    private String scene;
    private int size;
    private int mtime;
    private String scenes;
    private String retmsg;
    private int retcode;
    private String src;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getMtime() {
        return mtime;
    }

    public void setMtime(int mtime) {
        this.mtime = mtime;
    }

    public String getScenes() {
        return scenes;
    }

    public void setScenes(String scenes) {
        this.scenes = scenes;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
