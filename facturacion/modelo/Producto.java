package com.tuempresa.facturacion.modelo;

import java.math.*;

import javax.persistence.*;

import org.openxava.annotations.*;

import lombok.*;

@Entity @Getter @Setter
public class Producto {
 
	@Id @Column(length = 9)
	int numero;
	
	@Column(length = 50) @Required
	String descripcion;
	
	@ManyToOne(
	fetch=FetchType.LAZY, 
	optional=true) 
    @DescriptionsList 
	Categoria categoria;
	
	@Stereotype("DINERO") 
	BigDecimal precio;
	
	@Stereotype("GALERIA_IMAGENES") 
	@Column(length=32) 
	String fotos;
	
	@Stereotype("TEXTO_GRANDE")
	String observaciones;
	
	
	
}
