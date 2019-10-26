package com.web.chat;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.FilterInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.jboss.weld.environment.servlet.Listener;

import com.web.chat.security.AccessTokenFilter;
import com.web.chat.security.AuthenticationServlet;
import com.web.chat.websocket.ChatEndpoint;

import javax.servlet.DispatcherType;
import javax.servlet.ServletException;
import java.util.logging.Logger;

import static io.undertow.servlet.Servlets.listener;

/**
 * Initial Class.
 *
 * @author Omar
 */
public class Application {

    private static Undertow undertow;

    private static DeploymentManager deploymentManager;

    private static final int DEFAULT_PORT = 9191;

    private static final Logger LOGGER = Logger.getLogger(Application.class.getName());

    /**
     * Se asigna puerto 9191
     *
     * @param args
     */
    public static void main(final String[] args) {
        startServer(DEFAULT_PORT);
    }

    /**
     * Se ejecuta el server en el puerto 9191
     *
     * @param port
     */
    public static void startServer(int port) {
        LOGGER.info(String.format("Iniciando el servidor en el puerto %d", port));
        PathHandler path = Handlers.path();
        undertow = Undertow.builder()
                .addHttpListener(port, "localhost")
                .setHandler(path)
                .build();
        undertow.start();
        LOGGER.info(String.format("Servidor iniciado en puerto %d", port));

        DeploymentInfo servletBuilder = Servlets.deployment()
                .setClassLoader(Application.class.getClassLoader())
                .setContextPath("/")
                .addWelcomePage("index.html")
                .addListeners(listener(Listener.class))
                .setResourceManager(new ClassPathResourceManager(Application.class.getClassLoader()))
                .addFilter(new FilterInfo("accessTokenFilter", AccessTokenFilter.class))
                .addFilterUrlMapping("accessTokenFilter", "/chat/*", DispatcherType.REQUEST)
                .addServlet(Servlets.servlet("authenticationServlet", AuthenticationServlet.class).addMapping("/auth"))
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
                        new WebSocketDeploymentInfo()
                                .setBuffers(new DefaultByteBufferPool(true, 100))
                                .addEndpoint(ChatEndpoint.class))
                .setDeploymentName("application.war");

        LOGGER.info("Inicio de la aplicación");

        deploymentManager = Servlets.defaultContainer().addDeployment(servletBuilder);
        deploymentManager.deploy();

        try {
            path.addPrefixPath("/", deploymentManager.start());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Aplicación Iniciada");
    }

    /**
     * Stop server.
     */
    public static void stopServer() {

        if (undertow == null) {
            throw new IllegalStateException("El servidor no se ha iniciado.");
        }

        LOGGER.info("Stoping server");

        deploymentManager.undeploy();
        undertow.stop();

        LOGGER.info("Server stopped");
    }
}