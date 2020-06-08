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
    public static String toString(Category c)
    {
        if(c==Blues) return "Blues";
        if(c==Rock) return "Rock";
        if(c==Pop) return "Pop";
        if(c==Jazz) return "Jazz";
        return null;
    }
}
