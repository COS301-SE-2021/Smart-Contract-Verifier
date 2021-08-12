//This is for the ui to request data from the api in an abstract sense.
//This one deals with judge-related issues.

import 'dart:async';
import 'package:unison/services/Blockchain/jurorService.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:web3dart/credentials.dart';

import 'backendAPI.dart';
import '../../models/contract.dart';
import '../Blockchain/jurorService.dart';

class JudgeService {

  ApiInteraction _api = ApiInteraction();
  //JurorService _jurServ = JurorService();
  UnisonService _uniServ = UnisonService();


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
  //Adding it would make the class-hierarchy seem less strange
  Future<void> makeUserJudge() async { //Let's the current user act as a judge
      await _uniServ.addJuror();
  }

  Future<void> unMakeJudge() async { //Let's the user opt-out of being a judge
      await _uniServ.removeJuror();
  }

  Future<void> vote(BigInt id, bool vote) async { //True for yes, false for no
    await _uniServ.jurorVote(id, vote? 1 : 0);
  }

  Future<bool> isJudge(String add) async {

    return await _uniServ.isJuror(EthereumAddress.fromHex(add));
  }

}