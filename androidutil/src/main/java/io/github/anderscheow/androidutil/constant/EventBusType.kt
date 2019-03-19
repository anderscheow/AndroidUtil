package io.github.anderscheow.androidutil.constant

enum class EventBusType {
    ON_ATTACH,
    ON_CREATE,
    ON_START,
    ON_RESUME;

    companion object {

        fun isOnAttach(eventBusType: EventBusType?): Boolean {
            return eventBusType != null && eventBusType == ON_ATTACH
        }

        fun isOnCreate(eventBusType: EventBusType?): Boolean {
            return eventBusType != null && eventBusType == ON_CREATE
        }

        fun isOnStart(eventBusType: EventBusType?): Boolean {
            return eventBusType != null && eventBusType == ON_START
        }

        fun isOnResume(eventBusType: EventBusType?): Boolean {
            return eventBusType != null && eventBusType == ON_RESUME
        }
    }
}
