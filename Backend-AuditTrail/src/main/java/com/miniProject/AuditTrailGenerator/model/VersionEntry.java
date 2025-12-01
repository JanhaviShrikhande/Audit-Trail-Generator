package com.miniProject.AuditTrailGenerator.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Data
public class VersionEntry {
    private String id;
    private String timestamp;
    private List<String> addedWords;
    private List<String> removedWords;
    private int oldLength;
    private int newLength;


    public VersionEntry( List<String> addedWords, List<String> removedWords, int oldLength, int newLength) {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.addedWords = addedWords;
        this.removedWords = removedWords;
        this.oldLength = oldLength;
        this.newLength = newLength;
    }



}
