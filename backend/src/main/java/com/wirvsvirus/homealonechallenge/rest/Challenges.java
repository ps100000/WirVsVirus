package com.wirvsvirus.homealonechallenge.rest;

import com.wirvsvirus.homealonechallenge.dataClasses.BooleanReturn;
import com.wirvsvirus.homealonechallenge.dataClasses.ChallengeDbEntry;
import com.wirvsvirus.homealonechallenge.db.SpringJdbc;
import kotlin.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class Challenges {

    @GetMapping("/challenges/{challengeID}")
    public ResponseEntity<ChallengeDbEntry> loadChallenge(@PathVariable("challenge_id") int challengeID) {

        String challengeName = "", challengeDescription = "", challengeTag = "", creatorName = "";

        ResultSet rs = SpringJdbc.Companion.executeQuery("SELECT * FROM challenges WHERE challenge_id = ?", pst -> {
            try {
                pst.setInt(1, challengeID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        });
        try {
            if (!rs.next()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            challengeName = rs.getString("name");
            challengeDescription = rs.getString("description");
            challengeTag = rs.getString("tag");
            final int challengeCreator = rs.getInt("creator");
            ResultSet rs2 = SpringJdbc.Companion.executeQuery("SELECT username FROM users WHERE user_id = ?", pst -> {
                try {
                    pst.setInt(1, challengeCreator);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            });
            if (!rs2.next()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            creatorName = rs2.getString("username");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ChallengeDbEntry(challengeName, challengeDescription, challengeTag, creatorName), HttpStatus.OK);
    }

    @PostMapping("/challenges")
    public ResponseEntity<BooleanReturn> createChallenge(@RequestBody ChallengeDbEntry data) {

        ResultSet rs = SpringJdbc.Companion.executeQuery("SELECT user_id FROM users WHERE username = ?", pst -> {
            try {
                pst.setString(1, data.challengeCreator);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        });
        try {
            if (!rs.next()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            int userID = rs.getInt("user_id");
            SpringJdbc.Companion.executeUpdate("INSERT INTO challenges(name, description, tag, creator) VALUES(?, ?, ?, ?)", pst -> {
                try {
                    pst.setString(1, data.getChallengeName());
                    pst.setString(2, data.getChallengeDescription());
                    pst.setInt(3, data.getChallengeTag().equals("Charity") ? 0 : data.getChallengeTag().equals("Sport") ? 1 : data.getChallengeTag().equals("Kreativität") ? 2 : data.getChallengeTag().equals("Haushalt") ? 3 : 4);
                    pst.setInt(4, userID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new BooleanReturn(true), HttpStatus.OK);
    }

    @PutMapping("/challenges/{challengeID}")
    public ResponseEntity<BooleanReturn> overwriteChallenge(@PathVariable("challengeID") int challengeID, @RequestBody ChallengeDbEntry data) {

        ResultSet rs = SpringJdbc.Companion.executeQuery("SELECT * FROM users WHERE username = ?", pst -> {
            try {
                pst.setString(1, data.challengeCreator);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        });
        try {
            if (!rs.next()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            int userID = rs.getInt("user_id");

            SpringJdbc.Companion.executeUpdate("UPDATE challenges SET name = ?, description = ?, tag = ?, creator = ? WHERE challenge_id = ?", pst -> {
                try {
                    pst.setString(1, data.getChallengeName());
                    pst.setString(2, data.getChallengeDescription());
                    pst.setInt(3, data.getChallengeTag().equals("Charity") ? 0 : data.getChallengeTag().equals("Sport") ? 1 : data.getChallengeTag().equals("Kreativität") ? 2 : data.getChallengeTag().equals("Haushalt") ? 3 : 4);
                    pst.setInt(4, userID);
                    pst.setInt(5, challengeID);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new BooleanReturn(true), HttpStatus.OK);
    }

    @DeleteMapping("/challenges/{challengeID}")
    public ResponseEntity<BooleanReturn> deleteChallenge(@PathVariable("challengeID") int challengeID) {
        SpringJdbc.Companion.executeUpdate("DELETE FROM challenges WHERE challenge_id = ?", pst -> {
            try {
                pst.setInt(1, challengeID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Unit.INSTANCE;
        });
        return new ResponseEntity<>(new BooleanReturn(true), HttpStatus.OK);
    }
}
