package com.ucb.helpet.features.login.data.mapper

import com.ucb.helpet.features.login.data.database.entity.AuthTokenEntity
import com.ucb.helpet.features.login.domain.model.AuthToken

object AuthTokenMapper {

    fun toDomain(entity: AuthTokenEntity): AuthToken {
        return AuthToken(token = entity.token)
    }

    fun toEntity(domain: AuthToken): AuthTokenEntity {
        return AuthTokenEntity(token = domain.token)
    }
    
    fun toEntity(token: String): AuthTokenEntity {
        return AuthTokenEntity(token = token)
    }
}
