package com.betteru.ucsd.myapplication4;

/**
 * Created by verazou on 11/12/17.
 */


import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is handling all GraphAPI calls made in Friend Smash!
 * Simply put it wraps Facebook SDK's GraphRequest class providing convenience methods to make calls
 * and handle results through GraphApiCallback interface.
 * See https://developers.facebook.com/docs/graph-api for overview of Graph API and
 * https://developers.facebook.com/docs/android/graph for Android specific details.
 */
public class FBGraphAPICall {

    /**
     * When making Graph API calls different parameters can be specified. fields is a common
     * parameter used to specify which of the fields should be included in the response, as
     * by default, not all the fields in a node or edge are returned when you make a query.
     * This is really useful for making your API calls more efficient and fast.
     */
    private static final String PARAM_FIELDS = "fields";

    /**
     * Facebook SDK's class for handling Graph API calls
     * See https://developers.facebook.com/docs/reference/android/current/class/GraphRequest/
     */
    private GraphRequest graphRequest;

    /**
     * Facebook SDK's class encapsulating the response of a Graph API call
     * See https://developers.facebook.com/docs/reference/android/current/class/GraphResponse/
     */
    private GraphResponse graphResponse;

    /**
     * specific interface making it easier to handle Graph API responses
     */
    private FBGraphAPICallback graphAPICallback;

    /**
     * Private constructor to enforce usage of static convenience methods creatic a specific call
     */
    private FBGraphAPICall(String path, FBGraphAPICallback callback) {
        setUp(callback);
        createPathRequest(path);
    }

    /**
     * Same as above
     */
    private FBGraphAPICall(FBGraphAPICallback callback) {
        setUp(callback);
    }

    /**
     * Executes the call asynchronously
     */
    public void executeAsync() {
        graphRequest.executeAsync();
    }

    /**
     * Adds a parameter to the request. Parameters can be used for many different purposes, such
     * as specifying fields to be included in the response, specifying number of results to be
     * included in the response and many others.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/
     */
    public void addParam (String param, String value) {
        Bundle params = graphRequest.getParameters();
        params.putString(param, value);
        graphRequest.setParameters(params);
    }

    /**
     * Registers callback and checks if valid access token is available, as it is
     * required to make a Graph API call.
     */
    private void setUp (FBGraphAPICallback callback) {
        graphAPICallback = callback;
        if (!FacebookLogin.isAccessTokenValid()) {
            Log.e("GraphAPI", "Cannot call Graph API without a valid AccessToken");
        }
    }

