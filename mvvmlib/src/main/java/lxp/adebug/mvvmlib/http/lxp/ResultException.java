package lxp.adebug.mvvmlib.http.lxp;

/**
 * Created by lxp on 2016/4/19.
 */

public class ResultException extends RuntimeException {
    private int errCode = 400;

    public ResultException(int errCode, String msg) {
        super(msg);
        this.errCode = errCode;
    }

    public int getErrCode() {
        return errCode;
    }
}
