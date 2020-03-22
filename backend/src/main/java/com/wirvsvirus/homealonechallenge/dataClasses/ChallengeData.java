package com.wirvsvirus.homealonechallenge.dataClasses;

public class ChallengeData {

    public final String challengeName;
    public final String challengeDescription;
    public final String challengeTag;
    public final int challengeCreator;

    public ChallengeData(String challengeName, String challengeDescription, String challengeTag, int challengeCreator) {
        this.challengeName = challengeName;
        this.challengeDescription = challengeDescription;
        this.challengeTag = challengeTag;
        this.challengeCreator = challengeCreator;
    }

    public int getChallengeCreator() {
        return challengeCreator;
    }

    public String getChallengeName() {
        return challengeName;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public String getChallengeTag() {
        return challengeTag;
    }
}
