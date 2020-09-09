package org.dromara.hmily.admin.service.repository.mongo;

import org.dromara.hmily.admin.dto.HmilyParticipantDTO;
import org.dromara.hmily.admin.dto.HmilyTransactionDTO;
import org.dromara.hmily.admin.service.repository.mongo.entity.ParticipantMongoEntity;
import org.dromara.hmily.admin.service.repository.mongo.entity.TransactionMongoEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

/**
 * mongo entity convert.
 *
 * @author gcedar
 */
public class MongoEntityConvert {
    
    private static Logger logger = LoggerFactory.getLogger(MongoEntityConvert.class);
    
    /**
     * 转换mongo对象.
     * @param mongoEntity mongoEntity.
     * @return entity.
     */
    public static HmilyParticipantDTO convert(final ParticipantMongoEntity mongoEntity) {
        HmilyParticipantDTO hmilyParticipant = new HmilyParticipantDTO();
        hmilyParticipant.setParticipantId(mongoEntity.getParticipantId());
        hmilyParticipant.setParticipantRefId(mongoEntity.getParticipantRefId());
        hmilyParticipant.setTransId(mongoEntity.getTransId());
        hmilyParticipant.setTransType(mongoEntity.getTransType());
        hmilyParticipant.setStatus(mongoEntity.getStatus());
        hmilyParticipant.setRole(mongoEntity.getRole());
        hmilyParticipant.setRetry(mongoEntity.getRetry());
        hmilyParticipant.setAppName(mongoEntity.getAppName());
        hmilyParticipant.setTargetClass(mongoEntity.getTargetClass());
        hmilyParticipant.setTargetMethod(mongoEntity.getTargetMethod());
        hmilyParticipant.setConfirmMethod(mongoEntity.getConfirmMethod());
        hmilyParticipant.setCancelMethod(mongoEntity.getCancelMethod());
        hmilyParticipant.setVersion(mongoEntity.getVersion());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hmilyParticipant.setCreateTime(simpleDateFormat.format(mongoEntity.getCreateTime()));
        hmilyParticipant.setUpdateTime(simpleDateFormat.format(mongoEntity.getUpdateTime()));
        return hmilyParticipant;
    }

    /**
     * 转换mongo对象.
     * @param mongoEntity transaction mongo entity.
     * @return entity.
     */
    public static HmilyTransactionDTO convert(final TransactionMongoEntity mongoEntity) {
        HmilyTransactionDTO hmilyTransaction = new HmilyTransactionDTO();
        hmilyTransaction.setTransId(mongoEntity.getTransId());
        hmilyTransaction.setTransType(mongoEntity.getTransType());
        hmilyTransaction.setStatus(mongoEntity.getStatus());
        hmilyTransaction.setAppName(mongoEntity.getAppName());
        hmilyTransaction.setRetry(mongoEntity.getRetry());
        hmilyTransaction.setVersion(mongoEntity.getVersion());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        hmilyTransaction.setCreateTime(simpleDateFormat.format(mongoEntity.getCreateTime()));
        hmilyTransaction.setUpdateTime(simpleDateFormat.format(mongoEntity.getUpdateTime()));
        return hmilyTransaction;
    }

}
