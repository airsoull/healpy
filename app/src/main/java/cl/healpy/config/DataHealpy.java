package cl.healpy.config;


public class DataHealpy {

    private final String TOKEN = "Token ";
    private final String URL_COMMUNE = "http://healpy.herokuapp.com/api/commune/";
    private final String URL_DRUGSTORE = "http://healpy.herokuapp.com/api/drugstore/";

    public DataHealpy(){}

    public String[] getRegions(){

        String[] regions = new String[]{"Región de Arica-Parinacota",
                "Región de Tarapacá",
                "Región de Antofagasta",
                "Región de Atacama",
                "Región de Coquimbo",
                "Región de Valparaíso",
                "Región Metropolitana de Santiago",
                "Región del Libertador Bernardo O'Higgins",
                "Región del Maule",
                "Región del Bío Bío",
                "Región de la Araucanía",
                "Región de Los Ríos",
                "Región de los Lagos",
                "Región de Aysén del General Carlos Ibáñez del Campo",
                "Región de Magallanes y la Antártica Chilena"};

        return regions;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public String getURL_COMMUNE() {
        return URL_COMMUNE;
    }

    public String getURL_DRUGSTORE() { return URL_DRUGSTORE; }

    public String getDrugstoreByRegion(String fk_region){
        String url = getURL_DRUGSTORE().concat("?fk_region=").concat(fk_region);
        return url;
    }

    public String getDrugstoreByCommune(String fk_commune){
        String url = getURL_DRUGSTORE().concat("?fk_commune=").concat(fk_commune);
        return url;
    }

    public String getDrugstoreByRegionAndCommune(String fk_region, String fk_commune){
        String url = getDrugstoreByRegion(fk_region).concat("&fk_commune=").concat(fk_commune);
        return url;
    }
}
