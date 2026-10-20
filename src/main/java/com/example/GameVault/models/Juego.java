package com.example.GameVault.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    // Almacenaremos el nombre del archivo o URL de la imagen
    @Column(name = "portada_url")
    private String portadaUrl;

    @Column
    private Double precio;

    @Column
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

}
