package com.wirvsvirus.homealonechallenge.rest;

import com.wirvsvirus.homealonechallenge.dataClasses.ChallengeData;
import com.wirvsvirus.homealonechallenge.db.SpringJdbc;
import kotlin.Unit;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class Challenges {

    @GetMapping("/challenges/{challengeID}")
    public ChallengeData loadChallenge(@PathVariable("challengeID") String challengeID) {
        return new ChallengeData(challengeID, "", "", 0);
    }

    @PostMapping("/challenges")
    public boolean createChallenge(@RequestBody ChallengeData data) {
        SpringJdbc.Companion.executeUpdate("INSERT INTO Challenges(challengeName, challengeDescription) VALUES(?, ?)", pst -> {
                pst.setString(1, data.getChallengeName());
                pst.setString(2, data.getChallengeDescription());
            return Unit.INSTANCE;
        });
        return true;
    }

    @PutMapping("/challenges/{challengesID}")
    public ChallengeData editChallenge(@PathVariable("challengeID") String challengeID) {
        return new ChallengeData(challengeID, "", "", 0);
    }
}
