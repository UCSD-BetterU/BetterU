package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */

public enum FBPermission {

    USER_FRIENDS("user_friends", true),
    PUBLISH_ACTIONS("publish_actions", false);

    private String permisison;
    private boolean isRead;

    FBPermission (String permission, boolean isRead) {
        this.permisison = permission;
        this.isRead = isRead;
    }

    /**
     * Checks if the permission is a read permission or write permission. In general read permissions
     * Allow apps to read information about the user who grants them, write permissions allow apps
     * to change user information on their behalf, for example publish_actions permission allows
     * app to post on behalf of the user. It's needed because in some scenarios SDK provides
     * different methods for working with read and write permissions.
     */
    public boolean isRead() {return isRead;}

    /**
     * String representation of the permission, as defined in
     * https://developers.facebook.com/docs/facebook-login/permissions/
     */
    @Override
    public String toString() {
        return permisison;
    }
}

