package com.example.route

import com.example.dto.RequestDto
import com.example.dto.ResponseDto
import com.example.model.PostModel
import com.example.repository.Repository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

fun Routing.v1() {
    route("/api/v1/posts") {
        val repo by kodein().instance<Repository>()
        get {
            val response = repo.getAll().map { ResponseDto.fromModel(it) }
            call.respond(response)
        }
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            val model = repo.getById(id) ?: throw NotFoundException()
            val response = ResponseDto.fromModel(model)
            call.respond(response)
        }
        post {
            val input = call.receive<RequestDto>()
            val model = PostModel(id = input.id, author = input.author, content = input.content)
            val response = ResponseDto.fromModel(repo.save(model))
            call.respond(response)
        }
        delete("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.removeById(id)
            call.respond(HttpStatusCode.NoContent)
        }
        post("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.likeById(id)
        }
        post("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.dislikeById(id)
        }
        post("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: throw ParameterConversionException("id", "Long")
            repo.repostById(id)
        }
    }
}