//This is for the ui to request data from the api in an abstract sense.
//This one deals with judge-related issues.

import 'dart:async';
import 'package:unison/models/global.dart';
import 'package:unison/models/jury.dart';
import 'package:unison/services/Blockchain/tokenService.dart';
import 'package:unison/services/Blockchain/unisonService.dart';
import 'package:web3dart/credentials.dart';
import 'backendAPI.dart';
import '../../models/contract.dart';

class JudgeService {
  ApiInteraction _api = ApiInteraction();
  //JurorService _jurServ = JurorService();
  UnisonService _uniServ = UnisonService();
  TokenService _tokServ = TokenService();

  Future<List<Contract>> getInvolvedAgreements() async {
    //Get all agreements where a user is the judge

    var response;
    try {
      response = await _api.postData('/negotiation/get-judging-agreement',
          {'WalletID': Global.userAddress});

      if (response['Status'] != 'SUCCESSFUL')
        throw Exception('Agreements for judge could not be retrieved');
    } catch (e) {
      print(e);
      throw (e);
    }

    List<dynamic> jsonList = ((response['AgreementList']));
    List<Contract> ret = [];

    for (int i = 0; i < jsonList.length; i++) {
      ret.add(Contract.fromJson(jsonList[i]));
    }

    return ret;
  }

  Future<List<dynamic>> getNotifications(String party) async {
    //Get all notifications for a judge
    //TODO list:
    //Handle this later

    return [];
  }

  //Additional logic may be added to the below functions to enable functionality elsewhere
  //Adding it would make the class-hierarchy seem less strange
  Future<void> makeUserJudge() async {
    //Let's the current user act as a judge
    await _uniServ.addJuror();
    Global.isJudge = true;
    //Global.isJudge = await isJudge(); //In stead of setting to true, an alternative.
  }

  Future<void> unMakeJudge() async {
    //Let's the user opt-out of being a judge
    await _uniServ.removeJuror();
    Global.isJudge = false;
  }

  Future<void> vote(BigInt id, bool vote) async {
    //True for yes, false for no
    await _uniServ.jurorVote(
        id, vote ? 2 : 1); //Smart contract uses enum types for votes.
  }

  //Checks if the current user is a juror
  Future<bool> isJudge() async {
    bool res =
        await _uniServ.isJuror(EthereumAddress.fromHex(Global.userAddress));
    Global.isJudge = res;
    return res;
  }


  //Get the jury for an agreement from the blockchain
  Future<Jury> getJury(BigInt id) async {

    final res = await _uniServ.getJury(id);
    print('Jury res: ' + res[0].toString());
    return Jury.fromChain(res);

  }
}
