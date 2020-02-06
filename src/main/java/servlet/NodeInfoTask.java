package servlet;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.grid.internal.RemoteProxy;

import java.io.IOException;
import java.net.URL;

public class NodeInfoTask implements Runnable {

    public RemoteProxy proxy;
    private volatile JSONObject json;

    /**
     * @param proxy RemoteProxy
     */
    public NodeInfoTask(RemoteProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void run() {
        JSONObject proxyJson = new JSONObject();

        URL remoteHost = this.proxy.getRemoteHost();
        String nodeUrl = remoteHost.toString();

        proxyJson.put("url", nodeUrl);
        proxyJson.put("host", remoteHost.getHost());
        proxyJson.put("port", remoteHost.getPort());
        proxyJson.put("sessions", this.getNodeSessions(nodeUrl));

        this.json = proxyJson;
    }

    /**
     * @param nodeUrl String
     * @return JSONArray
     */
    private JSONArray getNodeSessions(String nodeUrl) {
        JSONArray sessions = new JSONArray();
        JSONTokener tokener;
        try {
            tokener = new JSONTokener(new URL(nodeUrl + "/wd/hub/sessions").openStream());
        } catch (IOException e) {
            return sessions;
        }

        JSONObject data = new JSONObject(tokener);
        if (data.length() > 0) {
            JSONArray array = data.getJSONArray("value");
            for (Object obj : array) {
                if (obj instanceof JSONObject) {
                    sessions.put(((JSONObject) obj).get("id"));
                }
            }
        }
        return sessions;
    }

    /**
     * @return JSONArray
     */
    public JSONObject getJson() {
        return this.json;
    }
}
