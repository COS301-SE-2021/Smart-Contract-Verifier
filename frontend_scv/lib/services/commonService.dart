//This is for the ui to request data from the api in an abstract sense.
//This one deals with common issues,
//like getting a list of agreements involving a user.

import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

class NegotiationService {

  apiInteraction api = apiInteraction();

  Future<List<Contract>> getInvolvedAgreements(String party) async {
    //TODO list:
    //Get list of all contracts with this party
    //Convert each item in the list to Contract object

    Map<String, String> body = {};
    final response = api.postData('/user/retrieve-suer-agreements', body);

    return [];
  }

  Future<Contract> getAgreement(String id) async { //Get agreement with specified id
    //TODO list:
    //Send api request
    //Convert map to contract
    //On Exception: Maybe special error contract object?

    return Contract();

  }

  Future</*Condition*/dynamic> getConditions(String id) async { //Get a list of conditions for an agreement

    return;
  }


}