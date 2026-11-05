package com.karthik.logisticsledger;

public class MessageService {

    private final MessageRepository messageRepository;

    public  MessageService(MessageRepository messageRepository){
        this.messageRepository= messageRepository;
    }

    public  String getMessage(){
        return messageRepository.getMessage();
    }
}
