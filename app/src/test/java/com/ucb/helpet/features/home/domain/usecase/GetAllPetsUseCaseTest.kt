package com.ucb.helpet.features.home.domain.usecase

import com.ucb.helpet.features.home.domain.model.Pet
import com.ucb.helpet.features.home.domain.repository.PetRepository
import com.ucb.helpet.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllPetsUseCaseTest {

    private lateinit var getAllPetsUseCase: GetAllPetsUseCase
    private lateinit var fakePetRepository: PetRepository

    @Before
    fun setUp() {
        fakePetRepository = FakePetRepository()
        getAllPetsUseCase = GetAllPetsUseCase(fakePetRepository)
    }

    @Test
    fun `invoke returns success from repository`() = runBlocking {
        // Given
        val pets = listOf(Pet(id = "1", name = "Pet 1"))
        (fakePetRepository as FakePetRepository).setPets(pets)

        // When
        val result = getAllPetsUseCase.invoke().first()

        // Then
        assertEquals(pets, (result as Resource.Success).data)
    }

    @Test
    fun `invoke returns error from repository`() = runBlocking {
        // Given
        val errorMessage = "Error fetching pets"
        (fakePetRepository as FakePetRepository).setError(errorMessage)

        // When
        val result = getAllPetsUseCase.invoke().first()

        // Then
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}

class FakePetRepository : PetRepository {
    private var pets: List<Pet>? = null
    private var error: String? = null

    fun setPets(pets: List<Pet>) {
        this.pets = pets
        this.error = null
    }

    fun setError(error: String) {
        this.error = error
        this.pets = null
    }

    override fun getAllPets(): Flow<Resource<List<Pet>>> {
        return when {
            error != null -> flowOf(Resource.Error(error!!))
            pets != null -> flowOf(Resource.Success(pets!!))
            else -> flowOf(Resource.Error("Not initialized"))
        }
    }

    override suspend fun reportPet(pet: Pet): Resource<Unit> {
        // Not needed for this test, can just return success
        return Resource.Success(Unit)
    }

    override fun getPetsByOwner(ownerId: String): Flow<Resource<List<Pet>>> {
        // Not needed for this test, can return an empty flow
        return flowOf(Resource.Success(emptyList()))
    }

    override suspend fun getPetById(petId: String): Resource<Pet> {
        val pet = pets?.find { it.id == petId }
        return if (pet != null) {
            Resource.Success(pet)
        } else {
            Resource.Error("Pet not found")
        }
    }
}
