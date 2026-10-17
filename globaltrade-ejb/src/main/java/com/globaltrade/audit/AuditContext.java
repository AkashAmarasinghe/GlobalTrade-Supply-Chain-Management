package com.globaltrade.audit;

public final class AuditContext {
    private static final ThreadLocal<String> CURRENT_ACTOR = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> AUDIT_RECORDED =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    private AuditContext() {
    }

    public static void setActor(String actor) {

        AUDIT_RECORDED.set(Boolean.FALSE);

        if (actor == null || actor.isBlank()) {
            CURRENT_ACTOR.remove();
        } else {
            CURRENT_ACTOR.set(actor.trim());
        }
    }

    public static String getActor() {
        return CURRENT_ACTOR.get();
    }

    public static void markRecorded() {
        AUDIT_RECORDED.set(Boolean.TRUE);
    }

    public static boolean wasRecorded() {
        return Boolean.TRUE.equals(AUDIT_RECORDED.get());
    }

    public static void clear() {
        CURRENT_ACTOR.remove();
        AUDIT_RECORDED.remove();
    }
}
