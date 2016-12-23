package net.mctournaments.bukkit.data;

public enum MessageType {

    BLANK("%s"),
    ERROR("&c»&4» &c%s"),
    INFO("&d»&5» &d%s"),
    INFO_ALTERNATIVE("&b»&3» &b%s"),
    SUCCESS("&a»&2» &a%s"),
    WARNING("&e»&6» &e%s");

    private final String msg;

    MessageType(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

}