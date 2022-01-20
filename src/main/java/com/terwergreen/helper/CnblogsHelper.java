package com.terwergreen.helper;

import com.terwergreen.model.Post;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author: terwer
 * @date: 2022/1/8 23:59
 * @description: CnblogsHelper
 */
public class CnblogsHelper extends BlogHelper {
    @Override
    public boolean addPost(Post post) {
        System.out.println("CnBlogs add Post");
        return false;
    }

    @Override
    public boolean updatePost(Post post) {
        System.out.println("CnBlogs update Post");
        return false;
    }


    // The location of our server.
    private final static String server_url =
            "http://xmlrpc-c.sourceforge.net/api/sample.php";

    public static void main(String[] args) {
        try {

            // Create an object to represent our server.
            XmlRpcClient server = new XmlRpcClient();

            // Build our parameter list.
            Vector params = new Vector();
            params.addElement(new Integer(5));
            params.addElement(new Integer(3));

            // Call the server, and get our result.
            Hashtable result =
                    (Hashtable) server.execute("sample.sumAndDifference", params);
            int sum = ((Integer) result.get("sum")).intValue();
            int difference = ((Integer) result.get("difference")).intValue();

            // Print out our result.
            System.out.println("Sum: " + Integer.toString(sum) +
                    ", Difference: " +
                    Integer.toString(difference));

        } catch (XmlRpcException exception) {
            System.err.println("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());
        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception.toString());
        }
    }
}
