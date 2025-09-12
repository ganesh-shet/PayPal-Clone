package com.paypal.notification_service.controller;

import com.paypal.notification_service.entity.Notification;
import com.paypal.notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity sendNotification(@RequestBody Notification notification){
        Notification sendNotification = notificationService.sendNotification(notification);
        return new ResponseEntity<>(sendNotification, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUserId(@PathVariable String userId){
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }
}
