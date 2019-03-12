package yx.pay.common.domain;

import java.util.HashMap;

public class FebsResponse extends HashMap<String, Object> {
    private static final int FAIL_CODE = -1;
    private static final int SUCCESS_CODE = 0;

    public FebsResponse success() {
        this.put("code", SUCCESS_CODE);
        this.put("msg", "success");
        return this;
    }

    public FebsResponse fail(String msg) {
        this.put("code", FAIL_CODE);
        this.put("msg", msg);
        return this;
    }

    private static final long serialVersionUID = -8713837118340960775L;

    public FebsResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public FebsResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public FebsResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
