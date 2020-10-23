package com.cw.productconsume2;

public class CmdRequest {
    private int commandId;
    private String commandName;

    CmdRequest(int commandId, String commandName) {
        this.commandId = commandId;
        this.commandName = commandName;
    }

    void setCommandId(int commandId) {
        this.commandId = commandId;
    }


    int getCommandId() {
        return this.commandId;
    }


    void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    int getCommandName() {
        return this.commandId;
    }


}
