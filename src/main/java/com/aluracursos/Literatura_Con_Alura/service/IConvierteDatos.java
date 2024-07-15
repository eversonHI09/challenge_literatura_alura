package com.aluracursos.Literatura_Con_Alura.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
