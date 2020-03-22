package com.wirvsvirus.homealonechallenge.dataClasses;

import com.fasterxml.jackson.databind.ser.BeanSerializer;

import java.io.Serializable;

public class ChallengeListEntry {

    String challengeName;
    int challengeID;

    public ChallengeListEntry(String challengeName, int challengeID) {
        this.challengeName = challengeName;
        this.challengeID = challengeID;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public int getChallengeID() {
        return challengeID;
    }
}
