package com.rideshare.auth.webentity;


import java.util.List;

public class UpdateRoleRequest {
    private List<String> roles;

    public UpdateRoleRequest() {}

    public UpdateRoleRequest(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
