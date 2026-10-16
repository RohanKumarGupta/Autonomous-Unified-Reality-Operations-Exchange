package com.aurax.common.event;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    private UUID eventId;
    private String eventType;
    private String version = "1.0";
    private Instant timestamp;
    private UUID correlationId;
    private UUID causationId;
    private UUID tenantId;
    private UUID actorId;
    private ActorType actorType;
    private String source;

    protected DomainEvent() {
    }

    protected DomainEvent(UUID eventId, String eventType, Instant timestamp, UUID correlationId, UUID causationId, UUID tenantId, UUID actorId, ActorType actorType, String source) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.tenantId = tenantId;
        this.actorId = actorId;
        this.actorType = actorType;
        this.source = source;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public UUID getCausationId() {
        return causationId;
    }

    public void setCausationId(UUID causationId) {
        this.causationId = causationId;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public UUID getActorId() {
        return actorId;
    }

    public void setActorId(UUID actorId) {
        this.actorId = actorId;
    }

    public ActorType getActorType() {
        return actorType;
    }

    public void setActorType(ActorType actorType) {
        this.actorType = actorType;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
