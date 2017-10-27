/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vlib;

import generated.Carto;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author tkint
 */
@Named(value = "cartoBean")
@SessionScoped
public class CartoBean implements Serializable {

    private Carto carto;
    
    /**
     * Creates a new instance of CartoBean
     */
    public CartoBean() {
    }
    
}
