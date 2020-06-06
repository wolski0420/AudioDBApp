package edu.agh.entities;

public enum Category {
    Blues,
    Rock,
    Pop,
    Jazz;

    public static Category fromString(String categoryName){
        if(categoryName == null) return null;
        if(categoryName.equalsIgnoreCase("blues")) return Blues;
        if(categoryName.equalsIgnoreCase("rock")) return Rock;
        if(categoryName.equalsIgnoreCase("jazz")) return Jazz;
        if(categoryName.equalsIgnoreCase("pop")) return Pop;
        return null;
    }
}
