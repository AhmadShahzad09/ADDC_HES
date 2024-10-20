package com.minsait.commands.impl.action;

import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@Service("BillingProfileActionWS")
public class BillingProfileActionWS extends AbstractAction{

    @Override
    public MessageChannel getMessageChannel() {
        return messageStream().messageOutMdmBillingProfile();
    }
}
