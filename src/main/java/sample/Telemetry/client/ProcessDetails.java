package sample.Telemetry.client;

/**
 * Holds pieces of information, which can be observed if changed.
 * If a process has a changed name, we will immediately find this
 */
public class ProcessDetails {

    private  String processName;
    private  String processCommandName;
    private  String processId;

    public ProcessDetails() {}

    public String getProcessName() {
        return processName;
    }

    public String getProcessCommandName() {
        return processCommandName;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public void setProcessCommandName(String processCommandName) {
        this.processCommandName = processCommandName;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }
}
