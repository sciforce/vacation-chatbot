package com.vacation_bot.gateway.inbound.slack;

import com.vacation_bot.core.InternalTranslationPort;
import com.vacation_bot.domain.CustomizedSentence;
import me.ramswaroop.jbot.core.slack.Bot;
import me.ramswaroop.jbot.core.slack.Controller;
import me.ramswaroop.jbot.core.slack.EventType;
import me.ramswaroop.jbot.core.slack.models.Event;
import me.ramswaroop.jbot.core.slack.models.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SlackBot extends Bot {

    private final InternalTranslationPort internalPort;

    SlackBot( final InternalTranslationPort anInternalPort ) {
        internalPort = anInternalPort;
    }

    @Value( "${slackBotToken}" )
    private String slackToken;

    @Controller( events = { EventType.DIRECT_MENTION, EventType.DIRECT_MESSAGE } )
    public void onReceiveDM( WebSocketSession session, Event event ) {
        if ( event.getText().contains( "hi" ) ) {
            reply( session, event, new Message( "Hi, I am " + slackService.getCurrentUser().getName() ) );
        }
        else {
            CustomizedSentence inputSentence = new CustomizedSentence();
            inputSentence.setOriginalSentence( event.getText() );
            inputSentence.setUserExternalCode( event.getUserId() );
            String response = internalPort.processSentence( MessageBuilder.withPayload( inputSentence ).build() );
            reply( session, event, new Message( response ) );
        }
    }

    @Override
    public String getSlackToken() {
        return slackToken;
    }

    @Override
    public Bot getSlackBot() {
        return this;
    }
}
