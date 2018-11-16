package com.vacation_bot.gateway.inbound.rest;

import com.vacation_bot.core.InternalTranslationPort;
import com.vacation_bot.domain.CustomizedSentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "/api" )
@CrossOrigin( "*" )
public class RestInboundGateway {

    private final InternalTranslationPort internalPort;

    @Autowired
    RestInboundGateway( final InternalTranslationPort anInternalPort ) {
        internalPort = anInternalPort;
    }

    @PostMapping()
    public void getResponse( @RequestBody String requestSentence ) {
        CustomizedSentence inputSentence = new CustomizedSentence();
        inputSentence.setOriginalSentence( requestSentence );
        Message<CustomizedSentence> message = MessageBuilder.withPayload( inputSentence ).build();
        String response = internalPort.processSentence( message );
        ResponseEntity.ok( response );
    }
}
