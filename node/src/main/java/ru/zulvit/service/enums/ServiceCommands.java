package ru.zulvit.service.enums;

public enum ServiceCommands {
    HELP("/help"),
    REGISTRATION("/registration"),
    CANCEL("/cancel"),
    START("/start");
    private final String cmd;

    ServiceCommands(String cmd) {
        this.cmd = cmd;
    }

    public static ServiceCommands fromValue(String s) {
        for (ServiceCommands c : ServiceCommands.values()) {
            if (c.cmd.equals(s)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return cmd;
    }
}
