package edu.ifrs.model;

import java.util.ArrayList;

public class Personagem { //baseado em https://rickandmortyapi.com/documentation#character-schema
    private int id;
    private String name;
    private String status; //The status of the character ('Alive', 'Dead' or 'unknown').
    private String species;
    private String type; //The type or subspecies of the character.
    private String gender; //The gender of the character ('Female', 'Male', 'Genderless' or 'unknown').
    private Object origin; //Name and link to the character's origin location.
    private Object location; //Name and link to the character's last known location endpoint.
    private String image; //Link to the character's image. All images are 300x300px and most are medium shots or portraits since they are intended to be used as avatars.
    private ArrayList<String> episode = new ArrayList<>(); //List of episodes in which this character appeared.
    private String url; //Link to the character's own URL endpoint.
    private String created; //Time at which the character was created in the database.
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getSpecies() {
        return species;
    }
    public void setSpecies(String species) {
        this.species = species;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Object getOrigin() {
        return origin;
    }
    public void setOrigin(Object origin) {
        this.origin = origin;
    }
    public Object getLocation() {
        return location;
    }
    public void setLocation(Object location) {
        this.location = location;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public ArrayList<String> getEpisode() {
        return episode;
    }
    public void setEpisode(ArrayList<String> episode) {
        this.episode = episode;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getCreated() {
        return created;
    }
    public void setCreated(String created) {
        this.created = created;
    }
}

