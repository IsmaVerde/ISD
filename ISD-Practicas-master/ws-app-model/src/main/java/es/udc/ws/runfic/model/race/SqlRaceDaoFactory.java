package es.udc.ws.runfic.model.race;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlRaceDaoFactory {
    private final static String CLASS_NAME_PARAM = "SqlRaceDaoFactory.className";
    private static SqlRaceDao dao = null;
    
    private SqlRaceDaoFactory(){};
    
    public static synchronized SqlRaceDao getDao(){
        if (dao == null){
            dao = getInstance();
        }
        return dao;
    }
    
    private static SqlRaceDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAM);
            Class daoClass = Class.forName(daoClassName);
            return (SqlRaceDao) daoClass.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
