package com.blito.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notification_id;
    @Column(columnDefinition="TEXT")
    private String message;
    private Boolean seen;
    private Timestamp date;
    @Column(name = "notification_type")
    private String notificationType;

    @ManyToOne(targetEntity=User.class, optional=false)
    @JoinColumn(name="receiverId")
    private User receiver;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="senderId")
    private User sender;


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

    public Boolean isSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
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
