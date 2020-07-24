package com.google.rolecall.restcontrollers;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.google.rolecall.Constants;
import com.google.rolecall.jsonobjects.CastInfo;
import com.google.rolecall.jsonobjects.ResponseSchema;
import com.google.rolecall.models.Cast;
import com.google.rolecall.restcontrollers.Annotations.Delete;
import com.google.rolecall.restcontrollers.Annotations.Endpoint;
import com.google.rolecall.restcontrollers.Annotations.Get;
import com.google.rolecall.restcontrollers.Annotations.Patch;
import com.google.rolecall.restcontrollers.Annotations.Post;
import com.google.rolecall.restcontrollers.exceptionhandling.RequestExceptions.EntityNotFoundException;
import com.google.rolecall.restcontrollers.exceptionhandling.RequestExceptions.InvalidParameterException;
import com.google.rolecall.services.CastServices;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Endpoint(Constants.Mappings.CAST_MANAGEMENT)
public class CastManagement {
  
  private final CastServices castService;

  @Get
  public CompletableFuture<ResponseSchema<List<CastInfo>>> getAllCasts() {
    List<CastInfo> allCasts = castService.getAllCasts().stream().map(c->c.toCastInfo())
        .collect(Collectors.toList());

    ResponseSchema<List<CastInfo>> response = new ResponseSchema<>(allCasts);
    return CompletableFuture.completedFuture(response);
  }

  @Post
  public CompletableFuture<ResponseSchema<CastInfo>> createCast(@RequestBody CastInfo cast) {
    Cast newCast;

    try {
      newCast = castService.createCast(cast);
    } catch(InvalidParameterException e) {
      return CompletableFuture.failedFuture(e);
    } catch(EntityNotFoundException e) {
      return CompletableFuture.failedFuture(e);
    }

    ResponseSchema<CastInfo> response = new ResponseSchema<>(newCast.toCastInfo());
    return CompletableFuture.completedFuture(response);
  }

  @Patch
  public CompletableFuture<ResponseSchema<CastInfo>> editCast(@RequestBody CastInfo cast) {
    Cast newCast;

    try {
      newCast = castService.editCast(cast);
    } catch(InvalidParameterException e) {
      return CompletableFuture.failedFuture(e);
    } catch(EntityNotFoundException e) {
      return CompletableFuture.failedFuture(e);
    }

    ResponseSchema<CastInfo> response = new ResponseSchema<>(newCast.toCastInfo());
    return CompletableFuture.completedFuture(response);
  }

  @Delete(Constants.RequestParameters.CAST_ID)
  public CompletableFuture<Void> deleteCast(@RequestParam(value=Constants.RequestParameters.CAST_ID, required=true) int id) {
    try {
      castService.deleteCast(id);
    } catch(EntityNotFoundException e) {
      return CompletableFuture.failedFuture(e);
    }
    
    return CompletableFuture.completedFuture(null);
  }

  public CastManagement(CastServices castService) {
    this.castService = castService;
  }
}