package com.miniProject.AuditTrailGenerator.controller;

import com.miniProject.AuditTrailGenerator.model.VersionEntry;
import com.miniProject.AuditTrailGenerator.service.DiffService;
import com.miniProject.AuditTrailGenerator.service.VersionStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "http://localhost:3000")
public class VersionController {

    private final DiffService diffService;
    private final VersionStore versionStore;

    public VersionController(DiffService diffService, VersionStore versionStore) {
        this.diffService = diffService;
        this.versionStore = versionStore;
    }

    // POST /save-version expects JSON { "text": "..." }
    @PostMapping("/save-version")
    public ResponseEntity<?> saveVersion(@RequestBody Map<String, String> body) {
        String newText = body.getOrDefault("text", "");
        String previousText = versionStore.getPreviousText();

        Map<String, List<String>> diff = diffService.getWordDiff(previousText, newText);
        List<String> added = diff.getOrDefault("added", List.of());
        List<String> removed = diff.getOrDefault("removed", List.of());

        VersionEntry entry = new VersionEntry(added, removed, previousText.length(), newText.length());
        versionStore.save(entry, newText);

        return ResponseEntity.ok(Map.of("message", "Version saved", "entry", entry));
    }

    // GET /versions returns list
    @GetMapping("/versions")
    public ResponseEntity<List<VersionEntry>> getVersions() {
        return ResponseEntity.ok(versionStore.getAll());
    }
}
