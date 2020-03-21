package com.wirvsvirus.homealonechallenge.dataClasses;

public class ChallengeData {

    public final String challengeID;
    public final String challengeName;
    public final String challengeDescription;
    public final String challengeTag;
    public final String challengeCreator;

    public ChallengeData(String challengeID, String challengeName, String challengeDescription, String challengeTag, String challengeCreator) {
        this.challengeID = challengeID;
        this.challengeName = challengeName;
        this.challengeDescription = challengeDescription;
        this.challengeTag = challengeTag;
        this.challengeCreator = challengeCreator;
    }

    public String getChallengeCreator() {
        return challengeCreator;
    }

    public String getChallengeID() {
        return challengeID;
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
