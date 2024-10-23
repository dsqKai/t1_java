package ru.t1.java.demo.kafka.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.mapper.TransactionMapper;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @KafkaListener(id = "${t1.kafka.listener.transaction-save-id}",
            topics = "${t1.kafka.topic.transactions}",
            containerFactory = "transactionKafkaListenerContainerFactory")
    public void listener(@Payload List<TransactionDto> messageList,
                         Acknowledgment ack) {
        try {
            List<Transaction> transactions = messageList.stream()
                    .map(transactionMapper::toEntity)
                    .toList();
            transactionService.saveTransactions(transactions);
        } catch (Exception e) {
            log.error("Failed to process Kafka message: {}", messageList, e);
        } finally {
            ack.acknowledge();
        }
    }
}
