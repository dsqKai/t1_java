package ru.t1.java.demo.kafka.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.mapper.AccountMapper;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.service.AccountService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaAccountConsumer {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @KafkaListener(id = "${t1.kafka.listener.account-save-id}",
            topics = "${t1.kafka.topic.accounts}",
            containerFactory = "accountKafkaListenerContainerFactory")
    public void listener(@Payload List<AccountDto> messageList,
                         Acknowledgment ack) {
        try {
            List<Account> accounts = messageList.stream()
                    .map(accountMapper::toEntity)
                    .toList();
            accountService.saveAccounts(accounts);
        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", messageList, e);
        } finally {
            ack.acknowledge();
        }
    }
}
