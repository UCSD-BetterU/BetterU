package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */

import com.facebook.FacebookRequestError;
import com.facebook.GraphResponse;

import org.json.JSONException;

/**
 * Callback for Graph API calls made using GraphAPICall object to easily handle
 * successful responses and errors.
 */
public interface FBGraphAPICallback {
    /**
     * Called when GraphAPICall returned successfully.
     */
    void handleResponse (GraphResponse response) throws JSONException;

    /**
     * Called when GraphAPICall returned with an error.
     */
    void handleError (FacebookRequestError error);
}
