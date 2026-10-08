package com.example.GameVault.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Juego {
    private Long id;
    private String titulo;
    private String descripcion;

    // Almacenamos el nombre del archivo o de la URL de la imagen
    private String portadaUrl;
}
