//This is for the ui to request data from the api in an abstract sense.
//This one deals with judge-related issues.

import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

class JudgeService {

  ApiInteraction api = ApiInteraction();

  Future<List<Contract>> getInvolvedAgreements(String party) async { //Get all agreements where a user is the judge

    //TODO list:
    //Handle judge functionality later
    //Post api to request all contracts the party is judging
    //Convert to contract objects

    return [];
  }

  Future<List<dynamic>> getNotifications(String party) async { //Get all notifications for a judge
    //TODO list:
    //Handle this later

    return [];
  }

}