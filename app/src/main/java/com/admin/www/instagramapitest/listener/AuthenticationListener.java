package com.admin.www.instagramapitest.listener;

public interface AuthenticationListener {

    void onCodeReceived(String auth_token);

}