package com.example.pets_backend.controller;


import com.example.pets_backend.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SendMailController
{
    private SendMailService mailService;

    @Autowired
    public void setMailService( SendMailService mailService )
    {
        this.mailService = mailService;
    }

    @Scheduled( cron = "0 */1 * * * ?" )
//    @Scheduled( cron = "0 0 7 * * ?" )
    private void sendMailOnSchedule() throws Exception
    {
        mailService.sendMail();
//        mailService.sendMailAttach( "C:/Users/gzw/Desktop/required_data_20210824.csv" );
        System.out.println( "Send mail at... " + new Date() );
    }
}
