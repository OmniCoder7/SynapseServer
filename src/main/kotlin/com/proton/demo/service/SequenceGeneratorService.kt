package com.proton.demo.service

import com.proton.demo.model.sequence.DatabaseSequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions.options
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service

@Service
class SequenceGeneratorService @Autowired constructor(private val mongoOperations: MongoOperations) {

    fun generateSequence(seqName: String?): Long {
        val counter = mongoOperations.findAndModify(
            query(where("_id").`is`(seqName)),
            Update().inc("seq", 1), options().returnNew(true).upsert(true),
            DatabaseSequence::class.java
        )
        return counter?.seq ?: 1
    }
}