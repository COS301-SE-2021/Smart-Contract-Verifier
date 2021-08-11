//This is for the ui to request data from the api in an abstract sense.
//This one deals with judge-related issues.

import 'dart:async';
import 'package:unison/services/Blockchain/jurorService.dart';

import 'backendAPI.dart';
import '../../models/contract.dart';
import '../Blockchain/jurorService.dart';

class JudgeService {

  ApiInteraction _api = ApiInteraction();
  JurorService _jurServ = JurorService();


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

  //Additional logic may be added to the below functions to enable functionality elsewhere
  Future<void> makeUserJudge() async { //Let's the current user act as a judge
      await _jurServ.addUserAsJuror();
  }

  Future<void> unMakeJudge() async { //Let's the user opt-out of being a judge
      await _jurServ.removeUserAsJuror();
  }

}