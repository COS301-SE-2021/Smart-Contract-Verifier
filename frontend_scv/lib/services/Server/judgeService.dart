//This is for the ui to request data from the api in an abstract sense.
//This one deals with judge-related issues.

import 'dart:async';
import 'package:unison/models/global.dart';
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

  //This method is mostly for testing.
  //It sets the UNT allowances of all addresses passed in, granted by the addresses responsible for the minting of the token.
  //All of the addresses will then be able to sign up to the jury.
  Future<void> setAllowances(List<String> add, BigInt amount) async {
    for (String a in add) {
      await _tokServ.setAllowance(
          a, BigInt.from(10000)); //User stakes 10 000 gwei
    }
  }

  //Lets Verifier use UNT
  Future<void> setContractAllowance() async {
    await _tokServ.setAllowance(
        await Global.getContractId('Verifier'), BigInt.from(10000000000));
  }

  //Get the jury for an agreement from the blockchain
  Future<void> getJury(BigInt id) async {

    final res = await _uniServ.getJury(id);
    print('Jury res: ' + res[0][0].toString());

  }
}
