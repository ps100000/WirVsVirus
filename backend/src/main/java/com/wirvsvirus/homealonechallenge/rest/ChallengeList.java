package com.wirvsvirus.homealonechallenge.rest;

import com.wirvsvirus.homealonechallenge.dataClasses.ChallengeListEntry;
import com.wirvsvirus.homealonechallenge.dataClasses.ChallengeListReturn;
import com.wirvsvirus.homealonechallenge.db.SpringJdbc;
import kotlin.Unit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@RestController
public class ChallengeList {

    @GetMapping("/challengelist")
    public ResponseEntity<ChallengeListReturn> loadChallengeList(@RequestParam(value = "tag") String tag) {

        ArrayList<ChallengeListEntry> challengeNames = new ArrayList<>();

        ResultSet rs;
        if(tag.trim().length() != 0) {
             rs = SpringJdbc.Companion.executeQuery("SELECT name, challenge_id FROM challenges WHERE challenge_id IN (SELECT challenge_id FROM challenge_tag_links WHERE tag_id IN (SELECT tag_id FROM tags WHERE name = ?))", pst -> {
                try {
                    pst.setString(1, tag);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return Unit.INSTANCE;
            });

        } else {
            rs = SpringJdbc.Companion.executeQuery("SELECT name, challenge_id FROM challenges", pst -> Unit.INSTANCE);
        }

        try {
            while (rs.next()) {
                challengeNames.add(new ChallengeListEntry(rs.getString("name"), rs.getInt("challenge_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ChallengeListReturn(challengeNames), HttpStatus.OK);
    }
}
