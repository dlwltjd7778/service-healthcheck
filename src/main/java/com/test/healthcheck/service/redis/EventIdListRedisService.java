package com.test.healthcheck.service.redis;

import com.test.healthcheck.model.incdtmg.EventId;
import com.test.healthcheck.repository.EventIdRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventIdListRedisService {

    private final EventIdRepository eventIdRepository;

    // event id 하나 리스트에 저장
    public void addEventCheckList(String eventId){
        EventId id = eventIdRepository.save(EventId.builder().eventId(eventId).build());
        log.info("add to EventIdList : {}", id);
    }

    // event id 하나 삭제
    public void deleteEventCheckListByEventId(String eventId){
        eventIdRepository.deleteById(eventId);
        log.info("delete from EventIdList : {}", eventId);
    }

    // 인시던트 생성여부 체크할 event id 전체 탐색
    public Iterable<EventId> getEventIdChkList(){
        return eventIdRepository.findAll();
    }


}
