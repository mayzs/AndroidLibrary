package com.example.app.bean;

/**
 * @createDate: 2020/4/19
 * @author: mayz
 * @version: 1.0
 */
public class TimeVo {

    /**
     * code : 200
     * data : {"timestamp":1587283723433}
     */

    private int code;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * timestamp : 1587283723433
         */

        private long timestamp;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
