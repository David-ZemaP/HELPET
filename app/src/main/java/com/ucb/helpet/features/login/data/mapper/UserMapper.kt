package com.ucb.helpet.features.login.data.mapper

import com.ucb.helpet.features.login.data.model.UserDto
import com.ucb.helpet.features.login.domain.model.Email
import com.ucb.helpet.features.login.domain.model.Location
import com.ucb.helpet.features.login.domain.model.Phone
import com.ucb.helpet.features.login.domain.model.User
import com.ucb.helpet.features.login.presentation.register.UserType

object UserMapper {

    fun toDomain(dto: UserDto): User {
        return User(
            userId = dto.userId,
            name = dto.name,
            email = Email.create(dto.email),
            phone = if (dto.phone.isNotEmpty()) Phone.create(dto.phone) else null,
            location = if (dto.location.isNotEmpty()) Location.create(dto.location) else null,
            userType = try {
                UserType.valueOf(dto.userType)
            } catch (e: Exception) {
                UserType.CLIENT
            }
        )
    }

    fun toDto(domain: User): UserDto {
        return UserDto(
            userId = domain.userId,
            name = domain.name,
            email = domain.email.value,
            phone = domain.phone?.value ?: "",
            location = domain.location?.value ?: "",
            userType = domain.userType.name
        )
    }
}