package com.karthik.logisticsledger;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Test
    void shouldReturnMessageFromRepository(){

        MessageRepository messageRepository = mock(MessageRepository.class);

        when(messageRepository.getMessage()).thenReturn("Hello from Mockito!");

        MessageService messageService = new MessageService(messageRepository);

        String result = messageService.getMessage();

        assertEquals("Hello from Mockito!",result);

        verify(messageRepository).getMessage();

    }
}
