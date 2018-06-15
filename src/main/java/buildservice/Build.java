package buildservice;

public class Build {

    private long ID;
    private String toolbox;
    private String ctf;

    public Build() {
    }

    public Build(long ID, String toolbox, String ctf) {
        this.ID = ID;
        this.toolbox = toolbox;
        this.ctf = ctf;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setToolbox(String toolbox) {
        this.toolbox = toolbox;
    }

    public String getToolbox() {
        return toolbox;
    }

    public long getID() {
        return ID;
    }

    public String getCtf() {
        return ctf;
    }

    public void setCtf(String ctf) {
        this.ctf = ctf;
    }

    @Override
    public String toString() {
        return "ID:  " + new Long(ID).toString() + ", toolbox: " + toolbox + ", ctf: " + ctf;
    }

}
