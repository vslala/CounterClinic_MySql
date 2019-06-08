package com.codesvenue.counterclinic.configuration;

public enum EndPoint {
    DOCTOR_ACTION_CALL_NEXT_PATIENT("doctor-action", "next-patient"), WEBSOCKET("websocket", "connect");

    private final String topic;
    private final String endPoint;

    EndPoint(String topic, String endPoint) {
        this.topic = topic;
        this.endPoint = endPoint;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getEndPoint() {
        return this.endPoint;
    }

    public String getEndPointUrl() {
        return String.format("/%s/%s", topic, endPoint);
    }
}
