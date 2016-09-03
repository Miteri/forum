package com.forum.server.models.staticInfo;

import java.util.List;

/**
 * 31.08.16
 *
 * @author Dinar Rafikov (First Software Engineering Platform)
 * @version 1.0
 */
public class Section {
    private long sectionId;
    private String name;
    private long themesCount;
    private long subsectionsCount;
    private String url;
    private List<Subsection> subsections;

    public void setSubsections(List<Subsection> subsections) {
        this.subsections = subsections;
    }

    public List<Subsection> getSubsections() {
        return subsections;
    }

    public String getUrl() {
        return url;
    }

    public long getSectionId() {
        return sectionId;
    }

    public String getName() {
        return name;
    }

    public long getThemesCount() {
        return themesCount;
    }

    public long getSubsectionsCount() {
        return subsectionsCount;
    }

    protected Section() {
    }

    private Section(Builder builder) {
        this.sectionId = builder.sectionId;
        this.name = builder.name;
        this.themesCount = builder.themesCount;
        this.subsectionsCount = builder.subsectionsCount;
        this.url = builder.url;
        this.subsections = builder.subsections;
    }

    public static class Builder {
        private long sectionId;
        private String name;
        private long themesCount;
        private long subsectionsCount;
        private String url;
        private List<Subsection> subsections;

        public Builder Subsections(List<Subsection> subsections) {
            this.subsections = subsections;
            return this;
        }

        public Builder Url(String url) {
            this.url = url;
            return this;
        }

        public Builder SectionId(long sectionId) {
            this.sectionId = sectionId;
            return this;
        }

        public Builder Name(String name) {
            this.name = name;
            return this;
        }

        public Builder ThemesCount(long themesCount) {
            this.themesCount = themesCount;
            return this;
        }

        public Builder SubsectionsCount(long subsectionsCount) {
            this.subsectionsCount = subsectionsCount;
            return this;
        }

        public Section build() { return new Section(this); }
    }
}
