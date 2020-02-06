package servlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.grid.common.exception.GridException;
import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.ProxySet;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Nodes Servlet for Selenium grid 3.141.59
 * http://localhost:4444/grid/admin/nodes
 * Example output:
 * {
 *   "nodes": [
 *     {
 *       "sessions": [
 *         "84dc816d439a74a0ec48e6bfbd659ee9",
 *         "ceb5b64922eb8fad6bda8a875506604e",
 *         "67710195-8323-4498-a649-ac3cb2ee99a5"
 *       ],
 *       "port": 5566,
 *       "host": "192.168.56.1",
 *       "url": "http://192.168.56.1:5566"
 *     },
 *     {
 *       "sessions": [],
 *       "port": 43952,
 *       "host": "192.168.56.1",
 *       "url": "http://192.168.56.1:43952"
 *     }
 *   ]
 * }
 */
public class nodes extends RegistryBasedServlet {

    /**
     * Default constructor
     */
    public nodes() {
        this(null);
    }

    /**
     * @param registry GridRegistry
     */
    public nodes(GridRegistry registry) {
        super(registry);
    }

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletRequest
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response);
    }

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletRequest
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        process(request, response);
    }

    /**
     * @param request  HttpServletRequest
     * @param response HttpServletRequest
     */
    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        try {
            JSONObject res = getResponse();
            response.getWriter().print(res);
            response.getWriter().close();
        } catch (JSONException e) {
            throw new GridException(e.getMessage());
        }
    }

    /**
     * @return JSONObject
     */
    private JSONObject getResponse() {
        JSONObject json = new JSONObject();
        ProxySet proxies = getRegistry().getAllProxies();
        json.put("nodes", getNodes(proxies));
        return json;
    }

    /**
     * @param proxies ProxySet
     * @return JSONArray
     */
    private JSONArray getNodes(ProxySet proxies) {
        JSONArray array = new JSONArray();

        List<Thread> threads = new ArrayList<>();
        List<NodeInfoTask> tasks = new ArrayList<>();

        for (RemoteProxy proxy : proxies) {
            tasks.add(new NodeInfoTask(proxy));
            Thread worker = new Thread(tasks.get(tasks.size() - 1));
            worker.start();
            threads.add(worker);
        }
        boolean running;
        do {
            running = false;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    running = true;
                    break;
                }
            }
        } while (running);

        for (NodeInfoTask task : tasks) {
            array.put(task.getJson());
        }

        return array;
    }
}