    /**
     * Creates a GraphRequest with the specified path
     */
    private void createPathRequest (final String path) {
        AccessToken token = AccessToken.getCurrentAccessToken();
        graphRequest = GraphRequest.newGraphPathRequest(token,
                path, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        handleResponse(response);
                    }
                });
    }

    /**
     * Creates a GraphRequest for /me Graph API call
     */
    private void createMeRequest () {
        AccessToken token = AccessToken.getCurrentAccessToken();
        graphRequest = GraphRequest.newMeRequest(token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        handleResponse(graphResponse);
                    }
                });
    }

    /**
     * Creates a GraphRequest for /me/friends Graph API call
     */
    private void createMyFriendsRequest () {
        AccessToken token = AccessToken.getCurrentAccessToken();
        graphRequest = GraphRequest.newMyFriendsRequest(token,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray users, GraphResponse response) {
                        handleResponse(response);
                    }
                });
    }

    /**
     * Creates a GraphRequest for a Graph API call to delete an object with given objectId.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/#deleting to understand
     * how deleting Graph API objects works.
     */
    private void createDeleteObjectRequest(String objectId) {
        AccessToken token = AccessToken.getCurrentAccessToken();
        graphRequest = GraphRequest.newDeleteObjectRequest(token, objectId, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                handleResponse(response);
            }
        });
    }

    /**
     * Handles GraphResponse. Checks if there's next page available and makes another request
     * if necessary. By default Graph API results are paged and return up to a specific number
     * of objects. This number is called page size. For example if there's 40 objects to be returned
     * and the page size is 25 the first call will return 25 objects. Calling next page will return
     * the remaining 15 objects.
     * You can specify page size using 'limit' parameter when making call. If page size is not
     * specified it defaults to 25.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/#paging for more details
     * on paging.
     */
    private void handleResponse (GraphResponse response) {
        FacebookRequestError error = response.getError();
        if (error != null) {
            Log.e("GraphAPI", error.toString());
            graphAPICallback.handleError(error);
        } else if (response != null) {
            addDataToResponse(response);
            if (hasNextPage(response)) {
                callNextPage(response);
            } else {
                graphAPICallback.handleResponse(graphResponse);
            }
        }
    }

    /**
     * Checks if GraphResponse has the next page.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/#paging for more details
     * on paging.
     */
    private boolean hasNextPage(GraphResponse response) {
        return response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT) != null;
    }

    /**
     * Calls the next page for a given GraphResponse.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/#paging for more details
     * on paging.
     */
    private void callNextPage (GraphResponse response) {
        graphRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
        if (graphRequest != null) {
            graphRequest.setCallback(new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse graphResponse) {
                    handleResponse(graphResponse);
                }
            });
            graphRequest.executeAsync();
        }
    }

    /**
     * Adds data from specified GraphResponse to stored graphResponse variable. This is used to
     * combine the data from multiple pages into single GraphResponse object.
     * See https://developers.facebook.com/docs/graph-api/using-graph-api/#paging for more details
     * on paging.
     */
    private void addDataToResponse(GraphResponse response) {
        if (graphResponse == null) {
            graphResponse = response;
        } else {
            JSONArray newData = response.getJSONObject().optJSONArray("data");
            JSONArray existingData = graphResponse.getJSONObject().optJSONArray("data");
            for (int i = 0; i < newData.length(); i++) {
                existingData.put(newData.opt(i));
            }
        }
    }

    /**
     * Creates GraphAPICall wrapper for GET /me call with specified fields.
     * Response is passed to the specified GraphAPICallback.
     * /me call returns the information about the current user
     */
    public static FBGraphAPICall callMe (String fields, FBGraphAPICallback callback) {
        FBGraphAPICall call = new FBGraphAPICall(callback);
        call.createMeRequest();
        call.addParam(PARAM_FIELDS, fields);
        return call;
    }

    /**
     * Creates GraphAPICall wrapper for GET /{user-Id} call with specified fields.
     * Response is passed to the specified GraphAPICallback.
     * /{user-id} returns the information about specific user
     */
    public static FBGraphAPICall callUser (String userId, String fields, FBGraphAPICallback callback) {
        FBGraphAPICall call = new FBGraphAPICall(userId, callback);
        call.addParam(PARAM_FIELDS, fields);
        return call;
    }

    /**
     * Creates GraphAPICall wrapper for GET /me/friends call with specified fields.
     * Response is passed to the specified GraphAPICallback.
     * Note user_friends permission is required to make this call
     * /me/friends returns the information about current user's friends who are also playing the game
     */
    public static FBGraphAPICall callMeFriends(String fields, FBGraphAPICallback callback) {
        if (FacebookLogin.isPermissionGranted(FBPermission.USER_FRIENDS)) {
            FBGraphAPICall call = new FBGraphAPICall(callback);
            call.createMyFriendsRequest();
            call.addParam(PARAM_FIELDS, fields);
            return call;
        } else {
            Log.w("GraphAPI", "Cannot call me/friends without user_friends permission");
            return null;
        }
    }


    /**
     * Creates GraphAPICall wrapper for GET /{request-id} call.
     * Response is passed to the specified GraphAPICallback.
     * /{request-id} returns the information about specific request
     */
    public static FBGraphAPICall callRequest(String requestId, FBGraphAPICallback callback) {
        FBGraphAPICall call = new FBGraphAPICall(requestId, callback);
        return call;
    }

    /**
     * Creates GraphAPICall wrapper for DELETE /{request-Id} call with specified fields.
     * Response is passed to the specified GraphAPICallback.
     * DELETE /{request-id} deletes specified request.
     */
    public static FBGraphAPICall deleteRequest(String requestId, FBGraphAPICallback callback) {
        FBGraphAPICall call = new FBGraphAPICall(callback);
        call.createDeleteObjectRequest(requestId);
        return call;
    }

    /**
     * Helper method to create a batch of multiple GraphAPICall objects.
     * Batching allows to pass instructions for several operations in a single HTTP request.
     * For more information on batching see
     * https://developers.facebook.com/docs/graph-api/making-multiple-requests and
     * https://developers.facebook.com/docs/reference/android/current/class/GraphRequestBatch/
     * for Android implementation.
     */
    public static GraphRequestBatch createRequestBatch (FBGraphAPICall... requests) {
        GraphRequestBatch batch = new GraphRequestBatch();
        for (FBGraphAPICall request : requests) {
            if (request != null) {
                batch.add(request.graphRequest);
            }
        }
        return batch;
    }

    /**
     * Helper method to extract JSONArray with data returned in GraphResponse.
     * Useful for example for extracting friends data returned in GraphResponse for /me/friends call
     */
    public static JSONArray getDataFromResponse(GraphResponse response) {
        JSONObject graphObject = response.getJSONObject();
        return graphObject.optJSONArray("data");
    }
}
