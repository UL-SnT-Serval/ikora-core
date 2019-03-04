package org.ukwikora.export.website.model;

import org.ukwikora.analytics.CloneDetection;
import org.ukwikora.analytics.Clones;
import org.ukwikora.model.Project;
import org.ukwikora.model.UserKeyword;

import java.util.HashSet;
import java.util.List;

public class ClonePage extends Page {
    private final Table table;

    public ClonePage(String id, String name, List<Project> projects) {
        super(id, name);

        this.table = new Table(
                "clones",
                "Duplicated Code",
                new String[]{"Group", "Type", "Name", "File", "Lines", "Project"}
        );

        Clones<UserKeyword> UserKewordClones = CloneDetection.findClones(new HashSet<>(projects), UserKeyword.class);
    }
}
