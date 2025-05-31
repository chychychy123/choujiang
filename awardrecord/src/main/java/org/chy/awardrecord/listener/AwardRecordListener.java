//package org.chy.awardrecord.listener;
//
//import lombok.RequiredArgsConstructor;
//import org.chy.awardrecord.service.AwardRecordService;
//import org.springframework.amqp.rabbit.annotation.Exchange;
//import org.springframework.amqp.rabbit.annotation.Queue;
//import org.springframework.amqp.rabbit.annotation.QueueBinding;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AwardRecordListener {
//    private final AwardRecordService awardRecordService;
//
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "draw.success.queue", durable = "true"),
//            exchange = @Exchange(name = "draw.topic"),
//            key = "draw.success"
//    ))
//
//    public void listenPaySuccess(Long orderId){
//         awardRecordService.showAward(orderId);
//    }
//}
