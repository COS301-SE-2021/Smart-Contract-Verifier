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

  apiInteraction api = apiInteraction();

  Future<List<Contract>> getInvolvedAgreements(String party) async {
    //TODO list:
    //Get list of all contracts with this party
    //Convert each item in the list to Contract object

    String thing;
    return [];
 }

 Future<dynamic> getNotifications() async {

    return;
 }

 Future<void> saveAgreement (Contract agr) async {
    //TODO list:
   //Change into map
   //Send map to api/post

   //Add exception handling. Maybe change return type to String

   return;
 }

 Future<Contract> getAgreement(String id) async {
    //TODO list:
   //Send api request
   //Convert map to contract
   //On Exception: Maybe special error contract object?

   return Contract();

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

 Future<List<Contract>> getJudgeAgreements(String party) async { //Get all agreements where a user is the judge

    //TODO list:
   //Handle judge functionality later
   //Post api to request all contracts the party is judging
   //Convert to contract objects

    return [];
}

 Future<List<dynamic>> getJudgeNotifications(String party) async { //Get all notifications for a judge
    //TODO list:
   //Handle this later

    return [];
 }


}

