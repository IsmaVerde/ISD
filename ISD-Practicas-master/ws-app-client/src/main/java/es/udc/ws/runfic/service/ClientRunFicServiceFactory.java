package es.udc.ws.runfic.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;
import java.lang.reflect.InvocationTargetException;

public class ClientRunFicServiceFactory {

    private final static String CLASS_NAME_PARAMETER
            = "ClientRunFicServiceFactory.className";
    private static Class<ClientRunFicService> serviceClass = null;

    private ClientRunFicServiceFactory() {
    }

    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientRunFicService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager
                        .getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientRunFicService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;

    }

    public static ClientRunFicService getService() {

        try {
            return (ClientRunFicService) getServiceClass().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

    }
}