/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlib;

import generated.Carto;
import java.io.FileInputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;

/**
 *
 * @author tkint
 */
@Named(value = "cartoBean")
@SessionScoped
public class CartoBean implements Serializable {

    private Carto carto;

    private MapModel mapModel;

    public Carto getCarto() {
        return carto;
    }

    public void setCarto(Carto carto) {
        this.carto = carto;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    @PostConstruct
    public void init() {
        try {
            mapModel = new DefaultMapModel();

            JAXBContext jAXBContext = JAXBContext.newInstance("generated");
            Unmarshaller u = jAXBContext.createUnmarshaller();
            
            carto = (Carto) u.unmarshal(new FileInputStream("C:\\Users\\tkint\\Google Drive\\EPSI\\I4\\Web Services\\TP 1\\carto.xml"));
            
            for (Carto.Markers.Marker m : carto.getMarkers().getMarker()) {
                org.primefaces.model.map.Marker marker = new org.primefaces.model.map.Marker(new LatLng(m.getLat(), m.getLng()), m.getName());
                mapModel.addOverlay(marker);
            }
        } catch (Exception ex) {
            Logger.getLogger(CartoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new instance of CartoBean
     */
    public CartoBean() {
    }

}
