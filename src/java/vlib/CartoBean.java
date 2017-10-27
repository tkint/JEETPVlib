/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlib;

import generated.Carto;
import generated.Station;
import java.io.FileInputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.primefaces.context.RequestContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author tkint
 */
@Named(value = "cartoBean")
@SessionScoped
public class CartoBean implements Serializable {

    private JAXBContext jAXBContext;

    private Unmarshaller unmarshaller;

    private Carto carto;

    private MapModel mapModel;

    private Marker marker;

    private Carto.Markers.Marker cartoMarker;
    
    private Station station;

    public JAXBContext getjAXBContext() {
        return jAXBContext;
    }

    public void setjAXBContext(JAXBContext jAXBContext) {
        this.jAXBContext = jAXBContext;
    }

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

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Carto.Markers.Marker getCartoMarker() {
        return cartoMarker;
    }

    public void setCartoMarker(Carto.Markers.Marker cartoMarker) {
        this.cartoMarker = cartoMarker;
    }

    public Unmarshaller getUnmarshaller() {
        return unmarshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    @PostConstruct
    public void init() {
        try {
            mapModel = new DefaultMapModel();

            jAXBContext = JAXBContext.newInstance("generated");
            unmarshaller = jAXBContext.createUnmarshaller();

            carto = (Carto) unmarshaller.unmarshal(new FileInputStream("C:\\Users\\tkint\\Google Drive\\EPSI\\I4\\Web Services\\TP 1\\carto.xml"));

            for (Carto.Markers.Marker m : carto.getMarkers().getMarker()) {
                org.primefaces.model.map.Marker marker = new org.primefaces.model.map.Marker(new LatLng(m.getLat(), m.getLng()), m.getNumber().toString());
                mapModel.addOverlay(marker);
            }
        } catch (Exception ex) {
            Logger.getLogger(CartoBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectMarker(OverlaySelectEvent event) throws MalformedURLException, JAXBException {
        marker = (Marker) event.getOverlay();

        station = (Station) unmarshaller.unmarshal(new URL("http://www.velib.paris.fr/service/stationdetails/" + marker.getTitle()));

        cartoMarker = getCartoMarkerByMarker(marker);

//        FacesContext fc = FacesContext.getCurrentInstance();
//        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, "Station", "TEST\nBOB++\nyyTEST");
//        RequestContext.getCurrentInstance().showMessageInDialog(fm);
//        String open = "Non";
//        if (station.getOpen() == 1) {
//            open = "Oui";
//        }
//        
//        String message = "Test\nBob";
//        
//        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Station Selected", "Adresse: " + cartoMarker.getAddress()));
//        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Station Selected", "Ouverte: " + open));
//        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Station Selected", "Dispos: " + station.getAvailable()));
    }

    private Carto.Markers.Marker getCartoMarkerByMarker(Marker marker) {
        Carto.Markers.Marker m = null;

        int i = 0;
        while (i < carto.getMarkers().getMarker().size() && m == null) {
            if (carto.getMarkers().getMarker().get(i).getNumber().toString().equals(marker.getTitle())) {
                m = carto.getMarkers().getMarker().get(i);
            }
            i++;
        }

        return m;
    }
    
    public String getDateUpdate() {
        Timestamp t = new Timestamp((long) station.getUpdated());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return dateFormat.format(new Date(t.getTime() * 1000));
    }
    
    public String getOuvert() {
        if (station.getOpen() == 1) {
            return "Ouvert";
        }
        return "Ferme";
    }
    
    public String getCouleur() {
        if (station.getOpen() == 1) {
            return "color: green";
        }
        return "color: red";
    }

    /**
     * Creates a new instance of CartoBean
     */
    public CartoBean() {
    }

}
