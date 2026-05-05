package com.example.worksync.model;

public class Tarea {
    private String idMongo;
    private String titulo;
    private String descripcion;
    private int idEmpleadoRelacional;

    public Tarea(String idMongo, String titulo, String descripcion, int idEmpleadoRelacional) {
        this.idMongo = idMongo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.idEmpleadoRelacional = idEmpleadoRelacional;
    }

    public String getIdMongo() { return idMongo; }
    public void setIdMongo(String idMongo) { this.idMongo = idMongo; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getIdEmpleadoRelacional() { return idEmpleadoRelacional; }
    public void setIdEmpleadoRelacional(int idEmpleadoRelacional) { this.idEmpleadoRelacional = idEmpleadoRelacional; }
}