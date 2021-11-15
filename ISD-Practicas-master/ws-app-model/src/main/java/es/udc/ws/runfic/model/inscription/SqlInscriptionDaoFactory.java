package es.udc.ws.runfic.model.inscription;

import es.udc.ws.runfic.model.race.SqlRaceDao;
import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlInscriptionDaoFactory {
    private final static String CLASS_NAME_PARAM = "SqlInscriptionDaoFactory.className";
    private static SqlInscriptionDao dao = null;

    private SqlInscriptionDaoFactory(){};

    public static synchronized SqlInscriptionDao getDao(){
        if (dao == null){
            dao = getInstance();
        }
        return dao;
    }

    private static SqlInscriptionDao getInstance(){
        try{
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAM);
            Class daoClass = Class.forName(daoClassName);
            return (SqlInscriptionDao) daoClass.getDeclaredConstructor().newInstance();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
