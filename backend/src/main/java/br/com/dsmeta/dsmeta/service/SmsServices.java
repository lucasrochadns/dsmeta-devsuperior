package br.com.dsmeta.dsmeta.service;

import br.com.dsmeta.dsmeta.entities.Sale;
import br.com.dsmeta.dsmeta.repositories.SaleRepository;
import br.com.dsmeta.dsmeta.service.exceptions.SmsException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServices {

    @Value("${twilio.sid}")
    private String twilioSid;

    @Value("${twilio.key}")
    private String twilioKey;

    @Value("${twilio.phone.from}")
    private String twilioPhoneFrom;

    @Value("${twilio.phone.to}")
    private String twilioPhoneTo;

    @Autowired
    private SaleRepository saleRepository;

    public void sendSms(Long saleId) {

        try {
            Sale sale = saleRepository.findById(saleId).get();

            String date = sale.getDate().getMonthValue() + "/" + sale.getDate().getYear();

            String msg = "O vendedor " + sale.getSellerName() + " foi destaque em " + date
                    + " com um total de R$ " + String.format("%.2f", sale.getAmount());

            Twilio.init(twilioSid, twilioKey);

            PhoneNumber to = new PhoneNumber(twilioPhoneTo);
            PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

            Message message = Message.creator(to, from, msg).create();

        }catch(Exception erro){
            throw new SmsException(erro.getMessage());
        }
    }
}