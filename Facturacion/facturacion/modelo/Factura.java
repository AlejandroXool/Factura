package com.tuempresa.facturacion.modelo;
 
import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;
 
@Entity @Getter @Setter
@View(extendsView="super.DEFAULT", 
	members="pedidos { pedidos }" 
)
@View( name="SinClienteNiPedidos", 
	members=                       
	    "anyo, numero, fecha;" +   
	    "detalles;" +
	    "observaciones"
)
@Tab(baseCondition = "eliminado = false")
@Tab(name="Eliminado", baseCondition = "eliminado = true") 
public class Factura extends DocumentoComercial {
 
    @OneToMany(mappedBy="factura")
    @CollectionView("SinClienteNiFactura")
    @AddAction("Factura.anyadirPedidos")
    Collection<Pedido> pedidos; 
    
    public static Factura crearDesdePedidos(Collection<Pedido> pedidos)
            throws CrearFacturaException
        {
            Factura factura = null;
            for (Pedido pedido: pedidos) {
                if (factura == null) { 
                    pedido.crearFactura();
                    factura = pedido.getFactura(); 
                }
                else { 
                    pedido.setFactura(factura); 
                    pedido.copiarDetallesAFactura(); 
                } 
            } 
            if (factura == null) { 
                throw new CrearFacturaException("pedidos_no_especificados");
            }
            return factura;
        }
}
