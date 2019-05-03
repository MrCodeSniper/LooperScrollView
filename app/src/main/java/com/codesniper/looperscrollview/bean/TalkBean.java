package com.codesniper.looperscrollview.bean;

import java.util.ArrayList;
import java.util.List;

public class TalkBean {


    private List<AccountDesListBean> accountDesList;

    public List<AccountDesListBean> getAccountDesList() {
        return accountDesList;
    }

    public void setAccountDesList(List<AccountDesListBean> accountDesList) {
        this.accountDesList = accountDesList;
    }


    public List<String> getUrls(){
        List<String> list=new ArrayList<>();
        for (AccountDesListBean item :getAccountDesList()) {
            list.add(item.getAvatarUrl());
        }
        return list;
    }


    public static class AccountDesListBean {
        @Override
        public String toString() {
            return "AccountDesListBean{" +
                    "accountId='" + accountId + '\'' +
                    ", address='" + address + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    ", balance='" + balance + '\'' +
                    ", content='" + content + '\'' +
                    ", job='" + job + '\'' +
                    ", nickName='" + nickName + '\'' +
                    '}';
        }

        /**
         * accountId :
         * address :
         * avatarUrl :
         * balance :
         * content :
         * job :
         * nickName :
         */


        private String accountId;
        private String address;
        private String avatarUrl;
        private String balance;
        private String content;
        private String job;
        private String nickName;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    @Override
    public String toString() {
        return "TalkBean{" +
                "accountDesList=" + accountDesList +
                '}';
    }
}
