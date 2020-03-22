package com.wirvsvirus.homealonechallenge.dataClasses;

import java.util.ArrayList;

public class ChallengeListReturn {

    ArrayList<ChallengeListEntry> challengeNames;

    public ChallengeListReturn(ArrayList<ChallengeListEntry> challengeNames) {
        this.challengeNames = challengeNames;
    }

    public ArrayList<ChallengeListEntry> getChallengeNames() {
        return challengeNames;
    }
}