//This is for the ui to request data from the api in an abstract sense.
//This one deals with negotiation-related issues.
//Should these be called services?
//Also, are these multiple files too fragmented?

import 'dart:convert';
import 'package:flutter/material.dart';
import 'dart:async';
import '../models/backendAPI.dart';
import '../providers/contract.dart';

class NegotiationService {

  apiInteraction api = apiInteraction();

 Future<dynamic> getNotifications(String party) async {

    return;
 }

 Future<void> saveAgreement (Contract agr) async { //Save current version of agreement to backend
    //TODO list:
   //Change into map
   //Send map to api/post

   //Add exception handling. Maybe change return type to String

   return;
 }

 Future<void> saveCondition(String id, /*Condition*/dynamic cond) async { //Save a condition associated with a contract

    //TODO list:
   //Create Condition class
   //Send to api
   //Review return type (for special cases)
   return;

 }

 Future<void> updateCondition(/*Condition*/ dynamic cond) async {

    //TODO list:
   //Use condition to either remove/resave,
   //or use update functionality if available

    return;
 }


}

