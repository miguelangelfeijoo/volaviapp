package com.app.Volavia.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //Genera automaticamente los getter y setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private String usuario;
	
	@Column(unique=true)
	private String email;
	
	@Column
	private String contraseña;
	
	@Column
	private Date fecha_registro;
	
	@OneToMany (cascade =  CascadeType.ALL, orphanRemoval=true)
	@JoinColumn (name = "id_user") 
	private List<Trip> trips = new LinkedList<Trip>();
	
	@Enumerated(EnumType.STRING)
	private Profile perfil;
	
	public User(String email,String usuario, String password, Profile perfil) {
		this.email = email;
		this.usuario = usuario;
		this.contraseña = password;
		this.fecha_registro = new Date();
		this.perfil = perfil;
	}

}
