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

    Map<String, String> body = {'UserID' : party};
    var response;

    try {
      response = api.postData('/user/retrieve-user-agreements', body);
    }
    on Exception catch(e) {
      //Handle Exception
      return [];
    }

    final jsonList = jsonDecode(response);
    List<Contract> ret;
    for (int i =0; i<jsonList.length;i++)
    {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }

  Future<Contract> getAgreement(String id) async { //Get agreement with specified id
    //TODO list:
    //Send api request
    //Convert map to contract
    //On Exception: Maybe special error contract object?

    Map<String, String> body = {'AgreementID' : id};
    var response;

    try {
      response = api.postData('/negotiation/get-agreement-details', body);
    }
    on Exception catch(e) {
      //Handle Exception,
      //possibly with custom contract object
      return Contract();
    }

    return Contract.fromJson(jsonDecode(response));

  }

  Future</*Condition*/dynamic> getConditions(String id) async { //Get a list of conditions for an agreement

    return;
  }


}