package org.zkovari.changelog.core.generator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkovari.changelog.domain.Release;

public class ChangelogGenerator {

    private static final String NEW_LINE = System.lineSeparator();
    private static final String VERSION_REGEX = "##\\s\\[\\d+([.]\\d+){1,3}\\]";
    private static final Pattern VERSION_PATTERN;

    private ReleaseEntryGenerator releaseEntryGenerator;

    static {
	VERSION_PATTERN = Pattern.compile(VERSION_REGEX);
    }

    public String generate(String currentChangelogContent, Release release) {
	String newChangelog = currentChangelogContent;
	String newReleaseEntry = getReleaseEntryGenerator().generate(release);

	Matcher matcher = VERSION_PATTERN.matcher(currentChangelogContent);
	boolean foundLastVersion = matcher.find();
	if (foundLastVersion) {
	    String previousVersion = matcher.group();
	    newChangelog = matcher.replaceFirst(newReleaseEntry + previousVersion);
	} else {
	    if (currentChangelogContent.isEmpty()) {
		newChangelog = getChangelogHeader();
	    }
	    newChangelog = newChangelog + NEW_LINE + newReleaseEntry;
	}

	return newChangelog;
    }

    private String getChangelogHeader() {
	// @formatter:off
	return new StringBuilder("# Changelog").append(NEW_LINE)
		.append("All notable changes to this project will be documented in this file.").append(NEW_LINE)
		.append(NEW_LINE)
		.append("The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),").append(NEW_LINE)
		.append("and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).").append(NEW_LINE)
		.toString();
	// @formatter:on
    }

    public ReleaseEntryGenerator getReleaseEntryGenerator() {
	if (releaseEntryGenerator == null) {
	    releaseEntryGenerator = new ReleaseEntryGenerator();
	}
	return releaseEntryGenerator;
    }

    // package level for testing
    void setReleaseEntryGenerator(ReleaseEntryGenerator releaseEntryGenerator) {
	this.releaseEntryGenerator = releaseEntryGenerator;
    }

}
