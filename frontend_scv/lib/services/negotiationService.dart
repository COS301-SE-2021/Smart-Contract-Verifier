//This is for the ui to request data from the api in an abstract sense.
//Should these be called services?

import 'dart:convert';
import 'package:retry/retry.dart';
import 'package:http/http.dart';
import 'package:flutter/material.dart';
import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

class NegotiationService {

  Future<List<Contract>> getInvolvedAgreements(String party) async {
    //Get list of all contracts with this party
    //Convert each item in the list to Contract object

    return [];

 }


}

