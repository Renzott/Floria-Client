package com.example.floria.modelo;

import java.io.Serializable;
import java.util.List;

public class Card_Post implements Serializable {

    public String uid;
    public String user;
    public String image;
    public String path;
    public String descripcion;
    public int like;
    public int views;

    public List<Comments> listaComments;
    public int countComments;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public List<Comments> getListaComments() {
        return listaComments;
    }

    public void setListaComments(List<Comments> listaComments) {
        this.listaComments = listaComments;
    }

    public int getCountComments() {
        return countComments;
    }

    public void setCountComments(int countComments) {
        this.countComments = countComments;
    }
}
