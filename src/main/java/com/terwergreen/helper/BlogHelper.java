package com.terwergreen.helper;

import com.terwergreen.model.Post;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: terwer
 * @date: 2022/1/9 18:51
 * @description: BlogHelper
 */
public abstract class BlogHelper {

    private static Logger logger = LoggerFactory.getLogger(BlogHelper.class);

    private String serverUrl;
    private String username;
    private String password;

    protected String getServerUrl() {
        return serverUrl;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;/**/
    }

    protected BlogHelper(String serverUrl, String username, String password) {
        this.serverUrl = serverUrl;
        this.username = username;
        this.password = password;
    }

    // ====================
    // 通用api开始
    // ====================
    protected Object executeMeteweblog(String pMethodName, List pParams) {
        Object result = null;
        try {
            // Create an object to represent our server.
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
            URL url = new URL(serverUrl);
            clientConfig.setServerURL(url);
            client.setConfig(clientConfig);

            // Call the server, and get our result.
            result = client.execute(pMethodName, pParams);
        } catch (XmlRpcException exception) {
            logger.error("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
        } catch (Exception exception) {
            logger.error("JavaClient: " + exception.toString());
        }
        return result;
    }

    protected void executeMeteweblogAsync(String pMethodName, List pParams, AsyncCallback pCallback) {
        Object result = null;
        try {
            // Create an object to represent our server.
            XmlRpcClient client = new XmlRpcClient();
            XmlRpcClientConfigImpl clientConfig = new XmlRpcClientConfigImpl();
            URL url = new URL(serverUrl);
            clientConfig.setServerURL(url);
            client.setConfig(clientConfig);

            // Call the server, and get our result.
            client.executeAsync(pMethodName, pParams, pCallback);
        } catch (XmlRpcException exception) {
            logger.error("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
        } catch (Exception exception) {
            logger.error("JavaClient: " + exception.toString());
        }
    }
    // ====================
    // 通用api结束
    // ====================

    // ====================
    // bloggerApi开始
    // ====================
    public Map<String, Object> getUsersBlogs() {
        List<String> pParams = new ArrayList<>();
        pParams.add("default");
        pParams.add(this.username);
        pParams.add(this.password);

        Object[] result = (Object[]) this.executeMeteweblog("blogger.getUsersBlogs", pParams);

        HashMap<String, Object> userBlog = new HashMap<>();
        if (result != null && result.length > 0) {
            userBlog = (HashMap<String, Object>) result[0];
        }

        logger.debug("blogger.getUsersBlogs=>");
        return userBlog;
    }
    // ====================
    // bloggerApi结束
    // ====================

    // ====================
    // metaWeblogApi开始
    // ====================
    public abstract boolean newPost(Map<String, Object> mappedParams);

    public abstract boolean editPost(Map<String, Object> mappedParams);

    public abstract <T> T getPost(Map<String, Object> mappedParams);

    public abstract <T> List<T> getRecentPosts(Map<String, Object> mappedParams);

    public abstract <T> List<T> getCategories(Map<String, Object> mappedParams);

    public abstract boolean newMediaObject(Map<String, Object> mappedParams);
    // ====================
    // metaWeblogApi结束
    // ====================
}
