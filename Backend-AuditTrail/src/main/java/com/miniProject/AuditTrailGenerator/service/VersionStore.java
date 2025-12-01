package com.miniProject.AuditTrailGenerator.service;

import com.miniProject.AuditTrailGenerator.model.VersionEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class VersionStore {

    private final List<VersionEntry> versions = new ArrayList<>();
    private String previousText = "";

    public synchronized void save(VersionEntry entry, String newText) {
        versions.add(entry);
        this.previousText = newText != null ? newText : "";
    }

    public synchronized List<VersionEntry> getAll() {
        // return in reverse chronological order (latest first)
        List<VersionEntry> copy = new ArrayList<>(versions);
        Collections.reverse(copy);
        return copy;
    }

    public synchronized String getPreviousText() {
        return previousText;
    }
}
