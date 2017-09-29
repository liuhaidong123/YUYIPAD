package com.technology.yuyipad.fragment.MyFrag;

/**
 * Created by wanyu on 2017/9/27.
 */
//用户实体类
public class UserBean {
    /**
     * result : {"createTimeString":"2017-03-20 16:40:56","updateTimeString":"","gender":1,"idCard":"123333333366666666","origin":"","avatar":"/static/image/2017512/1e6349018fe74e1eaad9666fd00d6d06.jpg","userName":"","token":"","trueName":"刘海东","marital":null,"id":18335277251,"age":25}
     * code : 0
     * message : msg
     */

    private ResultBean result;
    private String code;
    private String message;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class ResultBean {
        /**
         * createTimeString : 2017-03-20 16:40:56
         * updateTimeString :
         * gender : 1
         * idCard : 123333333366666666
         * origin :
         * avatar : /static/image/2017512/1e6349018fe74e1eaad9666fd00d6d06.jpg
         * userName :
         * token :
         * trueName : 刘海东
         * marital : null
         * id : 18335277251
         * age : 25
         */

        private String createTimeString;
        private String updateTimeString;
        private int gender;
        private String idCard;
        private String origin;
        private String avatar;
        private String userName;
        private String token;
        private String trueName;
        private Object marital;
        private long id;
        private int age;

        public String getCreateTimeString() {
            return createTimeString;
        }

        public void setCreateTimeString(String createTimeString) {
            this.createTimeString = createTimeString;
        }

        public String getUpdateTimeString() {
            return updateTimeString;
        }

        public void setUpdateTimeString(String updateTimeString) {
            this.updateTimeString = updateTimeString;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getTrueName() {
            return trueName;
        }

        public void setTrueName(String trueName) {
            this.trueName = trueName;
        }

        public Object getMarital() {
            return marital;
        }

        public void setMarital(Object marital) {
            this.marital = marital;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
