package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notification_id;
    private String message;
    private boolean seen;
    private Timestamp date;
    @Column(name = "notification_type")
    private String notificationType;

    @ManyToOne(targetEntity=User.class, optional=false)
    @JoinColumn(name="receiverId")
    User receiver;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="senderId")
    User sender;


    public Long getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(Long notification_id) {
        this.notification_id = notification_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
