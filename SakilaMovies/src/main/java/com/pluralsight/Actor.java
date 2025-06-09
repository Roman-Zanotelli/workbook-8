package com.pluralsight;

public class Actor {
    int actorId;
    String firstName, lastName;

    public Actor(String firstName, String lastName, int actorId) {
        this.firstName = firstName;
        this.actorId = actorId;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format("""
                Actor ID: %d
                Name: %s, %s
                ------------------------------
                """, actorId, firstName, lastName);
    }
}
