package com.wirvsvirus.homealonechallenge.dataClasses;

public class ChallengeDbEntry {

    public final String challengeName;
    public final String challengeDescription;
    public final String challengeTag;
    public final String challengeCreator;

    public ChallengeDbEntry(String challengeName, String challengeDescription, String challengeTag, String challengeCreator) {
        this.challengeName = challengeName;
        this.challengeDescription = challengeDescription;
        this.challengeTag = challengeTag;
        this.challengeCreator = challengeCreator;
    }

    public String getChallengeCreator() {
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
