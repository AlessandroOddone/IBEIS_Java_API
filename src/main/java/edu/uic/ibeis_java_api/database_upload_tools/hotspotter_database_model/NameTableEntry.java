package edu.uic.ibeis_java_api.database_upload_tools.hotspotter_database_model;

public class NameTableEntry implements Comparable<NameTableEntry> {

    private int id;
    private String name;

    public NameTableEntry(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof NameTableEntry)) {
            return false;
        }
        if (getId() == ((NameTableEntry) obj).getId()) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(NameTableEntry o) {
        return Integer.compare(getId(), o.getId());
    }

}
