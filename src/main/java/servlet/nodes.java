package servlet;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openqa.grid.internal.GridRegistry;
import org.openqa.grid.internal.ProxySet;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.web.servlet.RegistryBasedServlet;
import org.openqa.selenium.json.Json;

/**
 * Nodes Servlet for Selenium grid 3.141.59
 * http://localhost:4444/grid/admin/nodes
 */
public class nodes extends RegistryBasedServlet {

    public nodes() {
        this(null);
    }

    public nodes(GridRegistry registry) {
        super(registry);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        ProxySet proxySet = getRegistry().getAllProxies();
        Iterator<RemoteProxy> iterator = proxySet.iterator();
        Iterable<RemoteProxy> iterable = () -> iterator;
        List<Map<String, String>> maps = StreamSupport.stream(iterable.spliterator(), false)
                .map(remoteProxy -> {
                    Map<String, String> map = new HashMap<>();
                    URL remoteHost = remoteProxy.getRemoteHost();
                    String url = String.format("%s://%s:%d", remoteHost.getProtocol(),
                            remoteHost.getHost(), remoteHost.getPort());
                    map.put("IP_Address", remoteProxy.getRemoteHost().toString());
                    return map;
                }).collect(Collectors.toList());
        new Json().toJson(maps);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
        response.getWriter().append(new Json().toJson(maps));
    }
}
