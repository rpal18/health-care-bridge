package com.Lifelink.HeathCareBridge.payload;

import java.util.List;
import java.util.UUID;

public class UserInfoResponse {
    private UUID Id;
    private String jwtToken;
    private String userName;
    private List<String> roles;

    public UserInfoResponse( UUID userId , String jwtToken, String userName, List<String> roles) {
        this.jwtToken = jwtToken;
        this.userName = userName;
        this.roles = roles;
        this.Id = userId;
    }

    public UserInfoResponse(UUID id, String username, List<String> roles) {
        this.userName = username;
        this.roles = roles;
        this.Id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
